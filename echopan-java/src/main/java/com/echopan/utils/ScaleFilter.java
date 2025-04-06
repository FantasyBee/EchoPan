package com.echopan.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;

public class ScaleFilter {
    private static final Logger logger = LoggerFactory.getLogger(ScaleFilter.class);


    public static Boolean createThumbnailWidthFFmpeg(File file, int thumbnailWidth, File targetFile, Boolean delSource) {
        try {
            BufferedImage src = ImageIO.read(file);
            //thumbnailWidth 缩略图的宽度   thumbnailHeight 缩略图的高度
            int sorceW = src.getWidth();
            int sorceH = src.getHeight();
            //小于 指定高宽不压缩
            if (sorceW <= thumbnailWidth) {
                return false;
            }
            compressImage(file, thumbnailWidth, targetFile, delSource);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void compressImageWidthPercentage(File sourceFile, BigDecimal widthPercentage, File targetFile) {
        try {
            BigDecimal widthResult = widthPercentage.multiply(new BigDecimal(ImageIO.read(sourceFile).getWidth()));
            compressImage(sourceFile, widthResult.intValue(), targetFile, true);
        } catch (Exception e) {
            logger.error("压缩图片失败");
        }
    }

    public static void createCover4Video(File sourceFile, Integer width, File targetFile) {
        try {
            String cmd = "ffmpeg -i %s -y -vframes 1 -vf scale=%d:%d/a %s";
            ProcessUtils.executeCommand(String.format(cmd, sourceFile.getAbsoluteFile(), width, width, targetFile.getAbsoluteFile()), false);
        } catch (Exception e) {
            logger.error("生成视频封面失败", e);
        }
    }

    public static void compressImage(File sourceFile, Integer width, File targetFile, Boolean delSource) {
        try {
            String cmd = "ffmpeg -i %s -vf scale=%d:-1 %s -y";
            ProcessUtils.executeCommand(String.format(cmd, sourceFile.getAbsoluteFile(), width, targetFile.getAbsoluteFile()), false);
            if (delSource) {
                FileUtils.forceDelete(sourceFile);
            }
        } catch (Exception e) {
            logger.error("压缩图片失败");
        }
    }

    public static void main(String[] args) {
        compressImageWidthPercentage(new File("C:\\Users\\Administrator\\Pictures\\微信图片_20230107141436.png"), new BigDecimal(0.7),
                new File("C:\\Users\\Administrator" +
                        "\\Pictures" +
                        "\\微信图片_202106281029182.jpg"));
    }
}