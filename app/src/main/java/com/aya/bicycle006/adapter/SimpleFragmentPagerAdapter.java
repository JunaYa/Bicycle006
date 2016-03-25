package com.aya.bicycle006.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.aya.bicycle006.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Single on 2016/3/21.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"Manga","gank","Top250","科技","数码","移动互联","云课堂","读书","手机"};

    private Context context;
    private ArrayList<Fragment> mFragments;

    public SimpleFragmentPagerAdapter(FragmentManager fm,Context context,ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);

    }

    @Override
    public int getCount() {
        return mFragments!=null?mFragments.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = context.getResources().getDrawable(R.mipmap.ic_launcher);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}
