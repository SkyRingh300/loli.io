package io.loli.sc.config;

import io.loli.util.FileNameGenerator;

import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

public class Config {
	// Config save directory
	private static final String CONFIG_DIR = ".SC-JAVA";
	// Config file name
	private static final String CONFIG_FILE = "config.properties";

	private File path;
	private File propDir;
	private File propFile;
	private Properties properties;
	private ImgurConfig imgurConfig;
	private DropboxConfig dropboxConfig;
	private GDriveConfig gdriveConfig;
	private ImageCloudConfig imageCloudConfig;

	/**
	 * 返回截图的快捷键信息
	 * 
	 * @return 数组，第一个为mask，第二个为键
	 */
	public int[] getHotKeys(String option) {
		String[] ss = null;
		if (option.equals("select")) {
			ss = this.getSelectHotKey().split(",");
		} else if (option.equals("full")) {
			ss = this.getFullHotKey().split(",");
		}
		int[] result = new int[2];
		if (ss.length == 2) {

			result[0] = keyToMask(Integer.parseInt(ss[0]));
			result[1] = Integer.parseInt(ss[1]);
		} else if (ss.length == 3) {
			result[0] = keyToMask(Integer.parseInt(ss[0]))
					| keyToMask(Integer.parseInt(ss[1]));
			result[1] = Integer.parseInt(ss[2]);
		}
		return result;
	}

	/**
	 * 将key code转化为mask code
	 * 
	 * @param key
	 * @return
	 */
	private int keyToMask(int key) {
		if (key == KeyEvent.VK_CONTROL) {
			return KeyEvent.CTRL_MASK;
		} else if (key == KeyEvent.VK_ALT) {
			return KeyEvent.ALT_MASK;
		} else if (key == KeyEvent.VK_SHIFT) {
			return KeyEvent.SHIFT_MASK;
		} else if (key == KeyEvent.VK_META) {
			return KeyEvent.META_MASK;
		} else {
			return 0;
		}
	}

	/**
	 * Open the directory where images in
	 */
	@SuppressWarnings("unused")
	private void openFolder() {
		Desktop desktop = null;
		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		}
		try {
			desktop.open(new File(savePath));
		} catch (IOException e) {
		}
	}

	/**
	 * config转换成properties对象
	 * 
	 * @return 转换后的properties对象
	 */
	public Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
		}
		properties.setProperty("savePath", getSavePath());
		properties.setProperty("defaultUpload", getDefaultUpload());
		properties.setProperty("startWithSystem", isStartWithSystem()
				.toString());
		properties.setProperty("saveInDrive", isSaveInDrive().toString());
		properties.setProperty("fileNameFormat", getFileNameFormat());
		properties.setProperty("showNotifyAfterUpload",
				getShowNotifyAfterUpload().toString());
		properties.setProperty("playMusicAfterUpload",
				getPlayMusicAfterUpload().toString());
		properties.setProperty("selectHotKey", getSelectHotKey());
		properties.setProperty("fullHotKey", getFullHotKey());
		return properties;
	}

	private void init() {
		// 先是初始化各个目录
		path = FileSystemView.getFileSystemView().getDefaultDirectory();
		propDir = new File(path.getAbsolutePath() + File.separator + CONFIG_DIR);
		propFile = new File(propDir.getAbsolutePath() + File.separator
				+ CONFIG_FILE);
		// 不存在时创建
		if (!propDir.exists()) {
			propDir.mkdir();
		}
		if (!propFile.exists()) {
			save();
		}
		// 读取prop设置
		read();
	}

	// 初始化
	{
		init();
	}

	public Config() {
	}

	private String savePath;

	private String defaultUpload;

	private String fileNameFormat;

	private Boolean startWithSystem;

	private Boolean saveInDrive;

	private Boolean playMusicAfterUpload;

	private String fullHotKey;

	public String getFullHotKey() {
		if (fullHotKey == null) {
			fullHotKey = KeyEvent.VK_CONTROL + "," + KeyEvent.VK_SHIFT + ","
					+ KeyEvent.VK_F1;
		}
		return fullHotKey;
	}

	public void setFullHotKey(String fullHotKey) {
		this.fullHotKey = fullHotKey;
	}

	public String getSelectHotKey() {
		if (selectHotKey == null) {
			selectHotKey = KeyEvent.VK_CONTROL + "," + KeyEvent.VK_SHIFT + ","
					+ KeyEvent.VK_F2;
		}
		return selectHotKey;
	}

	public void setSelectHotKey(String selectHotKey) {
		this.selectHotKey = selectHotKey;
	}

	private String selectHotKey;

	public Boolean getPlayMusicAfterUpload() {
		if (playMusicAfterUpload == null) {
			playMusicAfterUpload = true;
		}
		return playMusicAfterUpload;
	}

	public void setPlayMusicAfterUpload(Boolean playMusicAfterUpload) {
		this.playMusicAfterUpload = playMusicAfterUpload;
	}

	public Boolean getShowNotifyAfterUpload() {
		if (showNotifyAfterUpload == null) {
			showNotifyAfterUpload = true;
		}
		return showNotifyAfterUpload;
	}

	public void setShowNotifyAfterUpload(Boolean showNotifyAfterUpload) {
		this.showNotifyAfterUpload = showNotifyAfterUpload;
	}

	private Boolean showNotifyAfterUpload;

	/**
	 * 获取保存路径
	 * 
	 * @return 保存路径 当其为空时使用默认目录
	 */
	public String getSavePath() {
		if (savePath == null) {
			savePath = propDir.getAbsolutePath();
		}
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/**
	 * 保存设置
	 */
	public void save() {
		properties = getProperties();
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(propFile);
			properties.store(output, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取prop设置
	 */
	public void read() {
		properties = getProperties();
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(propFile));
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.setSavePath(properties.getProperty("savePath"));
		this.setFileNameFormat(properties.getProperty("fileNameFormat"));
		this.setDefaultUpload(properties.getProperty("defaultUpload"));
		this.setSaveInDrive(Boolean.parseBoolean(properties
				.getProperty("saveInDrive")));
		this.setStartWithSystem(Boolean.parseBoolean(properties
				.getProperty("startWithSystem")));
		this.setPlayMusicAfterUpload(Boolean.parseBoolean(properties
				.getProperty("playMusicAfterUpload")));
		this.setShowNotifyAfterUpload(Boolean.parseBoolean(properties
				.getProperty("showNotifyAfterUpload")));
		this.setSelectHotKey(properties.getProperty("selectHotKey"));
		this.setFullHotKey(properties.getProperty("fullHotKey"));
		// imgurConfig
		if (properties.getProperty("imgur.date") != null) {
			imgurConfig = new ImgurConfig();
			try {
				imgurConfig.setDate(new SimpleDateFormat("yyyyMMddHHmmss")
						.parse(properties.getProperty("imgur.date")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			imgurConfig.setRefreshToken(properties
					.getProperty("imgur.refreshToken"));
			imgurConfig.setAccessToken(properties
					.getProperty("imgur.accessToken"));
		}
		if (properties.getProperty("dropbox.uid") != null) {
			dropboxConfig = new DropboxConfig();
			dropboxConfig.setAccessToken(properties
					.getProperty("dropbox.accessToken"));
			dropboxConfig.setUid(properties.getProperty("dropbox.uid"));
		}
		if (properties.getProperty("gdrive.accessToken") != null) {
			gdriveConfig = new GDriveConfig();
			gdriveConfig.setAccessToken(properties
					.getProperty("gdrive.accessToken"));
			gdriveConfig.setRefreshToken(properties
					.getProperty("gdrive.refreshToken"));
		}
		if (properties.getProperty("imageCloud.email") != null) {
			imageCloudConfig = new ImageCloudConfig();
			imageCloudConfig.setEmail(properties
					.getProperty("imageCloud.email"));
			imageCloudConfig.setToken(properties
					.getProperty("imageCloud.token"));
		}
	}

	public ImgurConfig getImgurConfig() {
		if (imgurConfig == null) {
			imgurConfig = new ImgurConfig();
		}
		return imgurConfig;
	}

	public void setImgurConfig(ImgurConfig imgurConfig) {
		this.imgurConfig = imgurConfig;
	}

	public DropboxConfig getDropboxConfig() {
		if (dropboxConfig == null) {
			dropboxConfig = new DropboxConfig();
		}
		return dropboxConfig;
	}

	public void setDropboxConfig(DropboxConfig dropboxConfig) {
		this.dropboxConfig = dropboxConfig;
	}

	public String getDefaultUpload() {
		if (defaultUpload == null) {
			defaultUpload = "";
		}
		return defaultUpload;
	}

	public void setDefaultUpload(String defaultUpload) {
		this.defaultUpload = defaultUpload;
	}

	public GDriveConfig getGdriveConfig() {
		if (gdriveConfig == null) {
			gdriveConfig = new GDriveConfig();
		}
		return gdriveConfig;
	}

	public void setGdriveConfig(GDriveConfig gdriveConfig) {
		this.gdriveConfig = gdriveConfig;
	}

	public ImageCloudConfig getImageCloudConfig() {
		if (imageCloudConfig == null) {
			imageCloudConfig = new ImageCloudConfig();
		}
		return imageCloudConfig;
	}

	public void setImageCloudConfig(ImageCloudConfig imageCloudConfig) {
		this.imageCloudConfig = imageCloudConfig;
	}

	public String getFileNameFormat() {
		if (fileNameFormat == null) {
			fileNameFormat = FileNameGenerator.getFormatString();
		}
		return fileNameFormat;
	}

	public void setFileNameFormat(String fileDescFormat) {
		this.fileNameFormat = fileDescFormat;
	}

	public Boolean isStartWithSystem() {
		if (startWithSystem == null) {
			startWithSystem = true;
		}
		return startWithSystem;
	}

	public void setStartWithSystem(boolean startWithSystem) {
		this.startWithSystem = startWithSystem;
	}

	public Boolean isSaveInDrive() {
		if (saveInDrive == null) {
			saveInDrive = true;
		}
		return saveInDrive;
	}

	public void setSaveInDrive(boolean saveInDrive) {
		this.saveInDrive = saveInDrive;
	}

}
