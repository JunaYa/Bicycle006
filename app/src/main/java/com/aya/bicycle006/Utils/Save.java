package com.aya.bicycle006.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aya.bicycle006.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Single on 2016/3/25.
 */
public class Save {
    private static final int DOWNLOAD_NOTIFICATION_ID_DONE = 911;


    public static Observable<Uri> saveImage(Context context, String url, String title) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context)
                                  .load(url)
                                  .asBitmap()
                                  .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                  .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(bitmap -> {
            File appDir = new File(Environment.getExternalStorageDirectory(), "Bicycle");
            if (!appDir.exists()) {
                appDir.mkdir();
            }

            String fileName = title.replace('/', '-') + ".jpg";
            File file = new File(appDir, fileName);
            try {
                FileOutputStream fos = null;
                fos = new FileOutputStream(file);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri uri = Uri.fromFile(file);
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            context.sendBroadcast(scannerIntent);
            return Observable.just(uri);
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<Bitmap> getBitmap(Context context, String url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context)
                                  .load(url)
                                  .asBitmap()
                                  .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                  .get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public static void showNotificationSaveOK(@NonNull Uri file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(file, "image/*");

        NotificationCompat.Builder mNotification = new NotificationCompat.Builder(context);
        
        mNotification
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Image save. Click to preview")
                .setTicker("Image save")
                .setSmallIcon(R.drawable.ic_done)
                .setOngoing(false)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                .setAutoCancel(true);

        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(DOWNLOAD_NOTIFICATION_ID_DONE, mNotification.build());
    }
}
