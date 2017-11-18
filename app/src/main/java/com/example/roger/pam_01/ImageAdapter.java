package com.example.roger.pam_01;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import org.malcdevelop.cyclicview.CyclicAdapter;

public class ImageAdapter extends CyclicAdapter {
    private Context context;
    int pos = 0;

    ImageAdapter(final Context context, int pos) {
        this.context = context;
        this.pos = pos;
    }

    @Override
    public int getItemsCount() {
        return MainScreen.mThumbIds.length;
    }

    @Override
    public View createView(int i) {
        int actualPos = (i + pos) % MainScreen.mThumbIds.length;
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(MainScreen.mThumbIds[actualPos]).fitCenter().into(imageView);
        return imageView;
    }

    @Override
    public void removeView(int i, View view) {}
}

