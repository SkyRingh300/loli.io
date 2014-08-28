package io.loli.sc.ui.swing;

import io.loli.sc.ui.SystemMenu;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import tray.SystemTrayAdapter;
import tray.SystemTrayProvider;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

public class SwingSystemMenu extends ApplicationAdapter implements SystemMenu {
    public void handleOpenFile(ApplicationEvent e) {
        System.out.println("got handleOpenFile event" + " " + e.getFilename());
    }

    public void handleOpenApplication(ApplicationEvent e) {
        System.out.println("got handleOpenApplication event");
    }

    private PopupMenu generateMenu() {
        // 创建弹出菜单
        PopupMenu popup = new PopupMenu();
        // 以下将各个菜单项加入到弹出菜单中
        MenuItem full = new MenuItem("全屏截图");
        full.addActionListener(listener);
        popup.add(full);
        MenuItem select = new MenuItem("选择截图");
        select.addActionListener(listener);
        popup.add(select);
        MenuItem settings = new MenuItem("设置");
        settings.addActionListener(listener);
        popup.add(settings);
        MenuItem quit = new MenuItem("退出");
        quit.addActionListener(listener);
        popup.add(quit);
        return popup;
    }

    private ActionListener listener;
    private TrayIcon trayIcon;

    @Override
    public void run() {
        // 判断该系统是否支持"系统托盘"
        if (SystemTray.isSupported()) {
            listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String cmd = e.getActionCommand();// 获得托盘的事件或点击图标弹出菜单的菜单项
                    switch (cmd) {
                    case "全屏截图":
                        SCLauncher.launch("full");
                        break;
                    case "选择截图":
                        SCLauncher.launch("select");
                        break;
                    case "设置":
                        SCLauncher.launch("option");
                        break;
                    case "退出":
                        System.exit(0);
                    }
                }
            };

            String osname = System.getProperty("os.name").toLowerCase();
            if (osname.indexOf("linux") >= 0) {
                SystemTrayAdapter trayAdapter = new SystemTrayProvider()
                        .getSystemTray();
                URL imageUrl = ClassLoader.getSystemResource("icon.png");
                String tooltip = "ImageCloud";
                trayAdapter.createAndAddTrayIcon(imageUrl, tooltip,
                        this.generateMenu());
            } else {
                SystemTray tray = SystemTray.getSystemTray();
                // 系统托盘对象
                Image image = null;
                try {
                    image = ImageIO.read(ClassLoader
                            .getSystemResourceAsStream("icon.png"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                trayIcon = new TrayIcon(image, "sc-java", this.generateMenu());
                trayIcon.setImageAutoSize(true);
                trayIcon.addActionListener(listener);
                trayIcon.addMouseMotionListener(new MouseAdapter() {
                    public void mouseDragged(MouseEvent e) {

                    }
                });
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    // TODO
                }

            }

        } else {
            JOptionPane.showMessageDialog(null,
                    "your system do not support system tray");
        }

    }

}
