package com.example.roger.pam_01;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ViewFlipper;

public class Gallery extends AppCompatActivity {
    private ViewFlipper vf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery);
        final ViewPager vp = (ViewPager) findViewById(R.id.vp);
        ImageAdapter im = new ImageAdapter(this);
        vp.setAdapter(im);
        vp.setCurrentItem(getIntent().getExtras().getInt("pos")+1);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) vp.setCurrentItem(MainScreen.mThumbIds.length, false);
                else if(position == MainScreen.mThumbIds.length+1) vp.setCurrentItem(1,false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //vp.setChangePositionFactor(20);
    }
}
