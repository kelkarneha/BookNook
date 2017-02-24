package com.example.android.booknook.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.booknook.utils.MyBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 12/21/2016.
 */

public class BookListLoader extends AsyncTaskLoader<List<MyBook>> {
    private static final String LOG_TAG = BookListLoader.class.getSimpleName();
    private List<MyBook> myReadingList;
    private ContentResolver contentResolver;
    private Cursor cursor;

    public BookListLoader(Context context, Uri uri, ContentResolver contentResolver1){
        super(context);
        contentResolver = contentResolver1;
    }

    @Override
    public List<MyBook> loadInBackground() {
        String[] projection = { BaseColumns._ID,
                BookContract.BookColumns.BOOK_AUTHOR,
                BookContract.BookColumns.BOOK_TITLE,
                BookContract.BookColumns.BOOK_ISBN_13,
                BookContract.BookColumns.BOOK_DESCRIPTION,
                BookContract.BookColumns.IMAGE_LINK,
                BookContract.BookColumns.BOOK_RANK,};

        List<MyBook> myReadingList = new ArrayList<MyBook>();
        cursor = contentResolver.query(BookContract.URI_TABLE, projection, null, null, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    int _id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                    String book_rank = cursor.getString((cursor.getColumnIndex(BookContract.BookColumns.BOOK_RANK)));
                    String book_isbn_13 = cursor.getString((cursor.getColumnIndex(BookContract.BookColumns.BOOK_ISBN_13)));
                    String image_link = cursor.getString((cursor.getColumnIndex(BookContract.BookColumns.IMAGE_LINK)));
                    String description = cursor.getString(cursor.getColumnIndex(BookContract.BookColumns.BOOK_DESCRIPTION));
                    String book_title = cursor.getString((cursor.getColumnIndex(BookContract.BookColumns.BOOK_TITLE)));
                    String book_author = cursor.getString((cursor.getColumnIndex(BookContract.BookColumns.BOOK_AUTHOR)));
                    MyBook book = new MyBook(_id, book_rank, book_title, book_author, image_link, description, book_isbn_13);
                    myReadingList.add(book);
                } while (cursor.moveToNext());
            }
            Log.d(LOG_TAG, "loading finished");
            cursor.close();
        }
        return myReadingList;
    }


    @Override
    public void deliverResult(List<MyBook> favorites) {
        if(isReset()){
            if(favorites != null){
                cursor.close();
            }
        }
        if(favorites == null || favorites.size() == 0){
            Log.d(LOG_TAG, "No data returned");
        }

        myReadingList = favorites;
        super.deliverResult(favorites);
        if(cursor != null){
            cursor.close();
        }


    }

    @Override
    protected void onStartLoading() {
        if(myReadingList != null){
            deliverResult(myReadingList);
        }

        if(takeContentChanged() || myReadingList == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(cursor != null){
            cursor.close();
        }

        myReadingList = null;
    }

    @Override
    public void onCanceled(List<MyBook> data) {
        super.onCanceled(data);
        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}
