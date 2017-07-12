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
import com.nepxion.zxing.core.ZxingDecoder;
import com.nepxion.zxing.core.ZxingEncoder;
import com.nepxion.zxing.entity.ZxingEntity;
import com.nepxion.zxing.util.ZxingUtils;

public class ZxingTest {
    public static void main(String[] args) throws Exception {
        String text = "https://github.com/Nepxion/";
        File file = new File("D:/二维码.jpg");

        ZxingEntity zxingEntity = new ZxingEntity();
        zxingEntity.setText(text);
        zxingEntity.setFile(file);

        ZxingEncoder encoder = new ZxingEncoder();
        ZxingDecoder decoder = new ZxingDecoder();

        boolean outputFile = true;
        Result result = null;
        if (outputFile) {
            // 以文件格式读取并导出
            File resultForFile = encoder.encodeForFile(zxingEntity);
            result = decoder.decodeByFile(resultForFile, zxingEntity.getEncoding());
        } else {
            // 以字节数组格式读取并导出
            byte[] resultForBytes = encoder.encodeForBytes(zxingEntity);
            result = decoder.decodeByBytes(resultForBytes, zxingEntity.getEncoding());

            ZxingUtils.createFile(resultForBytes, file);
        }

        System.out.println("Text : " + result.getText());
        System.out.println("Timestamp : " + result.getTimestamp());
        System.out.println("BarcodeFormat : " + result.getBarcodeFormat());
        System.out.println("NumBits : " + result.getNumBits());
    }
}