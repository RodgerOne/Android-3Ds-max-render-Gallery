package com.example.roger.pam_01;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;
import com.bumptech.glide.Glide;

public class Gallery extends AppCompatActivity {
    private ViewFlipper vf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.BLACK);
        final Activity a = this;
        final int _pos = a.getIntent().getExtras().getInt("pos");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery);
        final ViewSwitcher vs = (ViewSwitcher)findViewById(R.id.vs);
        final Animation right_out = AnimationUtils.loadAnimation(a, R.anim.swipe_out_right);
        final Animation right_in = AnimationUtils.loadAnimation(a, R.anim.swipe_in_right);
        final Animation left_out = AnimationUtils.loadAnimation(a, R.anim.swipe_out_left);
        final Animation left_in = AnimationUtils.loadAnimation(a, R.anim.swipe_in_left);
        ImageView im1 = new ImageView(this);
        ImageView im2 = new ImageView(this);
        vs.addView(im1);
        vs.addView(im2);
        vs.setOnTouchListener(new OnSwipeTouchListener(this){
            private int _i = _pos;
            private int _size = MainScreen.mThumbIds.length;
            private int getNextPosition(){return (_i = (1 + _i) % _size); }
            private int getPreviousPosition(){return _i > 0? --_i : (_i = _size - 1); }
                @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                    vs.setOutAnimation(left_out);
                    vs.setInAnimation(right_in);
                    Glide.with(a).load(MainScreen.mThumbIds[getNextPosition()]).into((ImageView)vs.getNextView());
                    Toast.makeText(a, "Lewo, _i = " + _i, Toast.LENGTH_SHORT);
                    vs.showNext();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                vs.setInAnimation(left_in);
                vs.setOutAnimation(right_out);
                Glide.with(a).load(MainScreen.mThumbIds[getPreviousPosition()]).into((ImageView)vs.getNextView());
                Toast.makeText(a, "Prawo, _i = " + _i, Toast.LENGTH_SHORT);
                vs.showNext();
            }
        });
        Glide.with(a).load(MainScreen.mThumbIds[_pos]).into((ImageView)vs.getNextView());
        vs.showNext();

    }
}
