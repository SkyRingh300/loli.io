package io.loli.sc.server.action;

import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.UploadedImageService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Named
@RequestMapping(value = "/img")
public class ImageAction {

    @Inject
    @Named("imageService")
    private UploadedImageService imageService;

    private int maxResults = 20;

    @RequestMapping(value = "/list/{page}", method = RequestMethod.GET)
    public String listByUId(@PathVariable(value = "page") int page,
            Model model, HttpServletRequest request) {
        if (page == 0) {
            page = 1;
        }
        int firstPosition = (page - 1) * maxResults;
        int u_id = ((User) request.getSession().getAttribute("user")).getId();

        List<UploadedImage> list = imageService.listByUId(u_id, firstPosition);
        model.addAttribute("imgList", list);
        return "image/list";
    }

    @RequestMapping(value = "/{filename:[a-zA-Z0-9]{1,}\\.png}")
    public void showImg(@PathVariable("filename") String filename,
            HttpServletRequest request, HttpServletResponse response) {
        String imagePath = request.getServletContext().getRealPath(
                "img");

        File file = new File(imagePath + File.separator + filename);
        if (file.exists()) {
            response.setContentType("image/png");
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try {
                out = new BufferedOutputStream(response.getOutputStream());
                in = new BufferedInputStream(new FileInputStream(file));
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = in.read(buff, 0, buff.length))) {
                    out.write(buff, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (in != null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } else {
            try {
                response.getWriter().println("File does not exists");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
