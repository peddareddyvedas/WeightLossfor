package com.vedas.weightloss.FoodModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.vedas.weightloss.R;

/**
 * Created by Vedas on 11/10/2016.
 */
public class WeightFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weigtht_fragment, container, false);

        return view;
    }

}