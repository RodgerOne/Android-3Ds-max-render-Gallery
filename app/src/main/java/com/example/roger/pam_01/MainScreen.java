package com.example.roger.pam_01;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainScreen extends AppCompatActivity {

    static int actionBarLength = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);;
        TextView text = (TextView) findViewById(R.id.main_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/a_m.ttf");
        text.setTypeface(tf);
        text.setHeight(actionBarLength);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);
        getActionBar().setDisplayShowTitleEnabled(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                    public void run() { viewF.smoothScrollBy(scrollByF, 500);}
                });
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });
    }
}
