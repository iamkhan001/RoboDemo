package com.csjbot.blackgaga.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/6/20.
 * @Package_name: BlackGaGa
 * 本地二维码生成
 */
public class ZxingUtil {

    private static final String CHARSET = "utf-8";//二维码编码格式

    private static final String QRCODE_PATH = Environment.getExternalStorageDirectory() + File.separator + "blackgaga" +
            File.separator + "zxing" + File.separator;

    /**
     * 单一生成一个二维码
     *
     * @param width   长度
     * @param height  高度
     * @param content 内容
     * @return
     */
    public static Bitmap createQRCode(String content, int width, int height) {
        if (content == null || content.equals("")) {
            return null;
        }

        Hashtable hints = new Hashtable();

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int y = 0; y < width; y++) {
                    if (bitMatrix.get(y, i)) {
                        pixels[i * width + y] = Color.BLACK;
                    } else pixels[i * width + y] = Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建带图片的二维码
     *
     * @param content  内容
     * @param width    长度
     * @param height   高度
     * @param drawable logo
     * @return
     */
    public Bitmap createQRCode(String content, int width, int height, Bitmap drawable) {
        int login_w = drawable.getWidth();
        int login_h = drawable.getHeight();

        Matrix m = new Matrix();

        float scale_login_w = width * 1.0f / 5 / login_w;
        float scale_login_h = height * 1.0f / 5 / login_h;

        m.setScale(scale_login_w, scale_login_h);

        Bitmap newLogoBitmap = Bitmap.createBitmap(drawable, 0, 0, login_w, login_h, m, false);

        int new_logo_w = newLogoBitmap.getWidth();
        int new_logo_h = newLogoBitmap.getHeight();

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//设置容错级别，H为最高
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int halfw = width / 2;
            int halfh = height / 2;
            int[] pixels = new int[width * height];
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    if (x > halfw - new_logo_w / 2 && x < halfw + new_logo_w / 2
                            && y > halfh - new_logo_h / 2 && y < halfh + new_logo_h / 2) {
                        pixels[y * width + x] = newLogoBitmap.getPixel(x - halfw + new_logo_w / 2
                                , y - halfh + new_logo_h / 2);
                    } else {
                        pixels[y * width + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建带图片的二维码
     *
     * @param content 内容
     * @param width   长度
     * @param height  高度
     * @param resId   资源id
     * @return
     */
    public static Bitmap createQRCode(Context context, String content, int width, int height, int resId) {
        Resources res = context.getResources();
        Bitmap drawable = BitmapFactory.decodeResource(res, resId);
        int login_w = drawable.getWidth();
        int login_h = drawable.getHeight();

        Matrix m = new Matrix();

        float scale_login_w = width * 1.0f / 5 / login_w;
        float scale_login_h = height * 1.0f / 5 / login_h;

        m.setScale(scale_login_w, scale_login_h);

        Bitmap newLogoBitmap = Bitmap.createBitmap(drawable, 0, 0, login_w, login_h, m, false);

        int new_logo_w = newLogoBitmap.getWidth();
        int new_logo_h = newLogoBitmap.getHeight();

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//设置容错级别，H为最高
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int halfw = width / 2;
            int halfh = height / 2;
            int[] pixels = new int[width * height];
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    if (x > halfw - new_logo_w / 2 && x < halfw + new_logo_w / 2
                            && y > halfh - new_logo_h / 2 && y < halfh + new_logo_h / 2) {
                        pixels[y * width + x] = newLogoBitmap.getPixel(x - halfw + new_logo_w / 2
                                , y - halfh + new_logo_h / 2);
                    } else {
                        pixels[y * width + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 把二维码保存在本地
     *
     * @param bitmap
     * @param name
     */
    public static void saveQRcodeToSdcard(Bitmap bitmap, String name) {
        File file = new File(QRCODE_PATH, "/" + name);
        if (!file.exists()) {
            file.mkdir();
        } else {
            file.delete();
        }

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
