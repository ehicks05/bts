package net.ehicks.bts.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CommonIO
{
    private static final Logger log = LoggerFactory.getLogger(CommonIO.class);

    public static String getName(FileItem fileItem)
    {
        String fileName = fileItem.getName();
        if (fileName != null)
            fileName = FilenameUtils.getName(fileName);
        return fileName == null ? "" : fileName;
    }

    public static byte[] getThumbnail(MultipartFile file) throws IOException
    {
        BufferedImage srcImage = ImageIO.read(file.getInputStream()); // Load image
        String formatName = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return getThumbnailHelper(srcImage, formatName);
    }

    public static byte[] getThumbnail(File file) throws IOException
    {
        BufferedImage srcImage = ImageIO.read(file); // Load image
        String formatName = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        return getThumbnailHelper(srcImage, formatName);
    }

    private static byte[] getThumbnailHelper(BufferedImage srcImage, String formatName) throws IOException
    {
        int targetWidth = Math.min(srcImage.getWidth(), 200);
        int targetHeight = Math.min(srcImage.getHeight(), 200);
        Scalr.Mode mode = srcImage.getWidth() > srcImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH : Scalr.Mode.FIT_TO_HEIGHT;
        BufferedImage scaledImage = Scalr.resize(srcImage, mode, targetWidth, targetHeight); // Scale image
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(scaledImage, formatName, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
