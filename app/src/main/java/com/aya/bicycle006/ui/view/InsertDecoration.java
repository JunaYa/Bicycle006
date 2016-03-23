package com.aya.bicycle006.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aya.bicycle006.R;


/**
 * Created by Single on 2016/3/1.
 */
public class InsertDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public InsertDecoration(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.material_8dp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin / 2, 0, margin / 2, margin);
    }
}
