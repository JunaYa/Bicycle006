package com.aya.bicycle006.ui.fragment_sub;

import android.os.Bundle;
import android.os.Environment;
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

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.DateUtils;
import com.aya.bicycle006.Utils.RecyclerViewUtils;
import com.aya.bicycle006.Utils.Save;
import com.aya.bicycle006.Utils.SnackBarUtils;
import com.aya.bicycle006.adapter.GankMeiZiAdapter;
import com.aya.bicycle006.component.RetrofitSingleton;
import com.aya.bicycle006.events.ChangeShowEvent;
import com.aya.bicycle006.events.FabStatusEvent;
import com.aya.bicycle006.listeners.OnBicycleImgClickListener;
import com.aya.bicycle006.listeners.OnBicycleItemLongClickListener;
import com.aya.bicycle006.model.Gank;
import com.aya.bicycle006.model.GankApi;
import com.aya.bicycle006.ui.activity.GankItemActivity;
import com.aya.bicycle006.ui.activity.ImageItemActivity;
import com.aya.bicycle006.ui.base_activity.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Single on 2016/3/25.
 */
public class GankFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int HIDE_THRESHOLD = 20;
    private boolean controlsVisible;
    private int scrolledDistance;

    private Observer<GankApi> mGankApiObserver;
    private int number = 20, page = 0;
    private int lastVisibleItem;

    @Bind(R.id.coordinator) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private GankMeiZiAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutView(), container, false);
        }
        ButterKnife.bind(this, rootView);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        initRecyclerView();
        load();
        return rootView;
    }

    private void initRecyclerView() {
        mAdapter = new GankMeiZiAdapter();
        mAdapter.setOnBicycleImgClickListener(mOnBicycleImgClickListener);
        mAdapter.setOnBicycleItemLongClickListener(mOnBicycleItemLongClickListener);
        mLinearLayoutManager = RecyclerViewUtils.LManager(mContext);
        mStaggeredGridLayoutManager = RecyclerViewUtils.SManager(2);
        if (mApp.isList) {
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        } else {
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    page += 1;
                    onLoadData(mGankApiObserver);
                    Snackbar.make(mCoordinatorLayout, "加载更多,~( ´•︵•` )~", Snackbar.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mApp.isList) {
                    lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                } else {
                    int[] lastPositions = new int[mStaggeredGridLayoutManager.getSpanCount()];
                    mStaggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItem = RecyclerViewUtils.findMax(lastPositions);
                }
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    EventBus.getDefault().post(new FabStatusEvent(false));
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    EventBus.getDefault().post(new FabStatusEvent(true));
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }


            }
        });

    }


    private OnBicycleImgClickListener mOnBicycleImgClickListener
            = (view, obj) -> {
        if (obj instanceof Gank) {
            Gank gank = (Gank) obj;
            startActivity(ImageItemActivity.newImageIntent(mContext, gank.getUrl(), gank.getDesc()));
        }
    };



private OnBicycleItemLongClickListener mOnBicycleItemLongClickListener = new OnBicycleItemLongClickListener() {

    @Override
    public void onLongClick(View view, Object obj) {
        SnackBarUtils.SnakeShort(mCoordinatorLayout, "cardClick");
        if (obj instanceof Gank) {
            Gank gank = (Gank) obj;
            String date = gank.getPublishedAt();
            Date dd = DateUtils.parseApiDateTZ(date);
            String dateStr = DateUtils.formatApiDateS(dd);
            startActivity(GankItemActivity.newGankIntent(mContext, dateStr));
        }
    }

};


    @Override
    protected int getLayoutView() {
        return R.layout.common_recycler_view;
    }

    private void load() {
        mGankApiObserver = new Observer<GankApi>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(GankApi ganks) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setGanks(ganks.getResults());
            }
        };
        onLoadData(mGankApiObserver);
    }

    private void onLoadData(Observer<GankApi> observer) {
        RetrofitSingleton.getGRetrofitSingleton().getGankService()
                         .mGankApi(number, page)
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .filter(gankApi -> !gankApi.isError())
                         .map(gankApi1 -> gankApi1)
                         .subscribe(observer);

    }

    @Override
    public void onRefresh() {
        onLoadData(mGankApiObserver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeShowEvent(ChangeShowEvent changeShow) {
        boolean isList = changeShow.isList();
        if (isList) {
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        } else {
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        }
    }


}
