package com.example.roger.pam_01;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
    private Context mContext;
    private Activity activity;

    public RecAdapter(Context c) {
        mContext = c;
        activity = (Activity)c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(mThumbIds[position]).override(320, 240).centerCrop().into(holder.im);
    }

    @Override
    public int getItemCount() {
        return mThumbIds.length;
    }

    private Integer[] mThumbIds = {
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView im;

        ViewHolder(View itemView) {
            super(itemView);
            im = (ImageView) itemView.findViewById(R.id.photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }
}