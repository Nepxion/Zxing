package com.nepxion.zxing.core;

/**
 * <p>Title: Nepxion Zxing</p>
 * <p>Description: Nepxion Zxing QR Code</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
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
import com.nepxion.zxing.util.ZxingUtil;

/**
 * 相关参数说明
 * text              二维码/条形码内容。二维码可以是文字，也可以是URL，条形码必须是数字
 * format            二维码/条形码图片格式，例如jpg，png
 * encoding          二维码/条形码内容编码，例如UTF-8
 * correctionLevel   二维码/条形码容错等级，例如ErrorCorrectionLevel.H(30%纠正率)，ErrorCorrectionLevel.Q(25%纠正率)，ErrorCorrectionLevel.M(15%纠正率)，ErrorCorrectionLevel.L(7%纠正率)。纠正率越高，扫描速度越慢
 * width             二维码/条形码图片宽度
 * height            二维码/条形码图片高度
 * margin            二维码/条形码图片白边大小，取值范围0~4
 * foregroundColor   二维码/条形码图片前景色。格式如0xFF000000
 * backgroundColor   二维码/条形码图片背景色。格式如0xFFFFFFFF
 * deleteWhiteBorder 二维码图片白边去除。当图片面积较小时候，可以利用该方法扩大二维码/条形码的显示面积
 * logoFile          二维码Logo图片的文件，File对象。显示在二维码中间的Logo图片，其在二维码中的尺寸最大为100x100左右，否则会覆盖二维码导致最后不能被识别
 * logoBytes         二维码Logo图片的字节数组。方便于使用者在外部传入，当logoBytes不为null的时候，使用logoBytes做为Logo图片渲染，当为null的时候采用上面的logoFile做为Logo图片渲染
 * outputFile        二维码/条形码图片的导出文件，File对象
 */
public class ZxingEncoder {
    private static final Logger LOG = LoggerFactory.getLogger(ZxingEncoder.class);

    public InputStream encodeForInputStream(ZxingEntity zxingEntity) {
        byte[] bytes = encodeForBytes(zxingEntity);

        return new ByteArrayInputStream(bytes);
    }

    public byte[] encodeForBytes(ZxingEntity zxingEntity) {
        BarcodeFormat barcodeFormat = zxingEntity.getBarcodeFormat();
        String text = zxingEntity.getText();
        String format = zxingEntity.getFormat();
        String encoding = zxingEntity.getEncoding();
        ErrorCorrectionLevel correctionLevel = zxingEntity.getCorrectionLevel();
        int width = zxingEntity.getWidth();
        int height = zxingEntity.getHeight();
        int margin = zxingEntity.getMargin();
        int foregroundColor = zxingEntity.getForegroundColor();
        int backgroundColor = zxingEntity.getBackgroundColor();
        boolean deleteWhiteBorder = zxingEntity.isDeleteWhiteBorder();
        boolean hasLogo = zxingEntity.hasLogo();

        if (barcodeFormat == null) {
            throw new ZxingException("Barcode format is null");
        }

        if (StringUtils.isEmpty(text)) {
            throw new ZxingException("Text is null or empty");
        }

        if (StringUtils.isEmpty(format)) {
            throw new ZxingException("Format is null or empty");
        }

        if (StringUtils.isEmpty(encoding)) {
            throw new ZxingException("Encoding is null or empty");
        }

        if (correctionLevel == null) {
            throw new ZxingException("Correction level is null");
        }

        if (width <= 0) {
            throw new ZxingException("Invalid width=" + width);
        }

        if (height <= 0) {
            throw new ZxingException("Invalid height=" + height);
        }

        if (margin < 0 || margin > 4) {
            throw new ZxingException("Invalid margin=" + margin + ", it must be [0, 4]");
        }

        ByteArrayOutputStream outputStream = null;
        try {
            Map<EncodeHintType, Object> hints = createHints(encoding, correctionLevel, margin);
            MultiFormatWriter formatWriter = new MultiFormatWriter();

            MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
            BitMatrix bitMatrix = formatWriter.encode(text, barcodeFormat, width, height, hints);

            // 删除二维码四周的白边
            if (barcodeFormat == BarcodeFormat.QR_CODE && deleteWhiteBorder) {
                bitMatrix = deleteWhiteBorder(bitMatrix);
            }

            outputStream = new ByteArrayOutputStream();

            // 先输出Logo
            if (barcodeFormat == BarcodeFormat.QR_CODE && hasLogo) {
                BufferedImage logoImage = createLogoImage(bitMatrix, foregroundColor, backgroundColor, zxingEntity);

                if (!ImageIO.write(logoImage, format, outputStream)) {
                    throw new ZxingException("Failed to write logo image");
                }
            }

            // 再输出二维码/条形码
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream, imageConfig);

            return outputStream.toByteArray();
        } catch (WriterException e) {
            LOG.error("Encode stream error", e);
            throw new ZxingException("Encode stream error", e);
        } catch (IOException e) {
            LOG.error("Encode stream error", e);
            throw new ZxingException("Encode stream error", e);
        } finally {
            if (outputStream != null) {
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    public File encodeForFile(ZxingEntity zxingEntity) {
        BarcodeFormat barcodeFormat = zxingEntity.getBarcodeFormat();
        String text = zxingEntity.getText();
        String format = zxingEntity.getFormat();
        String encoding = zxingEntity.getEncoding();
        ErrorCorrectionLevel correctionLevel = zxingEntity.getCorrectionLevel();
        int width = zxingEntity.getWidth();
        int height = zxingEntity.getHeight();
        int margin = zxingEntity.getMargin();
        int foregroundColor = zxingEntity.getForegroundColor();
        int backgroundColor = zxingEntity.getBackgroundColor();
        boolean deleteWhiteBorder = zxingEntity.isDeleteWhiteBorder();
        boolean hasLogo = zxingEntity.hasLogo();
        File outputFile = zxingEntity.getOutputFile();

        if (barcodeFormat == null) {
            throw new ZxingException("Barcode format is null");
        }

        if (StringUtils.isEmpty(text)) {
            throw new ZxingException("Text is null or empty");
        }

        if (StringUtils.isEmpty(format)) {
            throw new ZxingException("Format is null or empty");
        }

        if (StringUtils.isEmpty(encoding)) {
            throw new ZxingException("Encoding is null or empty");
        }

        if (correctionLevel == null) {
            throw new ZxingException("Correction level is null");
        }

        if (width <= 0) {
            throw new ZxingException("Invalid width=" + width);
        }

        if (height <= 0) {
            throw new ZxingException("Invalid height=" + height);
        }

        if (margin < 0 || margin > 4) {
            throw new ZxingException("Invalid margin=" + margin + ", it must be [0, 4]");
        }

        if (outputFile == null) {
            throw new ZxingException("Output file is null");
        }

        try {
            Map<EncodeHintType, Object> hints = createHints(encoding, correctionLevel, margin);
            MultiFormatWriter formatWriter = new MultiFormatWriter();

            MatrixToImageConfig imageConfig = new MatrixToImageConfig(foregroundColor, backgroundColor);
            BitMatrix bitMatrix = formatWriter.encode(text, barcodeFormat, width, height, hints);

            // 删除二维码四周的白边
            if (barcodeFormat == BarcodeFormat.QR_CODE && deleteWhiteBorder) {
                bitMatrix = deleteWhiteBorder(bitMatrix);
            }

            ZxingUtil.createDirectory(outputFile);

            // 先输出二维码/条形码
            MatrixToImageWriter.writeToPath(bitMatrix, format, outputFile.toPath(), imageConfig);

            // 再输出Logo
            if (barcodeFormat == BarcodeFormat.QR_CODE && hasLogo) {
                BufferedImage logoImage = createLogoImage(bitMatrix, foregroundColor, backgroundColor, zxingEntity);

                if (!ImageIO.write(logoImage, format, outputFile)) {
                    throw new ZxingException("Failed to write logo image");
                }
            }
        } catch (WriterException e) {
            LOG.error("Encode file=[{}] error", outputFile.getPath(), e);
            throw new ZxingException("Encode file=[" + outputFile.getPath() + "] error", e);
        } catch (IOException e) {
            LOG.error("Encode file=[{}] error", outputFile.getPath(), e);
            throw new ZxingException("Encode file=[" + outputFile.getPath() + "] error", e);
        }

        return outputFile;
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

    private BufferedImage createLogoImage(BitMatrix bitMatrix, int foregroundColor, int backgroundColor, ZxingEntity zxingEntity) throws IOException {
        BufferedImage image = createBufferedImage(bitMatrix, foregroundColor, backgroundColor);
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
        File logoFile = zxingEntity.getLogoFile();
        InputStream logoInputStream = zxingEntity.getLogoInputStream();

        Image logoImage = null;
        if (logoInputStream != null) {
            logoImage = ImageIO.read(logoInputStream);
        } else {
            logoImage = ImageIO.read(logoFile);
        }

        int logoWidth = logoImage.getWidth(null) > ratioWidth ? ratioWidth : logoImage.getWidth(null);
        int logoHeight = logoImage.getHeight(null) > ratioHeight ? ratioHeight : logoImage.getHeight(null);

        int x = (image.getWidth() - logoWidth) / 2;
        int y = (image.getHeight() - logoHeight) / 2;

        g2d.drawImage(logoImage, x, y, logoWidth, logoHeight, null);
        // g2d.drawImage(logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH), x, y, null);
        g2d.setColor(Color.black);
        g2d.setBackground(Color.WHITE);
        g2d.dispose();

        logoImage.flush();

        return image;
    }

    private BufferedImage createBufferedImage(BitMatrix bitMatrix, int foregroundColor, int backgroundColor) {
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