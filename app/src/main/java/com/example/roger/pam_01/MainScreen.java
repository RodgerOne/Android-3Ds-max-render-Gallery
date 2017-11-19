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

public class MainScreen extends AppCompatActivity {
    final Activity activity = this;
    static int actionBarLength = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        TextView text = (TextView) findViewById(R.id.main_title);
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
        super.onStart();
        GridView gridView = (GridView)findViewById(R.id.gridView);
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
                    public void run() { viewF.smoothScrollBy(scrollByF, 200);}
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
    public static Integer[] mThumbIds = {
            R.drawable.img_001,
            R.drawable.img_002,
            R.drawable.img_003,
            R.drawable.img_004,
            R.drawable.img_005,
            R.drawable.img_006,
            R.drawable.img_007,
            R.drawable.img_008,
            R.drawable.img_009,
            R.drawable.img_010,
            R.drawable.img_011,
            R.drawable.img_012,
            R.drawable.img_013,
            R.drawable.img_014,
            R.drawable.img_015,
            R.drawable.img_016,
    };
}
