package com.aya.bicycle006.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.RecyclerViewUtils;
import com.aya.bicycle006.adapter.GankItemAdapter;
import com.aya.bicycle006.component.GankRetrofit;
import com.aya.bicycle006.model.Gank;
import com.aya.bicycle006.model.GankData;
import com.aya.bicycle006.ui.base_activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Single on 2016/3/28.
 */
public class GankItemActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_DATE = "date";
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    private Observer<GankData> mGankDataObserver;
    private GankItemAdapter mAdapter;
    private List<Gank> mGanks;
    private String  dateStr;

    public static Intent newGankIntent(Context context, String requestStr) {
        Intent intent = new Intent(context, GankItemActivity.class);
        intent.putExtra(ARG_DATE,requestStr);
        return intent;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.common_recycler_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateStr = getIntent().getExtras().getString(ARG_DATE);

        mGanks = new ArrayList<>();
        mAdapter = new GankItemAdapter();
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        initRecyclerView();
        load();
    }

    private void initRecyclerView() {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(RecyclerViewUtils.LManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void load() {
        mGankDataObserver = new Observer<GankData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(GankData gankData) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setGanks(addResult(gankData.getResults()));
            }
        };
        onLoadData(mGankDataObserver);
    }

    private void onLoadData(Observer<GankData> observer) {
        GankRetrofit.getGankService().mGankDataApi(dateStr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(gankData1 -> !gankData1.isError())
                    .map(gankData -> gankData)
                    .subscribe(observer);
    }

    private List<Gank> addResult(GankData.ResultsEntity results) {
        if (results.getAndroid() != null) mGanks.addAll(results.getAndroid());
        if (results.getIOS() != null) mGanks.addAll(results.getIOS());
        if (results.getApp() != null) mGanks.addAll(results.getApp());
        if (results.get拓展资源() != null) mGanks.addAll(results.get拓展资源());
        if (results.get瞎推荐() != null) mGanks.addAll(results.get瞎推荐());
        if (results.get休息视频() != null) mGanks.addAll(results.get休息视频());
        return mGanks;
    }

    @Override
    public void onRefresh() {
        onLoadData(mGankDataObserver);
    }
}
