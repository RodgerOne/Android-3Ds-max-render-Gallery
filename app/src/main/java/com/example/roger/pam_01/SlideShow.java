package com.example.roger.pam_01;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean showInfo = false;

    private Thread th1, th2, th3;

    TextView tv;
    ImageView img;
    SeekBar sb;

    ImageButton elemOne, elemTwo;
    ConstraintLayout.LayoutParams lp1, lp2;
    //tmp
    TextView pos_pointer, _position_pointer;

    //wątek główny aktualizujący obrazki w widoku
    Runnable r = new Runnable(){
        @Override
        public void run() {
            if(go){
                double tmp = MoveListener.getPosition();
                if(lock_direction.tryLock()){ _step = tmp>0? 1 : tmp<0? -1 : 0; lock_direction.unlock();}
                if(buffer.peek() != null && _step != 0.0) {
                    img.setImageBitmap(buffer.poll());
                    pos = (pos + _step) % border;
                    if (pos < 0) pos = border + pos;
                }
                //INTERAKCJE
                if(pos > 50 && pos < 80){
                    elemOne.setVisibility(View.VISIBLE);
                    elemOne.setImageAlpha(pos < 55? (pos-50)*50 : pos>75? (80-pos)*50: 255);
                    lp1.horizontalBias = 0.27f - (pos<65? ((float)(pos - 50)/800) : ((float)(pos - 50)/500) - 0.009f);
                    lp1.verticalBias = 0.6f + (pos<65? ((float)(pos - 50)/300) : ((float)(pos - 50)/180 - 0.033f));
                    lp1.width = 150 + (int)((pos - 50));
                    lp1.height = 150 + (int)((pos - 50));
                    elemOne.setLayoutParams(lp1);
                }else{
                    elemOne.setVisibility(View.INVISIBLE); //pierwszy elem
                    lp1.horizontalBias = 0.27f;
                    lp1.verticalBias = 0.6f;
                    if(showInfo)hideDisplays();
                }

                if(pos > 170 && pos < 200){
                    elemTwo.setVisibility(View.VISIBLE);
                    elemTwo.setImageAlpha(pos < 175? (pos-170)*50 : pos>195? (200-pos)*50: 255);
                    lp2.horizontalBias = 0.36f + ( (float)(pos - 170)/1000);// - (pos<65? ((float)(pos - 170)/800) : ((float)(pos - 170)/500) - 0.009f);
                    lp2.verticalBias = 0.2f + ((float)(pos - 170)/65);//(pos<65? ((float)(pos - 170)/300) : ((float)(pos - 170)/180 - 0.033f));
                    lp2.width = 180 + (int)((pos - 170)*1.4);
                    lp2.height = 180 + (int)((pos - 170)*1.4);
                    elemTwo.setLayoutParams(lp2);
                }else{
                    elemTwo.setVisibility(View.INVISIBLE); //pierwszy elem
                    lp2.horizontalBias = 0.36f;
                    lp2.verticalBias = 0.2f;//drugi elem
                    if(showInfo)hideDisplays();
                }
                //KONIEC INTERAKCJI
                tv.setText(String.valueOf(buffer.size()));
                pos_pointer.setText(String.valueOf(pos));
                _position_pointer.setText(String.valueOf(_position));
                start();
            }
        }
    };


    //3 wątki, kazdy kolejny wlacza sie gdy drugi nie daje rady
    ReentrantLock lock_position = new ReentrantLock(), lock_direction = new ReentrantLock(), lock_test = new ReentrantLock();
    public void start(){
        go = true;
        double tmp = MoveListener.getPosition();
        if(tmp != 0)pauseThreads = false;
        tmp = tmp == 0.0? 1.0 : Math.abs(tmp);
        handler.postDelayed(r, (int)((double)q  / tmp));
    }

    public void stop(){
        go = false;
        pauseThreads = true;
        //buffer.clear();
        if(lock_position.tryLock()) {_position = pos; buffer.clear(); lock_position.unlock();}// - 3*_step;
    }

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
        pos = _position =  getIntent().getExtras().getInt("current") * frames.length / getIntent().getExtras().getInt("duration");
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv = (TextView)findViewById(R.id.buffer_size_pointer);
        img = (ImageView)findViewById(R.id.img);
        sb = (SeekBar)findViewById(R.id.seekBar);
        elemOne = (ImageButton)findViewById(R.id.elem_one);
        elemTwo = (ImageButton)findViewById(R.id.elem_two);
        lp1 = (ConstraintLayout.LayoutParams) elemOne.getLayoutParams();//new ConstraintLayout.LayoutParams(100,100);
        lp2 = (ConstraintLayout.LayoutParams) elemTwo.getLayoutParams();//new ConstraintLayout.LayoutParams(100,100);
        //tmp
        pos_pointer = (TextView)findViewById(R.id.pos);
        _position_pointer = (TextView)findViewById(R.id._position);

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
        createThreads();
        th1.start();
        th2.start();
        th3.start();
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        runThreads = false;
        buffer.clear();
    }

    private void showDisplays(String s){
        ImageView im = (ImageView)findViewById(R.id.imageView2);
        //im.setVisibility(View.VISIBLE);
        im.animate().alpha(1.0f);
        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setText(s);
        //tv.setVisibility(View.VISIBLE);
        tv.animate().alpha(1.0f);
        showInfo = true;
    }
    private void hideDisplays(){
        ImageView im = (ImageView)findViewById(R.id.imageView2);
        im.animate().alpha(0.0f);
        //im.setVisibility(View.INVISIBLE);
        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setText("");
        tv.animate().alpha(0.0f);
        //tv.setVisibility(View.INVISIBLE);
        showInfo = false;
    }
    public void elemOneClicked(View view){
        String s = "\tUmieszczona butelka ciemnego wina wraz" +
                " z napełnionymi nim lampkami wprowadza" +
                " w scenerię odpowiedni nastrój, stwarzając" +
                " pomieszczenie bardziej interesujacym dla odbiorcy.";
        if(!showInfo){showDisplays(s);}
        else{hideDisplays();}
    }
    public void elemTwoClicked(View view){
        String s = "\tBłyszczący, niemalże idealnie okrągły" +
                " wazon wypełniony pełnymi dysharmonii gałązkami," +
                " których cień podkreśla jakość wykonanego renderu.";
        if(!showInfo){showDisplays(s);}
        else{hideDisplays();}
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("ms", pos * getIntent().getExtras().getInt("duration")/ frames.length);
        setResult(0, i);
        finish();
        super.onBackPressed();
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

    private void createThreads() {
        th1 = new LoadingThread(16, 13);
        th2 = new LoadingThread(10, 12);
        th3 = new LoadingThread(5, 11);
    }

    class LoadingThread extends Thread{
        private int _timeQuant, _sizeLimit;
        public LoadingThread(int sizeLimit, int timeQuant){
            _timeQuant = timeQuant;
            _sizeLimit = sizeLimit;
        }
        @Override
        public void run() {
            while(runThreads) {
               while(pauseThreads)try{sleep(100);}catch (Exception e){continue;}
                if (buffer.size() < _sizeLimit)
                    try{
                        if(lock_position.tryLock() && lock_direction.tryLock()){
                            _position = (_position + _step) % border;
                            if(_position < 0) _position = border + _position;
                            buffer.put(decodeSampledBitmapFromResource(frames[_position]));
                            lock_position.unlock();
                            lock_direction.unlock();
                        }
                    }catch (Exception e){continue;}
                else try {sleep(_timeQuant);} catch (Exception e) {continue;}

            }
        }
    }
}
/*
th1 = new Thread(){
            @Override
            public void run() {
                while(runThreads) {
                    while(pauseThreads)try{sleep(100);}catch (Exception e){continue;}
                    if (buffer.size() < 16)
                        try{
                            if(lock_position.tryLock()){
                                _position = (_position + _step) % border;
                                if(_position < 0) _position = border + _position;
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
                                if(_position < 0) _position = border + _position;
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
                                if(_position < 0) _position = border + _position;
                                buffer.put(decodeSampledBitmapFromResource(frames[_position]));
                                lock_position.unlock();
                            }
                        }catch (Exception e){continue;}
                    else try {sleep(11);} catch (Exception e) {continue;}

                }
            }
        };
         */