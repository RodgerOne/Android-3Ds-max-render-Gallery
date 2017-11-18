package com.example.roger.pam_01;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
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

        RecyclerView recView = (RecyclerView)findViewById(R.id.gridView);
        GridLayoutManager gr = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        gr.setSpanCount(4);
        recView.setLayoutManager(gr);
        recView.setAdapter(new RecAdapter(this));
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recView);
    }
}
