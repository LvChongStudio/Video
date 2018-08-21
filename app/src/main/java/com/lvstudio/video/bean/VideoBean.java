package com.lvstudio.video.bean;

import java.io.Serializable;

/**
 * @author lvstudio
 */
public class VideoBean implements Serializable {
    private String thumbPath;
    private String path;
    private int duration;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "thumbPath='" + thumbPath + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                '}';
    }
}