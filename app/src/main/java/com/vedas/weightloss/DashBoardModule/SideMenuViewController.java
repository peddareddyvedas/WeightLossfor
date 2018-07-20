package com.vedas.weightloss.DashBoardModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.vedas.weightloss.R;

import butterknife.ButterKnife;


/**
 *
 */
public class SideMenuViewController extends Fragment {
    View view;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    View containerView;
    FragmentDrawerListener drawerListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_drawer_list, container, false);
        ButterKnife.bind(this, view);


        return view;
    }
    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ///for builtin button open close drawayer.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();

                Log.e("onDrawerItemSelected", "call");

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
                //below line used to remove shadow of drawer
                mDrawerLayout.setScrimColor(Color.TRANSPARENT);
                Log.e("onDrawerSlide", "call");

                SharedPreferences preferences = getActivity().getSharedPreferences("contactsDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

            }//this method helps you to aside menu drawer

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
