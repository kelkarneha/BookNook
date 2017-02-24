package com.example.android.booknook.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.booknook.R;
import com.example.android.booknook.data.BookContract;
import com.example.android.booknook.utils.MyBook;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {

    ImageView coverPage;
    TextView title;
    TextView author;
    TextView description;
    FloatingActionButton fab;
    private ContentResolver mContentResolver;
    boolean bookInList = false;
    String isbn13ToBeChecked;
    private Cursor cursor;

    public BookDetailsFragment() {

    }

    public interface OnItemClickedListener {
        public void OnItemClicked();
    }

    OnItemClickedListener mListener= sDummyCallbacks;

    private static OnItemClickedListener sDummyCallbacks = new OnItemClickedListener() {
        @Override
        public void OnItemClicked() {
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemClickedListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_book_details, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final MyBook book_details = (MyBook) getArguments().getSerializable("Book_transfer");

        title = (TextView) view.findViewById(R.id.details_title);
        author = (TextView) view.findViewById(R.id.details_author);
        description = (TextView) view.findViewById(R.id.details_description);
        coverPage = (ImageView) view.findViewById(R.id.details_image);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        mContentResolver = getActivity().getContentResolver();
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
            fab.setImageDrawable(getResources().getDrawable(R.drawable.books_icon, getActivity().getTheme()));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookInList == false){
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.books_icon, getActivity().getTheme()));
                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookColumns.BOOK_AUTHOR, book_details.getBook_author());
                    values.put(BookContract.BookColumns.BOOK_TITLE, book_details.getBook_title());
                    values.put(BookContract.BookColumns.BOOK_DESCRIPTION, book_details.getDescription());
                    values.put(BookContract.BookColumns.BOOK_RANK, book_details.getBook_rank());
                    values.put(BookContract.BookColumns.BOOK_ISBN_13, book_details.getIsbn_13());
                    values.put(BookContract.BookColumns.IMAGE_LINK, book_details.getImage_link());
                    Uri returned = mContentResolver.insert(BookContract.URI_TABLE, values);
                    bookInList = true;
                    Toast toast = new Toast(getActivity());
                    toast.makeText(getActivity(), book_details.getBook_title() + " " + getResources().getString(R.string.bookAdded), Toast.LENGTH_LONG).show();

                } else {
                    bookInList = false;
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.books_icon_bw, getActivity().getTheme()));
                    Uri uri = BookContract.Book.buildBookUri(String.valueOf(book_details.get_id()));
                    mContentResolver.delete(uri, null, null);
                    Toast toast = new Toast(getActivity());
                    toast.makeText(getActivity(), book_details.getBook_title() + " " + getResources().getString(R.string.bookRemoved), Toast.LENGTH_LONG).show();
                }
            }
        });

        title.setText(book_details.getBook_title());
        author.setText(book_details.getBook_author());
        description.setText(book_details.getDescription());

        if(book_details.getImage_link() == null){
            coverPage.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(getActivity()).load(book_details.getImage_link())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(coverPage);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
