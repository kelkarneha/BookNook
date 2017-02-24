package com.example.android.booknook.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.booknook.R;
import com.example.android.booknook.data.BookContract;
import com.example.android.booknook.utils.MyBook;
import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {

    ImageView coverPage;
    TextView title;
    TextView author;
    TextView description;
    FloatingActionButton fab;
    private ContentResolver mContentResolver;
    boolean bookInList = false;
    String isbn13ToBeChecked;
    private Cursor cursor;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isInFav", String.valueOf(bookInList));
        setResult(1, intent);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        //toolbar1.setTitle(getResources().getString(R.string.app_name));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.primaryTextColor));
        ActionBar ab = this.getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getResources().getString(R.string.app_name));

        Intent intent = getIntent();
        final MyBook book_details = (MyBook) intent.getSerializableExtra("Book_transfer");

        title = (TextView) findViewById(R.id.details_title);
        author = (TextView) findViewById(R.id.details_author);
        description = (TextView) findViewById(R.id.details_description);
        coverPage = (ImageView) findViewById(R.id.details_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        mContentResolver = BookDetails.this.getContentResolver();
        isbn13ToBeChecked = book_details.getIsbn_13();
        String[] projection =  {BaseColumns._ID,
                BookContract.BookColumns.BOOK_RANK,
                BookContract.BookColumns.BOOK_ID,
                BookContract.BookColumns.BOOK_TITLE,
                BookContract.BookColumns.BOOK_AUTHOR,
                BookContract.BookColumns.IMAGE_LINK,
                BookContract.BookColumns.BOOK_DESCRIPTION};
        String selection = BookContract.BookColumns.BOOK_ISBN_13 + " LIKE " + "'" + isbn13ToBeChecked + "'";
        cursor = mContentResolver.query(BookContract.URI_TABLE, projection, selection, null, null);

        if(cursor != null && cursor.moveToFirst()){
            if(cursor.getCount()>0){
                bookInList = true;
                int bookInListID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                book_details.set_id(bookInListID);
                cursor.close();
            } else {
                cursor.close();
            }
        }

        if(bookInList){
            fab.setImageDrawable(getResources().getDrawable(R.drawable.books_icon, this.getTheme()));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookInList == false){
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.books_icon, BookDetails.this.getTheme()));
                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookColumns.BOOK_AUTHOR, book_details.getBook_author());
                    values.put(BookContract.BookColumns.BOOK_TITLE, book_details.getBook_title());
                    values.put(BookContract.BookColumns.BOOK_DESCRIPTION, book_details.getDescription());
                    values.put(BookContract.BookColumns.BOOK_RANK, book_details.getBook_rank());
                    values.put(BookContract.BookColumns.BOOK_ISBN_13, book_details.getIsbn_13());
                    values.put(BookContract.BookColumns.IMAGE_LINK, book_details.getImage_link());
                    Uri returned = mContentResolver.insert(BookContract.URI_TABLE, values);
                    bookInList = true;
                    Toast toast = new Toast(BookDetails.this);
                    toast.makeText(BookDetails.this, book_details.getBook_title() + " " + getResources().getString(R.string.bookAdded), Toast.LENGTH_LONG).show();

                } else {
                    bookInList = false;
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.books_icon_bw, BookDetails.this.getTheme()));
                    Uri uri = BookContract.Book.buildBookUri(String.valueOf(book_details.get_id()));
                    mContentResolver.delete(uri, null, null);
                    Toast toast = new Toast(BookDetails.this);
                    toast.makeText(BookDetails.this, book_details.getBook_title() + " " + getResources().getString(R.string.bookRemoved), Toast.LENGTH_LONG).show();
                }
            }
        });

        title.setText(book_details.getBook_title());
        author.setText(book_details.getBook_author());
        description.setText(book_details.getDescription());

        if(book_details.getImage_link() == null){
            coverPage.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(this).load(book_details.getImage_link())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(coverPage);
        }
    }
}
