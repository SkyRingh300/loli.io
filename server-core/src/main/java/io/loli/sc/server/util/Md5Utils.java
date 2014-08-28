package io.loli.sc.server.util;

import io.loli.sc.server.service.pan.FileService;
import io.loli.util.file.FileUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

@Named
@Singleton
public class Md5Utils {
    private final static Logger logger = Logger.getLogger(Md5Utils.class);
    private final static ExecutorService service = Executors.newFixedThreadPool(20);

    @Inject
    private FileService fileService;

    public void addTask(final File file, final int id) {
        Future<String> result = service.submit(() -> {
            return FileUtils.md5Hash(file);
        });
        try {
            String md5 = result.get(200, TimeUnit.SECONDS);
            int i = fileService.updateMd5(id, md5);
            if (i == 1) {
                logger.info("Calculate md5 successful:[id=" + id + ",md5=" + md5 + "]");
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error(e);
        }

    }
}
