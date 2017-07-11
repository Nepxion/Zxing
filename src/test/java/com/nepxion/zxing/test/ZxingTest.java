package com.nepxion.zxing.test;

/**
 * <p>Title: Nepxion Zxing</p>
 * <p>Description: Nepxion Zxing QR Code</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.File;

import com.google.zxing.Result;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nepxion.zxing.constant.ZxingConstants;
import com.nepxion.zxing.core.ZxingDecoder;
import com.nepxion.zxing.core.ZxingEncoder;
import com.nepxion.zxing.util.ZxingUtils;

public class ZxingTest {
    public static void main(String[] args) throws Exception {
        String text = "Hello，尼普西";
        File file = new File("D:/二维码.jpg");
        String format = ZxingConstants.DEFAULT_FORMAT;
        String encoding = ZxingConstants.DEFAULT_ENCODING;
        ErrorCorrectionLevel level = ZxingConstants.DEFAULT_CORRECTION_LEVEL;
        int width = ZxingConstants.DEFAULT_WIDTH;
        int height = ZxingConstants.DEFAULT_HEIGHT;
        int margin = ZxingConstants.DEFAULT_MARGIN;
        int foregroundColor = ZxingConstants.DEFAULT_FOREGROUND_COLOR;
        int backgroundColor = ZxingConstants.DEFAULT_BACKGROUND_COLOR;
        boolean deleteWhiteBorder = ZxingConstants.DEFAULT_DELETE_WHITE_BORDER;
        String logoPath = "src/main/resources/logo.jpg";

        ZxingEncoder encoder = new ZxingEncoder();
        ZxingDecoder decoder = new ZxingDecoder();

        boolean outputFile = false;
        Result result = null;
        if (outputFile) {
            // 以文件格式读取并导出
            File resultForFile = encoder.encodeForFile(text, file, format, encoding, level, width, height, margin, foregroundColor, backgroundColor, deleteWhiteBorder, logoPath);
            result = decoder.decodeByFile(resultForFile, encoding);
        } else {
            // 以字节数组格式读取并导出
            byte[] resultForBytes = encoder.encodeForBytes(text, format, encoding, level, width, height, margin, foregroundColor, backgroundColor, deleteWhiteBorder, logoPath);
            result = decoder.decodeByBytes(resultForBytes, encoding);

            ZxingUtils.createFile(resultForBytes, file);
        }

        System.out.println("Text : " + result.getText());
        System.out.println("Timestamp : " + result.getTimestamp());
        System.out.println("BarcodeFormat : " + result.getBarcodeFormat());
        System.out.println("NumBits : " + result.getNumBits());
    }
}