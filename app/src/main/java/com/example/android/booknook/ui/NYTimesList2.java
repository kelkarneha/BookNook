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
import com.example.android.booknook.service.GetNonFiction;
import com.example.android.booknook.service.Results;
import com.example.android.booknook.utils.MyBook;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 12/29/2016.
 */

public class NYTimesList2 extends Fragment implements FragmentLifecycle {
    private static final String TAG = " NON FICTION LIST";
    private Tracker mTracker;

    public static String getTAG() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ny_times_list, container, false);
    }

    Example nonfiction1 = new Example();
    Results nonfiction2 = new Results();
    List<Book> nonfiction3 = new ArrayList<>();
    List<MyBook> NYTnonfictionList = new ArrayList<>();
    List<MyBook> SavedNYTnonfictionList = new ArrayList<>();
    ArrayList<MyBook> SaveInstanceBookList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    public NYTListRecyclerViewAdapter nytListRecyclerViewAdapter;
    boolean mTwoPane = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SaveInstanceBookList = (ArrayList<MyBook>) NYTnonfictionList;
        outState.putSerializable("Saved List", SaveInstanceBookList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            SavedNYTnonfictionList = (ArrayList<MyBook>) savedInstanceState.getSerializable("Saved List");
            mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_tab2);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            nytListRecyclerViewAdapter = new NYTListRecyclerViewAdapter(getActivity(), SavedNYTnonfictionList, new RecyclerOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mTwoPane){
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("Book_transfer", SavedNYTnonfictionList.get(position));
                        BookDetailsFragment fragmentTablet = new BookDetailsFragment();
                        fragmentTablet.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragmentTablet)
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), BookDetails.class);
                        intent.putExtra("Book_transfer", SavedNYTnonfictionList.get(position));
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
        //Log.d("Non Fiction List", "onPauseFragment()");
        //Toast.makeText(getActivity(), "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeFragment() {
        //Log.d("Non Fiction List", "onResumeFragment()");
        //nytListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Log.d("Non Fiction List", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mTwoPane = getArguments().getBoolean("isTwoPane");

        if(savedInstanceState != null){
            SavedNYTnonfictionList = (ArrayList<MyBook>) savedInstanceState.getSerializable("Saved List");
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_tab2);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            nytListRecyclerViewAdapter = new NYTListRecyclerViewAdapter(getActivity(), SavedNYTnonfictionList, new RecyclerOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mTwoPane){
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("Book_transfer", SavedNYTnonfictionList.get(position));
                        BookDetailsFragment fragmentTablet = new BookDetailsFragment();
                        fragmentTablet.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragmentTablet)
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), BookDetails.class);
                        intent.putExtra("Book_transfer", SavedNYTnonfictionList.get(position));
                        //startActivityForResult(intent, 2);
                        startActivity(intent);
                    }
                }
            });
            mRecyclerView.setAdapter(nytListRecyclerViewAdapter);
        } else {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_tab2);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            nytListRecyclerViewAdapter = new NYTListRecyclerViewAdapter(getActivity(), NYTnonfictionList, new RecyclerOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mTwoPane){
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("Book_transfer", NYTnonfictionList.get(position));
                        BookDetailsFragment fragmentTablet = new BookDetailsFragment();
                        fragmentTablet.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.book_detail_container, fragmentTablet)
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), BookDetails.class);
                        intent.putExtra("Book_transfer", NYTnonfictionList.get(position));
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

            GetNonFiction APIFictionMethod = retrofit1.create(GetNonFiction.class);
            final Call<Example> fiction = APIFictionMethod.getBestSellers(getResources().getString(R.string.APIKey));
            fiction.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    Log.d("Retrofit", call.request().url().toString());
                    if (response.isSuccessful()) {
                        nonfiction1 = response.body();
                        nonfiction2 = nonfiction1.getResults();
                        nonfiction3 = nonfiction2.getBooks();

                        for(Book singleBook : nonfiction3){
                            //Log.d("singleBook", singleBook.toString());
                            int _id = 0;
                            String rank = String.valueOf(singleBook.getRank());
                            String title =singleBook.getTitle();
                            String author =singleBook.getAuthor();
                            String imageLinkRaw = singleBook.getBookImage();
                            String description = singleBook.getDescription();
                            String isbn13 = singleBook.getPrimaryIsbn13();
                            MyBook book0 = new MyBook(_id, rank, title, author, imageLinkRaw, description, isbn13);
                            NYTnonfictionList.add(book0);
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
