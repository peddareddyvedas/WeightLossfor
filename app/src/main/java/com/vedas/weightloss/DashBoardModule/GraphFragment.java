package com.vedas.weightloss.DashBoardModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vedas.weightloss.FoodModule.CaloriesFragment;
import com.vedas.weightloss.FoodModule.WeightFragment;
import com.vedas.weightloss.R;

import butterknife.ButterKnife;

/**
 * Created by Rise on 24/05/2018.
 */

public class GraphFragment extends Fragment {
    TextView text;
    View view;
    TextView tool_text;
    ImageView back, add;
    Toolbar toolbar;
    private TabLayout tabLayout;
    private LinearLayout container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.graph_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        tool_text = (TextView) toolbar.findViewById(R.id.toolbar_text);
        tool_text.setText("Graph");
        setupTabIcons();
        return view;
    }
    public void init() {

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        container = (LinearLayout) view.findViewById(R.id.fragment_container);
    }
    private void setupTabIcons() {

        tabLayout.addTab(tabLayout.newTab().setText("Calories"));
        tabLayout.addTab(tabLayout.newTab().setText("Weight"));

        //replace default fragment
        replaceFragment(new CaloriesFragment());

        //handling tab click event
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new CaloriesFragment());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new WeightFragment());
                } /*else {
                    replaceFragment(new GameFragment());
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =getActivity(). getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }
}


///////////








