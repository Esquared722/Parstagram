package com.example.parstagram.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.HomeFragment;
import com.example.parstagram.fragments.ProfileFragment;

public class MyPagerAdapter extends FragmentStateAdapter {

    public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new Fragment();
        switch(position) {
            case 0:
                fragment = HomeFragment.newInstance();
                break;
            case 1:
                fragment = ComposeFragment.newInstance();
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
