package com.lvstudio.video.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by lvchong on 2018/4/20.
 */

public class BitmapUtils {


    public static Bitmap getImage(Context context, String srcPath) {
        return BitmapFactory.decodeFile(srcPath);
    }

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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
                default:
                    break;
            }
        } catch (IOException e) {
            LogUtils.e("BitmapUtils : printStackTrace", e.getMessage());
        }
        return degree;
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static String compressImage(Context context, String filePath, String targetPath) {
        Bitmap bm = getImage(context, filePath);
//        int degree = getBitmapDegree(filePath);// 获取相片拍摄角度
//        if (degree != 0) {// 旋转照片角度，防止头像横着显示
//            bm = rotateBitmapByDegree(bm, degree);
//        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            } else {
                outputFile.delete();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100;
            while (baos.toByteArray().length / 1024 > 300) {
                options -= 10;
                baos.reset();
                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, options, out);
            out.close();
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    public static String compressImage(Context context, Bitmap bm, String targetPath) {
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            } else {
                outputFile.delete();
            }
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
//            while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于1000kb,大于继续压缩
//                options -= 10;// 每次都减少10
//                baos.reset();// 重置baos即清空baos
//                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, options, out);
            out.close();
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    /**
     *  Bitmap add horizontal
     *
     * @param bitmaps
     * @return
     */
    public static Bitmap add2Bitmap(List<Bitmap> bitmaps) {
        int width = 0;
        int height = 0;

        if (bitmaps != null && bitmaps.size() > 0) {
            for (Bitmap bitmap : bitmaps) {
                width += bitmap.getWidth();
                height = Math.max(height, bitmap.getHeight());
            }

            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int currentDrawWidth = 0;
            for (Bitmap bitmap : bitmaps) {
                canvas.drawBitmap(bitmap, currentDrawWidth, 0, null);
                currentDrawWidth += bitmap.getWidth();
            }
            return result;
        }
        return null;
    }
}
