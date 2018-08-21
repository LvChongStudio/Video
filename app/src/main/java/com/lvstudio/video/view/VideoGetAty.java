package com.lvstudio.video.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.lvstudio.video.R;
import com.lvstudio.video.bean.VideoBean;
import com.lvstudio.video.utils.BitmapUtils;
import com.lvstudio.video.utils.CompressUtils;
import com.lvstudio.video.utils.FileUtils;
import com.lvstudio.video.view.adapter.VideoGetAdapter;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author lvchong
 * @date 2018/7/4
 */

public class VideoGetAty extends Activity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_video_get);

        recyclerView = findViewById(R.id.rv_video_get);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

        final VideoGetAdapter adapter = new VideoGetAdapter(this, this);
        recyclerView.setAdapter(adapter);

        Observable.create(new ObservableOnSubscribe<VideoBean>() {
            @Override
            public void subscribe(ObservableEmitter<VideoBean> emitter) throws Exception {
                getVideo(VideoGetAty.this, emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoBean videoBean) {
                        adapter.setData(videoBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getVideo(Context context, ObservableEmitter<VideoBean> emitter) {
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};

        String[] mediaColumns = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};

        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media
                        .EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if (cursor == null) {
            return;
        }
        if (cursor.moveToFirst()) {
            do {
                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                if (videoPath.toLowerCase().endsWith(".mp4")) {
                    VideoBean info = new VideoBean();
                    int id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Video.Media._ID));
                    Cursor thumbCursor = context.getContentResolver().query(
                            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                            thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                    + "=" + id, null, null);
                    if (thumbCursor.moveToFirst()) {
                        info.setThumbPath(thumbCursor.getString(thumbCursor
                                .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                    }
                    info.setPath(videoPath);
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    info.setDuration(duration / 1000);
                    info = checkVideo(info);
                    emitter.onNext(info);
                }
            } while (cursor.moveToNext());

            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private VideoBean checkVideo(VideoBean videoBean) {
        if (TextUtils.isEmpty(videoBean.getThumbPath()) || "null".equals(videoBean.getThumbPath())) {
            String picturePath = getFilesDir() + "/thumbnail/" +System.currentTimeMillis() + ".jpg";
            BitmapUtils.compressImage(this, FileUtils.getVideoPhoto(videoBean.getPath()), picturePath);
            videoBean.setThumbPath(picturePath);
        }

        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(videoBean.getPath());
        // 视频高度
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        // 视频宽度
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        // 视频旋转方向
        String rotation = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

        try {
            if ("90".equals(rotation) || "270".equals(rotation)) {
                videoBean.setWidth(Integer.parseInt(height));
                videoBean.setHeight(Integer.parseInt(width));
            } else {
                videoBean.setWidth(Integer.parseInt(width));
                videoBean.setHeight(Integer.parseInt(height));
            }
        } catch (Exception e) {
            if ("90".equals(rotation) || "270".equals(rotation)) {
                videoBean.setWidth(720);
                videoBean.setHeight(1280);
            } else {
                videoBean.setWidth(1280);
                videoBean.setHeight(720);
            }
        }

        return videoBean;
    }


    private ProgressDialog progressDialog;
    public void onItemClick(final VideoBean videoBean) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("compressing ...");
        }
        progressDialog.show();
        Observable.create(new ObservableOnSubscribe<VideoBean>() {
            @Override
            public void subscribe(ObservableEmitter<VideoBean> emitter) throws Exception {
                String path = CompressUtils.compressVideo(VideoGetAty.this,
                        videoBean.getPath(),
                        960,
                        540,
                        3);
                videoBean.setPath(path);
                emitter.onNext(videoBean);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoBean videoBean) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(VideoGetAty.this, "compress success : " + videoBean.getPath(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
