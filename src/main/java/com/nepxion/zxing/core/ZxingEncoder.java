package com.nepxion.zxing.core;

/**
 * <p>Title: Nepxion Zxing</p>
 * <p>Description: Nepxion Zxing QR Code</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nepxion.zxing.exception.ZxingException;

public class ZxingEncoder {
    private static final Logger LOG = LoggerFactory.getLogger(ZxingEncoder.class);

    public InputStream encodeForInputStream(String text, String format, String encoding, ErrorCorrectionLevel level, int width, int height, int margin, int foregroundColor, int backgroundColor) {
        byte[] bytes = encodeForBytes(text, format, encoding, level, width, height, margin, foregroundColor, backgroundColor);

        return new ByteArrayInputStream(bytes);
    }

    public byte[] encodeForBytes(String text, String format, String encoding, ErrorCorrectionLevel level, int width, int height, int margin, int foregroundColor, int backgroundColor) {
        ByteArrayOutputStream outputStream = null;
        try {
            Map<EncodeHintType, Object> hints = createHints(encoding, level, margin);
            MultiFormatWriter formatWriter = new MultiFormatWriter();

            MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
            BitMatrix bitMatrix = formatWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream, imageConfig);

            return outputStream.toByteArray();
        } catch (WriterException e) {
            LOG.error("Encode stream error", e);
            throw new ZxingException("Encode stream error");
        } catch (IOException e) {
            LOG.error("Encode stream error", e);
            throw new ZxingException("Encode stream error");
        } finally {
            if (outputStream != null) {
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    public File encodeForFile(String text, File file, String format, String encoding, ErrorCorrectionLevel level, int width, int height, int margin, int foregroundColor, int backgroundColor) {
        return encodeForFile(text, file, format, encoding, level, width, height, margin, foregroundColor, backgroundColor, null);
    }

    /**
     * 产生二维码图片的文件
     * @param text 二维码内容
     * @param file 二维码图片路径和名称
     * @param format 二维码图片格式
     * @param encoding 二维码内容编码
     * @param level 二维码容错率
     * @param width 二维码图片宽度
     * @param height 二维码图片高度
     * @param margin 二维码图片白边大小，取值范围0~4
     * @param foregroundColor 二维码图片前景色
     * @param backgroundColor 二维码图片背景色
     * @param logoPath 二维码Logo图片
     * @return
     */
    public File encodeForFile(String text, File file, String format, String encoding, ErrorCorrectionLevel level, int width, int height, int margin, int foregroundColor, int backgroundColor, String logoPath) {
        try {
            Map<EncodeHintType, Object> hints = createHints(encoding, level, margin);
            MultiFormatWriter formatWriter = new MultiFormatWriter();

            MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
            BitMatrix bitMatrix = formatWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            MatrixToImageWriter.writeToPath(bitMatrix, format, file.toPath(), imageConfig);

            if (StringUtils.isNotEmpty(logoPath)) {
                writeLogoFile(bitMatrix, file, format, foregroundColor, backgroundColor, logoPath);
            }
        } catch (WriterException e) {
            LOG.error("Encode file=[{}] error", file.getPath(), e);
            throw new ZxingException("Encode file=[" + file.getPath() + "] error");
        } catch (IOException e) {
            LOG.error("Encode file=[{}] error", file.getPath(), e);
            throw new ZxingException("Encode file=[" + file.getPath() + "] error");
        }

        return file;
    }

    private void writeLogoFile(BitMatrix bitMatrix, File file, String format, int foregroundColor, int backgroundColor, String logoPath) throws IOException {
        BufferedImage image = toBufferedImage(bitMatrix, foregroundColor, backgroundColor);
        Graphics2D g2d = image.createGraphics();

        int ratioWidth = image.getWidth() * 2 / 10;
        int ratioHeight = image.getHeight() * 2 / 10;

        // 载入Logo
        File logoFile = new File(logoPath);
        Image logoImage = ImageIO.read(logoFile);
        int logoWidth = logoImage.getWidth(null) > ratioWidth ? ratioWidth : logoImage.getWidth(null);
        int logoHeight = logoImage.getHeight(null) > ratioHeight ? ratioHeight : logoImage.getHeight(null);

        int x = (image.getWidth() - logoWidth) / 2;
        int y = (image.getHeight() - logoHeight) / 2;

        g2d.drawImage(logoImage, x, y, logoWidth, logoHeight, null);
        g2d.setColor(Color.black);
        g2d.setBackground(Color.WHITE);
        g2d.dispose();
        logoImage.flush();

        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    private BufferedImage toBufferedImage(BitMatrix bitMatrix, int foregroundColor, int backgroundColor) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? foregroundColor : backgroundColor);
            }
        }

        return image;
    }

    private Map<EncodeHintType, Object> createHints(String encoding, ErrorCorrectionLevel level, int margin) {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, encoding); // 设置编码
        hints.put(EncodeHintType.ERROR_CORRECTION, level); // 指定纠错等级    
        hints.put(EncodeHintType.MARGIN, margin); //设置白边

        return hints;
    }
}