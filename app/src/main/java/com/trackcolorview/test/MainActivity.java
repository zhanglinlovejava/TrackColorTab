package com.trackcolorview.test;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trackcolorview.test.trackcolorview.CustomScrollTab;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private CustomScrollTab tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViewPager();
    }

    public void initViewPager() {
        mViewPager.setAdapter(new CustomPagerAdapter(
                getSupportFragmentManager()));
        tabs.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    public void findViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_activity_main);
        tabs = (CustomScrollTab) findViewById(R.id.tabs);
    }


}
