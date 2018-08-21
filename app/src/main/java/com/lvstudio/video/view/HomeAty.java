package com.lvstudio.video.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lvstudio.video.R;

/**
 *
 * @author lvchong
 * @date 2018/5/14
 */

public class HomeAty extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_home);
    }

    public void takePicture(View view) {
        startActivity(new Intent(this, TakePictureAty.class));
    }

    public void takeVideo(View view) {
        startActivity(new Intent(this, TakeVideoAty.class));
    }

    public void playVideo(View view) {
        startActivity(new Intent(this, PlayVideoAty.class));
    }

    public void showVideos(View view) {
        startActivity(new Intent(this, VideoGetAty.class));
    }
}
