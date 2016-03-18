package com.aya.bicycle006.ui.view;


import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aya.bicycle006.R;

public class BottomSheetShareDialogView {

    private static int[] imgs = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
            , R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
            , R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private static String[] names = {"朋友圈", "微信好友", "QQ空间", "QQ好友", "新浪微博", "facebook", "QQ好友", "新浪微博", "facebook"};

    /**
     * remember to call setLocalNightMode for dialog
     *
     * @param context
     * @param dayNightMode current day night mode
     */
    public BottomSheetShareDialogView(Context context, int dayNightMode) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.getDelegate().setLocalNightMode(dayNightMode);

        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.bottom_sheet_share_dialog_recycler_view, null);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new SimpleAdapter());

        dialog.setContentView(view);
        dialog.show();
    }

    public static void show(Context context, int dayNightMode) {
        new BottomSheetShareDialogView(context, dayNightMode);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.share_name);
            img = (ImageView) itemView.findViewById(R.id.share_img);
        }
    }

    private static class SimpleAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_bottom_sheet_share_dialog_view, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(names[position]);
            holder.img.setImageResource(imgs[position]);

        }

        @Override
        public int getItemCount() {
            return names.length;
        }
    }
}
