package com.vedas.weightloss.FoodModule;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.WaterObject;
import com.vedas.weightloss.Models.WeightObject;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by VEDAS on 6/7/2018.
 */

public class AddWeightActivity extends BaseDetailActivity {
    int type;
    EditText textInputweight;
    Toolbar toolbar;
    TextView tool_text;
    ImageView btn_back;
    ArrayList<String> weightMeasures;
    String selectedMeasure = "Kilograms";
    String selectedWeight = "";
    WeightObject objWeight;

    // String selectedWeigthValue = "29";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addweight);
        PersonalInfoDataController.getInstance().fetchPersonalInfoData();
        PersonalInfoController.getInstance().loadWeightValuesArray();
        ButterKnife.bind(this);
        setupWindowAnimations();
        textInputweight = (EditText) findViewById(R.id.weight);
        setToolbar();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textInputweight.setVisibility(View.VISIBLE);
            }
        }, 500);
        textInputweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       /* textInputweight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("weight", "call" + s.toString());
                selectedWeight = s.toString();
            }
        });*/

        if (DayFoodDataController.getInstance().currentFoodObject != null) {
            if (DayFoodDataController.getInstance().currentFoodObject.weight != null) {
                String weight = DayFoodDataController.getInstance().currentFoodObject.weight;
                Log.e("selectedweight", "call" + weight);
                String objWeight[] = weight.split(" ");
                selectedWeight = objWeight[0];
                textInputweight.setText(selectedWeight);
                if (objWeight[1].contains("Kg")) {
                    selectedMeasure = "Kilograms";
                } else {
                    selectedMeasure = "Lbs";
                }

            }
        }
        loadWeghtMeasureSpinner();
        loadweightspinner();
    }

    @OnClick({R.id.set})
    public void saveAction() {
        if (selectedWeight.length() > 0) {
            objWeight = new WeightObject();
            objWeight.setWeight(selectedWeight);
            objWeight.setUnits(selectedMeasure);
            if (selectedMeasure.equals("Kilograms")) {
                DayFoodDataController.getInstance().currentFoodObject.weight = selectedWeight + " " + "Kg";
            } else {
                DayFoodDataController.getInstance().currentFoodObject.weight = selectedWeight + " " + "Lbs";
            }
            //DayFoodController.getInstance().selectedFoodObject.weight=selectedWeight+" "+selectedMeasure;
            DayFoodDataController.getInstance().updateDayFoodData(DayFoodDataController.getInstance().currentFoodObject);
            finish();
        } else {
            final View contextView = findViewById(R.id.context_view);
            Snackbar.make(contextView, R.string.selectitem2, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.CYAN)
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tool_text = (TextView) toolbar.findViewById(R.id.toolbar_text);
        tool_text.setText("Add Weight");
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void loadweightspinner() {
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().kgArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        if (textInputweight.getText().toString() != null && selectedWeight != null && selectedWeight.contains("Kg")) {
            String array[] = textInputweight.getText().toString().split(" ");
            selectedWeight = array[0];
            Log.e("textValuekg", "call" + array[0]);
            int spinnerPosition = aa.getPosition(selectedWeight);
            spin.setSelection(spinnerPosition);
        } else {
            int spinnerPosition = aa.getPosition(selectedWeight);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedMeasure.equals("Kg")) {
                    String selectedValue = PersonalInfoController.getInstance().kgArray.get(i);
                    selectedWeight = selectedValue + " " + "Kg";
                    Log.e("selectedWeigthValue", "call" + selectedWeight);
                    double lbsValue = new Double(PersonalInfoController.getInstance().kgArray.get(i)) * 2.2046;
                    String selectedValue1 = String.valueOf(Math.round(lbsValue));
                    int index = PersonalInfoController.getInstance().lbsArray.indexOf(selectedValue1);
                    selectedWeight = PersonalInfoController.getInstance().lbsArray.get(index);
                    Log.e("selectedWeigth", "call" + selectedWeight);
                } else {
                    String selectedValue = PersonalInfoController.getInstance().lbsArray.get(i);
                    selectedWeight = selectedValue + " " + "Lbs";
                    double kgValue = new Double(PersonalInfoController.getInstance().lbsArray.get(i)) * 0.453592;
                    String selectedValue1 = String.valueOf(Math.round(kgValue));
                    int index = PersonalInfoController.getInstance().kgArray.indexOf(selectedValue1);
                    selectedWeight = PersonalInfoController.getInstance().kgArray.get(index);
                    Log.e("selectedWeigth", "call" + selectedWeight);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void loadWeghtMeasureSpinner() {
        weightMeasures = new ArrayList<>();
        weightMeasures.add("Kilograms");
        weightMeasures.add("Lbs");

        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, weightMeasures);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedMeasure != null) {
            int spinnerPosition = aa.getPosition(selectedMeasure);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMeasure = weightMeasures.get(i);
                Log.e("selectedMeasure", "call" + selectedMeasure);
                if (selectedWeight.length() > 0 && DayFoodDataController.getInstance().currentFoodObject == null) {
                    if (selectedMeasure.equals("Kilograms")) {
                        int weight = Integer.parseInt(selectedWeight);
                        double kgValue = weight * 0.453592;
                        selectedWeight = String.valueOf(Math.round(kgValue));
                        textInputweight.setText(selectedWeight);
                    } else {
                        int weight = Integer.parseInt(selectedWeight);
                        double kgValue = weight * 2.2046;
                        selectedWeight = String.valueOf(Math.round(kgValue));
                        textInputweight.setText(selectedWeight);
                    }
                    loadweightspinner();
                }
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
