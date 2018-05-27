package com.lvstudio.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

/**
 * Created by lvchong on 2018/5/14.
 */

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void takePicture(View view) {
        startActivity(new Intent(this, TakePictureActivity.class));
    }

    public void takeVideo(View view) {
        startActivity(new Intent(this, TakeVideoActivity.class));
    }

    public void playVideo(View view) {
        startActivity(new Intent(this, PlayVideoActivity.class));
    }
}
