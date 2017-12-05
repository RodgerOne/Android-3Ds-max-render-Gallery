package com.example.roger.pam_01;

import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    int h, w;
    static VideoView sVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setStatusBarColor(Color.DKGRAY);

        final VideoView mVideoView = sVideoView = (VideoView)findViewById(R.id.videoView);
        final String path = "android.resource://" + getPackageName() + "/" + R.raw.movie;

        mVideoView.setVideoURI(Uri.parse(path));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        h = metrics.heightPixels;
        w = metrics.widthPixels;
        h = (h - getStatusBarHeight());

        ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
        params.height = (int)(h * 0.9);
        params.width = (int)(w * 0.9);
        mVideoView.setLayoutParams(params);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.start();
            }
        });

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public void play(View view){
        sVideoView.start();
    }

    public void pause(View view){
        sVideoView.pause();
    }

    public void interakcja(View view){
        Toast.makeText(this,
        sVideoView.getDuration() + "    "  +
        sVideoView.getCurrentPosition(),
                Toast.LENGTH_LONG).show();
    }
}
