package com.dotech.ammar_rajab.goldmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by ammar-rajab on 09/04/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private static int NUM_TABLE=3;

    public GoldFragment goldFragment=new GoldFragment();
    public SilverFragment silverFragment=new SilverFragment();
    public CurrencyFragment currencyFragment=new CurrencyFragment();


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("Tab",String.valueOf(position));
        switch (position){
            case 0:
                return goldFragment;
            case 1:
                return silverFragment;
            case 2:
                return currencyFragment;
            default:
                Log.i("Tab","ERR");
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABLE;
    }
}
