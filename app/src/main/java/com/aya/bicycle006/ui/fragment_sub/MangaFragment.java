package com.aya.bicycle006.ui.fragment_sub;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.RecyclerViewUtils;
import com.aya.bicycle006.Utils.Save;
import com.aya.bicycle006.adapter.BILILIFilmAdapter;
import com.aya.bicycle006.component.RetrofitSingleton;
import com.aya.bicycle006.events.ChangeShowEvent;
import com.aya.bicycle006.events.FabStatusEvent;
import com.aya.bicycle006.listeners.HideScrollListener;
import com.aya.bicycle006.listeners.OnBicycleImgClickListener;
import com.aya.bicycle006.model.BILILIFilm;
import com.aya.bicycle006.ui.activity.BIliBiliDetailActivity;
import com.aya.bicycle006.ui.base_activity.BaseFragment;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Single on 2016/3/22.
 */
public class MangaFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    public static SparseArray<Bitmap> bitmapSparseArray = new SparseArray<>(1);
    private Observer<List<BILILIFilm>> mBILILIFilmObserver;
    private BILILIFilmAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private List<BILILIFilm> mBILILIFilms = new ArrayList<>();
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    private View imgView;

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
    public void onResume() {
        super.onResume();
        if (imgView == null) return;
        ViewPropertyAnimator.animate(imgView).alpha(1.0f);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.common_recycler_view;
    }

    private void initRecyclerView() {
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new BILILIFilmAdapter();
        mAdapter.setOnBicycleImgClickListener(mOnBicycleImgClickListener);

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
                EventBus.getDefault().post(new FabStatusEvent(false));
            }

            @Override
            public void onShow() {
                EventBus.getDefault().post(new FabStatusEvent(true));
            }
        });
    }

    @Override
    public void onRefresh() {
        mBILILIFilms.clear();
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
                mBILILIFilms.addAll(bililiFilms);
                mAdapter.setBiliList(mBILILIFilms);
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

//    private void onLoadData() {
//        RetrofitSingleton.getBRetrofitSingleton().getBililiService()
//                         .mBILILIFilmApi()
//                         .map(d -> d.getRank())
//                         .single(d1 -> d1.getCode() == 0)
//                         .subscribeOn(Schedulers.io())
//                         .observeOn(AndroidSchedulers.mainThread())
//                         .subscribe(list -> {
//                             mBILILIFilms.clear();
//                             mAdapter.setBiliList(mBILILIFilms);
//                         });
//    }

    private OnBicycleImgClickListener mOnBicycleImgClickListener = (view, obj) ->
    {
        showPhoto(view, obj);
        imgView = view;
    };


    @SuppressWarnings("UnusedDeclaration")
    private void showPhoto(View view, Object obj) {

        ImageView imageView = (ImageView) view;
        imageView.buildDrawingCache(false);
        if (obj instanceof BILILIFilm) {
            BILILIFilm bililiFilm = (BILILIFilm) obj;
            onFetchBitmap(bililiFilm.getPic());
            Intent intent = BIliBiliDetailActivity.newBILIIntent(getActivity()
                    , bililiFilm.getPic()
                    , bililiFilm.getTitle()
                    , bililiFilm.getDescription());
            startActivityGingerBread(view, intent);
        }
    }

    private void onFetchBitmap(String url) {
        Subscription subscription = Save.getBitmap(getActivity(), url)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(bitmap1 -> {
                                            bitmapSparseArray.put(1, bitmap1);
                                        });

        CompositeSubscription compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(subscription);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startActivityLollipop(View view, Intent intent) {
        intent.setClass(getActivity(), BIliBiliDetailActivity.class);


    }

    private void startActivityGingerBread(View view, Intent intent) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        intent.
                      putExtra("left", screenLocation[0]).
                      putExtra("top", screenLocation[1]).
                      putExtra("width", view.getWidth()).
                      putExtra("height", view.getHeight());

        startActivity(intent);

        // Override transitions: we don't want the normal window animation in addition to our
        // custom one
        getActivity().overridePendingTransition(0, 0);

        // The detail activity handles the enter and exit animations. Both animations involve a
        // ghost view animating into its final or initial position respectively. Since the detail
        // activity starts translucent, the clicked view needs to be invisible in order for the
        // animation to look correct.
        ViewPropertyAnimator.animate(view).alpha(0.4f);
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
