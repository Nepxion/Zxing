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
import com.nepxion.zxing.core.ZxingGenerator;

public class ZxingTest {
    public static void main(String[] args) throws Exception {
        String text = "Hello，尼普西";
        File file = new File("E:/Download/二维码.jpg");
        String format = "jpg";
        String character = "UTF-8";
        int width = 200;
        int height = 200;

        File resultFile = ZxingGenerator.encodeForFile(text, file, format, character, width, height);
        Result result = ZxingGenerator.decodeByFile(resultFile, character);

        // InputStream inputStream = encodeForInputStream(text, format, character, width, height);
        // Result result = decodeByInputStream(inputStream, character);

        System.out.println("Text : " + result.getText());
        System.out.println("Encoder : " + result.getBarcodeFormat());
        System.out.println("Timestamp : " + result.getTimestamp());
    }
}