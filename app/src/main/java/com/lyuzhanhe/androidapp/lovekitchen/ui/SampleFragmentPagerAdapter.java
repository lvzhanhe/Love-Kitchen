package com.lyuzhanhe.androidapp.lovekitchen.ui;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;




public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "main course", "side dish", "dessert","appetizer" };
    private Context context;
    private String query;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, String q) {
        super(fm);
        this.context = context;
        query = q;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(tabTitles[position], query, tabTitles[position]);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
