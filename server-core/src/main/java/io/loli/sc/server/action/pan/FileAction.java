package io.loli.sc.server.action.pan;

import io.loli.sc.server.config.FileListConfig;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.entity.pan.FileEntity;
import io.loli.sc.server.entity.pan.FolderEntity;
import io.loli.sc.server.entity.pan.LinkEntity;
import io.loli.sc.server.service.pan.FileService;
import io.loli.sc.server.service.pan.FolderService;
import io.loli.sc.server.service.pan.LinkService;
import io.loli.sc.server.util.StorageFolders;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "pan/file")
@Named
public class FileAction {
    @Inject
    private StorageFolders storageFolders;
    @Inject
    private FolderService fs;

    @Inject
    private FileService fis;

    @Inject
    private LinkService ls;

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

    @RequestMapping("getPermentLinkByFileId")
    @ResponseBody
    public String getPermentLinkByFileId(@RequestParam(value = "fileId") Integer fileId) {
        FileEntity file = fis.findById(fileId);
        LinkEntity link = ls.getPermLinkByFileId(file);
        return "pan/file/dl" + "/" + link.getPath();
    }

    @RequestMapping(value = "dl/{md5}")
    public void download(@PathVariable(value = "md5") String md5, HttpServletRequest request,
        HttpServletResponse response, HttpSession session) {
        FileEntity file = ls.findFileByPath(md5);
        Object obj = session.getAttribute("user");
        if (obj != null && ((User) obj).getId() == file.getUser().getId()) {
        } else {
            response.setContentType("text/html");
            try {
                response.getWriter().println("尚未登录或者该文件不属于您");
            } catch (IOException e) {
            }
            return;
        }
        try (InputStream is = new BufferedInputStream(storageFolders.getFile(file.getKey()));
            OutputStream os = new BufferedOutputStream(response.getOutputStream());) {
            response.setContentLength(file.getLength().intValue());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getOriginName());
            byte[] buffer = new byte[2048];
            for (int length = 0; (length = is.read(buffer)) > 0;) {
                os.write(buffer, 0, length);
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e1) {

        } finally {
        }
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public String deleteByIds(@RequestParam("ids") String ids) {
        List<Integer> toDelete = new ArrayList<Integer>();
        for (String s : ids.split(",")) {
            int i = Integer.parseInt(s);
            toDelete.add(i);
        }

        fis.batchDelete(toDelete);
        return "success";

    }
}
