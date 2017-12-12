package com.example.roger.pam_01;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MoveListener implements View.OnTouchListener {
    private double _maxMultiplier, start_x, tmp_coef, min = 0.5, max = 2.5;
    private static double _coef = 0.0;
    private Activity _a;

    private DisplayMetrics metrics;
    private int w;
    private TextView tv;
    private SeekBar sb;
    private final MoveListener th = this;


    public static double getPosition(){return _coef;}

    ValueAnimator anim;

    public MoveListener(Activity activity, double maxMultiplier){
        _a = activity;
        _maxMultiplier = maxMultiplier;
        tv = (TextView)_a.findViewById(R.id.move_pointer);
        sb = (SeekBar)_a.findViewById(R.id.seekBar);
        sb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                th.onTouch(view, motionEvent);
                return true;
            }
        });
        metrics = new DisplayMetrics();
        _a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        w = metrics.widthPixels;
    }
    @Override
    public boolean onTouch(View v, final MotionEvent event) {
        float fx = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start_x = fx;
                ((SlideShow)_a).start();
                break;
            case MotionEvent.ACTION_MOVE:
                tmp_coef = (fx - start_x) / (w/2) * _maxMultiplier;
                tmp_coef = (tmp_coef < min && tmp_coef > -min)? 0 : tmp_coef;
                if(tmp_coef > 0 )tmp_coef *=tmp_coef; else tmp_coef *=-tmp_coef;
                tmp_coef = tmp_coef > max? max : tmp_coef < -max? -max : tmp_coef;
                //_coef = ((double)((int)(tmp_coef * 100)))/100;
                tmp_coef = ((double)((int)(tmp_coef * 100)))/100;

                //sb.setProgress((int)((_coef + max)*20));  // to byla zanim oddalem animacje
                anim = ValueAnimator.ofInt((int)((_coef + max)*20), (int)((tmp_coef+max)*20));
                anim.setDuration(250);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animProgress = (Integer) animation.getAnimatedValue();
                        sb.setProgress(animProgress);
                    }
                });
                anim.start();
                _coef = tmp_coef;

                tv.setText(String.valueOf(_coef));
                if(!SlideShow.go) ((SlideShow)_a).start();
                break;
            case MotionEvent.ACTION_UP:
                anim = ValueAnimator.ofInt((int)((_coef + max)*20), (int)(max*20));
                anim.setDuration(300);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int animProgress = (Integer) animation.getAnimatedValue();
                        sb.setProgress(animProgress);
                    }
                });
                anim.start();

                _coef = 0.0;
                //sb.setProgress((int)((_coef + max)*20));
                tv.setText(String.valueOf(_coef));
                ((SlideShow)_a).stop();
                break;
        }
        return true;
    }

}
