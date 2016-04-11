package com.aya.bicycle006.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.listeners.OnBicycleImgClickListener;
import com.aya.bicycle006.model.BILILIFilm;
import com.aya.bicycle006.ui.view.RatioImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nineoldandroids.view.ViewHelper;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Single on 2016/3/4.
 */
public class BILILIFilmAdapter extends RecyclerView.Adapter<BILILIFilmAdapter.ViewHolder> {


    private List<BILILIFilm> mBILILIFilms;

    private OnBicycleImgClickListener mOnBicycleImgClickListener;

    public void setOnBicycleImgClickListener(OnBicycleImgClickListener onBicycleImgClickListener) {
        mOnBicycleImgClickListener = onBicycleImgClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_bilili_film_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BILILIFilm bililiFilm = mBILILIFilms.get(position);
        Glide.with(holder.itemView.getContext())
             .load(bililiFilm.getPic())
             .placeholder(R.mipmap.default_bg)
             .fitCenter()
             .diskCacheStrategy(DiskCacheStrategy.RESULT)
             .into(holder.pic);
        if (mOnBicycleImgClickListener != null) {
//            holder.pic.setTag(R.id.image_tag,position);  glide set tag
            holder.pic.setOnClickListener(v ->
                            mOnBicycleImgClickListener.onClick(v, bililiFilm)
            );
        }
    }

    @Override
    public int getItemCount() {
        return mBILILIFilms == null ? 0 : mBILILIFilms.size();
    }

    public void setBiliList(List<BILILIFilm> arrayList) {
        mBILILIFilms = arrayList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pic) ImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
