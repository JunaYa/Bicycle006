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
import com.aya.bicycle006.Utils.RecyclerViewUtils;
import com.aya.bicycle006.adapter.NewsAdapter;
import com.aya.bicycle006.component.RetrofitSingleton;
import com.aya.bicycle006.events.ChangeShowEvent;
import com.aya.bicycle006.events.FabStatusEvent;
import com.aya.bicycle006.model.N;
import com.aya.bicycle006.model.News;
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
 * Created by Single on 2016/3/19.
 */
public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private int start, count = 10;
    private int lastVisibleItem;

    private NewsAdapter mAdapter;
    private List<News> mNewses = new ArrayList<>();
    private Context mContext;

    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private Observer<N> mNewsObserver;

    public static String mNewsWhich;
    private static int size;

    private App mApp;
    @Bind(R.id.coordinator) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static NewsFragment newInstance() {
        NewsFragment newsFragment = new NewsFragment();
        return newsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mApp = App.getApp();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsWhich = getArguments().getString("news");
        Log.d("which",mNewsWhich);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutView(), container, false);
        }
        ButterKnife.bind(this, rootView);
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);

        initRecyclerView();
        load();
        return rootView;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.common_recycler_view;
    }

    private void initRecyclerView() {
        mAdapter = new NewsAdapter(mContext, mNewses);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        if (mApp.isList) {
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    start += count;
                    loadData(mNewsObserver);
                    Snackbar.make(mCoordinatorLayout, "加载更多,~( ´•︵•` )~", Snackbar.LENGTH_LONG)
                            .show();

                }
                Log.d("bicycle---", lastVisibleItem + "----" + mAdapter.getItemCount());
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
                    Log.d("bicycle---", lastVisibleItem + "----" + mAdapter.getItemCount());
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

    private void loadData(Observer<N> observer) {

        RetrofitSingleton.getNRetrofitSingleton().getApiService()
                    .mNewsApi(mNewsWhich, start, count)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(newses -> {
                        switch (mNewsWhich) {
                            case "T1348649580692":
                                size = newses.T1348649580692.size();
                                break;
                            case "T1348649776727":
                                size = newses.T1348649776727.size();
                                break;
                            case "T1351233117091":
                                size = newses.T1351233117091.size();
                                break;
                            case "T1421997195219":
                                size = newses.T1421997195219.size();
                                break;
                            case "T1401272877187":
                                size = newses.T1401272877187.size();
                                break;
                            case "T1348649654285":
                                size = newses.T1348649654285.size();
                                break;
                        }
                        return size > 0;
                    }).map(newses1 -> {
            return newses1;
        }).doOnNext(news2 -> {
        }).subscribe(observer);
    }

    private void load() {
        mNewsObserver = new Observer<N>() {
            @Override
            public void onCompleted() {
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                RetrofitSingleton.disposeFailureInfo(e, mContext, mRecyclerView);
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                    Snackbar.make(mRecyclerView, "加载完毕，✺◟(∗❛ัᴗ❛ั∗)◞✺", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNext(N news) {
                switch (mNewsWhich) {
                    case "T1348649580692":
                        mNewses.addAll(news.T1348649580692);
                        break;
                    case "T1348649776727":
                        mNewses.addAll(news.T1348649776727);
                        break;
                    case "T1351233117091":
                        mNewses.addAll(news.T1351233117091);
                        break;
                    case "T1421997195219":
                        mNewses.addAll(news.T1421997195219);
                        break;
                    case "T1401272877187":
                        mNewses.addAll(news.T1401272877187);
                        break;
                    case "T1348649654285":
                        mNewses.addAll(news.T1348649654285);
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        loadData(mNewsObserver);
    }

    @Override
    public void onRefresh() {
        loadData(mNewsObserver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeShowEvent(ChangeShowEvent changeShow) {
        boolean isList = changeShow.isList();
        if (isList) {
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
    }

}
