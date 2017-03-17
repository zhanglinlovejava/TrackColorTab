package com.trackcolorview.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin.Zhang on 2017/1/19.
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    private List<String> titleList;

    public CustomPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        titleList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            titleList.add("第" + i + "个");
        }
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return MyFragment.getInstance(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
