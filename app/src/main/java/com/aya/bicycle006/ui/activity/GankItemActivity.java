package com.aya.bicycle006.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.RecyclerViewUtils;
import com.aya.bicycle006.adapter.GankItemAdapter;
import com.aya.bicycle006.component.GankRetrofit;
import com.aya.bicycle006.listeners.OnBicycleImgClickListener;
import com.aya.bicycle006.model.Gank;
import com.aya.bicycle006.model.GankData;
import com.aya.bicycle006.ui.base_activity.BaseActivity;
import com.aya.bicycle006.ui.view.WebVideoView;

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
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.stub_video_view) ViewStub mVideoViewStub;

    private WebVideoView mVideoView;
    private boolean mIsVideoViewInflated = false;
    private Observer<List<Gank>> mGankDataObserver;
    private GankItemAdapter mAdapter;
    private List<Gank> mGanks;
    private String dateStr;

    public static Intent newGankIntent(Context context, String requestStr) {
        Intent intent = new Intent(context, GankItemActivity.class);
        intent.putExtra(ARG_DATE, requestStr);
        return intent;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_gank_data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateStr = getIntent().getExtras().getString(ARG_DATE);

        mGanks = new ArrayList<>();
        mAdapter = new GankItemAdapter();
        mAdapter.setOnBicycleImgClickListener(mOnBicycleImgClickListener);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        mGankDataObserver = new Observer<List<Gank>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<Gank> gankData) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setGanks(gankData);
            }
        };
        onLoadData(mGankDataObserver);
    }

    private void onLoadData(Observer<List<Gank>> observer) {
        GankRetrofit.getGankService().mGankDataApi(dateStr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(gankData1 -> !gankData1.isError())
                    .map(gankData -> gankData.getResults())
                    .map(this::addResult)
                    .subscribe(observer);
    }

    private List<Gank> addResult(GankData.ResultsEntity results) {
        if (results.get休息视频() != null) mGanks.addAll(results.get休息视频());
        if (results.getAndroid() != null) mGanks.addAll(results.getAndroid());
        if (results.getApp() != null) mGanks.addAll(results.getApp());
        if (results.get瞎推荐() != null) mGanks.addAll(results.get瞎推荐());
        if (results.get拓展资源() != null) mGanks.addAll(results.get拓展资源());
        if (results.getIOS() != null) mGanks.addAll(results.getIOS());
        return mGanks;
    }

    @Override
    public void onRefresh() {
        onLoadData(mGankDataObserver);
    }

    private OnBicycleImgClickListener mOnBicycleImgClickListener = (view, obj) -> {
        if (obj instanceof Gank) {
            Gank gank = (Gank) obj;
            if (gank.getType().equals("休息视频")) {
                resumeVideoView();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            } else {
                Intent intent = WebActivity.newGankWebIntent(GankItemActivity.this, gank.getUrl(), gank
                        .getDesc());
                startActivity(intent);
            }
        }

    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setVideoViewPosition(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    private void setVideoViewPosition(Configuration configuration) {
        switch (configuration.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (mIsVideoViewInflated) {
                    mVideoViewStub.setVisibility(View.VISIBLE);
                } else {
                    mVideoView = (WebVideoView) mVideoViewStub.inflate();
                    mIsVideoViewInflated = true;
                }
                if (mGanks.size() > 0 && mGanks.get(0).getType().equals("休息视频")) {
                    mVideoView.loadUrl(mGanks.get(0).getUrl());
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
            case Configuration.ORIENTATION_UNDEFINED:
            default:
                mVideoViewStub.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeVideoView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseVideoView();
        clearVideoView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resumeVideoView();
    }

    private void pauseVideoView() {
        if (mVideoView != null) {
            mVideoView.onPause();
            mVideoView.pauseTimers();
        }
    }

    private void resumeVideoView() {
        if (mVideoView != null) {
            mVideoView.resumeTimers();
            mVideoView.onResume();
        }
    }

    private void clearVideoView() {
        if (mVideoView != null) {
            mVideoView.clearHistory();
            mVideoView.clearCache(true);
            mVideoView.loadUrl("about:blank");
            mVideoView.pauseTimers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gank_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.gank_io:
                String url = "http://gank.io/" + dateStr;
                startActivity(WebActivity.newGankWebIntent(this, url, "gank.io"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
