package com.jeanjnap.chat.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jeanjnap.chat.Fragments.ContactsFragment;
import com.jeanjnap.chat.Fragments.ConversationsFragment;

public class HomeAdapter extends FragmentPagerAdapter {

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("_res", "position: " + position);

        switch (position){
            case 0:
                return new ConversationsFragment();

            default:
                return new ContactsFragment();

        }

    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}