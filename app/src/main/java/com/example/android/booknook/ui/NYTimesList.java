package com.example.android.booknook.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.booknook.R;
import com.example.android.booknook.analytics.AnalyticsApplication;
import com.example.android.booknook.recyclerview.NYTListRecyclerViewAdapter;
import com.example.android.booknook.recyclerview.RecyclerOnItemClickListener;
import com.example.android.booknook.rest.FragmentLifecycle;
import com.example.android.booknook.service.Book;
import com.example.android.booknook.service.Example;
import com.example.android.booknook.service.GetFiction;
import com.example.android.booknook.service.Results;
import com.example.android.booknook.utils.MyBook;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.name;

/**
 * Created by Administrator on 12/29/2016.
 */

public class NYTimesList extends Fragment implements FragmentLifecycle {
    private static final String TAG = "FICTION LIST";
    private Tracker mTracker;

    public static String getTAG() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ny_times_list, container, false);
    }

    Example fiction1 = new Example();
    Results fiction2 = new Results();
    List<Book> fiction3 = new ArrayList<>();
    List<MyBook> NYTfictionList = new ArrayList<>();
    List<MyBook> SavedNYTFictionList = new ArrayList<>();
    ArrayList<MyBook> SaveInstanceBookList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    public NYTListRecyclerViewAdapter nytListRecyclerViewAdapter;
    boolean mTwoPane;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SaveInstanceBookList = (ArrayList<MyBook>) NYTfictionList;
        outState.putSerializable("Saved List", SaveInstanceBookList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            SavedNYTFictionList = (ArrayList<MyBook>) savedInstanceState.getSerializable("Saved List");
            mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_tab2);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            nytListRecyclerViewAdapter = new NYTListRecyclerViewAdapter(getActivity(), SavedNYTFictionList, new RecyclerOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mTwoPane){
                        //Log.d("Fiction List", "inside mTwoPane true");
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("Book_transfer", SavedNYTFictionList.get(position));
                        BookDetailsFragment fragmentTablet = new BookDetailsFragment();
                        fragmentTablet.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragmentTablet)
                                .commit();
                    } else {
                        //Log.d("Fiction List", "inside mTwoPane true");
                        Intent intent = new Intent(getActivity(), BookDetails.class);
                        intent.putExtra("Book_transfer", SavedNYTFictionList.get(position));
                        //startActivityForResult(intent, 2);
                        startActivity(intent);
                    }
                }
            });
            mRecyclerView.setAdapter(nytListRecyclerViewAdapter);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onPauseFragment() {
        //Log.d("Fiction List", "onPauseFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        //Log.d("Fiction List", "onResumeFragment()");
        //nytListRecyclerViewAdapter.notifyDataSetChanged();
        Log.i(TAG, getResources().getString(R.string.settingScreenName) + name);
        mTracker.setScreenName(getResources().getString(R.string.Image) + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
         //Log.d("Fiction List", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mTwoPane = getArguments().getBoolean("isTwoPane");

        if(savedInstanceState != null){
            SavedNYTFictionList = (ArrayList<MyBook>) savedInstanceState.getSerializable("Saved List");
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_tab2);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            nytListRecyclerViewAdapter = new NYTListRecyclerViewAdapter(getActivity(), SavedNYTFictionList, new RecyclerOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mTwoPane){
                        //Log.d("Fiction List", "inside mTwoPane true");
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("Book_transfer", SavedNYTFictionList.get(position));
                        BookDetailsFragment fragmentTablet = new BookDetailsFragment();
                        fragmentTablet.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragmentTablet)
                                .commit();
                    } else {
                        //Log.d("Fiction List", "inside mTwoPane true");
                        Intent intent = new Intent(getActivity(), BookDetails.class);
                        intent.putExtra("Book_transfer", SavedNYTFictionList.get(position));
                        //startActivityForResult(intent, 2);
                        startActivity(intent);
                    }
                }
            });
            mRecyclerView.setAdapter(nytListRecyclerViewAdapter);
        } else {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_tab2);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            nytListRecyclerViewAdapter = new NYTListRecyclerViewAdapter(getActivity(), NYTfictionList, new RecyclerOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mTwoPane){
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("Book_transfer", NYTfictionList.get(position));
                        BookDetailsFragment fragmentTablet = new BookDetailsFragment();
                        fragmentTablet.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragmentTablet)
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), BookDetails.class);
                        intent.putExtra("Book_transfer", NYTfictionList.get(position));
                        //startActivityForResult(intent, 2);
                        startActivity(intent);
                    }
                }
            });
            mRecyclerView.setAdapter(nytListRecyclerViewAdapter);

            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.baseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GetFiction APIFictionMethod = retrofit1.create(GetFiction.class);
            final Call<Example> fiction = APIFictionMethod.getBestSellers(getResources().getString(R.string.APIKey));
            fiction.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    //Log.d("Retrofit", call.request().url().toString());
                    if (response.isSuccessful()) {
                        fiction1 = response.body();
                        fiction2 = fiction1.getResults();
                        fiction3 = fiction2.getBooks();

                        for(Book singleBook : fiction3){
                            //Log.d("singleBook", singleBook.toString());
                            int _id = 0;
                            String rank = String.valueOf(singleBook.getRank());
                            String title =singleBook.getTitle();
                            String author =singleBook.getAuthor();
                            String imageLinkRaw = singleBook.getBookImage();
                            String description = singleBook.getDescription();
                            String isbn13 = singleBook.getPrimaryIsbn13();
                            MyBook book0 = new MyBook(_id, rank, title, author, imageLinkRaw, description, isbn13);
                            NYTfictionList.add(book0);
                        }
                        nytListRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {

                }
            });
        }
    }
}
