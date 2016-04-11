package com.aya.bicycle006;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by Single on 2016/4/6.
 */
public class MGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        //使用Glide给imageView加上图片后，不能设置tag,
//        ViewTarget.setTagId(R.id.image_tag);

        //DecodeFormat.PREFER_ARGB_8888   //每个像素使用了4个字节
//        DecodeFormat.PREFER_RGB_565 //每个像素使用了2两个字节
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);


        //动态值，应用 可用内存的1/8
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        Log.d("cache----", memoryCacheSize + "---");
        Log.d("cache----", defaultMemoryCacheSize + "default");
        Log.d("cache---", customMemoryCacheSize + "custom");
    }


    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
