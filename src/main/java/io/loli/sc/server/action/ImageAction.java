package io.loli.sc.server.action;

import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.UploadedImageService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Named
@RequestMapping(value="/img")
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
}
