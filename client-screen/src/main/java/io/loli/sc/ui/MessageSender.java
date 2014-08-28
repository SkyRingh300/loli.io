package io.loli.sc.ui;

import javax.swing.JOptionPane;

public interface MessageSender {
    default public void showInfo(String message) {
    };

    default public void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public static MessageSender getInstance() {
        return new MessageSender() {
        };
    }
}
