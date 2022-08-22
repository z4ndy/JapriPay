package com.masterweb.japripay.classes.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.masterweb.japripay.fragment.setting.PinFragment;
import com.masterweb.japripay.fragment.setting.ProfileFragment;
import com.masterweb.japripay.fragment.setting.RekeningFragment;

public class SettingAdapter extends FragmentPagerAdapter {

    public SettingAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new ProfileFragment();
        }
        else if (position == 1)
        {
            fragment = new PinFragment();
        }
        else if (position == 2)
        {
            fragment = new RekeningFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Profile";
        }
        else if (position == 1)
        {
            title = "PIN Transaksi";
        }
        else if (position == 2)
        {
            title = "Rekening";
        }
        return title;
    }
}
