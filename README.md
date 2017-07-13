# Zxing
[![Apache License 2](https://img.shields.io/badge/license-ASF2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

基于Google Zxing的二维码生成组件

## 使用
```java
    public static void executeForFile() {
        // 二维码内容
        String text = "https://github.com/Nepxion/";
        // 二维码图片导出路径
        File file = new File("D:/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setText(text);
        entity.setFile(file);

        // 以文件格式读取并导出，该方式适合本地调用
        ZxingEncoder encoder = new ZxingEncoder();
        encoder.encodeForFile(entity);

        // 以文件格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByFile(file, entity.getEncoding());

        System.out.println("Text : " + result.getText());
        System.out.println("Timestamp : " + result.getTimestamp());
        System.out.println("BarcodeFormat : " + result.getBarcodeFormat());
        System.out.println("NumBits : " + result.getNumBits());
    }
```

```java
    public static void executeForBytes() throws IOException {
        // 二维码内容
        String text = "https://github.com/Nepxion/";
        // 二维码图片导出路径
        File file = new File("D:/二维码.jpg");

        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
        ZxingEntity entity = new ZxingEntity();
        entity.setText(text);
        entity.setFile(file);

        // 以字节数组格式读取并导出，该方式适合服务端传输给客户端调用
        ZxingEncoder encoder = new ZxingEncoder();
        byte[] bytes = encoder.encodeForBytes(entity);

        ZxingUtils.createFile(bytes, file);

        // 以字节数组格式扫描并解析
        ZxingDecoder decoder = new ZxingDecoder();
        Result result = decoder.decodeByBytes(bytes, entity.getEncoding());

        System.out.println("Text : " + result.getText());
        System.out.println("Timestamp : " + result.getTimestamp());
        System.out.println("BarcodeFormat : " + result.getBarcodeFormat());
        System.out.println("NumBits : " + result.getNumBits());
    }
```
## 二维码示例图片
![Alt text](https://github.com/Nepxion/Zxing/blob/master/README.jpg)