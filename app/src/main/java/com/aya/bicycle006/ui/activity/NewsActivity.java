package com.aya.bicycle006.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.aya.bicycle006.R;
import com.aya.bicycle006.adapter.SimpleFragmentPagerAdapter;
import com.aya.bicycle006.events.FabStatus;
import com.aya.bicycle006.component.api.NewsApi;
import com.aya.bicycle006.ui.base_activity.BaseActivity;
import com.aya.bicycle006.ui.fragment_sub.DouBanMovieFragment;
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

        EventBus.getDefault().register(this);


        initFragment();
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        MangaFragment mangaFragment = new MangaFragment();
        mFragments.add(mangaFragment);
        DouBanMovieFragment douBanMovieFragment = new DouBanMovieFragment();
        mFragments.add(douBanMovieFragment);
        int count = NewsApi.road.length ;
        for (int i = 0; i < count; i++) {
            Bundle bundle = new Bundle();
            bundle.putString("news",NewsApi.road[i]);
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
                startActivity(new Intent(this, MangaActivity.class));
                break;
            case R.id.action_day_night_no:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FabStatus event) {
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
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
