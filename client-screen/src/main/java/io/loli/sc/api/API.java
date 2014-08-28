package io.loli.sc.api;

import java.io.File;

public interface API {
    public String upload(File fileToUpload) throws UploadException;

    public void auth() throws UploadException;
}
