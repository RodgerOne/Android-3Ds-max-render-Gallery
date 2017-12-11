package com.example.roger.pam_01;

import android.app.Activity;
import android.icu.text.DecimalFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Roger on 06.12.2017.
 */
public class OnTouchAndMoveListener implements View.OnTouchListener {
    TextView c;
    double _maxMultiplier, start_x;
    static double _coef = 1;
    public final static Object lock = new Object();
    Activity _a;

    DisplayMetrics metrics;
    int w;

    public OnTouchAndMoveListener(Activity activity, double maxMultiplier){
        _a = activity;
        _maxMultiplier = maxMultiplier;
        c = (TextView)_a.findViewById(R.id.c_pointer);

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
                synchronized (lock){_coef = (fx - start_x) / (w/2) * _maxMultiplier;}
                c.setText(Double.toString(_coef));
                if(!SlideShow.go) ((SlideShow)_a).start();
                break;
            case MotionEvent.ACTION_UP:
            synchronized (lock){_coef = _coef > 0? 1.0 : -1.0;}
                c.setText(Double.toString(_coef));
                ((SlideShow)_a).stop();
                break;
        }
        return true;
    }

}
