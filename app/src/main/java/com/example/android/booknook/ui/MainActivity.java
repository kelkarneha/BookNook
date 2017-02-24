package com.example.android.booknook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.booknook.R;
import com.example.android.booknook.asynctaskdemo.GetBookData;
import com.example.android.booknook.rest.FragmentLifecycle;
import com.example.android.booknook.rest.PagerTabAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity implements BookDetailsFragment.OnItemClickedListener{

    ViewPager viewPager;
    PagerTabAdapter pageAdapter;
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean mTwoPane = false;
        Bundle args = new Bundle();
        super.onCreate(savedInstanceState);
        GetBookData getData = new GetBookData();


        String[] asyncDemoURL = {"https://api.nytimes.com/svc/books/v3/lists.json?api-key=48471282c8b74bab8630df227a6013cc&list=e-book-fiction"};
        getData.execute(asyncDemoURL); //This is to demo the use of Async Task, please find data in LogCat

        setContentView(R.layout.activity_main);

        if(findViewById(R.id.book_detail_container) != null){
            mTwoPane = true;
        }
        //Log.d("MainActivity", String.valueOf(mTwoPane));
        args.putBoolean("isTwoPane", mTwoPane);

        if(!mTwoPane){
            MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.FreeAds));
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(getResources().getString(R.string.TestDeviceID))        // All emulators
                    .build();

            mAdView.loadAd(adRequest);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.NYTFictionBestsellers)).setIcon(R.drawable.poweredby_nytimes_30a));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.NYTNonFictionBestsellers)).setIcon(R.drawable.poweredby_nytimes_30a));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.myReadingList)).setIcon(R.drawable.books_icon));


        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pageAdapter = new PagerTabAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), args);
        
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentLifecycle fragmentLifecycle = (FragmentLifecycle) pageAdapter.instantiateItem(viewPager, position);
                fragmentLifecycle.onResumeFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Log.d("onTabSelected", String.valueOf(tab.getPosition()));
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Log.d("onTabUnselected", String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Log.d("onTabReselected", String.valueOf(tab.getPosition()));
            }

        });

        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 2){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    MyReadingList fragment = fragmentManager.findFragmentById(R.id.)
                    fragment.<specific_function_name>();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void OnItemClicked() {
        int position = viewPager.getCurrentItem();
        if(position == 3){
            FragmentLifecycle fragmentLifecycle = (FragmentLifecycle) pageAdapter.instantiateItem(viewPager, position);
            fragmentLifecycle.onResumeFragment();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("Main Activity", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
