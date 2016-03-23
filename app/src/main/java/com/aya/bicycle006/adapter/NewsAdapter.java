package com.aya.bicycle006.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.model.News;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Single on 2016/3/21.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mNewses;
    private Context mContext;
    private LayoutInflater mInflater;

    public NewsAdapter(Context context, List<News> newses) {
        mNewses = newses;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(mInflater.inflate(R.layout.item_news_list, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewses.get(position);
        holder.title.setText(news.getLtitle());
        Glide.with(mContext)
             .load(news.getImgsrc())
             .asBitmap()
             .diskCacheStrategy(DiskCacheStrategy.RESULT)
             .override(600, 200)
             .fitCenter()
             .dontAnimate()
             .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mNewses != null ? mNewses.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.img) ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
