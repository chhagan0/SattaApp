package com.madhurmilan.milan.adapterclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAppliance extends FragmentPagerAdapter {

    List<String> tabTitle = new ArrayList<>();
    List<Fragment> fragments = new ArrayList<>();
    private Boolean disable = false;

    public ViewPagerAppliance(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAppliance(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void add(Fragment fragment,String title) {
        fragments.add(fragment);
        tabTitle.add(title);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitle.get(position);
    }


}
