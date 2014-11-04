package io.loli.sc.server.action;

import io.loli.sc.server.entity.Gallery;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.GalleryService;
import io.loli.sc.server.service.UploadedImageService;
import io.loli.sc.server.service.UserService;
import io.loli.sc.server.util.StatusBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Named
@RequestMapping(value = "/img")
public class ImageAction {
    private static final Logger logger = Logger.getLogger(ImageAction.class);
    @Inject
    @Named("imageService")
    private UploadedImageService imageService;

    @Inject
    private UserService userService;

    @Inject
    private GalleryService gs;

    @RequestMapping(value = "/m/{redirectCode}", method = RequestMethod.GET)
    public String show(@PathVariable(value = "redirectCode") String redirectCode, Model model) {
        UploadedImage image = imageService.findByCode(redirectCode);
        model.addAttribute("image", image);
        return "image/show";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listByUId(Model model, HttpServletRequest request) {
        return "redirect:/img/list/1";
    }

    @RequestMapping(value = "/list/{page}", method = RequestMethod.GET)
    public String listByUId(@PathVariable(value = "page") int page, Model model, HttpServletRequest request) {
        if (page == 0) {
            page = 1;
        }
        int firstPosition = (page - 1) * imageService.getMaxResults();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        int uid = ((User) request.getSession().getAttribute("user")).getId();

        List<UploadedImage> list = imageService.listByUId(uid, firstPosition);
        int totalCount = imageService.countByUId(uid);
        int pageCount = (int) Math.ceil((float) totalCount / (float) imageService.getMaxResults());
        boolean hasLast = page != 1;
        boolean hasNext = page != pageCount;
        int current = page;
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("hasLast", hasLast);
        request.setAttribute("hasNext", hasNext);
        request.setAttribute("currentPage", current);
        request.setAttribute("count", totalCount);
        model.addAttribute("imgList", list);
        if (user != null) {
            user = userService.findById(user.getId());
            model.addAttribute("tagList", user.getTagList());
        }

        List<Gallery> galList = gs.listByUser(uid);
        model.addAttribute("galleries", galList);
        return "image/list";
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam(value = "fileName") String fileName,
        @RequestParam(value = "page", required = false) Integer page, HttpServletRequest request, Model model,
        @RequestParam(required = false) Integer tag) {
        if (page == null || page == 0) {
            page = 1;
        }
        int firstPosition = (page - 1) * imageService.getMaxResults();
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        int u_id = ((User) request.getSession().getAttribute("user")).getId();

        List<UploadedImage> list = imageService.listByUIdAndFileName(u_id, fileName, firstPosition, tag);
        int totalCount = imageService.countByUIdAndFileName(u_id, fileName, tag);
        int count = imageService.countByUId(u_id);
        int pageCount = (int) Math.ceil((float) totalCount / (float) imageService.getMaxResults());
        boolean hasLast = page != 1;
        boolean hasNext = page != pageCount;
        int current = page;
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("count", count);
        request.setAttribute("pageCount", pageCount);
        request.setAttribute("hasLast", hasLast);
        request.setAttribute("hasNext", hasNext);
        request.setAttribute("currentPage", current);
        request.setAttribute("fileName", fileName);
        model.addAttribute("imgList", list);
        if (user != null) {
            user = userService.findById(user.getId());
            model.addAttribute("tagList", user.getTagList());
        }
        return "image/list";
    }

    @RequestMapping(value = "/delete")
    public String delete(@RequestParam(value = "id") int id, RedirectAttributes redirectAttributes,
        HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        UploadedImage image = imageService.findById(id);
        if (user == null || image == null || image.getUser().getId() != user.getId()) {
            redirectAttributes.addFlashAttribute("message", "非法登录");
        } else {
            try {
                imageService.delete(id);

                redirectAttributes.addFlashAttribute("message", "删除成功");
            } catch (Exception e) {
                logger.error(e);
                redirectAttributes.addFlashAttribute("message", "删除失败，原因是" + e.getMessage());
            }
        }

        return "redirect:/img/list";
    }

    @RequestMapping(value = "/{filename:[a-zA-Z0-9]{1,}\\.png}")
    public void showImg(@PathVariable("filename") String filename, HttpServletRequest request,
        HttpServletResponse response) {
        String imagePath = request.getServletContext().getRealPath("img");

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
                logger.error(e);
            } finally {
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        logger.error(e);
                    }
                if (in != null)
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.error(e);
                    }
            }
        } else {
            try {
                response.getWriter().println("File does not exists");
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    @RequestMapping(value = "jsonList")
    @ResponseBody
    public List<UploadedImage> list(HttpSession session, @RequestParam(value = "gid", required = false) Integer gid,
        @RequestParam(value = "page", required = false) Integer page) {
        if (page == null || page == 0) {
            page = 1;
        }
        Object user = session.getAttribute("user");

        List<UploadedImage> result = new ArrayList<>();
        if (gid == null || gid == 0) {
            result = imageService.listByUId(((User) user).getId(), imageService.getMaxResults() * (page - 1));
        } else {
            result = imageService.findByGalIdAndUId(((User) user).getId(), gid, page);
        }

        return result;
    }

    @RequestMapping(value = "pageCount")
    @ResponseBody
    public String pageCount(HttpSession session, @RequestParam(value = "gid", required = false) Integer gid) {

        Object user = session.getAttribute("user");
        int result = 0;
        if (gid == null || gid == 0) {
            result = imageService.countByUId(((User) user).getId());
        } else {
            result = imageService.countByGalIdAndUId(((User) user).getId(), gid);
        }
        double d = (double) result / imageService.getMaxResults();
        BigDecimal bd = new BigDecimal(d);
        return String.valueOf(bd.setScale(0, BigDecimal.ROUND_UP).intValue());
    }

    @ResponseBody
    @RequestMapping(value = "batchDelete")
    public StatusBean batchDelete(@RequestParam(value = "ids", required = true) String ids, HttpSession session) {

        try {
            Object user = session.getAttribute("user");
            imageService.batchDelete(ids, (User) user);
        } catch (Exception e) {
            new StatusBean(StatusBean.STATUS_ERROR, e.getMessage());
        }
        return new StatusBean(StatusBean.STATUS_SUCCESS, "删除成功");
    }

    @ResponseBody
    @RequestMapping(value = "batchMove")
    public StatusBean batchMove(@RequestParam(value = "ids", required = true) String ids,
        @RequestParam(value = "gid", required = true) Integer gid, HttpSession session) {

        try {
            Object user = session.getAttribute("user");
            imageService.batchMove(ids, (User) user, gid);
        } catch (Exception e) {
            new StatusBean(StatusBean.STATUS_ERROR, e.getMessage());
        }
        return new StatusBean(StatusBean.STATUS_SUCCESS, "移动成功");
    }

}
