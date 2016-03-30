package com.aya.bicycle006.Utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Single on 2016/3/28.
 */
public class SnackBarUtils {

    public static void SnakeShort(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void SnakeShort(View view, Context context, @StringRes int text) {
        String t = context.getResources().getString(text);
        Snackbar.make(view, t, Snackbar.LENGTH_SHORT).show();
    }

    public static void SnakeLong(View view,String text){
        Snackbar.make(view,text,Snackbar.LENGTH_LONG).show();
    }
}
