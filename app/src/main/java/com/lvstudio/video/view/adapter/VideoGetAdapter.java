package com.lvstudio.video.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lvstudio.video.R;
import com.lvstudio.video.bean.VideoBean;
import com.lvstudio.video.utils.FormatUtil;
import com.lvstudio.video.utils.LogUtils;
import com.lvstudio.video.view.VideoGetAty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvchong
 * @date 08/04/2018
 */

public class VideoGetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "UpDetailVideoAdapter";
    private Context mContext;
    private List<VideoBean> mVideoBeans;
    private VideoGetAty mActivity;

    public VideoGetAdapter(Context context, VideoGetAty activity) {
        mContext = context;
        mActivity = activity;
        mVideoBeans = new ArrayList<>();
    }

    public void setData(VideoBean videoBean) {
        mVideoBeans.add(videoBean);
        LogUtils.d(TAG, "setData size" + (mVideoBeans != null ? mVideoBeans.size() : 0));
        try {
            notifyItemInserted(mVideoBeans.size() - 1);
        } catch (Exception e) {
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_get, parent, false);
        RecyclerView.ViewHolder myViewHolder = new ViewHolderOne(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoBean VideoBean = mVideoBeans.get(position);
        ViewHolderOne holderOne = (ViewHolderOne) holder;
        Glide.with(mContext).load(VideoBean.getThumbPath()).into(holderOne.ivThumb);
        holderOne.tvDuration.setText(FormatUtil.formatIntTime(VideoBean.getDuration()));
        holderOne.itemView.setOnClickListener(this);
        holderOne.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null != mVideoBeans ? mVideoBeans.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        VideoBean videoBean = mVideoBeans.get(pos);
        LogUtils.d(TAG, "onClick VideoBean : " + videoBean.toString());
        if (mActivity != null) {
            mActivity.onItemClick(videoBean);
        }
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvDuration;

        ViewHolderOne(View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.iv_video_get_thumb);
            tvDuration = itemView.findViewById(R.id.tv_video_get_duration);
        }
    }
}
