package com.example.android.booknook.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.booknook.R;
import com.example.android.booknook.utils.MyBook;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.android.booknook.R.drawable.no_image;
import static com.example.android.booknook.R.drawable.placeholder;

/**
 * Created by Administrator on 1/5/2017.
 */

public class NYTListRecyclerViewAdapter extends RecyclerView.Adapter<BookImageViewHolder> {
    private List<MyBook> BookList;
    private Context mContext;
    private RecyclerOnItemClickListener recyclerOnItemClickListener;

    public NYTListRecyclerViewAdapter(Context context, List<MyBook> booksList) {
        this.BookList = booksList;
        this.mContext = context;
    }



    public NYTListRecyclerViewAdapter(Context context, List<MyBook> booksList, RecyclerOnItemClickListener recyclerOnItemClickListener) {
        this.BookList = booksList;
        this.mContext = context;
        this.recyclerOnItemClickListener = recyclerOnItemClickListener;
    }

    public List<MyBook> getMovieList() {
        return BookList;
    }

    @Override
    public BookImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.v("onCreateViewHolder", "called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list, null);
        final BookImageViewHolder bookImageViewHolder = new BookImageViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerOnItemClickListener.onItemClick(v, bookImageViewHolder.getPosition());
            }
        });
        return bookImageViewHolder;
    }

    @Override
    public int getItemCount() {
        int itemCount = BookList.size();
        //Log.v("getItemCount", Integer.toString(itemCount));
        return BookList.size();
    }

    @Override
    public void onBindViewHolder(BookImageViewHolder bookImageViewHolder, int position) {
        //Log.v("onBindViewHolder", "called");
        MyBook bookObject = BookList.get(position);
        if(bookObject.getImage_link() == null){
            bookImageViewHolder.poster.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(mContext).load(bookObject.getImage_link())
                    .error(no_image)
                    .placeholder(placeholder)
                    .into(bookImageViewHolder.poster);

        }
    }

    public void newDataLoad(List<MyBook> newMovieData){
        this.BookList = newMovieData;
        notifyDataSetChanged();
    }
}
