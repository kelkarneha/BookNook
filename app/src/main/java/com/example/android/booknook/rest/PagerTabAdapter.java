package com.example.android.booknook.rest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.android.booknook.ui.MyReadingList;
import com.example.android.booknook.ui.NYTimesList;
import com.example.android.booknook.ui.NYTimesList2;

/**
 * Created by Administrator on 12/29/2016.
 */

public class PagerTabAdapter extends FragmentStatePagerAdapter {
    int numTabs;
    private Bundle args;

    public PagerTabAdapter(FragmentManager fr, int numTabs, Bundle args){
        super(fr);
        this.numTabs = numTabs;
        this.args = args;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                NYTimesList nytList = new NYTimesList();
                nytList.setArguments(args);
                Log.d("NYTimesList 1", "fragment returned");
                return nytList;
            case 1:
                NYTimesList2 nytList2 = new NYTimesList2();
                nytList2.setArguments(args);
                Log.d("NYTimesList 2", "fragment returned");
                return nytList2;
            case 2:
                MyReadingList myList = new MyReadingList();
                myList.setArguments(args);
                Log.d("My List", "fragment returned");
                return myList;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    public Bundle getArgs() {
        return args;
    }
}
