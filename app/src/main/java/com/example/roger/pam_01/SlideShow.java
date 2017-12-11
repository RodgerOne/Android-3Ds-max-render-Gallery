package com.example.roger.pam_01;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class SlideShow extends Activity {

    int w, h;
    final int _framerate = 24;
    final Integer[] frames = getIdOfDrawings();
    final Activity _a = this;

    private int pos = 10;
    private int q = 1000 / _framerate;

    Handler handler = new Handler();
    public static boolean go = false;
    ArrayList<Bitmap> buffer = new ArrayList<>();
    ImageView img;
    Runnable r = new Runnable(){
        @Override
        public void run() {
            if(go){
                double tmp = MoveListener.getPosition();
                if(tmp != 0.0 ){   if (tmp > 0) pos++; else pos --;}
                pos = pos >= frames.length? 0 : pos <= 0? frames.length-1 : pos;
                //Glide.with(_a).load(frames[pos]).into(img);
                //img.setImageResource(frames[pos]);
                img.setImageBitmap(buffer.remove(0));
                //rv.scrollToPosition(pos);
                start();
                buffer.add((decodeSampledBitmapFromResource(frames[pos])));
            }
        }
    };
    public void start(){
        go = true;
        double tmp = Math.abs(MoveListener.getPosition());
        tmp = tmp == 0.0? 1.0 : tmp;
        handler.postDelayed(r, (int)((double)q  / tmp));
    }
    public void stop(){ go = false; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setStatusBarColor(Color.DKGRAY);

        img = (ImageView)findViewById(R.id.img);

        for(int i=0; i<pos; i++){
            //buffer.add(getDrawable(frames[i]));
            buffer.add(decodeSampledBitmapFromResource(frames[pos]));
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        h = metrics.heightPixels - getStatusBarHeight();
        w = metrics.widthPixels;
        findViewById(R.id.activity_slide_show).setOnTouchListener(new MoveListener(this, 1.5));
        /*
        rv = (RecyclerView)findViewById(R.id.slider);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_holder, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Glide.with(_a).load(frames[position]).into(((ViewHolder)holder).img);
                //((ViewHolder)holder).img.setImageResource(frames[position]);
            }

            @Override
            public int getItemCount() {
                return frames.length;
            }
            class ViewHolder extends RecyclerView.ViewHolder{
                private ImageView img;
                public ViewHolder(View view) {
                    super(view);
                    img = (ImageView) view.findViewById(R.id.photo);
                }
            }
        });*/
    }



    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



    public Integer[] getIdOfDrawings(){
        final Field[] fields =  R.raw.class.getDeclaredFields();
        final R.raw rawResources = new R.raw();
        List<Integer> lipsList = new ArrayList<>();
        int resId;
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getName().contains("frame")){
                    resId = fields[i].getInt(rawResources);
                    lipsList.add(resId);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return lipsList.toArray(new Integer[lipsList.size()]);
    }

    public  Bitmap decodeSampledBitmapFromResource(int resId) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(resId, options);

        // Calculate inSampleSize
        //options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeResource(_a.getResources(), resId, options);
    }
}
