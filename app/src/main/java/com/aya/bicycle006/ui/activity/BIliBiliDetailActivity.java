package com.aya.bicycle006.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.Utils.ImageUrils;
import com.aya.bicycle006.listeners.AnimatorListener;
import com.aya.bicycle006.ui.base_activity.BaseActivity;
import com.aya.bicycle006.ui.fragment_sub.MangaFragment;
import com.aya.bicycle006.ui.view.SpotlightView;
import com.bumptech.glide.Glide;
import com.makeramen.RoundedImageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.view.View;
import android.widget.TextView;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Single on 2016/4/8.
 */
public class BIliBiliDetailActivity extends BaseActivity {
    private static final Interpolator sDecelerator = new DecelerateInterpolator();
    private static final Interpolator sAccelerator = new AccelerateInterpolator();

    @Bind(R.id.back_btn) ImageView btnBack;
    @Bind(R.id.photo) ImageView hero;
    @Bind(R.id.container) View container;
    @Bind(R.id.spotlight) SpotlightView spotlight;
    @Bind(R.id.info_container) View infoContainer;
    @Bind(R.id.title) TextView title;
    @Bind(R.id.description) TextView description;
    private Bitmap photo;
    @Bind(R.id.animated_photo) RoundedImageView animatedHero;

    private static final String ARG_PIC = "pic";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";


    public static Intent newBILIIntent(Context context, String picUrl, String title, String description) {
        Intent intent = new Intent(context, BIliBiliDetailActivity.class);
        intent.putExtra(ARG_PIC, picUrl);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_DESCRIPTION, description);
        return intent;


    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_bilibili_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the alpha values here for Gingerbread support
//        photo = setupPhoto(getIntent().getStringExtra("pic"));
        title.setText(getIntent().getStringExtra(ARG_TITLE));
        description.setText(getIntent().getStringExtra(ARG_DESCRIPTION));
        postCreate();
        setupEnterAnimation();
    }


    private void postCreate() {
        // Hide the back button until the entry animation is complete
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btnBack.setVisibility(View.GONE);
        // Setup the alpha values here for Gingerbread support
        ViewHelper.setAlpha(infoContainer, 0);
        ViewHelper.setAlpha(container, 0);

        Glide.with(this).load(getIntent().getStringExtra(ARG_PIC)).into(animatedHero);
        Glide.with(this).load(getIntent().getStringExtra(ARG_PIC)).into(hero);
        photo = ImageViewToBitmap();

        colorize(photo);
    }

    private Bitmap ImageViewToBitmap() {
        Glide.with(this).load(getIntent().getStringExtra(ARG_PIC)).into(hero);
        hero.buildDrawingCache(true);
        Bitmap bitmap = hero.getDrawingCache();
        return bitmap == null ? MangaFragment.bitmap : bitmap;
    }

    @OnClick(R.id.back_btn)
    void onClick() {
        onBackPressed();
    }

    private Bitmap setupPhoto(int resId) {
        Bitmap bitmap = ImageUrils.decodeSampledBitmapFromResource(getResources(),
                resId, 200, 200);
        hero.setImageBitmap(bitmap);
        return bitmap;
    }

    private void colorize(Bitmap photo) {
        Palette palette = Palette.generate(photo);
        applyPalette(palette);
    }

    public void applyPalette(Palette palette) {
        Resources res = getResources();

        container.setBackgroundColor(palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)));

        title.setTextColor(palette.getVibrantColor(res.getColor(R.color.default_vibrant)));

        description.setTextColor(palette.getLightVibrantColor(res.getColor(R.color.default_light_vibrant)));

        colorButton(R.id.info_button, palette.getDarkMutedColor(res.getColor(R.color.default_dark_muted)),
                palette.getDarkVibrantColor(res.getColor(R.color.default_dark_vibrant)));
        colorButton(R.id.star_button, palette.getMutedColor(res.getColor(R.color.default_muted)),
                palette.getVibrantColor(res.getColor(R.color.default_vibrant)));
    }

    public void colorButton(int id, int bgColor, int tintColor) {
        ImageButton buttonView = (ImageButton) findViewById(id);

        StateListDrawable bg = new StateListDrawable();
        ShapeDrawable normal = new ShapeDrawable(new OvalShape());
        normal.getPaint().setColor(bgColor);
        ShapeDrawable pressed = new ShapeDrawable(new OvalShape());
        pressed.getPaint().setColor(tintColor);
        bg.addState(new int[]{android.R.attr.state_pressed}, pressed);
        bg.addState(new int[]{}, normal);
        ImageUrils.setBackgroundCompat(buttonView, bg);
    }

    public void setupEnterAnimation() {
        spotlight.getViewTreeObserver()
                 .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                     @SuppressWarnings("deprecation")
                     @Override
                     public void onGlobalLayout() {
                         View button = findViewById(R.id.info);
                         spotlight.setMaskX(button.getRight() - (button.getWidth() / 2));
                         spotlight.setMaskY(button.getBottom() - (button.getHeight() / 2));
                         runEnterAnimation();
                         ImageUrils.removeOnGlobalLayoutListenerCompat(spotlight, this);
                     }
                 });
    }

    /**
     * The enter animation scales the hero in from its previous thumbnail
     * size/location. In parallel, the container of the activity is fading in.
     * When the picture is in place, we crossfade in the actual hero image.
     */
    public void runEnterAnimation() {
        // Retrieve the data we need for the picture to display and
        // the thumbnail to animate it from
        Bundle bundle = getIntent().getExtras();
        final int thumbnailTop = bundle.getInt("top");
        final int thumbnailLeft = bundle.getInt("left");
        final int thumbnailWidth = bundle.getInt("width");
        final int thumbnailHeight = bundle.getInt("height");

        // Scale factors to make the large version the same size as the thumbnail
        float mWidthScale = (float) thumbnailWidth / animatedHero.getWidth();
        float mHeightScale = (float) thumbnailHeight / animatedHero.getHeight();

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        ViewHelper.setPivotX(animatedHero, 0);
        ViewHelper.setPivotY(animatedHero, 0);
        ViewHelper.setScaleX(animatedHero, mWidthScale);
        ViewHelper.setScaleY(animatedHero, mHeightScale);
        ViewHelper.setTranslationX(animatedHero, thumbnailLeft);
        ViewHelper.setTranslationY(animatedHero, thumbnailTop);

        // Animate scale and translation to go from thumbnail to full size
        ViewPropertyAnimator.animate(animatedHero).
                scaleX(1).scaleY(1).
                                    translationX(0).translationY(0).
                                    setInterpolator(sDecelerator).
                                    setListener(new AnimatorListener() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            ViewPropertyAnimator.animate(animatedHero).alpha(0);
                                            ViewPropertyAnimator.animate(infoContainer).alpha(1);

                                            // The back button can be shown
                                            btnBack.setVisibility(View.VISIBLE);
                                        }
                                    });
        // Animate in the container with the background and text
        ViewPropertyAnimator.animate(container).alpha(1);
    }

    public void setupExitAnimation() {
        ViewPropertyAnimator.animate(animatedHero).alpha(1);
        ViewPropertyAnimator.animate(infoContainer).alpha(0).setListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runExitAnimation();
            }
        });
    }

    /**
     * The exit animation is basically a reverse of the enter animation
     */
    public void runExitAnimation() {
        Bundle bundle = getIntent().getExtras();
        final int thumbnailTop = bundle.getInt("top");
        final int thumbnailLeft = bundle.getInt("left");
        final int thumbnailWidth = bundle.getInt("width");
        final int thumbnailHeight = bundle.getInt("height");

        float mWidthScale = (float) thumbnailWidth / animatedHero.getWidth();
        float mHeightScale = (float) thumbnailHeight / animatedHero.getHeight();

        ViewHelper.setPivotX(animatedHero, 0);
        ViewHelper.setPivotY(animatedHero, 0);
        ViewHelper.setScaleX(animatedHero, 1);
        ViewHelper.setScaleY(animatedHero, 1);
        ViewHelper.setTranslationX(animatedHero, 0);
        ViewHelper.setTranslationY(animatedHero, 0);

        ViewPropertyAnimator.animate(animatedHero).
                scaleX(mWidthScale).scaleY(mHeightScale).
                                    translationX(thumbnailLeft).translationY(thumbnailTop).
                                    setInterpolator(sAccelerator).
                                    setListener(new AnimatorListener() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            finish();
                                        }
                                    });
        ViewPropertyAnimator.animate(container).alpha(0);

        // Hide the back button during the exit animation
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btnBack.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        setupExitAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        // Override transitions: we don't want the normal window animation in addition to our
        // custom one
        overridePendingTransition(0, 0);
    }
}
