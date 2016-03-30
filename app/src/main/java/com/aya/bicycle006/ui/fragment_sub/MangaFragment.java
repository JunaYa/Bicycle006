package com.aya.bicycle006.ui.fragment_sub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.RecyclerViewUtils;
import com.aya.bicycle006.adapter.BILILIFilmAdapter;
import com.aya.bicycle006.component.RetrofitSingleton;
import com.aya.bicycle006.events.ChangeShow;
import com.aya.bicycle006.events.FabStatus;
import com.aya.bicycle006.listeners.HideScrollListener;
import com.aya.bicycle006.model.BILILIFilm;
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
public class MangaFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private Observer<List<BILILIFilm>> mBILILIFilmObserver;
    private BILILIFilmAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private List<BILILIFilm> mBILILIFilms = new ArrayList<>();
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutView(), container, false);
        }
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        onLoadManga();
        return rootView;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.common_recycler_view;
    }

    private void initRecyclerView() {
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new BILILIFilmAdapter(mContext, mBILILIFilms);

        linearLayoutManager = RecyclerViewUtils.LManager(mContext);
        staggeredGridLayoutManager = RecyclerViewUtils.SManager(2);

        if (mApp.isList) {
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new HideScrollListener() {
            @Override
            public void onHide() {
                EventBus.getDefault().post(new FabStatus(false));
            }

            @Override
            public void onShow() {
                EventBus.getDefault().post(new FabStatus(true));
            }
        });
    }

    @Override
    public void onRefresh() {
        onLoadMangaByNet(mBILILIFilmObserver);
    }

    private void onLoadManga() {
        mBILILIFilmObserver = new Observer<List<BILILIFilm>>() {
            @Override
            public void onCompleted() {
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                RetrofitSingleton.disposeFailureInfo(e, mContext, mRecyclerView);
            }

            @Override
            public void onNext(List<BILILIFilm> bililiFilms) {
                mBILILIFilms.clear();
                mBILILIFilms.addAll(bililiFilms);
                mAdapter.notifyDataSetChanged();
            }
        };
        onLoadMangaByNet(mBILILIFilmObserver);
    }

    private void onLoadMangaByNet(Observer<List<BILILIFilm>> observer) {
        RetrofitSingleton.getBRetrofitSingleton().getBililiService()
                         .mBILILIFilmApi()
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .filter(bililiFilmApi -> bililiFilmApi.getRank().getCode() == 0)
                         .map(bililiFilmApi1 -> bililiFilmApi1.getRank().getList()
                         ).subscribe(observer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeShowEvent(ChangeShow changeShow) {
        boolean isList = changeShow.isList();
        if (isList) {
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
    }
}
