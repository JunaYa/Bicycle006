package com.aya.bicycle006.Utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;

/**
 * Created by Single on 2016/3/28.
 */
public class StringStyleUtils {
    public static SpannableString format(Context context,String text,int style){
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context,style),0,text.length(),0);
        return spannableString;
    }
}
