package io.loli.sc.server.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ThumbnailUtil {
    public final static int SMALL_SIZE = 150;
    public final static int MIDDLE_SIZE = 300;
    public final static int LARGE_SIZE = 600;

    public static OutputStream formatSmall(InputStream is, String format) throws IOException {
        return format(is, SMALL_SIZE, SMALL_SIZE, format);
    }

    public static OutputStream format(InputStream is, int thumbWidth, int thumbHeight, String format)
        throws IOException {
        BufferedImage image = ImageIO.read(is);

        double thumbRatio = (double) thumbWidth / (double) thumbHeight;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double imageRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < imageRatio) {
            thumbHeight = (int) (thumbWidth / imageRatio);
        } else {
            thumbWidth = (int) (thumbHeight * imageRatio);
        }

        if (imageWidth < thumbWidth && imageHeight < thumbHeight) {
            thumbWidth = imageWidth;
            thumbHeight = imageHeight;
        } else if (imageWidth < thumbWidth)
            thumbWidth = imageWidth;
        else if (imageHeight < thumbHeight)
            thumbHeight = imageHeight;

        Image img = image.getScaledInstance(thumbWidth, thumbHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bufImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufImg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(bufImg, format, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;
    }

    public static void main(String[] args) throws IOException {
        Files.write(
            new File("/tmp/test.png").toPath(),
            ((ByteArrayOutputStream) cutSqureWithFormat(new FileInputStream(
                "/home/choco/images/weibo/79c1fb50gw1eizrrfj5wdj20qu18bn2q (2).jpg"), 150, 150, "png")).toByteArray());
    }

    public static OutputStream formatMiddle(InputStream is, String format) throws IOException {
        return format(is, MIDDLE_SIZE, MIDDLE_SIZE, format);
    }

    public static OutputStream formatLarge(InputStream is, String format) throws IOException {
        return format(is, MIDDLE_SIZE, SMALL_SIZE, format);
    }

    // 该方法未测试
    public static OutputStream cut(InputStream is, String format, int x, int y, int width, int height)
        throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> itr = ImageIO.getImageReaders(iis);
        ImageReader reader = itr.next();
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Rectangle rect = new Rectangle(x, y, width, height);
        param.setSourceRegion(rect);
        BufferedImage bi = reader.read(0, param);
        // 保存新图片
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, format, os);

        return os;
    }

    public static OutputStream cutSqure(InputStream is, String format) throws IOException {
        BufferedImage image = ImageIO.read(is);
        int w = image.getWidth();
        int h = image.getHeight();
        if (w > h) {
            int x = (w - h) / 2;
            int y = 0;
            int width = h;
            int height = h;
            return cut(is, format, x, y, width, height);
        } else {
            int x = 0;
            int y = (h - w) / 2;
            int width = w;
            int height = w;
            return cut(is, format, x, y, width, height);
        }
    }

    public static OutputStream cutSqureWithFormatSmall(InputStream is, String format) throws IOException {
        return cutSqureWithFormat(is, SMALL_SIZE, SMALL_SIZE, format);
    }

    public static OutputStream cutSqureWithFormat(InputStream is, int thumbWidth, int thumbHeight, String format)
        throws IOException {
        BufferedImage image = ImageIO.read(is);

        double thumbRatio = (double) thumbWidth / (double) thumbHeight;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double imageRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < imageRatio) {
            thumbHeight = (int) (thumbWidth / imageRatio);
        } else {
            thumbWidth = (int) (thumbHeight * imageRatio);
        }

        if (imageWidth < thumbWidth && imageHeight < thumbHeight) {
            thumbWidth = imageWidth;
            thumbHeight = imageHeight;
        } else if (imageWidth < thumbWidth)
            thumbWidth = imageWidth;
        else if (imageHeight < thumbHeight)
            thumbHeight = imageHeight;

        int w = thumbWidth;
        int h = thumbHeight;
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;
        if (w > h) {
            x = (w - h) / 2;
            y = 0;
            width = h;
            height = h;
        } else {
            x = 0;
            y = (h - w) / 2;
            width = w;
            height = w;
        }

        Image img = image.getScaledInstance(thumbWidth, thumbHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufImg.createGraphics();
        g.drawImage(img, -x, -y, null);
        g.dispose();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            javax.imageio.ImageIO.write(bufImg, format, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;

    }
}
