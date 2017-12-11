package com.example.roger.pam_01;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MoveListener implements View.OnTouchListener {
    private double _maxMultiplier, start_x, tmp_coef, min = 0.7;
    private static double _coef = 1.0;
    Activity _a;

    private DisplayMetrics metrics;
    private int w;

    public static double getPosition(){return _coef;}

    public MoveListener(Activity activity, double maxMultiplier){
        _a = activity;
        _maxMultiplier = maxMultiplier;

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
                //tmp_coef = (tmp_coef > 0 && tmp_coef < min)? min : (tmp_coef < 0 && tmp_coef > -min)?  -min : tmp_coef;
                tmp_coef = (tmp_coef < min && tmp_coef > -min)? 0 : tmp_coef;
                if(tmp_coef > 0 )tmp_coef *=tmp_coef; else tmp_coef *=-tmp_coef;

                _coef = ((double)((int)(tmp_coef * 100)))/100;
                if(!SlideShow.go) ((SlideShow)_a).start();
                break;
            case MotionEvent.ACTION_UP:
                _coef = _coef > 0? min : -min;
                ((SlideShow)_a).stop();
                break;
        }
        return true;
    }

}
