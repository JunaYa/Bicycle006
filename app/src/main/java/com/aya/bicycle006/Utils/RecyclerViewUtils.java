package com.aya.bicycle006.Utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Single on 2016/3/24.
 */
public class RecyclerViewUtils {

    //找到数组中的最大值
    public static int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static StaggeredGridLayoutManager SManager(int num) {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(num, StaggeredGridLayoutManager.VERTICAL);
        return staggeredGridLayoutManager;
    }

    public static LinearLayoutManager LManager(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }
}
