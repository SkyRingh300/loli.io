package io.loli.sc.ui.swing;

import io.loli.sc.config.Config;
import io.loli.sc.core.MP3Player;
import io.loli.sc.core.ScreenCaptor;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class SCLauncher {
    private static ConfigFrame configFrame;

    public static void launch(final String type) {
        switch (type) {
        case "option":
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (configFrame == null) {
                        configFrame = new ConfigFrame(new Config());
                    } else {
                        configFrame.setVisible(true);
                    }
                }
            });

            break;
        case "select":
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 如果不延迟会连菜单一起捕捉到
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    DragFrame df = new DragFrame();
                    String s = df.getResult();
                    if (!df.isCanStop()) {
                        ScreenCaptor.copyToClipboard(s);
                        Config config = new Config();
                        if (config.getPlayMusicAfterUpload()) {
                            new MP3Player().play(ClassLoader
                                    .getSystemResourceAsStream("message.mp3"));
                        }
                        while (ifStrinClipboard(s)) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }).start();
            break;
        case "full":
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        // 如果不延迟会连菜单一起捕捉到
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    ScreenCaptor sc = ScreenCaptor.newInstance();
                    String result = sc.getLink();
                    ScreenCaptor.copyToClipboard(result);
                    Config config = new Config();
                    if (config.getPlayMusicAfterUpload()) {
                        new MP3Player().play(ClassLoader
                                .getSystemResourceAsStream("message.mp3"));
                    }
                    while (ifStrinClipboard(result)) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            break;
        }

    }

    public static void main(String[] args) {
        launch(args[0]);
    }

    public static boolean ifStrinClipboard(String str) {
        return getClipboard().equals(str);
    }

    public static String getClipboard() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tra = cb.getContents(null);
        String result = null;
        try {
            result = (String) tra.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
