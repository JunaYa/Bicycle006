package com.aya.bicycle006.ui.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.aya.bicycle006.R;
import com.aya.bicycle006.ui.base_activity.BaseActivity;
import com.aya.bicycle006.ui.view.BottomShareDialogView;
import com.aya.bicycle006.ui.view.BottomSheetDialogView;
import com.aya.bicycle006.ui.view.BottomSheetShareDialogView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind(R.id.fab)
    FloatingActionButton mFAB;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.status_text_view)
    TextView mStatusTextView;
    @Bind(R.id.vector_drawable_cpu_ani)
    ImageView mCpuAniImageView;

    private int mDayNightMode = AppCompatDelegate.MODE_NIGHT_AUTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(mToolbar);

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Drawable drawable = mCpuAniImageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }

        int uiMode = getResources().getConfiguration().uiMode;
        int dayNightUiMode = uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (dayNightUiMode == Configuration.UI_MODE_NIGHT_NO) {
            mDayNightMode = AppCompatDelegate.MODE_NIGHT_NO;
            mStatusTextView.setText(R.string.text_for_day_night_mode_night_no);
        } else if (dayNightUiMode == Configuration.UI_MODE_NIGHT_YES) {
            mDayNightMode = AppCompatDelegate.MODE_NIGHT_YES;
            mStatusTextView.setText(R.string.text_for_day_night_mode_night_yes);
        } else {
            mDayNightMode = AppCompatDelegate.MODE_NIGHT_AUTO;
            mStatusTextView.setText(R.string.text_for_day_night_mode_night_auto);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fab)
    void fabClick() {
        Snackbar.make(mFAB, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            BottomSheetShareDialogView.show(this,mDayNightMode);
        } else if (id == R.id.action_day_night_yes) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            recreate();
            return true;
        } else if (id == R.id.ChangeShow) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
            return true;
        } else {
            if (id == R.id.action_bottom_sheet_dialog) {
                BottomSheetDialogView.show(this, mDayNightMode);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
