# Zxing
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

基于Google Zxing的二维码/条形码生成组件

## 参数说明
```java
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
 * outputFile        二维码/条形码图片的导出文件，File对象
 */
```

## 创建二维码图片并扫描的示例
```java
    public static void executeForQRFile() {
        // 二维码内容
        String text = "https://github.com/Nepxion/";
        // 二维码图片导出路径
        File file = new File("E:/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setBarcodeFormat(BarcodeFormat.QR_CODE);
        entity.setText(text);
        entity.setOutputFile(file);
        entity.setWidth(300);
        entity.setHeight(300);

        // 以文件格式读取并导出，该方式适合本地调用
        ZxingEncoder encoder = new ZxingEncoder();
        encoder.encodeForFile(entity);

        // 以文件格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByFile(file, entity.getEncoding());

        System.out.println("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }
```

## 创建二维码图片字节数组(用于网络传递)并扫描的示例
```java
    public static void executeForQRBytes() throws IOException {
        // 二维码内容
        String text = "https://github.com/Nepxion/";
        // 二维码图片导出路径
        File file = new File("E:/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setBarcodeFormat(BarcodeFormat.QR_CODE);
        entity.setText(text);
        entity.setOutputFile(file);
        entity.setWidth(300);
        entity.setHeight(300);

        // 以字节数组格式读取并导出，该方式适合服务端传输给客户端调用
        ZxingEncoder encoder = new ZxingEncoder();
        byte[] bytes = encoder.encodeForBytes(entity);

        ZxingUtils.createFile(bytes, file);

        // 以字节数组格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByBytes(bytes, entity.getEncoding());

        System.out.println("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }
```

## 创建条形码图片并扫描的示例
```java
    public static void executeForEANFile() {
        // 条形码内容
        String text = "6943620593115";
        // 条形码图片导出路径
        File file = new File("E:/条形码.jpg");

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

        System.out.println("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }
```

## 创建条形码图片字节数组(用于网络传递)并扫描的示例
```java
    public static void executeForEANBytes() throws IOException {
        // 条形码内容
        String text = "6943620593115";
        // 条形码图片导出路径
        File file = new File("E:/条形码.jpg");

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

        ZxingUtils.createFile(bytes, file);

        // 以字节数组格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByBytes(bytes, entity.getEncoding());

        System.out.println("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());
    }
```

## 二维码示例图片
![Alt text](https://github.com/Nepxion/Zxing/blob/master/二维码示例.jpg)

## 条形码示例图片
![Alt text](https://github.com/Nepxion/Zxing/blob/master/条形码示例.jpg)