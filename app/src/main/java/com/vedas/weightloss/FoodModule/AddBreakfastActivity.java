package com.vedas.weightloss.FoodModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rise on 21/05/2018.
 */

public class AddBreakfastActivity extends BaseDetailActivity {
    Toolbar toolbar;
    TextView tool_text;
    ImageView btn_back, home;
    int type;
    ArrayList<String> servingArray;
    ArrayList<String> servingContainerArray;
    ArrayList<String> foodMeasureArray;

    String selectedMeasure = "Units";
    String selectedServing = "1";
    String selectedServingCounter = "1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_createfood);
        ButterKnife.bind(this);
        setToolbar();
        setupWindowAnimations();
        loadFoodCountValuesArray();
        loadMeasureSpinners();
        loadNumberSpinners();
        loadservingContainerSpinners();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tool_text = (TextView) toolbar.findViewById(R.id.toolbar_text);
        tool_text.setText("Create Food");
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        home = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @OnClick({R.id.done})
    public void doneAction() {
        finish();
        /*Intent intent1 = new Intent(getApplicationContext(),DayOneActivity.class);
        intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
        transitionTo(intent1);
        */
    }

    public void loadFoodCountValuesArray() {
        servingArray = new ArrayList<String>();
        servingContainerArray = new ArrayList<String>();
        foodMeasureArray = new ArrayList<>();

        foodMeasureArray.add("Units");
        foodMeasureArray.add("Plates");

        for (int i = 1; i <= 10; i++) {
            servingArray.add(String.valueOf(i));
        }
        for (int i = 1; i <= 10; i++) {
            servingContainerArray.add(String.valueOf(i));
        }
    }

    private void loadMeasureSpinners() {
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.inchspinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, foodMeasureArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedMeasure != null) {
            int spinnerPosition = aa.getPosition(selectedMeasure);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMeasure = foodMeasureArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadNumberSpinners() {
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.feetspinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, servingArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedServing != null) {
            int spinnerPosition = aa.getPosition(selectedServing);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedServing = servingArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadservingContainerSpinners() {
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, servingContainerArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedServingCounter != null) {
            int spinnerPosition = aa.getPosition(selectedServing);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedServingCounter = servingContainerArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupWindowAnimations() {
        Transition transition;
        if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        } else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        }
        getWindow().setEnterTransition(transition);
    }

    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        return enterTransition;
    }

}
