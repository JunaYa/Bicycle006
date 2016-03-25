package com.aya.bicycle006.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.model.BILILIFilm;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Single on 2016/3/4.
 */
public class BILILIFilmAdapter extends RecyclerView.Adapter<BILILIFilmAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<BILILIFilm> mBILILIFilms;

    public BILILIFilmAdapter(Context context, List<BILILIFilm> BILILIFilms) {
        mContext = context;
        mBILILIFilms = (ArrayList<BILILIFilm>) BILILIFilms;
        mInflater = LayoutInflater.from(context);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_bilili_film_list, parent, false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        BILILIFilm bililiFilm = mBILILIFilms.get(position);
        holder.title.setText(bililiFilm.getTitle());
        Glide.with(mContext)
                .load(bililiFilm.getPic())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.pic);
    }

    @Override public int getItemCount() {
        return mBILILIFilms != null ? mBILILIFilms.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.pic) ImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
