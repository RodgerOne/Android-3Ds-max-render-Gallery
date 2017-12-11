package com.example.roger.pam_01;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toolbar;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    final Activity activity = this;
    static int actionBarLength = 200;
    public static Integer[] mThumbIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mThumbIds = getIdOfDrawings();
        TextView text = (TextView) findViewById(R.id.main_title);
        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, VideoActivity.class);
                startActivity(i);
            }
        });
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/a_m.ttf");
        text.setTypeface(tf);
        text.setHeight(actionBarLength);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);
        getActionBar().setDisplayShowTitleEnabled(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setStatusBarColor(Color.DKGRAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setSmoothScrollbarEnabled(true);
        gridView.setAdapter(new GridAdapter(this));
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    View itemView = view.getChildAt(0);
                    int top = Math.abs(itemView.getTop());
                    int bottom = Math.abs(itemView.getBottom());
                    int scrollBy = top >= bottom ? bottom : -top;
                    if (scrollBy == 0) return;
                    smoothScrollDeferred(scrollBy, (GridView)view);
                }
            }
            private void smoothScrollDeferred(final int scrollByF, final GridView viewF) {
                final Handler h = new Handler();
                h.post(new Runnable() {
                    @Override
                    public void run() { viewF.smoothScrollBy(scrollByF, 300);}
                });
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(activity, Gallery.class);
                i.putExtra("pos", position);
                startActivity(i);
            }
        });
    }
    public Integer[] getIdOfDrawings(){
        final Field[] fields =  R.drawable.class.getDeclaredFields();
        final R.drawable drawableResources = new R.drawable();
        List<Integer> lipsList = new ArrayList<>();
        int resId;
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getName().contains("img_")){
                    resId = fields[i].getInt(drawableResources);
                    lipsList.add(resId);
                }
            }catch(Exception e){continue;}
        }
        return lipsList.toArray(new Integer[lipsList.size()]);
    }
}
