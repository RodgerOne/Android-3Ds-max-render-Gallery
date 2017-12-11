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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class SlideShow extends Activity {

    int w, h;
    final int _framerate = 24;
    final Integer[] frames = getIdOfDrawings();
    final Activity _a = this;

    private int pos = 8;
    private int q = 1000 / _framerate;

    Handler handler = new Handler();
    public static boolean go = false;
    ArrayList<Bitmap> buffer = new ArrayList<>();
    ImageView img;
    Runnable r = new Runnable(){
        @Override
        public void run() {
            if(go){
                double tmp = MoveListener.getPosition();
                if(tmp != 0.0 ){   if (tmp > 0) pos++; else pos --;}
                pos = pos >= frames.length? 0 : pos <= 0? frames.length-1 : pos;
                img.setImageBitmap(buffer.remove(0));
                //img.setImageBitmap((decodeSampledBitmapFromResource(frames[pos])));
                start();
                buffer.add((decodeSampledBitmapFromResource(frames[pos])));
            }
        }
    };

    public void start(){
        go = true;
        double tmp = Math.abs(MoveListener.getPosition());
        tmp = tmp == 0.0? 1.0 : tmp;
        handler.postDelayed(r, (int)((double)q  / tmp));
    }
    public void stop(){ go = false; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setStatusBarColor(Color.DKGRAY);

        for(int i=0; i<pos; i++){
            buffer.add(decodeSampledBitmapFromResource(frames[pos]));
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        h = metrics.heightPixels - getStatusBarHeight();
        w = metrics.widthPixels;

        img = (ImageView)findViewById(R.id.img);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int)(w * 0.75), (int)(h*0.75));
        params.leftToLeft = R.id.activity_slide_show;
        params.topToTop = R.id.activity_slide_show;
        params.rightToRight = R.id.activity_slide_show;
        params.bottomToBottom = R.id.activity_slide_show;
        params.horizontalBias = 0.2f;
        params.verticalBias = 0.2f;
        img.setLayoutParams(params);
        findViewById(R.id.activity_slide_show).setOnTouchListener(new MoveListener(this, 1.5));
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
