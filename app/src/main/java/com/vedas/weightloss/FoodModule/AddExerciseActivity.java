package com.vedas.weightloss.FoodModule;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by VEDAS on 6/12/2018.
 */

public class AddExerciseActivity extends BaseDetailActivity {
    TextInputEditText textInputName, textInputminutes, textInputcalories;
    Toolbar toolbar;
    ImageView btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexercise);
        ButterKnife.bind(this);
        textInputName = (TextInputEditText) findViewById(R.id.name);
        textInputminutes = (TextInputEditText) findViewById(R.id.time);
        textInputcalories = (TextInputEditText) findViewById(R.id.cal);
        setToolbar();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textInputName.setVisibility(View.VISIBLE);
                textInputminutes.setVisibility(View.VISIBLE);
                textInputcalories.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbartext.append("New Exercise");
    }

    @OnClick({R.id.set})
    public void saveAction() {
        finish();
    }
}
