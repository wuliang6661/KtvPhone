package com.ipad.ktvphone.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * https://www.jianshu.com/p/e5a45f9fbce3
 */
public class ZxingUtils {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;


    /**
     * @param contents 需要生成二维码的文字、网址等  ，二维码存在白边
     * @return bitmap
     */
    public static Bitmap createQRCode(String contents, int size) {
        try {
            return encodeBitmap(contents, BarcodeFormat.QR_CODE, size, size);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成无白边框的二维码
     */
    public static Bitmap createNoRectQRCode(String contents, int size) {
        try {
            return create2DCode(contents, size, size, 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成自定义白框边距的二维码
     *
     * @param margin 白框宽度
     */
    public static Bitmap create2DCode(String str, int width, int height, int margin) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, width, height);
            matrix = deleteWhite(matrix, margin);//删除白边
            width = matrix.getWidth();
            height = matrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = Color.BLACK;
                    } else {
                        pixels[y * width + x] = Color.WHITE;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


    private static BitMatrix deleteWhite(BitMatrix matrix, int margin) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + (margin * 2);
        int resHeight = rec[3] + (margin * 2);

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) {
            for (int j = 0; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }


    public static Bitmap encodeBitmap(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        BitMatrix encode = encode(contents, format, width, height);
        return createBitmap(encode);
    }

    public static BitMatrix encode(String contents, BarcodeFormat format, int width, int height) throws WriterException {
        try {
            return new MultiFormatWriter().encode(contents, format, width, height);
        } catch (WriterException e) {
            throw e;
        } catch (Exception e) {
            // ZXing sometimes throws an IllegalArgumentException
            throw new WriterException(e);
        }
    }

    public static Bitmap createBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}

