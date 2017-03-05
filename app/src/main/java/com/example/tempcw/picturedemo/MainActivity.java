package com.example.tempcw.picturedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button screenShot,oneShoh,takephoto,localPhoto;
    private ImageView imageView;
    private String path="";
//    private Bitmap bitmap;
    private Bitmap twoBitmap;
    private Bitmap localBitmap;      //第一张bitmap
    private Bitmap drawTextBitmap;   //含有文本的bitmap

    private Bitmap firstBitmap;
    private Bitmap secondBitmap;
    private Bitmap thirdBitmap;

    private Bitmap totalFirst;
    private Bitmap totalSecond;

    private Bitmap endBitmap;

    private int number=0;

    private final int TAKE_PHOTO = 1;    //拍照上传
    private final int LOCAL_PHOTO = 2;   //本地图片上传
    private File picFile;
    private Bitmap picBitmap;  //拍照或者本地的bitmap
    private int titleWidth;
    private Bitmap titleBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenShot = (Button) findViewById(R.id.screenShot);
        takephoto= (Button) findViewById(R.id.takephoto);
        takephoto.setOnClickListener(this);
        localPhoto= (Button) findViewById(R.id.localphoto);
        localPhoto.setOnClickListener(this);
        imageView= (ImageView) findViewById(R.id.image);
        oneShoh= (Button) findViewById(R.id.oneShot);
        //创建第一张图片
//        createTopBitMap();
        //创建一张空白图（含有高新区管理局和时间,该图片的大小是根据用户选择的第一张大小决定的）
//        drawTextBitmap=drawTextAtBitmap(localBitmap,"高新区城管局\n2017年三月15号",10,localBitmap.getHeight()/2);
        //合成图片(2乘2)

        screenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number!=3){
                    Toast.makeText(MainActivity.this,"至少三张图片才能合成",Toast.LENGTH_SHORT).show();
                    return;
                }
                new AsyncTask<Void,Void,Bitmap>(){

                    @Override
                    protected Bitmap doInBackground(Void... params) {
//                        //第一次合并图片，上下合并
//                        mergePhoto("ljc.png",localBitmap,drawTextBitmap);
//                        //第二次合并图片,上下合并
//                        mergePhoto1("ljzy.png",createBottomBitMap(),createThirdBitMap());

//-----------------------拍照或选择本地图片---------------------------------------------------------------------
                        mergePhoto("jack.png",firstBitmap,secondBitmap);
                        mergePhoto1("ie.png",thirdBitmap,drawTextBitmap);

                        return null ;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);

                        new AsyncTask<Void,Void,Bitmap>(){

                            @Override
                            protected Bitmap doInBackground(Void... params) {
//                                mergePhotoHorizontal("ljzyljc.png",secondBitmap,firstBitmap);
// -----------------------拍照或选择本地图片---------------------------------------------------------------------
                                mergePhotoHorizontal("jackie.png",totalFirst,totalSecond);

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Bitmap bitmap) {
                                super.onPostExecute(bitmap);
                                imageView.setImageBitmap(endBitmap);
                                new AsyncTask<Void,Void,Bitmap>(){

                                    @Override
                                    protected Bitmap doInBackground(Void... params) {
//                                        mergePhoto2("ljzyljc.png");
                                        return null;
                                    }
                                }.execute();

                            }
                        }.execute();


                    }
                }.execute();

            }
        });
        //1乘3（含有标题栏）
        oneShoh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        mergePhoto("li.png",firstBitmap,secondBitmap);
                        mergePhoto1("jun.png",titleBitmap,thirdBitmap);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        new AsyncTask<Void,Void,Void>(){

                            @Override
                            protected Void doInBackground(Void... params) {
                                mergePhoto("lijun.png",totalSecond,totalFirst);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                imageView.setImageBitmap(totalFirst);
                            }
                        }.execute();

                    }
                }.execute();
            }
        });

    }
    private  File f1;
    //拼接两张图片（竖着拼接）
    public boolean mergePhoto(String name,Bitmap bitmap1,Bitmap bitmap2) {
        boolean flag;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File dir = new File(path + "/0ayPic/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
             f1 = new File(path + "/0ayPic/", name);  //"ljc.png"
            if (f1.exists()){
                f1.delete();
            }
            try {
                f1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fos = new FileOutputStream(f1);
                totalFirst=add2Bitmap(bitmap1,bitmap2);
//                 flag=add2Bitmap(createTopBitMap(), createBottomBitMap()).compress(Bitmap.CompressFormat.PNG, 90, fos);
                flag=totalFirst.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return flag;
            } catch (Exception e) {
                e.printStackTrace();
            }
            //   viewScreen.destroyDrawingCache();
        }
        return false;
    }
    //拼接两张图片（竖着拼接）
    public boolean mergePhoto1(String name,Bitmap bitmap1,Bitmap bitmap2) {
        boolean flag;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File dir = new File(path + "/0ayPic/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            f1 = new File(path + "/0ayPic/", name);  //"ljc.png"
            if (f1.exists()){
                f1.delete();
            }
            try {
                f1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fos = new FileOutputStream(f1);
                totalSecond=add2Bitmap(bitmap1,bitmap2);
//                 flag=add2Bitmap(createTopBitMap(), createBottomBitMap()).compress(Bitmap.CompressFormat.PNG, 90, fos);
                flag=totalSecond.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return flag;
            } catch (Exception e) {
                e.printStackTrace();
            }
            //   viewScreen.destroyDrawingCache();
        }
        return false;
    }
    //拼接两张图片（横着拼接）
    public boolean mergePhotoHorizontal(String name,Bitmap bitmap1,Bitmap bitmap2) {
        boolean flag;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File dir = new File(path + "/0ayPic/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            f1 = new File(path + "/0ayPic/", name);  //"ljc.png"
            if (f1.exists()){
                f1.delete();
            }
            try {
                f1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fos = new FileOutputStream(f1);
                endBitmap=addRight2Bitmap(bitmap1, bitmap2);
                flag=endBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return flag;
            } catch (Exception e) {
                e.printStackTrace();
            }
            //   viewScreen.destroyDrawingCache();
        }
        return false;
    }

    //拼接两张图片(第一张接在第二张下方)
    public Bitmap add2Bitmap(Bitmap one, Bitmap two) {
        int width = Math.max(one.getWidth(), two.getWidth());
        int height = one.getHeight() + two.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(one, 0, 0, null);

        canvas.drawBitmap(two, 0, one.getHeight(), null);
        return result;
    }
    //拼接两张图片(第二张接在第一张右方)
    public Bitmap addRight2Bitmap(Bitmap one, Bitmap two) {
        int width = one.getWidth()+two.getWidth();              //两张合并的宽度
        int height = Math.max(one.getHeight(),two.getHeight()); //选择两张中最高的
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(one, 0, 0, null);

        canvas.drawBitmap(two, one.getWidth(), 0, null);
        return result;
    }

    //顶部的图片
    public Bitmap createTopBitMap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dfirst);      //图片宽度540左右最合适
        localBitmap=bitmap;
        return bitmap;
    }
    //底部的图片
    public Bitmap createBottomBitMap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.second);      //图片宽度540左右最合适
        return bitmap;
    }
    //底部的图片（第三张）
    public Bitmap createThirdBitMap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.third);      //图片宽度540左右最合适
        return bitmap;
    }
    //绘制图片同时给图片添加文字
    private Bitmap drawTextAtBitmap(Bitmap bitmap, String text, float textX, float textY) {
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        if (x<200){
            x=200;
        }
        if (y<100){
            y=100;
        }

        // 创建一个和原图同样大小的位图
        Bitmap newbit = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newbit);
        canvas.drawColor(getResources().getColor(R.color.backgro));

        Paint paint = new Paint();

        // 在原始位置0，0插入原图
        canvas.drawBitmap(newbit, 0, 0, paint);     //如果设置为bitmap就是原图来进行绘制
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setTextSize(10);

        // 在原图指定位置写上字
        canvas.drawText(text, textX, textY, paint);

        canvas.save(Canvas.ALL_SAVE_FLAG);

        // 存储
        canvas.restore();

        return newbit;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //拍照
            case R.id.takephoto:
                Intent tIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                picFile = FileManagerUtils.createNewFile();
                tIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
                startActivityForResult(tIntent, TAKE_PHOTO);
                break;
            //选择本地图片
            case R.id.localphoto:
                Intent iconIntent = new Intent(Intent.ACTION_GET_CONTENT);
                iconIntent.addCategory(Intent.CATEGORY_OPENABLE);
                iconIntent.setType("image/jpeg");
                startActivityForResult(iconIntent, LOCAL_PHOTO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case LOCAL_PHOTO:  //本地图片上传
                Uri uri = data.getData();
                String picPath = UriUtil.getPathByUri4kitkat(this, uri);
                picFile = FileManagerUtils.createNewFile(picPath);
                //压缩图片
                picBitmap = FileManager.compressPicture(this, picFile);
                setThreeBitmap(picBitmap);
                break;
            case TAKE_PHOTO:   //拍照上传
                //压缩图片
                picBitmap = FileManager.compressPicture(this, picFile);
                setThreeBitmap(picBitmap);
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //分别给三张图片添加bitmap
    public void setThreeBitmap(Bitmap bitmap){
        if (firstBitmap==null){
            firstBitmap=bitmap;
            drawTextBitmap=drawTextAtBitmap(firstBitmap,"高新区城管局\n2017年三月15号",10,firstBitmap.getHeight()/2);
            number=1;
            return;
        }else if (secondBitmap==null){
            secondBitmap=bitmap;
            number=2;
            return;
        }else if (thirdBitmap==null){
            thirdBitmap=bitmap;
            number=3;
        }
        setMaxWidth(firstBitmap,secondBitmap,thirdBitmap);
        titleBitmap=drawTextAtBitmapTitle("高新区城管局\n2017年三月15号",10,50);

    }
    //选择三张图片中最宽的
    public int setMaxWidth(Bitmap bitmap1,Bitmap bitmap2,Bitmap bitmap3){
        int maxBitmapWidth=Math.max(bitmap1.getWidth(),bitmap2.getWidth());
        maxBitmapWidth=Math.max(maxBitmapWidth,bitmap3.getWidth());
        titleWidth=maxBitmapWidth;
        return maxBitmapWidth;
    }

    private Bitmap drawTextAtBitmapTitle(String text, float textX, float textY) {



        // 创建一个和原图同样大小的位图
        Bitmap newbit = Bitmap.createBitmap(titleWidth, 100, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newbit);
        canvas.drawColor(getResources().getColor(R.color.backgro));

        Paint paint = new Paint();

        // 在原始位置0，0插入原图
        canvas.drawBitmap(newbit, 0, 0, paint);     //如果设置为bitmap就是原图来进行绘制
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setTextSize(10);

        // 在原图指定位置写上字
        canvas.drawText(text, textX, textY, paint);

        canvas.save(Canvas.ALL_SAVE_FLAG);

        // 存储
        canvas.restore();

        return newbit;
    }


    //    //底部的图片
//    public Bitmap ImageCrop(Bitmap bitmap) {
//        int w = bitmap.getWidth(); // 得到图片的宽  720                     y + height must be <= bitmap.height()
//        Log.d("jc", "屏幕的高度" + width + "==========图片高度" + w);
//        int h = bitmap.getHeight();  //1280
//        //  int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
//        int widthX = width;              //要截图的宽度
//        int heightY = height - 61;       //要截图的高度
//
////    int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
////    int retY = w > h ? 0 : (h - w) / 2;
//        //下面这句是关键
////    Bitmap source：要从中截图的原始位图
////    int x:  起始x坐标
////    int y：起始y坐标
////    int width：  要截的图的宽度
////    int height：要截的图的高度
//        return Bitmap.createBitmap(bitmap, 0, 61, widthX, heightY, null, false);
//    }
}
