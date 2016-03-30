package com.aya.bicycle006.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


import com.aya.bicycle006.R;
import com.aya.bicycle006.adapter.BILILIFilmAdapter;
import com.aya.bicycle006.component.BilibiliRetrofit;
import com.aya.bicycle006.component.RetrofitSingleton;
import com.aya.bicycle006.model.BILILIFilm;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Single on 2016/3/3.
 */
public class MangaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private WeakReference<AppCompatActivity> mWeakReference;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Observer<List<BILILIFilm>> mBILILIFilmObserver;
    private BILILIFilmAdapter mAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler_view);
        ButterKnife.bind(this);
        initView();
        onLoadManga();

        if (mWeakReference == null) {
            mWeakReference = new WeakReference<>(MangaActivity.this);
        }
    }

    private void initView() {
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MangaActivity.this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    private void onLoadManga() {
        mBILILIFilmObserver = new Observer<List<BILILIFilm>>() {
            @Override public void onCompleted() {
                mRefreshLayout.setRefreshing(false);
            }

            @Override public void onError(Throwable e) {
                RetrofitSingleton.disposeFailureInfo(e, MangaActivity.this, mRecyclerView);
            }

            @Override public void onNext(List<BILILIFilm> bililiFilms) {
                mAdapter = new BILILIFilmAdapter(MangaActivity.this, bililiFilms);
                mRecyclerView.setAdapter(mAdapter);
            }
        };
        onLoadMangaByNet(mBILILIFilmObserver);
    }

    private void onLoadMangaByNet(Observer<List<BILILIFilm>> observer) {
        BilibiliRetrofit.getBililiService()
                .mBILILIFilmApi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(bililiFilmApi ->bililiFilmApi.getRank().getCode() == 0
                ).map(bililiFilmApi1 -> bililiFilmApi1.getRank().getList()
        ).subscribe(observer);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override public void onRefresh() {
        onLoadManga();
    }
}
