package io.loli.sc.server.action;

import io.loli.sc.server.entity.Gallery;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.GalleryService;
import io.loli.sc.server.util.StatusBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Named
@RequestMapping("gallery")
public class GalleryAction {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Inject
    private GalleryService gs;

    @RequestMapping(value = "edit/addWithJsonResponse")
    @ResponseBody
    public Gallery addWithJsonResponse(@RequestParam(required = false) String title,
        @RequestParam(required = false) String description, HttpSession session) {
        if (StringUtils.isBlank(title)) {
            title = format.format(new Date());
        }
        Object userObj = session.getAttribute("user");

        Gallery gallery = new Gallery();
        gallery.setUser((User) userObj);
        gallery.setTitle(title);
        gallery.setDescription(description);
        gs.save(gallery);
        return gallery;
    }

    @RequestMapping(value = "edit/add")
    public String add(@RequestParam(required = false) String title, @RequestParam(required = false) String description,
        HttpSession session) {
        Gallery gal = this.addWithJsonResponse(title, description, session);
        return "/gallery/show/" + gal.getId();
    }

    @RequestMapping(value = "img/{id}")
    public String list(@PathVariable(value = "id") int id, Model model, HttpServletRequest request) {
        return "redirect:/gallery/img/" + id + "/" + 1;
    }

    @RequestMapping(value = "fetch/jsonList")
    @ResponseBody
    public List<Gallery> jsonList(HttpSession session) {
        List<Gallery> galList = new ArrayList<>();
        Object user = session.getAttribute("user");
        if (user instanceof User) {
            int uid = ((User) user).getId();
            galList = gs.listByUser(uid);
        }
        return galList;
    }

    @RequestMapping(value = "list")
    public String list(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        List<Gallery> list = gs.listByUserReversed(((User) user).getId());
        model.addAttribute("galList", list);
        return "/gallery/list";
    }

    @RequestMapping(value = "edit/update")
    public String update(@RequestParam(required = true) Integer gid, @RequestParam String title,
        @RequestParam(required = false) String description, HttpSession session, Model model,
        RedirectAttributes redirectAttributes) {
        Object user = session.getAttribute("user");
        gs.update(gid, title, description, (User) user);
        Gallery gal = gs.findById(gid);
        redirectAttributes.addFlashAttribute("message", "相册名已被修改为:" + gal.getTitle());
        return "redirect:/gallery/list";
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public StatusBean delete(@RequestParam(required = true) Integer gid, @RequestParam(required = true) String type,
        HttpSession session) {
        try {
            Object user = session.getAttribute("user");
            gs.delete(gid, type, (User) user);
        } catch (Exception e) {
            return new StatusBean("error", "删除失败:" + e.getMessage());
        }
        return new StatusBean("success", "删除成功");
    }
}
