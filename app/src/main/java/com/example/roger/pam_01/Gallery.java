package com.example.roger.pam_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import org.malcdevelop.cyclicview.CyclicAdapter;
import org.malcdevelop.cyclicview.CyclicView;

public class Gallery extends AppCompatActivity {
    private ViewFlipper vf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery);
        final CyclicView vp = (CyclicView) findViewById(R.id.vp);
        ImageAdapter im = new ImageAdapter(this, getIntent().getExtras().getInt("pos"));
        vp.setAdapter(im);
        //vp.setChangePositionFactor(20);
    }
}
