package com.example.android.booknook.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 12/21/2016.
 */

public class BookProvider extends ContentProvider {
    private BookDatabase mOpenHelper;
    private static String TAG = BookProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int BOOK = 100;
    private static final int BOOK_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BookContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "book", BOOK);
        matcher.addURI(authority, "book/*", BOOK_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BookDatabase(getContext());
        return true;
    }

    private void deleteDatabase(){
        mOpenHelper.close();
        BookDatabase.deleteDatabase(getContext());
        mOpenHelper = new BookDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case BOOK:
                return BookContract.Book.CONTENT_TYPE;
            case BOOK_ID:
                return BookContract.Book.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BookDatabase.Tables.BOOK);

        switch(match){
            case BOOK:
                break;
            case BOOK_ID:
                String id = BookContract.Book.getBookID(uri);
                queryBuilder.appendWhere(BaseColumns._ID +"=" + id);
                break;
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + ", values" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match){
            case BOOK:
                long recordId = db.insertOrThrow(BookDatabase.Tables.BOOK, null, values);
                return BookContract.Book.buildBookUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG, "insert(uri=" + uri + ", values" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String selectionCriteria = selection;

        switch(match){
            case BOOK:
                break;
            case BOOK_ID:
                String id = BookContract.Book.getBookID(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }

        return db.update(BookDatabase.Tables.BOOK, values, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri);

        if(uri.equals(BookContract.BASE_CONTENT_URI)){
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match){
            case BOOK_ID:
                String id = BookContract.Book.getBookID(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.delete( BookDatabase.Tables.BOOK, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
    }
}
