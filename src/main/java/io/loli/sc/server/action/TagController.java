package io.loli.sc.server.action;

import io.loli.sc.server.entity.Tag;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.TagService;
import io.loli.sc.server.service.UploadedImageService;
import io.loli.sc.server.service.UserService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Named
@RequestMapping(value = "/tag")
public class TagController {

    @Inject
    private TagService tagService;

    @Inject
    private UserService userService;

    @Inject
    private UploadedImageService uploadedImageService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public String add(Tag tag, HttpServletRequest request, int imageId) {
        Object obj = request.getSession().getAttribute("user");
        User user = obj == null ? null : (User) obj;

        if (user != null) {
            user = userService.findById(user.getId());
            Tag tagInDb = null;
            try {
                if (null != tag.getName()) {
                    tagInDb = tagService.findByNameAndUser(tag.getName(), user);
                } else {
                    tagInDb = tagService.getById(tag.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (tagInDb == null) {
                tag.setUser(user);
                tagService.save(tag);
            } else {
                tag = tagInDb;
            }
            UploadedImage img = uploadedImageService.findById(imageId);
            img.setTag(tag);
            uploadedImageService.update(img);
        }
        return "" + tag.getId();
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public List<Tag> list(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute("user");
        User user = obj == null ? null : (User) obj;
        if (user != null) {
            user = userService.findById(user.getId());
        } else {
            return null;
        }
        return user.getTagList();
    }
}
