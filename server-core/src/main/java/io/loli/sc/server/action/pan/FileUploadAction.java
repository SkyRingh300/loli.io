package io.loli.sc.server.action.pan;

import io.loli.aliyun.oss.StorageFile;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.entity.pan.FileEntity;
import io.loli.sc.server.entity.pan.FolderEntity;
import io.loli.sc.server.service.pan.FolderService;
import io.loli.sc.server.util.StorageFolders;
import io.loli.util.string.MD5Util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Named
@RequestMapping(value = "pan/file/upload")
public class FileUploadAction {

    @Inject
    private StorageFolders storageFolders;

    @Inject
    private FolderService sfs;

    private final static Logger logger = Logger.getLogger(FileUploadAction.class);

    @RequestMapping(value = "", method = { RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public FileEntity upload(@RequestParam int folderId,
        @RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
        User user = (User) session.getAttribute("user");

        String originName = file.getOriginalFilename();
        long now = System.nanoTime();
        String email = user.getEmail();
        String strToHash = originName + now + email;
        String generatedName = null;
        try {
            generatedName = MD5Util.hash(strToHash);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            e.printStackTrace();
        }

        File f = this.saveFile(file, generatedName);
        StorageFile uploadedFile = storageFolders.uploadFile(f);
        FolderEntity parent = sfs.findById(folderId);
        FileEntity entity = new FileEntity();
        entity.setUser(user);
        entity.setCreateDate(new Date());
        entity.setFolder(parent);
        entity.setKey(uploadedFile.getKey());
        entity.setNewName(originName);
        entity.setOriginName(originName);
        
        
        return entity;
    }

    private File saveFile(MultipartFile mfile, String name) {
        File file = new File(System.getProperty("java.io.tmpdir"), name);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileUtils.writeByteArrayToFile(file, mfile.getBytes());
        } catch (IOException e) {
            logger.error(e);
        }
        return file;
    }

}
