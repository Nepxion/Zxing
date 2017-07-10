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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.nepxion.zxing.exception.ZxingException;

public class ZxingGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(ZxingGenerator.class);

    public static InputStream encodeForInputStream(String text, String format, String encoding, int width, int height) {
        byte[] bytes = encodeForBytes(text, format, encoding, width, height);

        return new ByteArrayInputStream(bytes);
    }

    public static byte[] encodeForBytes(String text, String format, String encoding, int width, int height) {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, encoding);

        ByteArrayOutputStream outputStream = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);

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

    public static File encodeForFile(String text, File file, String format, String encoding, int width, int height) {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, encoding);

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            MatrixToImageWriter.writeToPath(bitMatrix, format, file.toPath());
        } catch (WriterException e) {
            LOG.error("Encode file=[{}] error", file.getPath(), e);
            throw new ZxingException("Encode file=[" + file.getPath() + "] error");
        } catch (IOException e) {
            LOG.error("Encode file=[{}] error", file.getPath(), e);
            throw new ZxingException("Encode file=[" + file.getPath() + "] error");
        }

        return file;
    }

    public static Result decodeByFile(File file, String encoding) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            LOG.error("Decode file=[{}] error", file.getPath(), e);
            throw new ZxingException("Decode file=[" + file.getPath() + "] error");
        }

        try {
            return decode(image, encoding);
        } catch (NotFoundException e) {
            LOG.error("Decode file=[{}] error", file.getPath(), e);
            throw new ZxingException("Decode file=[" + file.getPath() + "] error");
        }
    }

    public static Result decodeByInputStream(InputStream inputStream, String encoding) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            LOG.error("Decode stream error", e);
            throw new ZxingException("Decode stream error");
        }

        try {
            return decode(image, encoding);
        } catch (NotFoundException e) {
            LOG.error("Decode stream error", e);
            throw new ZxingException("Decode stream error");
        }
    }

    public static Result decodeByURL(URL url, String encoding) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            LOG.error("Decode url error", e);
            throw new ZxingException("Decode url error");
        }

        try {
            return decode(image, encoding);
        } catch (NotFoundException e) {
            LOG.error("Decode url error", e);
            throw new ZxingException("Decode url error");
        }
    }

    public static Result decodeByBytes(byte[] bytes, String encoding) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        return decodeByInputStream(inputStream, encoding);
    }

    private static Result decode(BufferedImage image, String encoding) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, encoding);

        MultiFormatReader reader = new MultiFormatReader();

        return reader.decode(binaryBitmap, hints);
    }
}