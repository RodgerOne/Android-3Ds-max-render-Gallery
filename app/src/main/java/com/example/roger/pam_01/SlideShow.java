package com.example.roger.pam_01;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.example.roger.pam_01.OnTouchAndMoveListener._coef;

public class SlideShow extends AppCompatActivity {

    int w, h;
    final int _framerate = 24;
    final Integer[] frames = getIdOfDrawings();
    final Activity _a = this;

    private int pos = 8;
    private int q = 1000 / _framerate;

    Handler handler = new Handler();
    public static boolean go = false;
    ArrayList<Drawable> buffer = new ArrayList<>();
    ImageView img;
    Runnable r = new Runnable(){
        @Override
        public void run() {
            if(go){
                synchronized (OnTouchAndMoveListener.lock){
                    if (_coef > 0) pos++; else pos --;
                    pos = pos >= frames.length? 0 : pos <= 0? frames.length-1 : pos;
                    //Glide.with(_a).load(frames[pos]).transition(GenericTransitionOptions.<Drawable>withNoTransition()).into(img);
                    img.setImageResource(frames[pos]);
                    start();
                }
            }
        }
    };
    public void start(){
        go = true;
        synchronized (OnTouchAndMoveListener.lock){
            _coef = _coef == 0? 0.001: _coef;
            handler.postDelayed(r, (int)((double)q  / Math.abs(_coef)));
        }
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
            buffer.add(getDrawable(frames[i]));
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        h = metrics.heightPixels;
        w = metrics.widthPixels;
        h = (h - getStatusBarHeight());
        findViewById(R.id.activity_slide_show).setOnTouchListener(new OnTouchAndMoveListener(this, 2.5));
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
        final Field[] fields =  R.drawable.class.getDeclaredFields();
        final R.drawable drawableResources = new R.drawable();
        List<Integer> lipsList = new ArrayList<>();
        int resId;
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getName().contains("frame")){
                    resId = fields[i].getInt(drawableResources);
                    lipsList.add(resId);
                }
            } catch (Exception e) {
                continue;
            }
        }
        return lipsList.toArray(new Integer[lipsList.size()]);
    }

}
