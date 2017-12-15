package com.example.roger.pam_01;

import android.animation.ValueAnimator;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    int h, w, ms = 0;
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
    mVideoView.seekTo(ms);
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
        findViewById(R.id.play).setBackground(getDrawable(R.drawable.border_highlighted));
        findViewById(R.id.pause).setBackground(getDrawable(R.drawable.border_normal));
        sVideoView.start();
    }

    public void pause(View view){
        findViewById(R.id.pause).setBackground(getDrawable(R.drawable.border_highlighted));
        findViewById(R.id.play).setBackground(getDrawable(R.drawable.border_normal));
        sVideoView.pause();
    }

    public void interakcja(View view){
        findViewById(R.id.interakcja).setBackground(getDrawable(R.drawable.border_highlighted));
        findViewById(R.id.pause).setBackground(getDrawable(R.drawable.border_normal));
        findViewById(R.id.play).setBackground(getDrawable(R.drawable.border_normal));
        Intent i = new Intent(this, SlideShow.class);
        i.putExtra("duration", sVideoView.getDuration());
        i.putExtra("current", sVideoView.getCurrentPosition());
        startActivityForResult(i,0);
        findViewById(R.id.interakcja).setBackground(getDrawable(R.drawable.border_normal));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sVideoView.seekTo(data.getExtras().getInt("ms"));
        play(null);
    }
}
