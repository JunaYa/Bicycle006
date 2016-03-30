package com.aya.bicycle006.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.StringStyleUtils;
import com.aya.bicycle006.model.Gank;
import com.aya.bicycle006.ui.activity.WebActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Single on 2016/3/28.
 */
public class GankItemAdapter extends RecyclerView.Adapter {

    private List<Gank> mGanks;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_gank_item_list, parent, false);
        return new GankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Gank gank = mGanks.get(position);
        GankViewHolder gankViewHolder = (GankViewHolder) holder;
        if (position == 0) {
            showCategory(gankViewHolder);
        } else {
            boolean theCategoryOfLastEqualsToThis = mGanks.get(position - 1).getType()
                                                          .equals(mGanks.get(position).getType());
            if (!theCategoryOfLastEqualsToThis) {
                showCategory(gankViewHolder);
            } else {
                hideCategory(gankViewHolder);
            }
        }
        gankViewHolder.category.setText(gank.getType());
        SpannableStringBuilder builder = new SpannableStringBuilder(gank.getDesc()).append(
                StringStyleUtils.format(gankViewHolder.gank.getContext()
                        , "(via. " + gank.getWho() + ")"
                        , R.style.ViaTextAppearance));
        CharSequence gankText = builder.subSequence(0, builder.length());
        gankViewHolder.gank.setText(gankText);

    }

    private void showCategory(GankViewHolder gankViewHolder) {
        if (gankViewHolder.category.getVisibility() == View.GONE) {
            gankViewHolder.category.setVisibility(View.VISIBLE);
        }
    }

    private void hideCategory(GankViewHolder gankViewHolder) {
        if (gankViewHolder.category.getVisibility() == View.VISIBLE) {
            gankViewHolder.category.setVisibility(View.GONE);
        }
    }

    public void setGanks(List<Gank> ganks){
        mGanks = ganks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mGanks == null ? 0 : mGanks.size();
    }

     class GankViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_category) TextView category;
        @Bind(R.id.tv_title) TextView gank;

        public GankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick(R.id.ll_gank_parent) void onGank(View v){
            Gank gank = mGanks.get(getLayoutPosition());
            Intent intent = WebActivity.newGankWebIntent(v.getContext(),gank.getUrl(),gank.getDesc());
            v.getContext().startActivity(intent);
        }
    }
}
