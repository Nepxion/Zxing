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
import java.io.IOException;

import com.google.zxing.Result;
import com.nepxion.zxing.core.ZxingDecoder;
import com.nepxion.zxing.core.ZxingEncoder;
import com.nepxion.zxing.entity.ZxingEntity;
import com.nepxion.zxing.util.ZxingUtils;

public class ZxingTest {
    public static void main(String[] args) throws Exception {
        executeForFile();
        executeForBytes();
    }

    public static void executeForFile() {
        // 二维码内容
        String text = "https://github.com/Nepxion/";
        // 二维码图片导出路径
        File outputFile = new File("D:/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setText(text);
        entity.setOutputFile(outputFile);

        // 以文件格式读取并导出，该方式适合本地调用
        ZxingEncoder encoder = new ZxingEncoder();
        encoder.encodeForFile(entity);

        // 以文件格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByFile(outputFile, entity.getEncoding());

        System.out.println("Text : " + result.getText());
        System.out.println("Timestamp : " + result.getTimestamp());
        System.out.println("BarcodeFormat : " + result.getBarcodeFormat());
        System.out.println("NumBits : " + result.getNumBits());
    }

    public static void executeForBytes() throws IOException {
        // 二维码内容
        String text = "https://github.com/Nepxion/";
        // 二维码图片导出路径
        File outputFile = new File("D:/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setText(text);
        entity.setOutputFile(outputFile);

        // 以字节数组格式读取并导出，该方式适合服务端传输给客户端调用
        ZxingEncoder encoder = new ZxingEncoder();
        byte[] bytes = encoder.encodeForBytes(entity);

        ZxingUtils.createFile(bytes, outputFile);

        // 以字节数组格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByBytes(bytes, entity.getEncoding());

        System.out.println("Text : " + result.getText());
        System.out.println("Timestamp : " + result.getTimestamp());
        System.out.println("BarcodeFormat : " + result.getBarcodeFormat());
        System.out.println("NumBits : " + result.getNumBits());
    }
}