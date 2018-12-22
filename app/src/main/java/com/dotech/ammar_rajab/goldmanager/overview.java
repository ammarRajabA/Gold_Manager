package com.dotech.ammar_rajab.goldmanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.*;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class overview extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public SwipeRefreshLayout swipeRefreshLayout;
    public static int tabIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        setupTabs();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(overview.this,About.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(overview.this,SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onRefresh(){
        if (!isNetworkStatusAvialable(getBaseContext())){
            final Snackbar offline=Snackbar.make(findViewById(R.id.parentLayout),"You are Currently Offline !",10000);
            offline.setAction("Close", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    offline.dismiss();
                }
            });
            offline.show();
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter=new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(tabIndex);
    }
    public void setupTabs(){
        final TabLayout tabs=(TabLayout) findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter=new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!isNetworkStatusAvialable(getBaseContext())){
                    final Snackbar offline=Snackbar.make(findViewById(R.id.parentLayout),"You are Currently Offline !",10000);
                    offline.setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            offline.dismiss();
                        }
                    });
                    offline.show();
                }
                tabIndex=tab.getPosition();
                viewPager.setCurrentItem(tabIndex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabIndex=tab.getPosition();
                viewPager.setCurrentItem(tabIndex);
            }
        });
    }

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }
}
