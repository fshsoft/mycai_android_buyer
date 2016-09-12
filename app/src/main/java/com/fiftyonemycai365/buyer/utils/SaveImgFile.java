package com.fiftyonemycai365.buyer.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.fiftyonemycai365.buyer.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by fengshuai on 16/5/17.
 */
public class SaveImgFile {
    public static File getSDPath(String filename) {
        File dir;
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getExternalStorageDirectory() + "/myCai");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/myCai/" + filename + ".jpg");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            dir = new File("/myCai");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File("/myCai/" + filename + ".jpg");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File saveMyBitmap(Context context,String  f) throws IOException {

        Bitmap bmp = drawable2Bitmap(context.getResources().getDrawable(R.mipmap.ic_launcher));//这里的drawable2Bitmap方法是我把ImageView中 的drawable转化成bitmap，当然实验的时候可以自己创建bitmap
        File file = getSDPath(f);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static Bitmap drawable2Bitmap(Drawable d){
        int width=d.getIntrinsicWidth();
        int height=d.getIntrinsicHeight();
        Bitmap.Config config=d.getOpacity()!= PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565;
        Bitmap bitmap=Bitmap.createBitmap(width,height,config);
        Canvas canvas=new Canvas(bitmap);
        d.setBounds(0, 0, width, height);
        d.draw(canvas);
        return bitmap;
    }

//    /**
//     * 将bitmap转为file保存
//     */
//    public static File saveBitmapFile(Bitmap bitmap, String filename,Context context){
//
//        File file;
//        BufferedOutputStream bos;
//            file =getSDPath(filename);
//            try {
//                bos = new BufferedOutputStream(new FileOutputStream(file));
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                bos.flush();
//                bos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                file.getAbsolutePath(), fileName, null);
//        return file;
//
//    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "MyCai");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName ="dimensional_code" + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }


}

