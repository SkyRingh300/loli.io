package io.loli.sc.ui.swing;

import io.loli.sc.config.Config;
import io.loli.sc.core.ScreenCaptor;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputAdapter;

public class DragFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JFrame jframe;
    private JPanel imgPanel;
    private JLabel bgLabel;
    private BufferedImage img;
    private DragPanel dragPanel;
    private Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    private BufferedImage subImg = null;
    private volatile String result;

    private void useSystemUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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

    private void fullScreen() {
        setVisible(true);
        validate();
        // 必须加这句否则linux下无法全屏
        dispose();
        setUndecorated(true);
        requestFocus();
        jframe.getGraphicsConfiguration().getDevice()
                .setFullScreenWindow(jframe);
    }

    class DragPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        public DragPanel() {
            super();
            setBackground(new Color(0, 0, 0, 80));
            setPreferredSize(scrSize);
            setVisible(false);
        }

        public void processParentEvent(AWTEvent e) {
            this.processEvent(e);
        }

        public void move(int x, int y) {
            int xx = getX();
            int yy = getY();
            if (xx + x < 0) {
                xx = 0;
                x = 0;
            }
            if (yy + y < 0) {
                yy = 0;
                y = 0;
            }
            if (xx + x > (int) scrSize.getWidth() - getWidth()) {
                xx = (int) scrSize.getWidth() - getWidth();
                x = 0;
            }
            if (yy + y > (int) scrSize.getHeight() - getHeight()) {
                yy = (int) scrSize.getHeight() - getHeight();
                y = 0;
            }

            xx += x;
            yy += y;

            setBounds(xx, yy, getWidth(), getHeight());
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            int a = getX();
            int b = getY();
            int w = getWidth();
            int h = getHeight();
            subImg = img.getSubimage(a, b, w, h);
            originSubImg = originImg.getSubimage((int) (a * bi),
                    (int) (b * bi), (int) (w * bi), (int) (h * bi));
            g.drawImage(subImg, 0, 0, null);
        }

        public void resize() {
            dragPanel.setBounds(Math.min(a1, a2), Math.min(b1, b2),
                    Math.abs(a2 - a1), Math.abs(b2 - b1));
            dragPanel.setVisible(true);
        }
    }

    private void addComponents() {
        addDragPanel();
        addGreyPanel();
    }

    private void addDragPanel() {

        dragPanel = new DragPanel();
        add(dragPanel);

    }

    BufferedImage originImg;
    BufferedImage originSubImg;
    private double bi;
    
    
    private void setBackground() {
        originImg = ScreenCaptor.screenShot();
        
        img = new BufferedImage(Toolkit
                .getDefaultToolkit().getScreenSize().width, Toolkit
                .getDefaultToolkit().getScreenSize().height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D bg = img.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        bg.drawImage(originImg , 0, 0, Toolkit
                .getDefaultToolkit().getScreenSize().width,Toolkit
                .getDefaultToolkit().getScreenSize().height,null);
        bg.dispose();
        bi = ((double) originImg.getWidth()) / img.getWidth();

        ImageIcon background = new ImageIcon(img);
        bgLabel = new JLabel(background);
        bgLabel.setBounds(0, 0, background.getIconWidth(),
                background.getIconHeight());

        imgPanel = (JPanel) jframe.getContentPane();
        imgPanel.setOpaque(false);
        imgPanel.setLayout(null);

        getLayeredPane().setLayout(null);
        getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
    }

    private JPanel greyPanel;

    public void addGreyPanel() {
        greyPanel = new JPanel();
        this.getContentPane().add(greyPanel);
        greyPanel.setBackground(new Color(0, 0, 0, 60));
        greyPanel.setPreferredSize(scrSize);
        greyPanel.setVisible(true);
        greyPanel.setBounds(0, 0, (int) scrSize.getWidth(),
                (int) scrSize.getHeight());
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        this.jframe = this;
        setLayout(new FlowLayout());
        this.setBackground();
    }

    int m1, n1;
    int m2, n2;

    int a1, b1;
    int a2, b2;

    private void addListeners() {

        greyPanel.addMouseMotionListener(new DrawListener());
        greyPanel.addMouseListener(new DrawListener());

        dragPanel.addMouseListener(new DragListener());
        dragPanel.addMouseMotionListener(new DragListener());
        jframe.addKeyListener(new CostumKeyListener());

    }

    private boolean canStop = false;

    class CostumKeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (dragPanel.isVisible()) {
                    dragPanel.setVisible(false);
                } else {
                    canStop = true;
                    jframe.remove(dragPanel);
                    jframe.dispose();

                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                new Thread(new UploadRun()).start();
                jframe.dispose();
            }
        }
    }

    class UploadRun implements Runnable {
        @Override
        public void run() {
            ScreenCaptor sc = ScreenCaptor.newInstance(false);
            Config config = new Config();
            sc.setConfig(config);
            result = sc.uploadImage(config.getDefaultUpload(),
                    getOriginSubImg());
        }
    }

    class DrawListener extends MouseInputAdapter {
        boolean dragged = false;

        @Override
        public void mousePressed(MouseEvent e) {
            if (!e.isMetaDown()) {
                a1 = e.getX();
                b1 = e.getY();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!e.isMetaDown()) {
                if (!dragged)
                    dragged = true;
                a2 = e.getX();
                b2 = e.getY();
                dragPanel.resize();
                jframe.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!e.isMetaDown()) {
                a2 = e.getX();
                b2 = e.getY();
                if (dragged)
                    dragPanel.resize();
                jframe.repaint();
            } else {
                if (dragPanel.isVisible()) {
                    dragPanel.setVisible(false);
                } else {
                    canStop = true;
                    jframe.remove(dragPanel);
                    jframe.dispose();

                }
            }
        }
    }

    class DragListener extends MouseInputAdapter {
        boolean dragged = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!e.isMetaDown()) {

                if (e.getClickCount() == 2) {
                    new Thread(new UploadRun()).start();
                    jframe.dispose();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!e.isMetaDown()) {

                m1 = e.getX();
                n1 = e.getY();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!e.isMetaDown()) {

                m2 = e.getX();
                n2 = e.getY();
                if (!dragged)
                    dragged = true;
                if (dragged)
                    dragPanel.move(m2 - m1, n2 - n1);
                jframe.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!e.isMetaDown()) {
                m2 = e.getX();
                n2 = e.getY();
                if (dragged)
                    dragPanel.move(m2 - m1, n2 - n1);
                jframe.repaint();
            } else {
                if (dragPanel.isVisible()) {
                    dragPanel.setVisible(false);
                } else {
                    canStop = true;
                    jframe.remove(dragPanel);
                    jframe.dispose();
                }
            }
        }

    }

    public DragFrame() {
        this.useSystemUI();
        this.init();
        this.addComponents();
        this.addListeners();
        this.fullScreen();
    }

    public String getResult() {
        while ((result == null || result.equals("")) && !canStop) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public BufferedImage getOriginSubImg() {
        return originSubImg;
    }

    public void setOriginSubImg(BufferedImage originSubImg) {
        this.originSubImg = originSubImg;
    }

    public BufferedImage getSubImg() {
        return subImg;
    }

    public void setSubImg(BufferedImage subImg) {
        this.subImg = subImg;
    }

    public boolean isCanStop() {
        return canStop;
    }
}
