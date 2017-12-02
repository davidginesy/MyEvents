package com.example.user.myevents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;



public class EventPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public EventPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EventCreatedFragment tab1 = new EventCreatedFragment();
                return tab1;
            case 1:
                EventInvitedFragment tab2 = new EventInvitedFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
