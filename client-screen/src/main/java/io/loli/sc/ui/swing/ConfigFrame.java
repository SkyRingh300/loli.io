package io.loli.sc.ui.swing;

import io.loli.sc.SystemMenuSelector;
import io.loli.sc.api.DropboxAPI;
import io.loli.sc.api.GDriveAPI;
import io.loli.sc.api.ImageCloudAPI;
import io.loli.sc.api.ImgurAPI;
import io.loli.sc.api.UploadException;
import io.loli.sc.config.Config;
import io.loli.sc.ui.MessageSender;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ConfigFrame extends JFrame {
    private JPanel jpanel1;
    private JPanel jpanel2;
    private JPanel jpanel3;
    private static final long serialVersionUID = 1L;
    private Config config;

    private static Logger logger = Logger.getLogger(ConfigFrame.class);
    private JFrame jframe;

    class KeyListenPanel extends JDialog {
        private static final long serialVersionUID = 5526655422709068055L;
        private JLabel infoLabel = new JLabel("请按键");
        private String option;
        private Set<String> keyList = new LinkedHashSet<String>();
        private Set<String> tempKeyList = new LinkedHashSet<String>();
        private JButton okButton = new JButton("确认");
        private Set<Integer> keyIntSet = new LinkedHashSet<Integer>();
        private String hotkeyStr;
        private JButton cancelButton = new JButton("取消");

        public KeyListenPanel(JFrame parentComponent, String option) {
            super(parentComponent, true);
            this.setComponentOrientation(((parentComponent == null) ? getRootPane() : parentComponent)
                .getComponentOrientation());
            this.setLayout(null);
            this.add(infoLabel);
            this.add(okButton);
            this.add(cancelButton);
            infoLabel.setBounds(40, 5, 160, 30);
            okButton.setBounds(40, 40, 50, 30);
            cancelButton.setBounds(100, 40, 50, 30);
            this.option = option;
            this.setSize(200, 110);

            this.addListener();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int WIDTH = screenSize.width;
            final int HEIGHT = screenSize.height;
            this.setLocation(WIDTH / 2 - getWidth() / 2, HEIGHT / 2 - getHeight() / 2);

            okButton.setEnabled(false);
            this.setVisible(true);
            this.requestFocus();
        }

        private boolean checkBind() {
            StringBuilder sb = new StringBuilder();
            boolean result = true;
            if (keyIntSet.size() < 2) {
                sb.append("请按两个或两个以上的键");
                result = false;
            }
            if (!keyIntSet.contains(KeyEvent.VK_CONTROL) && !keyIntSet.contains(KeyEvent.VK_META)
                && !keyIntSet.contains(KeyEvent.VK_ALT) && !keyIntSet.contains(KeyEvent.SHIFT_MASK)) {
                sb.append("请包含ctrl(meta),alt,shift中的至少一个");
                result = false;
            }
            if (!keyIntSet.contains(KeyEvent.VK_WINDOWS)) {
                sb.append("不能包含Windows键");
                result = false;
            }
            Iterator<Integer> itr = keyIntSet.iterator();
            int count = 0;
            while (itr.hasNext()) {
                int i = itr.next();
                if (i != KeyEvent.VK_CONTROL && i != KeyEvent.VK_ALT && i != KeyEvent.VK_SHIFT && i != KeyEvent.VK_META) {
                    count++;
                }
            }
            if (count != 1) {
                sb.append("只能含有一个数值键");
            }
            return false;
        }

        private void addListener() {
            this.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent e) {
                    keyList.clear();
                    tempKeyList.clear();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    keyList.clear();
                    tempKeyList.clear();
                }

            });
            this.addKeyListener(new KeyAdapter() {
                // 按键应该一次性输入，如果按着多个松开一个然后再按另一个，这样会重新计算
                String lastReleaseKey;

                @Override
                public void keyPressed(KeyEvent e) {
                    okButton.setEnabled(true);
                    // 判断是否是一次新的输入
                    if (tempKeyList.size() == 0) {
                        // 如果是的就清空已经存储的快捷键
                        keyList.clear();
                    }
                    // 判断用户上次是否松开
                    if (lastReleaseKey != null) {
                        keyList.remove(lastReleaseKey);
                    }
                    keyList.add(KeyEvent.getKeyText(e.getKeyCode()));
                    tempKeyList.add(KeyEvent.getKeyText(e.getKeyCode()));
                    keyIntSet.add(e.getKeyCode());
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    Iterator<String> itr = keyList.iterator();
                    Iterator<Integer> itr2 = keyIntSet.iterator();
                    for (int i = 0; itr.hasNext(); i++) {
                        sb.append(itr.next());
                        sb2.append(itr2.next());
                        if (i != keyList.size() - 1) {
                            sb.append("+");
                            sb2.append(",");
                        }
                    }
                    hotkeyStr = sb2.toString();
                    infoLabel.setText(sb.toString());
                    lastReleaseKey = null;
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    tempKeyList.remove(KeyEvent.getKeyText(e.getKeyCode()));
                    lastReleaseKey = KeyEvent.getKeyText(e.getKeyCode());
                }
            });
            okButton.addActionListener(e -> {

                if (option.equals("select")) {
                    config.setSelectHotKey(hotkeyStr);
                    selectShotKeyShowLabel.setText(infoLabel.getText());
                } else {
                    config.setFullHotKey(hotkeyStr);
                    fullShotKeyShowLabel.setText(infoLabel.getText());
                }
                dispose();
            });

            cancelButton.addActionListener(e -> {
                dispose();
            });
        }
    }

    private void useSystemUI() {
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("linux") == -1)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            else {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                } catch (ClassNotFoundException e) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            }
            // System.setProperty("awt.useSystemAAFontSettings", "on");
            // System.setProperty("swing.aatext", "true");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void initFatherFrame() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(392, 487);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int WIDTH = screenSize.width;
        final int HEIGHT = screenSize.height;
        this.setLocation(WIDTH / 2 - this.getWidth() / 2, HEIGHT / 2 - this.getHeight() / 2);
        this.setResizable(false);
        setVisible(true);
    }

    private void addChoice(JComboBox<String> choice) {
        if (config.getImgurConfig().getAccessToken() != null)
            choice.addItem("imgur");
        if (config.getDropboxConfig().getAccessToken() != null)
            choice.addItem("dropbox");
        if (config.getGdriveConfig().getAccessToken() != null)
            choice.addItem("gdrive");
        if (config.getImageCloudConfig().getToken() != null)
            choice.addItem("screenshot.pics");
    }

    private void initComponents() {
        savePathLabel = new JLabel("地址：");
        savePathField = new JTextField(20);
        browsePathButton = new JButton("浏览");
        jpanel1 = new JPanel();
        jpanel1.setLayout(null);

        jpanel2 = new JPanel();
        jpanel2.setLayout(null);
        uploadChoice = new JComboBox<String>();
        addChoice(uploadChoice);
        uploadChoice.setSelectedItem(config.getDefaultUpload());

        okButton = new JButton("确定");
        cancelButton = new JButton("取消");
        chooser = new JFileChooser();
        savePathField.setText(config.getSavePath());

        imgurLabel = new JLabel("imgur");
        imgurAuthLabel = new JLabel();
        imgurAuthButton = new JButton("连接");
        imgurRemoveAuthButton = new JButton("移除");

        dropboxLabel = new JLabel("dropbox");
        dropboxAuthLabel = new JLabel();
        dropboxAuthButton = new JButton("连接");
        dropboxRemoveAuthButton = new JButton("移除");

        gDriveLabel = new JLabel("gdrive");
        gDriveAuthLabel = new JLabel();
        gDriveAuthButton = new JButton("连接");
        gDriveRemoveAuthButton = new JButton("移除");

        imageCloudLabel = new JLabel("screenshot.pics");
        imageCloudAuthLabel = new JLabel();
        imageCloudAuthButton = new JButton("连接");
        imageCloudRemoveAuthButton = new JButton("移除");
        tab = new JTabbedPane();
        uploadToLabel = new JLabel("上传到");

        startWithSystemCheck = new JCheckBox("开机自启动");
        startWithSystemCheck.setSelected(config.isStartWithSystem());

        showNotifyAfterUploadCheck = new JCheckBox("上传后显示通知");
        showNotifyAfterUploadCheck.setSelected(config.getShowNotifyAfterUpload());

        playMusicAfterUploadCheck = new JCheckBox("上传后播放音乐");
        playMusicAfterUploadCheck.setSelected(config.getPlayMusicAfterUpload());

        saveAsTitleLabel = new JLabel("<html><strong>截图保存在</strong></html>");

        fileNameFormatLabel = new JLabel("<html><strong>截图文件名格式</strong></html>");
        fileNameFormatField = new JTextField();
        fileNameFormatField.setText(config.getFileNameFormat());

        jpanel3 = new JPanel();
        jpanel3.setLayout(null);
        fullShotKeyLabel = new JLabel("全屏截图: ");
        fullShotKeyButton = new JButton("点击设置");
        fullShotKeyShowLabel = new JLabel(StringUtils.join(new ArrayList<String>() {
            private static final long serialVersionUID = 4083852950428261739L;

            {
                for (String s : Arrays.asList(config.getFullHotKey().split(","))) {
                    add(KeyEvent.getKeyText(Integer.parseInt(s)));
                }
            }
        }, "+"));

        selectShotKeyLabel = new JLabel("选择截图: ");
        selectShotKeyButton = new JButton("点击设置");
        selectShotKeyShowLabel = new JLabel(StringUtils.join(new ArrayList<String>() {
            private static final long serialVersionUID = 4083852950428261739L;

            {
                for (String s : Arrays.asList(config.getSelectHotKey().split(","))) {
                    add(KeyEvent.getKeyText(Integer.parseInt(s)));
                }
            }
        }, "+"));
        // serviceListTable = new JTable();
    }

    private void initButton() {
        if (config.getImgurConfig().getAccessToken() == null) {
            imgurAuthLabel.setText("未连接");
            imgurRemoveAuthButton.setEnabled(false);
        } else {
            imgurAuthLabel.setText("已连接");
            imgurAuthButton.setEnabled(false);
        }
        if (config.getDropboxConfig().getAccessToken() == null) {
            dropboxAuthLabel.setText("未连接");
            dropboxRemoveAuthButton.setEnabled(false);
        } else {
            dropboxAuthLabel.setText("已连接");
            dropboxAuthButton.setEnabled(false);
        }
        if (config.getGdriveConfig().getAccessToken() == null) {
            gDriveAuthLabel.setText("未连接");
            gDriveRemoveAuthButton.setEnabled(false);
        } else {
            gDriveAuthLabel.setText("已连接");
            gDriveAuthButton.setEnabled(false);
        }

        if (config.getImageCloudConfig().getToken() == null) {
            imageCloudAuthLabel.setText("未连接");
            imageCloudRemoveAuthButton.setEnabled(false);
        } else {
            imageCloudAuthLabel.setText("已连接");
            imageCloudAuthButton.setEnabled(false);
        }

    }

    private void initComposition() {

        savePathLabel.setBounds(30, 40, 70, 30);
        savePathField.setBounds(90, 40, 180, 30);
        browsePathButton.setBounds(280, 40, 60, 30);
        okButton.setBounds(200, 415, 70, 30);
        cancelButton.setBounds(290, 415, 70, 30);
        uploadChoice.setBounds(90, 5, 140, 30);
        uploadToLabel.setBounds(30, 5, 60, 30);

        imgurLabel.setBounds(40, 75, 60, 30);
        imgurAuthLabel.setBounds(85, 75, 60, 30);
        imgurAuthButton.setBounds(185, 75, 60, 30);
        imgurRemoveAuthButton.setBounds(250, 75, 60, 30);

        dropboxLabel.setBounds(40, 110, 60, 30);
        dropboxAuthLabel.setBounds(110, 110, 60, 30);
        dropboxAuthButton.setBounds(185, 110, 60, 30);
        dropboxRemoveAuthButton.setBounds(250, 110, 60, 30);

        gDriveLabel.setBounds(40, 145, 60, 30);
        gDriveAuthLabel.setBounds(110, 145, 60, 30);
        gDriveAuthButton.setBounds(185, 145, 60, 30);
        gDriveRemoveAuthButton.setBounds(250, 145, 60, 30);

        imageCloudLabel.setBounds(40, 180, 60, 30);
        imageCloudAuthLabel.setBounds(110, 180, 60, 30);
        imageCloudAuthButton.setBounds(185, 180, 60, 30);
        imageCloudRemoveAuthButton.setBounds(250, 180, 60, 30);

        startWithSystemCheck.setBounds(40, 230, 200, 30);

        saveAsTitleLabel.setBounds(10, 10, 190, 30);

        fileNameFormatLabel.setBounds(10, 40, 300, 90);

        fileNameFormatField.setBounds(40, 100, 200, 30);

        playMusicAfterUploadCheck.setBounds(40, 150, 200, 30);

        showNotifyAfterUploadCheck.setBounds(40, 190, 200, 30);

        selectShotKeyLabel.setBounds(10, 30, 70, 30);
        selectShotKeyButton.setBounds(80, 30, 80, 30);
        selectShotKeyShowLabel.setBounds(180, 30, 100, 30);
        fullShotKeyLabel.setBounds(10, 80, 70, 30);
        fullShotKeyButton.setBounds(80, 80, 80, 30);
        fullShotKeyShowLabel.setBounds(180, 80, 100, 30);
    }

    private JLabel savePathLabel;
    private JTextField savePathField;
    private JButton browsePathButton;

    private JButton okButton;
    private JButton cancelButton;

    private JFileChooser chooser;
    private JLabel imgurLabel;
    private JLabel imgurAuthLabel;
    private JButton imgurAuthButton;
    private JButton imgurRemoveAuthButton;

    private JLabel dropboxLabel;
    private JLabel dropboxAuthLabel;
    private JButton dropboxAuthButton;
    private JButton dropboxRemoveAuthButton;

    private JLabel gDriveLabel;
    private JLabel gDriveAuthLabel;
    private JButton gDriveAuthButton;
    private JButton gDriveRemoveAuthButton;

    private JLabel imageCloudLabel;
    private JLabel imageCloudAuthLabel;
    private JButton imageCloudAuthButton;
    private JButton imageCloudRemoveAuthButton;

    private JComboBox<String> uploadChoice;

    private JLabel uploadToLabel;

    private JCheckBox startWithSystemCheck;

    private JCheckBox showNotifyAfterUploadCheck;
    private JCheckBox playMusicAfterUploadCheck;

    private JTabbedPane tab;

    private JLabel saveAsTitleLabel;

    private JLabel fileNameFormatLabel;

    private JTextField fileNameFormatField;

    private JLabel fullShotKeyLabel;
    private JButton fullShotKeyButton;
    private JLabel fullShotKeyShowLabel;

    private JLabel selectShotKeyLabel;
    private JButton selectShotKeyButton;
    private JLabel selectShotKeyShowLabel;

    // private JTable serviceListTable;

    private void addcomponents() {
        setLayout(null);
        jpanel1.setBounds(3, 3, getWidth(), getHeight());
        jpanel2.setBounds(3, 3, getWidth(), getHeight());
        jpanel1.add(savePathLabel);
        jpanel1.add(savePathField);
        jpanel1.add(browsePathButton);

        jpanel2.add(imgurLabel);
        jpanel2.add(imgurAuthLabel);
        jpanel2.add(imgurAuthButton);
        jpanel2.add(imgurRemoveAuthButton);

        jpanel2.add(dropboxLabel);
        jpanel2.add(dropboxAuthLabel);
        jpanel2.add(dropboxAuthButton);
        jpanel2.add(dropboxRemoveAuthButton);

        jpanel2.add(gDriveLabel);
        jpanel2.add(gDriveAuthLabel);
        jpanel2.add(gDriveAuthButton);
        jpanel2.add(gDriveRemoveAuthButton);

        jpanel2.add(imageCloudLabel);
        jpanel2.add(imageCloudAuthLabel);
        jpanel2.add(imageCloudAuthButton);
        jpanel2.add(imageCloudRemoveAuthButton);

        jpanel2.add(uploadChoice);
        jpanel2.add(uploadToLabel);
        jpanel1.add(startWithSystemCheck);
        jpanel1.add(saveAsTitleLabel);

        jpanel1.add(fileNameFormatLabel);

        jpanel1.add(fileNameFormatField);

        jpanel1.add(playMusicAfterUploadCheck);
        jpanel1.add(showNotifyAfterUploadCheck);

        jpanel3.add(fullShotKeyLabel);
        jpanel3.add(fullShotKeyButton);
        jpanel3.add(fullShotKeyShowLabel);

        jpanel3.add(selectShotKeyLabel);
        jpanel3.add(selectShotKeyButton);
        jpanel3.add(selectShotKeyShowLabel);

        add(tab);
        tab.setTabPlacement(JTabbedPane.TOP);
        tab.add("通常设置", jpanel1);
        tab.add("连接网站", jpanel2);
        tab.add("快捷键设置", jpanel3);
        // TODO 快捷键设置的TAB
        // TODO 关于，版本，自动升级的选项
        // TODO 上传后显示托盘消息
        // TODO 上传成功后播放音乐
        tab.setBounds(5, 5, 382, 400);

        add(okButton);
        add(cancelButton);

        // add(chooser);
    }

    public void addListeners() {
        browsePathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result;
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                File file;
                result = chooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                    if (file.exists()) {
                        savePathField.setText(file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(null, "文件不存在");
                    }
                } else if (result == JFileChooser.CANCEL_OPTION) {
                } else if (result == JFileChooser.ERROR_OPTION) {
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.setSavePath(savePathField.getText());
                config.setFileNameFormat(fileNameFormatField.getText());
                config.setStartWithSystem(startWithSystemCheck.isSelected());
                config.setPlayMusicAfterUpload(playMusicAfterUploadCheck.isSelected());
                config.setShowNotifyAfterUpload(showNotifyAfterUploadCheck.isSelected());
                Object obj = uploadChoice.getSelectedItem();
                if (obj != null)
                    config.setDefaultUpload((String) obj);
                config.save();
                SystemMenuSelector.restart();
                setVisible(false);
            }

        });

        imgurAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ImgurAPI api = new ImgurAPI();
                try {
                    api.auth();
                    String pin = JOptionPane.showInputDialog("请输入认证后的PIN码");
                    if (pin == null || pin == "") {
                        return;
                    }
                    ImgurAPI.AccessToken token = api.pinToToken(pin);
                    config.getImgurConfig().setAccessToken(token.getAccess_token());
                    config.getImgurConfig().setRefreshToken(token.getRefresh_token());
                    config.getImgurConfig().setDate(new Date());
                    config.getImgurConfig().updateProperties(config.getProperties());
                    config.save();
                    imgurAuthButton.setEnabled(false);
                    imgurRemoveAuthButton.setEnabled(true);
                    imgurAuthLabel.setText("已连接");
                    uploadChoice.addItem("imgur");
                } catch (UploadException e1) {
                    logger.error("认证错误:" + e1.getMessage());
                    MessageSender.getInstance().showDialog("认证错误:" + e1.getMessage());
                }
            }

        });

        imgurRemoveAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.getImgurConfig().removeFromProperties(config.getProperties());
                config.save();
                imgurAuthButton.setEnabled(true);
                imgurRemoveAuthButton.setEnabled(false);
                imgurAuthLabel.setText("未连接");
                uploadChoice.removeItem("imgur");
            }

        });

        dropboxAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DropboxAPI api = new DropboxAPI();
                try {
                    api.auth();
                    String pin = JOptionPane.showInputDialog("请输入认证码");
                    if (pin == null || pin == "") {
                        return;
                    }
                    DropboxAPI.AccessToken token = api.pinToToken(pin);
                    config.getDropboxConfig().setAccessToken(token.getAccess_token());

                    config.getDropboxConfig().setUid(token.getUid());
                    config.getDropboxConfig().updateProperties(config.getProperties());
                    config.save();
                    dropboxAuthButton.setEnabled(false);
                    dropboxRemoveAuthButton.setEnabled(true);
                    dropboxAuthLabel.setText("已连接");
                    uploadChoice.addItem("dropbox");
                } catch (UploadException e1) {
                    logger.error("认证错误:" + e1.getMessage());
                    MessageSender.getInstance().showDialog("认证错误:" + e1.getMessage());
                }
            }

        });

        dropboxRemoveAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.getDropboxConfig().removeFromProperties(config.getProperties());
                config.save();
                dropboxAuthButton.setEnabled(true);
                dropboxRemoveAuthButton.setEnabled(false);
                dropboxAuthLabel.setText("未连接");
                uploadChoice.removeItem("dropbox");
            }

        });
        gDriveAuthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GDriveAPI api = new GDriveAPI();
                    api.auth();
                    String pin = JOptionPane.showInputDialog("请输入code");
                    if (pin == null || pin == "") {
                        return;
                    }
                    GDriveAPI.AccessToken token = api.pinToToken(pin);
                    config.getGdriveConfig().setAccessToken(token.getAccess_token());
                    config.getGdriveConfig().setRefreshToken(token.getRefresh_token());
                    config.getGdriveConfig().updateProperties(config.getProperties());
                    config.save();
                    gDriveAuthLabel.setText("已连接");
                    uploadChoice.addItem("gdrive");
                    gDriveAuthButton.setEnabled(false);
                    gDriveRemoveAuthButton.setEnabled(true);
                } catch (UploadException e1) {
                    logger.error("认证错误:" + e1.getMessage());
                    MessageSender.getInstance().showDialog("认证错误:" + e1.getMessage());
                }
            }
        });
        gDriveRemoveAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.getGdriveConfig().removeFromProperties(config.getProperties());
                config.save();
                gDriveAuthButton.setEnabled(true);
                gDriveRemoveAuthButton.setEnabled(false);
                gDriveAuthLabel.setText("未连接");
                uploadChoice.removeItem("gdrive");
            }
        });

        imageCloudAuthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ImageCloudAPI api = new ImageCloudAPI();
                    api.auth();

                    if (api.getId() != null) {
                        config.getImageCloudConfig().setToken(api.getTokenStr());
                        config.getImageCloudConfig().setEmail(api.getEmail());
                        config.getImageCloudConfig().updateProperties(config.getProperties());
                        config.save();
                        imageCloudAuthLabel.setText("已连接");
                        uploadChoice.addItem("screenshot.pics");
                        imageCloudAuthButton.setEnabled(false);
                        imageCloudRemoveAuthButton.setEnabled(true);
                    }
                } catch (UploadException e1) {
                    logger.error("认证错误:" + e1.getMessage());
                    MessageSender.getInstance().showDialog("认证错误:" + e1.getMessage());
                }
            }
        });
        imageCloudRemoveAuthButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                config.getImageCloudConfig().removeFromProperties(config.getProperties());
                config.save();
                imageCloudAuthButton.setEnabled(true);
                imageCloudRemoveAuthButton.setEnabled(false);
                imageCloudAuthLabel.setText("未连接");
                uploadChoice.removeItem("screenshot.pics");
            }
        });
        fullShotKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new KeyListenPanel(jframe, "full");
            }
        });

        selectShotKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new KeyListenPanel(jframe, "select");
            }
        });

    }

    public ConfigFrame(Config config) {
        super("设置");
        setConfig(config);
        this.useSystemUI();
        this.initComponents();
        this.initComposition();
        this.initButton();
        this.addcomponents();
        this.addListeners();
        this.initFatherFrame();
        this.jframe = this;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConfigFrame(new Config());
            }
        });
    }
}
