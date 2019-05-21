package com.csjbot.blackgaga.feature.content3;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 孙秀艳 on 2018/2/1.
 */

public class Content3FragmentAdapter extends FragmentStatePagerAdapter {
    private List<String> mTitles = new ArrayList<>();
    private List<String> mUrls = new ArrayList<>();
    private Context context;

    public Content3FragmentAdapter(List<String> urls, List<String> titles, FragmentManager fm, Context context) {
        super(fm);
        mUrls.clear();
        mUrls.addAll(urls);
        this.context = context;
        mTitles.clear();
        mTitles.addAll(titles);
    }


    @Override
    public Fragment getItem(int position) {
        return Content3Fragment.newInstance(mUrls.get(position));
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles.get(position).length() <= 7) {
            return mTitles.get(position);
        } else {
            return mTitles.get(position).substring(0, 5) + "...";
        }
    }
}
