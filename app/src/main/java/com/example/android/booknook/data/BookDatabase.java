package com.example.android.booknook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 12/21/2016.
 */

public class BookDatabase extends SQLiteOpenHelper {
    private static final String TAG = BookDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "book.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    interface Tables{
        String BOOK = "book";
    }

    public BookDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.BOOK + " ("
                + BaseColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookColumns.BOOK_AUTHOR +" TEXT NOT NULL, "
                + BookContract.BookColumns.BOOK_TITLE +" TEXT NOT NULL, "
                + BookContract.BookColumns.BOOK_ISBN_13 +" TEXT NOT NULL, "
                + BookContract.BookColumns.BOOK_DESCRIPTION +" TEXT NOT NULL, "
                + BookContract.BookColumns.IMAGE_LINK +" TEXT NOT NULL, "
                + BookContract.BookColumns.BOOK_RANK +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if(version == 1){
            //Code to update database from version 1 to 2
            version = 2;
        }
        if(version != DATABASE_VERSION){
            db.execSQL("DROP TABLE IF EXISTS "+ Tables.BOOK);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
