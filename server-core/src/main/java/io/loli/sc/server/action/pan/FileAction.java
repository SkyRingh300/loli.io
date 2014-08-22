package io.loli.sc.server.action.pan;

import io.loli.sc.server.entity.User;
import io.loli.sc.server.entity.pan.FolderEntity;
import io.loli.sc.server.service.pan.FolderService;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "pan/file")
@Named
public class FileAction {

    @Inject
    private FolderService fs;

    @RequestMapping("list")
    public String list(@RequestParam(value = "pid", required = false) Integer pid, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        FolderEntity root = fs.findRootByUser(user);
        if (pid == null) {
            pid = root.getId();
        }
        List<FolderEntity> folders = fs.listByUserAndPath(user, pid);
        request.setAttribute("folderList", folders);

        request.setAttribute("rootFolder", root);
        request.setAttribute("parentList", fs.findParentsByFolder(pid));

        root = fs.findById(pid);
        request.setAttribute("parent", root);
        return "pan/fileList";

    }

    @RequestMapping("add")
    public String add(@RequestParam(value = "pid") int parentId, @RequestParam(value = "name") String folderName,
        HttpServletRequest request) {
        FolderEntity parent = fs.findById(parentId);
        FolderEntity fe = new FolderEntity();
        fe.setParent(parent);
        fe.setName(folderName);
        fe.setFullPath(parent.getFullPath() + folderName + "/");
        fe.setCreateDate(new Date());

        User user = (User) request.getSession().getAttribute("user");

        fe.setUser(user);
        fs.save(fe);
        return this.list(parentId, request);
    }
}
