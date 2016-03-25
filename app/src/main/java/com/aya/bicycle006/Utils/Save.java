package com.aya.bicycle006.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Single on 2016/3/25.
 */
public class Save {
    public static Observable<Uri> saveImage(Context context, String url, String title) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                File bitmap = null;
                try {
                    bitmap = Glide.with(context).load(url).downloadOnly(800, 800).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(bitmap -> {
            Uri uri = Uri.fromFile(bitmap);
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            context.sendBroadcast(scannerIntent);
            Log.d("save","ok");
            return Observable.just(uri);
        }).subscribeOn(Schedulers.io());
    }
}
