package com.aya.bicycle006.ui.fragment_sub;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aya.bicycle006.R;
import com.aya.bicycle006.adapter.NewsAdapter;
import com.aya.bicycle006.component.NewsRetrofit;
import com.aya.bicycle006.component.RetrofitSingleton;
import com.aya.bicycle006.events.FabStatus;
import com.aya.bicycle006.listeners.HideScrollListener;
import com.aya.bicycle006.model.N;
import com.aya.bicycle006.model.News;
import com.aya.bicycle006.ui.base_activity.BaseFragment;
import com.aya.bicycle006.ui.view.DividerItemDecoration;
import com.aya.bicycle006.ui.view.InsertDecoration;

import org.greenrobot.eventbus.EventBus;

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

    private Observer<N> mNewsObserver;

    public static String mNewsWhich;
    private static int size;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsWhich = getArguments().getString("news");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.common_recycler_view, container, false);
        }
        ButterKnife.bind(this, rootView);

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);

        initRecyclerView();
        load();
        return rootView;
    }

    private void initRecyclerView() {
        mAdapter = new NewsAdapter(mContext, mNewses);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        mRecyclerView.setLayoutManager(linearLayoutManager);
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
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
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

    private void loadData(Observer<N> observer) {

        NewsRetrofit.getApiService()
                    .mNews(mNewsWhich, start, count)
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
}
