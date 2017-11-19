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
import com.bumptech.glide.request.RequestOptions;

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
        h = (h - getStatusBarHeight() - (int)(MainScreen.actionBarLength)) / 4;
        w /= 2;
    }

    public int getCount() {
        return MainScreen.mThumbIds.length;
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
            imageView.setLayoutParams(new GridView.LayoutParams(w, h));
        } else {
            imageView = (ImageView) convertView;
        }
        Glide.with(mContext).load(MainScreen.mThumbIds[position]).apply(RequestOptions.centerCropTransform()).into(imageView);
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


}