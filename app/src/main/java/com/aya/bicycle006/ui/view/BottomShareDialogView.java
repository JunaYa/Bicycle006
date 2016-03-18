package com.aya.bicycle006.ui.view;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aya.bicycle006.R;

/**
 * Created by Single on 2016/3/18.
 */
public class BottomShareDialogView {
    private static Context mContext;
    public LayoutInflater mInflater;
    private static int[] imgs = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher
            , R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private static String[] names = {"朋友圈", "微信好友", "QQ空间", "QQ好友", "新浪微博", "facebook"};

    public BottomShareDialogView(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.bottom_sheet_dialog_recycler_view, null);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new ShareAdapter());

        dialog.setContentView(view);
        dialog.show();
//        View view = mInflater.inflate(R.layout.bottom_sheet_dialog_recycler_view, null);
//        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setAdapter(new ShareAdapter());
//        dialog.setContentView(view);
//        dialog.show();

    }

    public static void show(Context context) {
        mContext = context;
        new BottomShareDialogView(context);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView img;


        public ViewHolder(View itemView) {
            super(itemView);
//            this.name = (TextView) itemView.findViewById(R.id.share_name);
            this.name = (TextView) itemView.findViewById(R.id.list_item_text_view);
//            this.img = (ImageView) itemView.findViewById(R.id.share_img);

        }
    }

    private class ShareAdapter extends RecyclerView.Adapter<ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//            View view = inflater.inflate(R.layout.item_bottom_sheet_share_dialog_view, null);
            View view = inflater.inflate(R.layout.item_bottom_sheet_share_dialog_view, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String name = names[position];
            holder.name.setText(name);
//            int img = imgs[position];
////            holder.img.setImageResource(img);
//            holder.name.setText(name);
        }

        @Override
        public int getItemCount() {
            return names.length;
        }
    }
}
