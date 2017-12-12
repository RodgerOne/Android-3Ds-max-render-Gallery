package com.example.roger.pam_01;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


public class SlideShow extends Activity {

    final Activity _a = this;
    Handler handler = new Handler();
    LinkedBlockingQueue<Bitmap> buffer = new LinkedBlockingQueue<>();
    final Integer[] frames = getIdOfDrawings();

    final int _framerate = 24;
    private static volatile int _step = 1;
    private volatile int _position = 0;
    private int pos = 0;
    private final int q = 1000 / _framerate;
    private int border = frames.length;
    private int w, h;

    public static boolean go = false;
    private volatile boolean runThreads = false;
    private volatile boolean pauseThreads = false;

    private Thread th1, th2, th3;

    TextView tv;
    ImageView img;
    SeekBar sb;

    Runnable r = new Runnable(){
        @Override
        public void run() {
            if(go){
                double tmp = MoveListener.getPosition();
                if(buffer.peek() != null && tmp!= 0.0)img.setImageBitmap(buffer.poll());
                tv.setText(String.valueOf(buffer.size()));
                start();
            }
        }
    };

    //3 wątki, kazdy kolejny wlacza sie gdy drugi nie daje rady
    ReentrantLock lock_position = new ReentrantLock(), lock_direction = new ReentrantLock();
    public void start(){
        go = true;
        pauseThreads = false;
        double tmp = MoveListener.getPosition();

        if(lock_direction.tryLock()){ _step = tmp>0? 1 : tmp<0? -1 : 0; lock_direction.unlock();}

        tmp = Math.abs(tmp);
        tmp = tmp == 0.0? 1.0 : tmp;

        handler.postDelayed(r, (int)((double)q  / tmp));
    }
    public void stop(){ go = false; /*buffer.clear();*/}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setStatusBarColor(Color.DKGRAY);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        h = metrics.heightPixels - getStatusBarHeight();
        w = metrics.widthPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv = (TextView)findViewById(R.id.buffer_size_pointer);
        img = (ImageView)findViewById(R.id.img);
        sb = (SeekBar)findViewById(R.id.seekBar);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int)(w * 0.75 / 1.28), (int)(h*0.75));
        params.leftToLeft = R.id.activity_slide_show;
        params.topToTop = R.id.activity_slide_show;
        params.rightToRight = R.id.activity_slide_show;
        params.bottomToBottom = R.id.activity_slide_show;
        params.horizontalBias = 0.1f;
        params.verticalBias = 0.2f;
        img.setLayoutParams(params);
        img.setImageResource(frames[_position]);

        findViewById(R.id.activity_slide_show).setOnTouchListener(new MoveListener(this, 2.25));

        runThreads = true;
        th1 = new Thread(){
            @Override
            public void run() {
                while(runThreads) {
                    while(pauseThreads)try{sleep(100);}catch (Exception e){continue;}
                    if (buffer.size() < 16)
                        try{
                            if(lock_position.tryLock()){
                                _position = (_position + _step) % border;
                                if(_position < 0) _position = frames.length + _position;
                                buffer.put(decodeSampledBitmapFromResource(frames[_position]));
                                lock_position.unlock();
                            }
                        }catch (Exception e){continue;}
                    else try {sleep(13);} catch (Exception e) {continue;}

                }
            }
        };
        th2 = new Thread(){
            @Override
            public void run() {
                while(runThreads) {
                    while(pauseThreads)try{sleep(100);}catch (Exception e){continue;}
                    if (buffer.size() < 10)
                        try{
                            if(lock_position.tryLock()){
                                _position = (_position + _step) % border;
                                if(_position < 0) _position = frames.length + _position;
                                buffer.put(decodeSampledBitmapFromResource(frames[_position]));
                                lock_position.unlock();
                            }
                        }catch (Exception e){continue;}
                    else try {sleep(12);} catch (Exception e) {continue;}

                }
            }
        };
        th3 = new Thread(){
            @Override
            public void run() {
                while(runThreads) {
                    while(pauseThreads)try{sleep(100);}catch (Exception e){continue;}
                    if (buffer.size() < 5)
                        try{
                            if(lock_position.tryLock()){
                                _position = (_position + _step) % border;
                                if(_position < 0) _position = frames.length + _position;
                                buffer.put(decodeSampledBitmapFromResource(frames[_position]));
                                lock_position.unlock();
                            }
                        }catch (Exception e){continue;}
                    else try {sleep(11);} catch (Exception e) {continue;}

                }
            }
        };
        th1.start();
        th2.start();
        th3.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        buffer.clear();
        runThreads = false;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public Integer[] getIdOfDrawings(){
        final Field[] fields =  R.raw.class.getDeclaredFields();
        final R.raw rawResources = new R.raw();
        List<Integer> lipsList = new ArrayList<>();
        int resId;
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getName().contains("frame")){
                    resId = fields[i].getInt(rawResources);
                    lipsList.add(resId);
                }
            }catch(Exception e){continue;}
        }
        return lipsList.toArray(new Integer[lipsList.size()]);
    }

    private  Bitmap decodeSampledBitmapFromResource(int resId){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        return BitmapFactory.decodeResource(_a.getResources(), resId, options);
    }
}
