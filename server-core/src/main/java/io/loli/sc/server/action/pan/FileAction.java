package io.loli.sc.server.action.pan;

import io.loli.sc.server.config.FileListConfig;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.entity.pan.FileEntity;
import io.loli.sc.server.entity.pan.FolderEntity;
import io.loli.sc.server.service.pan.FileService;
import io.loli.sc.server.service.pan.FolderService;

import java.util.ArrayList;
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

    @Inject
    private FileService fis;

    @RequestMapping("list")
    public String list(@RequestParam(value = "pid", required = false) Integer pid, HttpServletRequest request,
        @RequestParam(value = "start", required = false) Integer startIndex,
        @RequestParam(value = "max", required = false) Integer maxCount,
        @RequestParam(value = "type", required = false) String type

    ) {
        User user = (User) request.getSession().getAttribute("user");
        FolderEntity root = fs.findRootByUser(user);
        boolean begin = false;
        if (pid == null || pid == 0) {
            pid = root.getId();
        }

        if (startIndex == null || startIndex == 0) {
            startIndex = 0;
            begin = true;
        }

        if (type == null) {
            type = "folder";
        }
        if (maxCount == null) {
            maxCount = FileListConfig.PAGE_DEFAULT_COUNT;
        }

        List<FolderEntity> folders = null;
        List<FileEntity> files = null;
        if (type.equals("folder")) {

            folders = fs.listByUserAndPath(user, pid, startIndex, maxCount);
            int folderSize = folders.size();
            int fileSize = folderSize < maxCount ? maxCount - folderSize : 0;

            files = fis.listByUserIdAndFolderId(user.getId(), pid, 0, fileSize);

        } else {
            folders = new ArrayList<>();
            files = fis.listByUserIdAndFolderId(user.getId(), pid, startIndex, maxCount);
        }
        request.setAttribute("folderList", folders);
        request.setAttribute("fileList", files);
        request.setAttribute("rootFolder", root);
        request.setAttribute("parentList", fs.findParentsByFolder(pid));

        root = fs.findById(pid);
        request.setAttribute("parent", root);
        request.setAttribute("begin", begin);
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
        return this.list(parentId, request, 0, FileListConfig.PAGE_DEFAULT_COUNT, "folder");
    }
}
