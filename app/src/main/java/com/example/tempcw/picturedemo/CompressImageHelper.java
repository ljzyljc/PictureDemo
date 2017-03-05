package com.example.tempcw.picturedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CompressImageHelper {
    private Bitmap bm;
    private String filePath;
    private Activity context;
    private Uri uri;
    private InputStream inputStream;
    private File mfile;

    public CompressImageHelper(Activity context, String filePath, File file) {
        this.filePath = filePath;
        this.context = context;
        this.mfile = file;
    }

    public CompressImageHelper(Activity context, Uri uri, File file) {
        this.uri = uri;
        this.context = context;
        this.mfile = file;
    }

    public Bitmap getUriBitmap() throws Exception {
        inputStream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, new Rect(), opt);
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        // 获取屏的宽度和高度
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        // 想要缩放的目标尺寸
        float hh = 640;// 设置高度为640f时，可以明显看到图片缩小了
        float ww = 480;// 设置宽度为480f，可以明显看到图片缩小了
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        if (picWidth > picHeight && picWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            opt.inSampleSize = (int) (picWidth / ww);
        } else if (picWidth < picHeight && picHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            opt.inSampleSize = (int) (picHeight / hh);
        }
        if (opt.inSampleSize <= 0)
            opt.inSampleSize = 1;
        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        inputStream.close();
        inputStream = context.getContentResolver().openInputStream(uri);
        bm = BitmapFactory.decodeStream(inputStream, new Rect(), opt);
        return compressImage(bm, 300);
    }

    public Bitmap getBitmap() throws Exception {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // 这个isjustdecodebounds很重要
        opt.inJustDecodeBounds = true;
        bm = BitmapFactory.decodeFile(filePath, opt);
        // 获取到这个图片的原始宽度和高度
        int picWidth = opt.outWidth;
        int picHeight = opt.outHeight;
        // 获取屏的宽度和高度
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        float hh = 640;// 设置高度为640f时，可以明显看到图片缩小了
        float ww = 480;// 设置宽度为480f，可以明显看到图片缩小了
        // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        opt.inSampleSize = 1;
        if (picWidth > picHeight && picWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            opt.inSampleSize = (int) (picWidth / ww);
        } else if (picWidth < picHeight && picHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            opt.inSampleSize = (int) (picHeight / hh);
        }
        if (opt.inSampleSize <= 0)
            opt.inSampleSize = 1;
        // 这次再真正地生成一个有像素的，经过缩放了的bitmap
        opt.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath, opt);
        return compressImage(bm, 300);
    }

    private Bitmap compressImage(Bitmap image, int size) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于350kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩比options=50，把压缩后的数据存放到baos中
                options = options - 5;
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            saveFile(isBm);
            Bitmap bitmap = BitmapFactory.decodeStream(isBm);// 把ByteArrayInputStream数据生成图片
            return bitmap;
        } catch (Exception e) {
            String eMsg = e.getMessage();
            e.printStackTrace();
            return null;
        }
    }

    private void saveFile(ByteArrayInputStream isBm) {
        try {
            FileOutputStream outputStream = new FileOutputStream(mfile);
            byte[] b = new byte[1024];
            while (isBm.read(b, 0, b.length) != -1) {
                try {
                    outputStream.write(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Bitmap bm, String filePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
