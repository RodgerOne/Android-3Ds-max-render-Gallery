package com.example.roger.pam_01;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MoveListener implements View.OnTouchListener {
    private double _maxMultiplier, start_x, tmp_coef, min = 0.5, max = 2.5;
    private static double _coef = 1.0;
    Activity _a;

    private DisplayMetrics metrics;
    private int w;
    TextView tv;

    public static double getPosition(){return _coef;}

    public MoveListener(Activity activity, double maxMultiplier){
        _a = activity;
        _maxMultiplier = maxMultiplier;

        tv = (TextView)_a.findViewById(R.id.move_pointer);
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
                tmp_coef = tmp_coef > max? max : tmp_coef < -max? -max : tmp_coef;
                _coef = ((double)((int)(tmp_coef * 100)))/100;
                tv.setText(String.valueOf(_coef));
                if(!SlideShow.go) ((SlideShow)_a).start();
                break;
            case MotionEvent.ACTION_UP:
                //_coef = _coef > 0? min : -min;
                _coef = 0.0;
                tv.setText(String.valueOf(_coef));
                ((SlideShow)_a).stop();
                break;
        }
        return true;
    }

}
