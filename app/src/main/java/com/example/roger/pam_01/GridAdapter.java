package com.example.roger.pam_01;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private Activity activity;
    private int w, h;
    boolean done = false;
    public GridAdapter(Context c) {
        mContext = c;
        activity = (Activity)c;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        h = metrics.heightPixels;
        w = metrics.widthPixels;
        h = (h - getStatusBarHeight() - (int)(MainScreen.actionBarLength * 1.05)) / 4;
        w /= 2;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)(mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            v = inflater.inflate(R.layout.image_holder, null);
            imageView = (ImageView)(v.findViewById(R.id.photo));
            //imageView.setPadding(20, 20, 20, 20);
            imageView.setLayoutParams(new GridView.LayoutParams(w, h));

        } else {
            imageView = (ImageView) convertView;
        }
        Glide.with(mContext).load(mThumbIds[position]).override(320, 240).centerCrop().into(imageView);

        return imageView;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private Integer[] mThumbIds = {
            R.drawable.img_001,
            R.drawable.img_002,
            R.drawable.img_003,
            R.drawable.img_004,
            R.drawable.img_005,
            R.drawable.img_006,
            R.drawable.img_007,
            R.drawable.img_008,
            R.drawable.img_009,
            R.drawable.img_010,
            R.drawable.img_011,
            R.drawable.img_012,
            R.drawable.img_013,
            R.drawable.img_014,
            R.drawable.img_015,
            R.drawable.img_016,
    };
}