package com.nepxion.zxing.constant;

/**
 * <p>Title: Nepxion Zxing</p>
 * <p>Description: Nepxion Zxing QR Code</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.taobao.text.Color;

public class ZxingConstant {
    public static final String ZXING_VERSION = "1.1.0";

    public static final String DEFAULT_FORMAT = "jpg";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final ErrorCorrectionLevel DEFAULT_CORRECTION_LEVEL = ErrorCorrectionLevel.H;
    public static final int DEFAULT_MARGIN = 1;
    public static final int DEFAULT_FOREGROUND_COLOR = 0xFF000000;
    public static final int DEFAULT_BACKGROUND_COLOR = 0xFFFFFFFF;
    public static final boolean DEFAULT_DELETE_WHITE_BORDER = false;

    static {
        /*String bannerShown = System.getProperty(BannerConstant.BANNER_SHOWN, "true");
        if (Boolean.valueOf(bannerShown)) {
            System.out.println("");
            System.out.println("╔════╗");
            System.out.println("╚══╗═║");
            System.out.println("  ╔╝╔╬╗╔╦╦═╗╔══╗");
            System.out.println(" ╔╝╔╝╚╬╬╬╣╔╗╣╔╗║");
            System.out.println("╔╝═╚═╦╬╬╣║║║║╚╝║");
            System.out.println("╚════╩╝╚╩╩╝╚╩═╗║");
            System.out.println("            ╔═╝║");
            System.out.println("            ╚══╝");
            System.out.println("Nepxion Zxing  v" + ZXING_VERSION);
            System.out.println("");
        }*/

        LogoBanner logoBanner = new LogoBanner(ZxingConstant.class, "/com/nepxion/zxing/resource/logo.txt", "Welcome to Nepxion", 5, 5, new Color[] { Color.red, Color.green, Color.cyan, Color.blue, Color.yellow }, true);

        NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":", ZXING_VERSION, 0, 1), new Description(BannerConstant.GITHUB + ":", BannerConstant.NEPXION_GITHUB + "/Zxing", 0, 1));
    }
}