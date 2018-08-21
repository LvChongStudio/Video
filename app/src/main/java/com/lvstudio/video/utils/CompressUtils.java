package com.lvstudio.video.utils;

import android.content.Context;
import android.os.Environment;

import com.iceteck.silicompressorr.SiliCompressor;

import java.net.URISyntaxException;

/**
 *
 * @author lvchong
 * @date 2018/8/21
 */

public class CompressUtils {

    public static String compressVideo(Context context, String path, int w, int h, float perPixelRate) {
        int bitrate = (int) (w * h * perPixelRate);
        LogUtils.d("perPixelRate : " + perPixelRate);
        LogUtils.d("bitrate : " + bitrate);
        LogUtils.d("w : " + w);
        LogUtils.d("h : " + h);
        try {
            String newVideoPath = SiliCompressor.with(context).compressVideo(path,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(),
                    w, h, bitrate);
            LogUtils.d("******************** compress done : " + newVideoPath);
            return newVideoPath;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
