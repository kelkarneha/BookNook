package com.example.android.booknook.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 12/21/2016.
 */

public class BookContract {
    public interface BookColumns{
        String BOOK_ID ="_id";
        String BOOK_RANK = "book_rank";
        String BOOK_ISBN_13 = "book_isbn_13";
        String BOOK_TITLE ="book_title";
        String BOOK_AUTHOR ="book_author";
        String IMAGE_LINK = "image_link";
        String BOOK_DESCRIPTION = "description";
    }

    public static final String CONTENT_AUTHORITY = "com.example.android.booknook.data.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    private static final String PATH_BOOK = "book";
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_BOOK);

    public static final String[] TOP_LEVEL_PATHS = {PATH_BOOK};

    public static class Book implements BookColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_BOOK).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"+CONTENT_AUTHORITY+".book";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"+CONTENT_AUTHORITY+".book";

        public static Uri buildBookUri(String bookID){
            return CONTENT_URI.buildUpon().appendEncodedPath(bookID).build();
        }

        public static String getBookID(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
