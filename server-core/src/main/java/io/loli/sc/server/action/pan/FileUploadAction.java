package io.loli.sc.server.action.pan;

import io.loli.sc.server.entity.pan.FileEntity;

import javax.inject.Named;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Named
@RequestMapping(value = "/file/upload")
public class FileUploadAction {

    @RequestMapping(value = "", method = { RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public FileEntity upload(@RequestParam int folderId,
        @RequestParam(value = "file", required = true) MultipartFile file) {

        return null;
    }

}
