package com.nepxion.zxing.test;

/**
 * <p>Title: Nepxion Zxing</p>
 * <p>Description: Nepxion Zxing QR Code</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.nepxion.zxing.core.ZxingDecoder;
import com.nepxion.zxing.core.ZxingEncoder;
import com.nepxion.zxing.entity.ZxingEntity;
import com.nepxion.zxing.util.ZxingUtil;

public class ZxingTest {
    private static final Logger LOG = LoggerFactory.getLogger(ZxingTest.class);

    public static void main(String[] args) throws Exception {
        executeForQRFile();
        // executeForQRBytes();

        executeForEANFile();
        // executeForEANBytes();
    }

    public static void executeForQRFile() {
        // 二维码内容
        String text = "http://www.nepxion.com";
        // 二维码图片导出路径
        File file = new File("E:/Download/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setBarcodeFormat(BarcodeFormat.QR_CODE);
        entity.setLogoFile(new File("src/test/resources/logo.png"));
        entity.setText(text);
        entity.setOutputFile(file);
        entity.setWidth(430);
        entity.setHeight(430);

        // 以文件格式读取并导出，该方式适合本地调用
        ZxingEncoder encoder = new ZxingEncoder();
        encoder.encodeForFile(entity);

        // 以文件格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByFile(file, entity.getEncoding());

        LOG.info("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }

    public static void executeForQRBytes() throws IOException {
        // 二维码内容
        String text = "http://www.nepxion.com";
        // 二维码图片导出路径
        File file = new File("E:/Download/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setBarcodeFormat(BarcodeFormat.QR_CODE);
        entity.setLogoFile(new File("src/test/resources/logo.png"));
        entity.setText(text);
        entity.setOutputFile(file);
        entity.setWidth(430);
        entity.setHeight(430);

        // 以字节数组格式读取并导出，该方式适合服务端传输给客户端调用
        ZxingEncoder encoder = new ZxingEncoder();
        byte[] bytes = encoder.encodeForBytes(entity);

        ZxingUtil.createFile(bytes, file);

        // 以字节数组格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByBytes(bytes, entity.getEncoding());

        LOG.info("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }

    public static void executeForEANFile() {
        // 条形码内容
        String text = "6943620593115";
        // 条形码图片导出路径
        File file = new File("E:/Download/条形码.jpg");

        // 条形码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setBarcodeFormat(BarcodeFormat.EAN_13);
        entity.setText(text);
        entity.setOutputFile(file);
        entity.setWidth(560);
        entity.setHeight(200);

        // 以文件格式读取并导出，该方式适合本地调用
        ZxingEncoder encoder = new ZxingEncoder();
        encoder.encodeForFile(entity);

        // 以文件格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByFile(file, entity.getEncoding());

        LOG.info("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }

    public static void executeForEANBytes() throws IOException {
        // 条形码内容
        String text = "6943620593115";
        // 条形码图片导出路径
        File file = new File("E:/Download/条形码.jpg");

        // 条形码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setBarcodeFormat(BarcodeFormat.EAN_13);
        entity.setText(text);
        entity.setOutputFile(file);
        entity.setWidth(560);
        entity.setHeight(200);

        // 以字节数组格式读取并导出，该方式适合服务端传输给客户端调用
        ZxingEncoder encoder = new ZxingEncoder();
        byte[] bytes = encoder.encodeForBytes(entity);

        ZxingUtil.createFile(bytes, file);

        // 以字节数组格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByBytes(bytes, entity.getEncoding());

        LOG.info("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }
}