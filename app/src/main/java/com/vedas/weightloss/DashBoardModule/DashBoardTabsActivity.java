package com.vedas.weightloss.DashBoardModule;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.NewsFeedController;
import com.vedas.weightloss.Controllers.RegionController;
import com.vedas.weightloss.R;

import java.util.ArrayList;
import java.util.List;

public class DashBoardTabsActivity extends AppCompatActivity {
    //Declaring the Toolbar
    Toolbar toolbar;
    //Declaring the Tablayout
    TabLayout tabLayout;
    String key;
    //Tabicons
    int[] tabicons = {R.drawable.dashboard_tab, R.drawable.graph_tab, R.drawable.news_tab, R.drawable.more_tab};
    //ViewPager
    CustomViewPager viewPager;
    ImageView back, add;
    TextView tool_text;
    SideMenuViewController drawerFragment;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTabbar();
        setDefaultTabPosition();
        NewsFeedController.getInstance().fetchnews();
       // RegionController.getInstance().RegionServerAPI();

        try {
            Bundle bundle = getIntent().getExtras();
            key = bundle.getString("key");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (savedInstanceState == null) {
            if (key == null) {
                ////

            } else if (key.equals("homefrag")) {
                Fragment fragment = new DashBordFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame1, fragment, "homefrag").commit();
            } else if (key.equals("operationfrag")) {
                Fragment fragment = new DashBordFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame1, fragment, "operationfrag").commit();
            } else if (key.equals("storefrag")) {
                Fragment fragment = new DashBordFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame1, fragment, "storefrag").commit();

            } else if (key.equals("settingfrag")) {
                Fragment fragment = new DashBordFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame1, fragment, "settingfrag").commit();
            }
        }
    }
    public void setDefaultTabPosition() {

            Fragment fragment = new DashBordFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame1, fragment, "homefrag").commit();
            tabLayout.getTabAt(0).select();

    }
    //setting Tababar icons and Text
    public void setTabbar() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {

                    DashBordFragment fragment = new DashBordFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame1, fragment, "home");
                    fragmentTransaction.commitAllowingStateLoss();

                } else if (tab.getPosition() == 1) {
                    GraphFragment fragment = new GraphFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame1, fragment, "Operation");
                    fragmentTransaction.commitAllowingStateLoss();

                } else if (tab.getPosition() == 2) {
                    NewsFragment fragment = new NewsFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame1, fragment, "store");
                    fragmentTransaction.commitAllowingStateLoss();
                } else if (tab.getPosition() == 3) {
                    MoreFragment fragment = new MoreFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame1, fragment, "setup");
                    fragmentTransaction.commitAllowingStateLoss();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new DashBordFragment());
        pagerAdapter.addFragment(new GraphFragment());
        pagerAdapter.addFragment(new NewsFragment());
        pagerAdapter.addFragment(new MoreFragment());

        tabLayout.setTabsFromPagerAdapter(pagerAdapter);

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            tabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 4;
        private final String[] mTabsTitle = {"DashBoard", "Graph", "News", "More"};
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(DashBoardTabsActivity.this).inflate(R.layout.activity_custom_tabbar, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(tabicons[position]);
            Log.e("position", " " + tabicons[position]);
            return view;

        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Log.e("fragment", "position 0");
            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            //mFragmentTitleList.add(""+img);
            //img.setBackgroundResource(image);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

