package com.example.android.booknook.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.booknook.R;
import com.example.android.booknook.data.BookContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2/12/2017.
 */

public class BookDataProvider implements RemoteViewsService.RemoteViewsFactory{
    List<String> myBookList = new ArrayList<>();
    Context context;
    Intent intent;
    ContentResolver contentResolver;

    public void getTitles(){
        myBookList.clear();
        //ArrayList<String> symbolList = new ArrayList<String>();
        Cursor cursor;
        contentResolver = context.getContentResolver();

        String[] projection = {
                BookContract.BookColumns.BOOK_ID,
                BookContract.BookColumns.BOOK_TITLE};

        cursor = contentResolver.query(BookContract.Book.CONTENT_URI, projection, null, null, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String title = cursor.getString(cursor.getColumnIndex(BookContract.BookColumns.BOOK_TITLE));
                    myBookList.add(title);
                } while (cursor.moveToNext());
            }
        }

        //for (int i=0; i<= symbolList.size(); i++){
        //    Log.d("In Provider", symbolList.get(i));
        //    Log.d("In Provider", priceList.get(i));
        //}

        cursor.close();
    }

    public BookDataProvider(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
    }
    @Override
    public void onCreate() {
        getTitles();
    }

    @Override
    public void onDataSetChanged() {
        getTitles();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return myBookList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteView.setTextViewText(R.id.book_title, myBookList.get(position));
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
