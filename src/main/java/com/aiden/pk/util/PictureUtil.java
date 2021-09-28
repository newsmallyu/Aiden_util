package com.aiden.pk.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author ay05
 */
public class PictureUtil {

    private static final String IMAGE_FILE_PATH = "H:\\logx.png";
       private static final int SEP_X_NUM = 1;
       private static final int SEP_Y_NUM = 3;

    public static void main(String[] args) throws Exception {
       cutPic();
    }

    /**
     * 分割图片
     * @return
     * @throws Exception
     */
    public static String cutPic() throws Exception {
        File file = new File(IMAGE_FILE_PATH);
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("file not exists or un-file: "+ IMAGE_FILE_PATH);
        }
        BufferedImage image = ImageIO.read(file);
        int totalWidth = image.getWidth();
        int totalHeight = image.getHeight();
        int width = totalWidth / SEP_X_NUM;
        int height = totalHeight / SEP_Y_NUM;
        File dirFile = new File(file.getParent(), file.getName().substring(0, file.getName().lastIndexOf(".")));
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        for (int y = 0, j = 1; y <= totalHeight - height; y += height, j++) {
            for (int x = 0, i = 1; x <= totalWidth - width; x += width, i++) {
                File targetFile = new File(dirFile, j + "_ "+ i + ".jpg");
                BufferedImage targetImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = targetImage.getGraphics();
                g.drawImage(image.getSubimage(x, y, width, height), 0, 0, null);
                ImageIO.write(targetImage, "JPG", targetFile);
            }
        }
        return dirFile.getPath();

    }
}
