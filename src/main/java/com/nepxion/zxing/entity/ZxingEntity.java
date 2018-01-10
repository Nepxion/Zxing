package com.nepxion.zxing.entity;

/**
 * <p>Title: Nepxion Zxing</p>
 * <p>Description: Nepxion Zxing QR Code</p>
 * <p>Copyright: Copyright (c) 2017-2020</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nepxion.zxing.constant.ZxingConstant;

public class ZxingEntity implements Serializable {
    private static final long serialVersionUID = 4989668966674254634L;

    private BarcodeFormat barcodeFormat;
    private String text;
    private String format = ZxingConstant.DEFAULT_FORMAT;
    private String encoding = ZxingConstant.DEFAULT_ENCODING;
    private ErrorCorrectionLevel correctionLevel = ZxingConstant.DEFAULT_CORRECTION_LEVEL;
    private int width;
    private int height;
    private int margin = ZxingConstant.DEFAULT_MARGIN;
    private int foregroundColor = ZxingConstant.DEFAULT_FOREGROUND_COLOR;
    private int backgroundColor = ZxingConstant.DEFAULT_BACKGROUND_COLOR;
    private boolean deleteWhiteBorder = ZxingConstant.DEFAULT_DELETE_WHITE_BORDER;
    private File logoFile = new File(ZxingConstant.DEFAULT_LOGO_PATH);
    private byte[] logoBytes;
    private File outputFile;

    public BarcodeFormat getBarcodeFormat() {
        return barcodeFormat;
    }

    public void setBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public ErrorCorrectionLevel getCorrectionLevel() {
        return correctionLevel;
    }

    public void setCorrectionLevel(ErrorCorrectionLevel correctionLevel) {
        this.correctionLevel = correctionLevel;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isDeleteWhiteBorder() {
        return deleteWhiteBorder;
    }

    public void setDeleteWhiteBorder(boolean deleteWhiteBorder) {
        this.deleteWhiteBorder = deleteWhiteBorder;
    }

    public File getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(File logoFile) {
        this.logoFile = logoFile;
    }

    public byte[] getLogoBytes() {
        return logoBytes;
    }

    public void setLogoBytes(byte[] logoBytes) {
        this.logoBytes = logoBytes;
    }

    public boolean hasLogo() {
        return logoFile != null || logoBytes != null;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}