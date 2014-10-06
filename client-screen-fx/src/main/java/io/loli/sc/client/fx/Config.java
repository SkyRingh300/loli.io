package io.loli.sc.client.fx;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by choco on 14-10-2.
 */
public class Config {
    private static Properties prop = null;
    private static File propFolder = null;
    private static File propFile = null;

    private static final Logger logger = Logger.getLogger(Config.class.getCanonicalName());

    private String token;
    private String username;

    public boolean getAutoLogin() {
        return Boolean.valueOf(this.read("autoLogin"));
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
        this.write("autoLogin", String.valueOf(autoLogin));
    }

    private boolean autoLogin;

    static {
        logger.info("Start reading Properties.");
        File defaultDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();
        propFolder = new File(defaultDirectory, "LOLIIO");
        if (!propFolder.exists()) {
            propFolder.mkdirs();
            logger.info("Folder LOLIIO doesn't exist and create it.");
        }
        propFile = new File(propFolder, "config.properties");

        if (!propFile.exists()) {
            try {
                propFile.createNewFile();
                logger.info("File config.properties doesn't exist and create it");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        prop = new Properties();
        try {
            prop.load(new FileInputStream(propFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String read(String str) {
        return prop.getProperty(str);
    }

    private void write(String name, String value) {
        prop.setProperty(name, value);
        try {
            prop.store(new FileOutputStream(propFile), null);
            logger.info("Success to write properties to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
