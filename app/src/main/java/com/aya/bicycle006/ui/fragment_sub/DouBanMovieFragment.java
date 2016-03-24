package com.aya.bicycle006.ui.fragment_sub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aya.bicycle006.App;
import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.Const;
import com.aya.bicycle006.Utils.PreferencesUtils;
import com.aya.bicycle006.Utils.RecyclerViewUtils;
import com.aya.bicycle006.adapter.DouBanMovieAdapter;
import com.aya.bicycle006.component.DouBanMovieRetrofit;
import com.aya.bicycle006.events.ChangeShow;
import com.aya.bicycle006.events.FabStatus;
import com.aya.bicycle006.model.D;
import com.aya.bicycle006.ui.base_activity.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Single on 2016/3/22.
 */
public class DouBanMovieFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private int start, count = 10;
    private int lastVisibleItem;

    private Context mContext;
    private DouBanMovieAdapter mAdapter;
    private List<D> mDList = new ArrayList<>();
    private Observer<List<D>> mObserver;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;


    private App mApp;

    @Bind(R.id.coordinator) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Log.d("aya-----", "fragment-----onAttach");
        mApp = App.getApp();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("aya-----", "fragment-----onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.common_recycler_view, container, false);
        }
        ButterKnife.bind(this, rootView);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        initRecyclerView();
        load();
        Log.d("aya-----", "fragment-----onCreateView");
        return rootView;

    }


    private void initRecyclerView() {
        linearLayoutManager = RecyclerViewUtils.LManager(mContext);
        staggeredGridLayoutManager = RecyclerViewUtils.SManager(3);
        mAdapter = new DouBanMovieAdapter(mContext, mDList);
        if (mApp.isList) {
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mAdapter.setIsList(true);
        } else {
            mAdapter.setIsList(false);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("bicycle---", lastVisibleItem + "----" + mAdapter.getItemCount());
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    start += count;
                    onLoadData(mObserver);
                    Snackbar.make(mCoordinatorLayout, "加载更多,~( ´•︵•` )~", Snackbar.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mApp.isList) {
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                } else {
                    int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItem = RecyclerViewUtils.findMax(lastPositions);
                }
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    EventBus.getDefault().post(new FabStatus(false));
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    EventBus.getDefault().post(new FabStatus(true));
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }
            }
        });
    }

    private void load() {
        mObserver = new Observer<List<D>>() {
            @Override
            public void onCompleted() {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<D> douBanMovies) {
                mDList.addAll(douBanMovies);
                mAdapter.notifyDataSetChanged();
            }
        };
        onLoadData(mObserver);
    }

    private void onLoadData(Observer<List<D>> observer) {
        DouBanMovieRetrofit.getApiService()
                           .getTopMovie(start, count)
                           .subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread())
                           .filter(douBanMovie -> {
                               return douBanMovie.getTotal() > 0;
                           }).map(douBanMovie1 -> {
            return douBanMovie1.getSubjects();
        }).subscribe(observer);
    }

    @Override
    public void onRefresh() {
        start = 0;
        mDList.clear();
        onLoadData(mObserver);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("aya-----", "fragment-----onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("aya-----", "fragment-----onResume");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeShowEvent(ChangeShow changeShow) {
        boolean isList = changeShow.isList();
        if (isList) {
            mAdapter.setIsList(true);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            mAdapter.setIsList(false);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("aya-----", "fragment-----onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("aya-----", "fragment-----onViewStateRestored");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        Log.d("aya-----", "fragment-----onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aya-----", "fragment-----onDestroy");
    }
}
