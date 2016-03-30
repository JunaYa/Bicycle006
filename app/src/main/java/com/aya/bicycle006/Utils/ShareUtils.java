package com.aya.bicycle006.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Single on 2016/3/30.
 */
public class ShareUtils {
    public static void share(Context context) {

    }

    public static void shareImage(Context context, Uri uri,String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }
}
