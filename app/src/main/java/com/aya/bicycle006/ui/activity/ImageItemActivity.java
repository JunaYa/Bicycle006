package com.aya.bicycle006.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.Save;
import com.aya.bicycle006.ui.base_activity.BaseActivity;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.Bind;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Single on 2016/3/31.
 */
public class ImageItemActivity extends BaseActivity {
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String TRANSIT_PIC = "picture";


    @Bind(R.id.img) ImageView mImg;
    @Bind(R.id.app_bar_layout) AppBarLayout mBarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private boolean mIsHidden = true;
    private PhotoViewAttacher mPhotoViewAttacher;
    String mImageUrl, mImageTitle;

    private CompositeSubscription mCompositeSubscription;

    public static Intent newImageIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, ImageItemActivity.class);
        intent.putExtra(ImageItemActivity.EXTRA_IMAGE_URL, url);
        intent.putExtra(ImageItemActivity.EXTRA_IMAGE_TITLE, desc);
        return intent;
    }


    private void parseIntent() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mImageTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
    }


    @Override
    protected int getLayoutView() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent();
        ViewCompat.setTransitionName(mImg, TRANSIT_PIC);
        Glide.with(this).load(mImageUrl).into(mImg);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBarLayout.setAlpha(0.6f);
        setTitle(mImageTitle);
        setupPhotoAttacher();
    }

    private void setupPhotoAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(mImg);
        mPhotoViewAttacher.setOnViewTapListener((view, v, v1) -> hideOrShowToolbar());
        mPhotoViewAttacher.setOnLongClickListener(v -> {
            new AlertDialog.Builder(ImageItemActivity.this)
                    .setMessage("保存到手机")
                    .setNegativeButton("取消", ((dialog, which) -> dialog.dismiss()))
                    .setPositiveButton("保存", ((dialog1, which1) -> {
                        saveImage();
                        dialog1.dismiss();
                    })).show();
            return true;
        });
    }

    private void saveImage() {
        Subscription subscription = Save.saveImage(this, mImageUrl, mImageTitle)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(uri -> {
                                            File appDir = new File(Environment.getExternalStorageDirectory(), "bicycle");
                                            String msg = String.format("bicycle", appDir.getAbsolutePath());
                                            Log.d("save", "msg" + msg);
//                                            Save.showNotificationSaveOK(uri,mContext);
                                        });
        addSubscribe(subscription);
    }

    private void addSubscribe(Subscription s) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(s);
    }

    private void hideOrShowToolbar() {
        mBarLayout.animate()
                  .translationY(mIsHidden ? 0 : -mBarLayout.getHeight())
                  .setInterpolator(new DecelerateInterpolator(2))
                  .start();

        mIsHidden = !mIsHidden;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

}
