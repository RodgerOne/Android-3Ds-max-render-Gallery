package com.example.roger.pam_01;

import android.app.Activity;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Roger on 06.12.2017.
 */
public class OnTouchAndMoveListener implements View.OnTouchListener {
    final TextView x, y, mx, my;
    Activity _a;
    public OnTouchAndMoveListener(Activity activity){
        _a = activity;
        x = (TextView)_a.findViewById(R.id.x_pointer);
        y = (TextView)_a.findViewById(R.id.y_pointer);
        mx = (TextView)_a.findViewById(R.id.mx_pointer);
        my = (TextView)_a.findViewById(R.id.my_pointer);
    }
    @Override
    public boolean onTouch(View v, final MotionEvent event) {
        float fx = event.getX();
        float fy = event.getY();
        boolean isClicked;
        x.setText(Float.toString(fx));
        y.setText(Float.toString(fy));
/*
        Handler h = new Handler();
        h.post(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try{wait(200);}catch(Exception e){}
                    mx.setText(Float.toString(event.getX()));
                    my.setText(Float.toString(event.getY()));
                }
            }
        });
*/
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Write your code to perform an action on down
                break;
            case MotionEvent.ACTION_MOVE:
                // Write your code to perform an action on contineus touch move
                //post(mx.setText(Float.toString(event.getX())));


                break;
            case MotionEvent.ACTION_UP:
                // Write your code to perform an action on touch up
                break;
        }
        return true;
    }

}
