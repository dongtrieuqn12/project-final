package com.example.user.bk_android.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.bk_android.fragment.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

//    final int PAGE_COUNT = 2;
//    private String tabTitles[] = new String[] { "THỜI GIAN CHỜ", "CÁC TUYẾN BUÝT" };
//    private Context context;
//
//    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
//        super(fm);
//        this.context = context;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return PageFragment.newInstance(position + 1);
//    }
//
//    @Override
//    public int getCount() {
//        return PAGE_COUNT;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitles[position];
//    }

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SampleFragmentPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
        //return null;
    }
}


