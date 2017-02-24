package com.example.android.booknook.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.android.booknook.R;

/**
 * Created by Administrator on 1/5/2017.
 */




public class BookImageViewHolder  extends RecyclerView.ViewHolder{
    protected ImageView poster;

    public BookImageViewHolder(View view) {
        super(view);
        this.poster = (ImageView) view.findViewById(R.id.poster);
    }


}
