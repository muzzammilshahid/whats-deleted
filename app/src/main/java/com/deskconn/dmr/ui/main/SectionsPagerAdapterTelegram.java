package com.deskconn.dmr.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.deskconn.dmr.R;
import com.deskconn.dmr.fragments.ChatFragmentTelegram;
import com.deskconn.dmr.fragments.ImagesFragmentTelegram;


public class SectionsPagerAdapterTelegram extends FragmentPagerAdapter {
    @StringRes
    private static final int[]
            TAB_TITLES = new int[]{R.string.tab_chat, R.string.tab_images};
    private final Context mContext;

    public SectionsPagerAdapterTelegram(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ChatFragmentTelegram();
                break;
            case 1:
                fragment = new ImagesFragmentTelegram();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
