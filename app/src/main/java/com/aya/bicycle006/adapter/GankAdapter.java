package com.aya.bicycle006.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.listeners.OnBicycleImgClickListener;
import com.aya.bicycle006.model.Gank;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Single on 2016/3/25.
 */
public class GankAdapter extends RecyclerView.Adapter {
    private List<Gank> mGanks;
    private boolean isList = true;
    private OnBicycleImgClickListener mOnBicycleImgClickListener;

    public void setOnBicycleImgClickListener(OnBicycleImgClickListener onBicycleImgClickListener) {
        mOnBicycleImgClickListener = onBicycleImgClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_gank_list, parent, false);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GankViewHolder gankViewHolder = (GankViewHolder) holder;
        Gank gank = mGanks.get(position);
        Glide.with(holder.itemView.getContext())
             .load(gank.getUrl())
             .fitCenter()
             .override(1024, 1024)
             .diskCacheStrategy(DiskCacheStrategy.NONE)
             .skipMemoryCache(false)
             .into(gankViewHolder.img);
        if (mOnBicycleImgClickListener != null) {
            gankViewHolder.img.setOnClickListener(v -> {
                        mOnBicycleImgClickListener.onClick(v, gank);
                    }
            );
        }

    }

    @Override
    public int getItemCount() {
        return mGanks == null ? 0 : mGanks.size();
    }

    static class GankViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img) ImageView img;

        public GankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    public void setGanks(List<Gank> ganks) {
        mGanks = ganks;
        notifyDataSetChanged();
    }

    public void setIsList(boolean isList) {
        this.isList = isList;
    }


}
