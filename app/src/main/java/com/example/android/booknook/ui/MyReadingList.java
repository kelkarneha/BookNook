package com.example.android.booknook.ui;


import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.booknook.R;
import com.example.android.booknook.data.BookContract;
import com.example.android.booknook.data.BookListLoader;
import com.example.android.booknook.recyclerview.NYTListRecyclerViewAdapter;
import com.example.android.booknook.recyclerview.RecyclerOnItemClickListener;
import com.example.android.booknook.rest.FragmentLifecycle;
import com.example.android.booknook.utils.MyBook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 12/29/2016.
 */

public class MyReadingList extends Fragment implements LoaderManager.LoaderCallbacks<List<MyBook>>, FragmentLifecycle {
    ContentResolver contentResolver;
    private static int LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    public NYTListRecyclerViewAdapter nytListRecyclerViewAdapter;
    List<MyBook> FavList = new ArrayList<>();
    LoaderManager loaderManager;
    boolean mTwoPane = false;

    private static final String TAG = "MY LIST";

    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onPauseFragment() {
        //Log.d("My List", "onPauseFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        //Log.d("My List", "onResumeFragment() "+ String.valueOf(isAdded()));
        if(isAdded()){
            getLoaderManager().initLoader(LOADER_ID++, null, this).forceLoad();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getLoaderManager().initLoader(LOADER_ID++, null, this).forceLoad();
        //Log.d("My List", "onCreateView");
        return inflater.inflate(R.layout.my_reading_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Log.d("My List", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mTwoPane = getArguments().getBoolean("isTwoPane");

        contentResolver = getActivity().getContentResolver();
        loaderManager = getActivity().getSupportLoaderManager();
        getLoaderManager().initLoader(LOADER_ID++, null, this).forceLoad();
    }

    public Loader<List<MyBook>> onCreateLoader(int id, Bundle args) {
        //Log.v("create loader called", String.valueOf(id));
        contentResolver = getActivity().getContentResolver();
        return new BookListLoader(getActivity(), BookContract.URI_TABLE, contentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<MyBook>> loader, List<MyBook> data) {
        //Log.d("on load finished", "called");
        FavList = data;
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_tab1);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        nytListRecyclerViewAdapter = new NYTListRecyclerViewAdapter(getActivity(), FavList, new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mTwoPane){
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("Book_transfer", FavList.get(position));
                    BookDetailsFragment fragmentTablet = new BookDetailsFragment();
                    fragmentTablet.setArguments(arguments);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.book_detail_container, fragmentTablet)
                            .commit();
                } else {
                    Intent intent = new Intent(getActivity(), BookDetails.class);
                    intent.putExtra("Book_transfer", FavList.get(position));
                    startActivityForResult(intent, 1);
                    //startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(nytListRecyclerViewAdapter);

        nytListRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("My List", "onActivityResult");
        String result = "true";
        if (requestCode == 1) {
            if(resultCode == 1){
                result = data.getStringExtra("isInFav");
            }
        }
        Log.d("My List", result);
        if(result.equals("false")){
            getLoaderManager().initLoader(LOADER_ID++, null, this).forceLoad();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MyBook>> loader) {
        Log.d("My List", "onLoaderReset");
        FavList = null;
    }
}
