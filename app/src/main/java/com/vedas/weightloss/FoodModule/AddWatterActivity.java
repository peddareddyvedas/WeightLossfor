package com.vedas.weightloss.FoodModule;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.WaterDataController;
import com.vedas.weightloss.Models.WaterObject;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by VEDAS on 6/7/2018.
 */

public class AddWatterActivity extends BaseDetailActivity {
    int type;
    Toolbar toolbar;
    TextView tool_text, txt_measure;
    ImageView btn_back, imag_alarm, img_info;
    Button ml, ml1, ml2;
    EditText txt_water;
    ArrayList<String> waterArray;
    String selectedMeasure = "Milliliter";
    int selectedmlVal;
    double selectedliVal = 0.0;
    WaterObject waterObject = null;
    boolean isSpinnerInitial = false;
    public static WaterObject selectedWaterObj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwatter);
        ButterKnife.bind(this);
        setupWindowAnimations();
        setToolbar();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                txt_water.setVisibility(View.VISIBLE);
            }
        }, 500);
        loadWaterMeasureSpinner();
        setButtonActions();

        if (selectedWaterObj != null) {
            waterObject = selectedWaterObj;
            txt_water.setText(waterObject.getWaterContent());
            selectedMeasure=waterObject.getWaterUnits();
            Log.e("gettingid", "call"+selectedMeasure);
            if (waterObject.getWaterUnits().equals("Milliliter")) {
                selectedmlVal=Integer.parseInt(waterObject.getWaterContent());
                txt_measure.setText("ml");
                txt_water.setHint("0 ml");
            } else {
                selectedliVal=Double.parseDouble(waterObject.getWaterContent());
                txt_measure.setText("li");
                txt_water.setHint("0 li");

            }
            loadButtonText(selectedMeasure);
            loadWaterMeasureSpinner();
            txt_water.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() > 0) {
                        if (!s.toString().contains("."))
                        {
                            int txt_val = Integer.parseInt(txt_water.getText().toString());
                            selectedmlVal = txt_val;
                        }else {
                            Log.e("doublreval","call"+txt_water.getText().toString());
                            double txt_val = Double.parseDouble(txt_water.getText().toString());
                            selectedliVal = txt_val;
                        }

                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    @OnClick({R.id.set})
    public void saveAction() {
        WaterObject waterObj = new WaterObject();
        waterObj.setWaterUnits(selectedMeasure);
        if (waterObject!=null){
            waterObj.setId(waterObject.getId());
        }else {
            waterObj.setId(WaterDataController.getInstance().fetchWaterData(DayFoodDataController.getInstance().currentFoodObject).size()+1);
        }
        waterObj.setAddedTime(DayFoodController.getInstance().gettingCurrentDate());
        if (selectedMeasure.equals("Milliliter")) {
            waterObj.setWaterContent(String.valueOf(selectedmlVal));
        } else {
            waterObj.setWaterContent(String.valueOf(selectedliVal));
        }
        Log.e("gettingmeasurevalue","cll"+waterObj.getWaterContent());
        loadWaterDataIntodb(WaterDataController.getInstance().fetchWaterData(DayFoodDataController.getInstance().currentFoodObject), waterObj);
        finish();

    }

    private void loadWaterDataIntodb(ArrayList<WaterObject> arrayList, WaterObject waterObj) {
        if (waterObject != null) {
            if (arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getWaterContent().equals(waterObject.getWaterContent())) {
                       // int index = arrayList.indexOf(waterObject);
                        if (waterObject.getId()==waterObj.getId()) {
                            Log.e("notsame", "call");
                          //  arrayList.add(waterObj);
                            WaterDataController.getInstance().updateWaterData(waterObj);
                        } else {
                            Log.e("same", "call");
                           // arrayList.set(index, waterObj);
                            WaterDataController.getInstance().insertWaterData(waterObj,DayFoodDataController.getInstance().currentFoodObject);
                        }
                    }
                }
            }
        } else {
            WaterDataController.getInstance().insertWaterData(waterObj,DayFoodDataController.getInstance().currentFoodObject);
            //arrayList.add(waterObj);
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tool_text = (TextView) toolbar.findViewById(R.id.toolbar_text);
        tool_text.setText("Add Water");
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        imag_alarm = (ImageView) toolbar.findViewById(R.id.img_share);
        img_info = (ImageView) toolbar.findViewById(R.id.img_refresh);
        imag_alarm.setBackgroundResource(R.drawable.ic_alarm);
        img_info.setBackgroundResource(R.drawable.ic_info);
        txt_water = (EditText) findViewById(R.id.water);
        txt_measure = (TextView) findViewById(R.id.txt_measure);

        ml = (Button) findViewById(R.id.ml);
        ml1 = (Button) findViewById(R.id.ml1);
        ml2 = (Button) findViewById(R.id.ml2);

        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setButtonActions() {

        ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadfirstButtonData();
            }
        });
        ml1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = ml1.getText().toString();
                String[] a = buttonText.split(" ");
                if (buttonText.contains("ml")) {
                    if (txt_water.getText().toString().length() > 0) {
                        int txt_val = Integer.parseInt(txt_water.getText().toString());
                        Log.e("txt_val", "call" + txt_val);
                        selectedmlVal = txt_val;
                        selectedmlVal = selectedmlVal + Integer.parseInt(a[1]);
                        Log.e("chanduty", "call" + selectedmlVal);
                        txt_water.setText(String.valueOf(selectedmlVal));
                    } else {
                        selectedmlVal = 500;
                        txt_water.setText("500");
                    }
                } else {
                    if (txt_water.getText().toString().length() > 0) {
                        selectedliVal = selectedliVal + Double.parseDouble(a[1]);
                        Log.e("chanduty", "call" + selectedliVal);
                        txt_water.setText(String.valueOf(selectedliVal));
                    } else {
                        selectedliVal = 0.5;
                        txt_water.setText("0.5");
                    }
                }


            }
        });
        ml2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = ml2.getText().toString();
                String[] a = buttonText.split(" ");
                if (buttonText.contains("ml")) {
                    if (txt_water.getText().toString().length() > 0) {
                        int txt_val = Integer.parseInt(txt_water.getText().toString());
                        selectedmlVal = txt_val;
                        selectedmlVal = selectedmlVal + Integer.parseInt(a[1]);
                        Log.e("chanduty", "call" + selectedmlVal);
                        txt_water.setText(String.valueOf(selectedmlVal));
                    } else {
                        selectedmlVal = 1000;
                        txt_water.setText("1000");
                    }
                } else {
                    Log.e("licalled", "call" + Double.parseDouble(a[1]));
                    if (txt_water.getText().toString().length() > 0) {
                        selectedliVal = selectedliVal + Double.parseDouble(a[1]);
                        Log.e("licalled", "call" + selectedliVal);
                        txt_water.setText(String.valueOf(selectedliVal));
                    } else {
                        selectedliVal = 1.0;
                        txt_water.setText("1.0");
                    }
                }

            }
        });

    }
    public void loadfirstButtonData()
    {
        String buttonText = ml.getText().toString();
        String[] a = buttonText.split(" ");
        if (buttonText.contains("ml")) {
            if (txt_water.getText().toString().length() > 0) {
                int txt_val = Integer.parseInt(txt_water.getText().toString());
                selectedmlVal = txt_val;
                selectedmlVal = selectedmlVal + Integer.parseInt(a[1]);
                Log.e("chanduty", "call" + selectedmlVal);
                txt_water.setText(String.valueOf(selectedmlVal));
            } else {
                selectedmlVal = 250;
                txt_water.setText("250");
            }
        } else {

            if (txt_water.getText().toString().length() > 0) {
                selectedliVal = selectedliVal + Double.parseDouble(a[1]);
                Log.e("chanduty", "call" + selectedliVal);
                txt_water.setText(String.valueOf(selectedliVal));
            } else {
                selectedliVal = 0.25;
                txt_water.setText("0.25");
            }
        }
    }

    private double convertMilliletersTOLiters(int milliVal) {
        Log.e("millileater", "call" + milliVal);
        double liters = milliVal / 1000.0;
        Log.e("liters", "calll" + liters);
        return liters;
    }

    private int convertLitersToMillileters(int liters) {
        int millil = liters * 1000;
        Log.e("millil", "calll" + millil);
        return millil;
    }

    private void loadWaterMeasureSpinner() {
        waterArray = new ArrayList<>();
        waterArray.add("Milliliter");
        waterArray.add("Liter");
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinnerwater);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, waterArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedMeasure != null) {
            Log.e("ddfdfddd", "call"+selectedMeasure);
            int spinnerPosition = aa.getPosition(selectedMeasure);
            spin.setSelection(spinnerPosition);
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isSpinnerInitial) {
                    selectedMeasure = waterArray.get(i);
                    txt_measure.setText(selectedMeasure);
                    loadButtonText(selectedMeasure);
                    if (selectedMeasure == "Milliliter") {
                        String v = txt_water.getText().toString();
                        txt_measure.setText("ml");
                        if (v.contains(".")) {
                            int d = (int) (Double.parseDouble(v) * 1000);
                            selectedmlVal = d;
                            txt_water.setText(String.valueOf(selectedmlVal));
                        } else {
                            int li = convertLitersToMillileters(selectedmlVal);
                            txt_water.setText(String.valueOf(li));
                        }
                    } else {
                        double d = convertMilliletersTOLiters(selectedmlVal);
                        txt_water.setText(String.valueOf(d));
                        selectedliVal = d;
                        txt_measure.setText("li");
                    }
                } else {
                    isSpinnerInitial = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadButtonText(String measure) {
        if (measure.equals("Milliliter")) {
            Log.e("measure", "call" + measure);
            ml.setText("+ 250 ml");
            ml1.setText("+ 500 ml");
            ml2.setText("+ 1000 ml");
        } else {
            ml.setText("+ 0.25 li");
            ml1.setText("+ 0.5 li");
            ml2.setText("+ 1.0 li");
        }
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
