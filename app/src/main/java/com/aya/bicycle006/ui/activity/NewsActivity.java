package com.aya.bicycle006.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.aya.bicycle006.R;
import com.aya.bicycle006.adapter.SimpleFragmentPagerAdapter;
import com.aya.bicycle006.events.ChangeShowEvent;
import com.aya.bicycle006.events.FabStatusEvent;
import com.aya.bicycle006.component.api.NewsService;
import com.aya.bicycle006.ui.base_activity.BaseActivity;
import com.aya.bicycle006.ui.fragment_sub.DouBanMovieFragment;
import com.aya.bicycle006.ui.fragment_sub.GankFragment;
import com.aya.bicycle006.ui.fragment_sub.MangaFragment;
import com.aya.bicycle006.ui.fragment_sub.NewsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by Single on 2016/3/18.
 */
public class NewsActivity extends BaseActivity {
    @Bind(R.id.root_coordinator) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.sliding_tabs) TabLayout mTabLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.fab) FloatingActionButton mFab;

    private ArrayList<Fragment> mFragments;
    private SimpleFragmentPagerAdapter pagerAdapter;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_news;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFragment();

    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        MangaFragment mangaFragment = new MangaFragment();
        mFragments.add(mangaFragment);
        GankFragment gankFragment = new GankFragment();
        mFragments.add(gankFragment);
        DouBanMovieFragment douBanMovieFragment = new DouBanMovieFragment();
        mFragments.add(douBanMovieFragment);
        int count = NewsService.road.length;
        for (int i = 0; i < count; i++) {
            Bundle bundle = new Bundle();
            bundle.putString("news", NewsService.road[i]);
            NewsFragment itemFragment = NewsFragment.newInstance();
            itemFragment.setArguments(bundle);
            mFragments.add(itemFragment);
        }


        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this, mFragments);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt("position"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                startActivity(WebActivity.newGankWebIntent(NewsActivity.this, "http://kotlindoc.com/index.html", "kotlin"));
                break;
            case R.id.ChangeShow:
                boolean isList = mApp.isList;
                if (isList) {
                    mApp.isList = false;
                    EventBus.getDefault().post(new ChangeShowEvent(false));
                } else {
                    mApp.isList = true;
                    EventBus.getDefault().post(new ChangeShowEvent(true));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FabStatusEvent event) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
        final int fabBottomMargin = lp.bottomMargin;
        boolean isShow = event.getIsShow();
        if (isShow) {
            mFab.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        } else {
            mFab.animate()
                .translationY(mFab.getHeight() + fabBottomMargin)
                .setInterpolator(new AccelerateInterpolator())
                .start();
        }
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        Snackbar.make(mCoordinatorLayout, "this-snakebar", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> Log.d("aya-----", "activity----lambda")
                )
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("aya-----", "activity----onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
