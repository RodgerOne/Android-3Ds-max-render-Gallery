package com.example.roger.pam_01;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    int pos = 0;
    int _innerSize = MainScreen.mThumbIds.length + 2; // first one and last one to make a loop
    int _actualSize = MainScreen.mThumbIds.length;
    ImageAdapter(final Context context) {
        this.context = context;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        if(position == 0){
            Glide.with(context).load(MainScreen.mThumbIds[_actualSize-1]).fitCenter().into(imageView);
        }else if(position == _actualSize + 1) {
            Glide.with(context).load(MainScreen.mThumbIds[1]).fitCenter().into(imageView);
        }else
         {
            Glide.with(context).load(MainScreen.mThumbIds[position-1]).fitCenter().into(imageView);
            container.addView(imageView);
        }
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (ImageView)object;
    }

    @Override
    public int getCount() {
        return _innerSize;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
        //super.destroyItem(container, position, object);
    }
}

