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
import com.nepxion.zxing.entity.ZxingEntity;
import com.nepxion.zxing.exception.ZxingException;
import com.nepxion.zxing.util.ZxingUtils;

/**
 * 相关参数说明
 * text              二维码内容。可以是文字，也可以是URL
 * file              二维码图片的文件，File对象
 * format            二维码图片格式，例如jpg，png
 * encoding          二维码内容编码，例如UTF-8
 * correctionLevel   二维码容错等级，例如ErrorCorrectionLevel.H(30%纠正率)，ErrorCorrectionLevel.Q(25%纠正率)，ErrorCorrectionLevel.M(15%纠正率)，ErrorCorrectionLevel.L(7%纠正率)。纠正率越高，扫描速度越慢
 * width             二维码图片宽度
 * height            二维码图片高度
 * margin            二维码图片白边大小，取值范围0~4
 * foregroundColor   二维码图片前景色。格式如0xFF000000
 * backgroundColor   二维码图片背景色。格式如0xFFFFFFFF
 * deleteWhiteBorder 二维码图片白边去除。当图片面积较小时候，可以利用该方法扩大二维码的显示面积
 * logoPath          二维码Logo图片。显示在二维码中间的Logo图片，其在二维码中的尺寸最大为100x100左右，否则会覆盖二维码导致最后不能被识别
 */
public class ZxingEncoder {
    private static final Logger LOG = LoggerFactory.getLogger(ZxingEncoder.class);

    public InputStream encodeForInputStream(ZxingEntity zxingEntity) {
        return encodeForInputStream(zxingEntity.getText(), zxingEntity.getFormat(), zxingEntity.getEncoding(), zxingEntity.getCorrectionLevel(), zxingEntity.getWidth(), zxingEntity.getHeight(), zxingEntity.getMargin(), zxingEntity.getForegroundColor(), zxingEntity.getBackgroundColor(), zxingEntity.isDeleteWhiteBorder(), zxingEntity.getLogoPath());
    }

    public InputStream encodeForInputStream(String text, String format, String encoding, ErrorCorrectionLevel correctionLevel, int width, int height, int margin, int foregroundColor, int backgroundColor, boolean deleteWhiteBorder, String logoPath) {
        byte[] bytes = encodeForBytes(text, format, encoding, correctionLevel, width, height, margin, foregroundColor, backgroundColor, deleteWhiteBorder, logoPath);

        return new ByteArrayInputStream(bytes);
    }

    public byte[] encodeForBytes(ZxingEntity zxingEntity) {
        return encodeForBytes(zxingEntity.getText(), zxingEntity.getFormat(), zxingEntity.getEncoding(), zxingEntity.getCorrectionLevel(), zxingEntity.getWidth(), zxingEntity.getHeight(), zxingEntity.getMargin(), zxingEntity.getForegroundColor(), zxingEntity.getBackgroundColor(), zxingEntity.isDeleteWhiteBorder(), zxingEntity.getLogoPath());
    }

    public byte[] encodeForBytes(String text, String format, String encoding, ErrorCorrectionLevel correctionLevel, int width, int height, int margin, int foregroundColor, int backgroundColor, boolean deleteWhiteBorder, String logoPath) {
        ByteArrayOutputStream outputStream = null;
        try {
            Map<EncodeHintType, Object> hints = createHints(encoding, correctionLevel, margin);
            MultiFormatWriter formatWriter = new MultiFormatWriter();

            MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
            BitMatrix bitMatrix = formatWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            // 删除二维码四周的白边
            if (deleteWhiteBorder) {
                bitMatrix = deleteWhiteBorder(bitMatrix);
            }

            outputStream = new ByteArrayOutputStream();

            // 先输出Logo
            if (StringUtils.isNotEmpty(logoPath)) {
                BufferedImage image = toLogoImage(bitMatrix, foregroundColor, backgroundColor, logoPath);

                if (!ImageIO.write(image, format, outputStream)) {
                    throw new ZxingException("Failed to write logo image");
                }
            }

            // 再输出二维码
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

    public File encodeForFile(ZxingEntity zxingEntity) {
        return encodeForFile(zxingEntity.getText(), zxingEntity.getFile(), zxingEntity.getFormat(), zxingEntity.getEncoding(), zxingEntity.getCorrectionLevel(), zxingEntity.getWidth(), zxingEntity.getHeight(), zxingEntity.getMargin(), zxingEntity.getForegroundColor(), zxingEntity.getBackgroundColor(), zxingEntity.isDeleteWhiteBorder(), zxingEntity.getLogoPath());
    }

    public File encodeForFile(String text, File file, String format, String encoding, ErrorCorrectionLevel correctionLevel, int width, int height, int margin, int foregroundColor, int backgroundColor, boolean deleteWhiteBorder) {
        return encodeForFile(text, file, format, encoding, correctionLevel, width, height, margin, foregroundColor, backgroundColor, deleteWhiteBorder, null);
    }

    public File encodeForFile(String text, File file, String format, String encoding, ErrorCorrectionLevel correctionLevel, int width, int height, int margin, int foregroundColor, int backgroundColor, boolean deleteWhiteBorder, String logoPath) {
        try {
            Map<EncodeHintType, Object> hints = createHints(encoding, correctionLevel, margin);
            MultiFormatWriter formatWriter = new MultiFormatWriter();

            MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
            BitMatrix bitMatrix = formatWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            // 删除二维码四周的白边
            if (deleteWhiteBorder) {
                bitMatrix = deleteWhiteBorder(bitMatrix);
            }

            ZxingUtils.createDirectory(file);

            // 先输出二维码
            MatrixToImageWriter.writeToPath(bitMatrix, format, file.toPath(), imageConfig);

            // 再输出Logo
            if (StringUtils.isNotEmpty(logoPath)) {
                BufferedImage image = toLogoImage(bitMatrix, foregroundColor, backgroundColor, logoPath);

                if (!ImageIO.write(image, format, file)) {
                    throw new ZxingException("Failed to write logo image");
                }
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

    private BitMatrix deleteWhiteBorder(BitMatrix bitMatrix) {
        int[] rectangle = bitMatrix.getEnclosingRectangle();
        int width = rectangle[2] + 1;
        int height = rectangle[3] + 1;

        BitMatrix matrix = new BitMatrix(width, height);
        matrix.clear();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (bitMatrix.get(i + rectangle[0], j + rectangle[1]))
                    matrix.set(i, j);
            }
        }
        return matrix;
    }

    private BufferedImage toLogoImage(BitMatrix bitMatrix, int foregroundColor, int backgroundColor, String logoPath) throws IOException {
        BufferedImage image = toBufferedImage(bitMatrix, foregroundColor, backgroundColor);
        Graphics2D g2d = image.createGraphics();

        int ratioWidth = image.getWidth() * 2 / 10;
        int ratioHeight = image.getHeight() * 2 / 10;

        if (ratioWidth > 100) {
            LOG.warn("Ratio width=[{}] for logo image is more than 100", ratioWidth);
        }

        if (ratioHeight > 100) {
            LOG.warn("Ratio height=[{}] for logo image is more than 100", ratioHeight);
        }

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

        return image;
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

    private Map<EncodeHintType, Object> createHints(String encoding, ErrorCorrectionLevel correctionLevel, int margin) {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, encoding); // 设置编码
        hints.put(EncodeHintType.ERROR_CORRECTION, correctionLevel); // 指定纠错等级    
        hints.put(EncodeHintType.MARGIN, margin); //设置白边

        return hints;
    }
}