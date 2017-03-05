package com.example.tempcw.picturedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;


import java.io.File;
import java.io.IOException;

/**
 * Created by LSH on 2017/2/10.
 */

public class FileManager {

    /**
     * 图片压缩工具
     *
     * @param context
     * @param picFile
     * @return
     */
    public static Bitmap compressPicture(final Activity context, final File picFile) {
        BitmapFactory.Options options;
        options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;

        CompressImageHelper compressImageHelper = null;
        if (picFile != null)
            compressImageHelper = new CompressImageHelper(context, picFile.getAbsolutePath(), picFile);
        try {
            compressImageHelper.getBitmap();
            int degree = readPictureDegree(picFile.getAbsolutePath());
            Matrix m = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(picFile.getAbsolutePath(), options);
            m.setRotate(degree, bitmap.getWidth()/2, bitmap.getHeight()/2);
            Bitmap resBit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return resBit;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取图片旋转角度
     *
     * @param path
     * @return
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
