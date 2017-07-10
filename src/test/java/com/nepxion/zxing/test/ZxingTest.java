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
import com.nepxion.zxing.core.ZxingDecoder;
import com.nepxion.zxing.core.ZxingEncoder;

public class ZxingTest {
    public static void main(String[] args) throws Exception {
        String text = "Hello，尼普西";
        File file = new File("E:/Download/二维码.jpg");
        String format = "jpg";
        String encoding = "UTF-8";
        ErrorCorrectionLevel level = ErrorCorrectionLevel.H;
        int width = 300;
        int height = 300;
        int margin = 1;

        int foregroundColor = 0xFF000000;
        int backgroundColor = 0xFFFFFFFF;
        String logoPath = "src/main/resources/logo.jpg";

        ZxingEncoder encoder = new ZxingEncoder();
        ZxingDecoder decoder = new ZxingDecoder();

        File resultFile = encoder.encodeForFile(text, file, format, encoding, level, width, height, margin, foregroundColor, backgroundColor, logoPath);
        Result result = decoder.decodeByFile(resultFile, encoding);

        // InputStream inputStream = encoder.encodeForInputStream(text, format, encoding, level, width, height, margin, foregroundColor, backgroundColor);
        // Result result = decoder.decodeByInputStream(inputStream, encoding);

        System.out.println("Text : " + result.getText());
        System.out.println("Encoder : " + result.getBarcodeFormat());
        System.out.println("Timestamp : " + result.getTimestamp());
    }
}