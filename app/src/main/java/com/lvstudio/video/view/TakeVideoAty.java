package com.lvstudio.video.view;
import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lvstudio.video.R;

import java.io.File;

/**
 * @author lvstudio
 */
public class TakeVideoAty extends Activity implements OnClickListener {

    private static final String TAG = "TakeVideoAty";
    // 程序中的两个按钮
    TextView record, stop;
    // 系统的视频文件
    File videoFile;
    MediaRecorder mRecorder;
    // 显示视频预览的SurfaceView
    SurfaceView sView;
    // 记录是否正在进行录制
    private boolean isRecording = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main1);
        // 获取程序界面中的两个按钮
        record = (TextView) findViewById(R.id.record);
        stop = (TextView) findViewById(R.id.stop);
        // 让stop按钮不可用
        stop.setEnabled(false);
        // 为两个按钮的单击事件绑定监听器
        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        // 获取程序界面中的SurfaceView
        sView = (SurfaceView) this.findViewById(R.id.sView);
        // 设置分辨率
        sView.getHolder().setFixedSize(1920, 1080);   // 1080P
        // 设置该组件让屏幕不会自动关闭
        sView.getHolder().setKeepScreenOn(true);
    }

    @Override
    public void onClick(View v) {
        // 单击录制按钮
        if (v.getId() == R.id.record) {
            if (!Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                Toast.makeText(TakeVideoAty.this, "SD卡不存在，请插入SD卡！", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                // 创建保存录制视频的视频文件
                videoFile = new File(Environment.getExternalStorageDirectory()
                        .getCanonicalFile() + "/my_video.mp4");
                // 创建MediaPlayer对象
                mRecorder = new MediaRecorder();
                mRecorder.reset();
                // 设置从麦克风采集声音
                mRecorder.setAudioSource(MediaRecorder
                        .AudioSource.MIC);
                // 设置从摄像头采集图像
                mRecorder.setVideoSource(MediaRecorder
                        .VideoSource.CAMERA);
                // 设置视频文件的输出格式
                // 必须在设置声音编码格式、图像编码格式之前设置
                mRecorder.setOutputFormat(MediaRecorder
                        .OutputFormat.MPEG_4);
                // 设置声音编码的格式
                mRecorder.setAudioEncoder(MediaRecorder
                        .AudioEncoder.DEFAULT);
                // 设置图像编码的格式
                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                mRecorder.setVideoSize(1920, 1080);  // 1080P
                // 每秒16帧
                mRecorder.setVideoFrameRate(16);
                mRecorder.setOutputFile(videoFile.getAbsolutePath());
                // 指定使用SurfaceView来预览视频
                mRecorder.setPreviewDisplay(sView.getHolder().getSurface());  // ①
                mRecorder.prepare();
                // 开始录制
                mRecorder.start();
                Log.d(TAG, "---recording---");
                // 让record按钮不可用
                record.setEnabled(false);
                // 让stop按钮可用
                stop.setEnabled(true);
                isRecording = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.stop) {   // 单击停止按钮
            // 如果正在进行录制
            if (isRecording) {
                // 停止录制
                mRecorder.stop();
                // 释放资源
                mRecorder.release();
                mRecorder = null;
                // 让record按钮可用
                record.setEnabled(true);
                // 让stop按钮不可用
                stop.setEnabled(false);
            }
        }
    }
}