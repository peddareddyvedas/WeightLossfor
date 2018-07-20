package com.vedas.weightloss.Settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by VEDAS on 5/5/2018.
 */
public class PersonalInfoNextActivity extends BaseDetailActivity {
    int type;
    TextInputEditText textInputHeight, textInputWeight;
    ArrayList<String> weightMeasures = new ArrayList<>();
    String selectedMeasure = "Kg";
    String selectedWeigthValue = "29";
    String selectedWeight;
    TextView bmrResult;
    /*For height*/
    ArrayList<String> heightMeasures = new ArrayList<>();
    String selectedHeightMeasure = "Cm";
    String selectedFeetVal = "3", selectedInchValue = "0", selectedCmValue = "93";
    String mAge, mGender;
    PieChart pieChart, bmrPiechart;
    protected String[] BMIcategory = new String[]
            {
                    "Very Severly Underweight", "Severly Underweight", "Underweight", "Normal",
                    "Overweight", "Obese Class I", "Obese Class II", "Obese Class III",
            };
    protected String[] BMRcategory = new String[]
            {
                    "Liver", "Brain", "Skeletal Muscle", "Kidneys",
                    "Heart", "Other organs",
            };
    private LinearLayout l1, l2, l3, l4, l5, l6, l7, l8;
    RelativeLayout rl_mainView, rl_webView;
    private TextView t1, t2, t3, t4, t5, t6, t7, t8;
    String bmrValue = "";
    WebView webView;
    WebView simpleWebView;
    Button btn_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo_next);
        ButterKnife.bind(this);
        setupToolbar();
        setupWindowAnimations();
        PersonalInfoController.getInstance().loadWeightValuesArray();
        PersonalInfoController.getInstance().loadHeightValuesArray();
        textInputHeight = (TextInputEditText) findViewById(R.id.height);
        textInputWeight = (TextInputEditText) findViewById(R.id.dob);
        /*textInputBMI = (TextInputEditText) findViewById(R.id.loc);
        textInputBMI.setShowSoftInputOnFocus(false);*/
        rl_mainView = (RelativeLayout) findViewById(R.id.rl_mainview);
        rl_webView = (RelativeLayout) findViewById(R.id.rl_webview);
        bmrResult = (TextView) findViewById(R.id.bmrresult);
        pieChart = (PieChart) findViewById(R.id.piechart);
        webView = (WebView) findViewById(R.id.webview);
        btn_info = (Button) findViewById(R.id.info);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            mAge = (String) bd.get("age");
            mGender = (String) bd.get("gender");
            Log.e("agegender", "call" + mAge + "" + mGender);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textInputHeight.setVisibility(View.VISIBLE);
                textInputWeight.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.VISIBLE);
            }
        }, 1000);
        loadTextfeildActions();
        loadBMIPieChart();
        loadBMRPiechart();

        simpleWebView = (WebView) findViewById(R.id.webview);
        // specify the url of the web page in loadUrl function
        Animation slideRightAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.layoutanimation_falldown);
        simpleWebView.startAnimation(slideRightAnimation);
        simpleWebView.loadUrl("file:///android_asset/webview.html");
        setUserdata();
        // checkIsUserHavingData();

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_mainView.setVisibility(View.GONE);
                rl_webView.setVisibility(View.VISIBLE);
                if (rl_webView.isShown()) {
                    btn_info.setClickable(false);
                    Animation slideRightAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.layoutanimation_falldown);
                    simpleWebView.startAnimation(slideRightAnimation);
                    simpleWebView.loadUrl("file:///android_asset/webview.html");
                }

            }
        });
    }

    @OnClick({R.id.btn_hide})
    public void hideInfo() {
        btn_info.setClickable(true);
        rl_mainView.setVisibility(View.VISIBLE);
        rl_webView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBMIPieChart();
        // checkIsUserHavingData();
        setUserdata();
    }

    private void setUserdata() {
        if (PersonalInfoController.getInstance().retrivePersonalDataFromUserDefaults()) {
            Log.e("selectedmodel", "call");
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getBirthday() != null) {
                String selectedDate[] = PersonalInfoController.getInstance().selectedPersonalInfoModel.getBirthday().split("/");
                //convert them to int
                Log.e("currente", "call" + PersonalInfoController.getInstance().selectedPersonalInfoModel.getBirthday());
                int mDay = Integer.valueOf(selectedDate[0]);
                int mMonth = Integer.valueOf(selectedDate[1]);
                int mYear = Integer.valueOf(selectedDate[2]);
                mAge = PersonalInfoController.getInstance().calculatingAge(mYear, mMonth, mDay);
            }

            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getHeight() != null) {
                textInputHeight.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getHeight());
            }
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getWeight() != null) {
                textInputWeight.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getWeight());
            }
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmi() != null) {
                pieChart.setCenterText("BMI:" + PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmi()); // set center text to piechart
                PersonalInfoController.getInstance().SetBackColorToLayouts(Double.parseDouble(PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmi()), mAge, PersonalInfoController.getInstance().selectedPersonalInfoModel.getGender(), pieChart, l1, l2, l3, l4, l5, l6, l7, l8);
                //calculatingBMR();
            }
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmr() != null) {
                bmrResult.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmr() + " " + "Kcal");
            }

        }
    }

   /* private void checkIsUserHavingData() {
        if (PersonalInfoDataController.getInstance().personalInfoArray.size() > 0) {
            Log.e("selectedmodel", "call" + PersonalInfoDataController.getInstance().personalInfoArray);
            PersonalInfoDataController.getInstance().fetchPersonalInfoData();
            objPersonalInfoModel = PersonalInfoDataController.getInstance().currentMember;
            *//*pieChart.setCenterText("BMI:" + objPersonalInfoModel.getBmi()); // set center text to piechart
            bmrResult.setText(objPersonalInfoModel.getBmr() + " " + "Kcal");
           *//*
            textInputHeight.setText(objPersonalInfoModel.getHeight());
            textInputWeight.setText(objPersonalInfoModel.getWeight());
            String selectedDate[] = objPersonalInfoModel.getBirthday().split("/");
            //convert them to int
            Log.e("currente", "call" + objPersonalInfoModel.getBirthday());
            int mDay = Integer.valueOf(selectedDate[0]);
            int mMonth = Integer.valueOf(selectedDate[1]);
            int mYear = Integer.valueOf(selectedDate[2]);
            mAge = PersonalInfoController.getInstance().calculatingAge(mYear, mMonth, mDay);
            PersonalInfoController.getInstance().SetBackColorToLayouts(Double.parseDouble(objPersonalInfoModel.getBmi()), mAge, objPersonalInfoModel.getGender(), pieChart, l1, l2, l3, l4, l5, l6, l7, l8);
            calculatingBMR();
        }
    }*/

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        textView.setText("You");
        Button btn_back = (Button) toolbar.findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.centerImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View contextView = findViewById(R.id.context_view);
                if (textInputHeight.getText().toString().length() > 0) {
                    if (textInputWeight.getText().toString().length() > 0) {
                       /* PersonalInfoController.getInstance().selectedPersonalInfoModel.setHeight(textInputHeight.getText().toString());
                        PersonalInfoController.getInstance().selectedPersonalInfoModel.setWeight(textInputWeight.getText().toString());
*/
                        Intent i = new Intent(PersonalInfoNextActivity.this, ActivityLevelActivity.class);
                        i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        Log.e("bmrValue", "" + bmrValue);
                        i.putExtra(bmrValue, "bmrValue");
                        transitionTo(i);
                    } else {
                        Snackbar.make(contextView, R.string.selectitem2, Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.CYAN)
                                .setAction(R.string.undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }

                                }).show();
                    }
                } else {
                    Snackbar.make(contextView, R.string.selectitem1, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.CYAN)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }

                            }).show();
                }

            }
        });
    }

    private void loadTextfeildActions() {
        l1 = (LinearLayout) findViewById(R.id.linearlayout1);
        l2 = (LinearLayout) findViewById(R.id.linearlayout2);
        l3 = (LinearLayout) findViewById(R.id.linearlayout3);
        l4 = (LinearLayout) findViewById(R.id.linearlayout4);
        l5 = (LinearLayout) findViewById(R.id.linearlayout5);
        l6 = (LinearLayout) findViewById(R.id.linearlayout6);
        l7 = (LinearLayout) findViewById(R.id.linearlayout7);
        l8 = (LinearLayout) findViewById(R.id.linearlayout8);

        t1 = (TextView) findViewById(R.id.text1);
        t2 = (TextView) findViewById(R.id.text2);
        t3 = (TextView) findViewById(R.id.text3);
        t4 = (TextView) findViewById(R.id.text4);
        t5 = (TextView) findViewById(R.id.text5);
        t6 = (TextView) findViewById(R.id.text6);
        t7 = (TextView) findViewById(R.id.text7);
        t8 = (TextView) findViewById(R.id.text8);
        t1.setText("<16.0");

        textInputHeight.setShowSoftInputOnFocus(false);
        textInputHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textInputHeight.getWindowToken(), 0);
                    loadHeightMeasureSpinner();
                }

            }
        });
        textInputHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textInputHeight.getWindowToken(), 0);
                loadHeightMeasureSpinner();
            }
        });

        textInputWeight.setShowSoftInputOnFocus(false);
        textInputWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (hasfocus) {
                    loadWeightMeasuresSpinner();
                }
            }
        });
        textInputWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWeightMeasuresSpinner();
            }
        });

    }

    private void loadBMRPiechart() {
        bmrPiechart = (PieChart) findViewById(R.id.piechartbmr);
        bmrPiechart.setUsePercentValues(false);
        bmrPiechart.getDescription().setEnabled(false);
        bmrPiechart.setDrawCenterText(false);
        bmrPiechart.setDrawSliceText(false);
        bmrPiechart.setRotationEnabled(false);
        bmrPiechart.setHighlightPerTapEnabled(false);
        bmrPiechart.setMaxAngle(360f); // HALF CHART
        bmrPiechart.setRotationAngle(135f);
        bmrPiechart.getLegend().setEnabled(false);
        setBMRChartData();
        bmrPiechart.setDrawHoleEnabled(false);
        bmrPiechart.animateX(1000);
        bmrPiechart.invalidate();

    }

    private void setBMRChartData() {
        ArrayList<PieEntry> values = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(values, "BMI Category");
        ArrayList<Integer> colors = new ArrayList<Integer>();

        values.add(new PieEntry(27f, BMRcategory[0], BMRcategory[0]));
        values.add(new PieEntry(19f, BMRcategory[1], BMRcategory[1]));
        values.add(new PieEntry(18f, BMRcategory[2], BMRcategory[2]));
        values.add(new PieEntry(10f, BMRcategory[3], BMRcategory[3]));
        values.add(new PieEntry(7f, BMRcategory[4], BMRcategory[4]));
        values.add(new PieEntry(19f, BMRcategory[5], BMRcategory[5]));

        colors.add(Color.rgb(31, 127, 218));
        colors.add(Color.rgb(37, 169, 254));
        colors.add(Color.rgb(63, 200, 247));
        colors.add(Color.rgb(65, 189, 103));
        colors.add(Color.rgb(243, 207, 0));
        colors.add(Color.rgb(248, 154, 20));

        dataSet.setColors(colors);
        dataSet.setSliceSpace(0);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.parseColor("#ffffff"));
        data.setValueTextSize(10f);

        bmrPiechart.setDrawEntryLabels(true);
        bmrPiechart.setData(data);
        bmrPiechart.invalidate();
    }

    private void loadBMIPieChart() {
        pieChart = (PieChart) findViewById(R.id.piechart);
        //pieChart.setBackgroundColor(Color.rgb(255, 255, 255));
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(18f);
        pieChart.setCenterText("BMI:0.0");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.rgb(255, 255, 255));
        //pieChart.setTransparentCircleColor(Color.rgb(255, 255, 255));
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(65f);
        pieChart.setTransparentCircleRadius(68f);
        pieChart.setDrawSliceText(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(false);
        pieChart.setMaxAngle(270f); // HALF CHART
        pieChart.setRotationAngle(135f);
        pieChart.getLegend().setEnabled(false);
        setData(SetDatasetEntries(), 80);
        pieChart.animateX(1000);
        pieChart.invalidate();
        PersonalInfoController.getInstance().ChangeText(mGender, Integer.parseInt(mAge), t1, t2, t3, t4, t5, t6, t7, t8);
    }

    public int SetDatasetEntries() {

        if (mAge.length() != 0) {
            if (Integer.parseInt(mAge) >= 1 && Integer.parseInt(mAge) <= 6)
                return 8;
            else if (Integer.parseInt(mAge) >= 7 && Integer.parseInt(mAge) <= 18)
                return 4;
            else return 8;
        }
        return 8;
    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> values = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(values, "BMI Category");
        ArrayList<Integer> colors = new ArrayList<Integer>();

        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        if (count == 8) {
            values.add(new PieEntry(10f, BMIcategory[0]));
            values.add(new PieEntry(15f, BMIcategory[1]));
            values.add(new PieEntry(8f, BMIcategory[2]));
            values.add(new PieEntry(12f, BMIcategory[3]));
            values.add(new PieEntry(5f, BMIcategory[4]));
            values.add(new PieEntry(5f, BMIcategory[5]));
            values.add(new PieEntry(15f, BMIcategory[6]));
            values.add(new PieEntry(10f, BMIcategory[7]));

            colors.add(Color.rgb(31, 127, 218));
            colors.add(Color.rgb(37, 169, 254));
            colors.add(Color.rgb(63, 200, 247));
            colors.add(Color.rgb(65, 189, 103));
            colors.add(Color.rgb(243, 207, 0));
            colors.add(Color.rgb(248, 154, 20));
            colors.add(Color.rgb(238, 87, 59));
            colors.add(Color.rgb(171, 18, 14));

        } else {
            values.add(new PieEntry(8f, BMIcategory[2]));
            values.add(new PieEntry(12f, BMIcategory[3]));
            values.add(new PieEntry(5f, BMIcategory[4]));
            values.add(new PieEntry(5f, BMIcategory[5]));

            colors.add(Color.rgb(63, 200, 247));
            colors.add(Color.rgb(65, 189, 103));
            colors.add(Color.rgb(243, 207, 0));
            colors.add(Color.rgb(248, 154, 20));
        }
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void loadHeightMeasureSpinner() {
        heightMeasures = new ArrayList<>();
        heightMeasures.add("Cm");
        heightMeasures.add("Feets & Inches");

        final Dialog mod = new Dialog(PersonalInfoNextActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.activity_height_picker);
        mod.setCanceledOnTouchOutside(true);
        mod.setCancelable(true);
        mod.show();
        mod.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        final RelativeLayout rl_feetinch = (RelativeLayout) mod.findViewById(R.id.rl_feetinch);
        final RelativeLayout rl_cm = (RelativeLayout) mod.findViewById(R.id.rl_cm);
        final TextView txt_set = (TextView) mod.findViewById(R.id.set);
        txt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mod.dismiss();
                if (selectedHeightMeasure.equals("Cm")) {
                    textInputHeight.setText(selectedCmValue + " cm");
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setHeight(textInputHeight.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                    calculatingBMR();
                } else {
                    textInputHeight.setText(selectedFeetVal + " feet " + selectedInchValue + " inch");
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setHeight(textInputHeight.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                    calculatingBMR();
                }

            }
        });
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) mod.findViewById(R.id.spinnerHeight);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, heightMeasures);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedHeightMeasure != null) {
            int spinnerPosition = aa.getPosition(selectedHeightMeasure);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedHeightMeasure = heightMeasures.get(i);
                Log.e("selectedHeightMeasure", "call" + selectedHeightMeasure);
                if (selectedHeightMeasure.equals("Cm")) {
                    rl_feetinch.setVisibility(View.GONE);
                    rl_cm.setVisibility(View.VISIBLE);
                    loadCmSpinnerValues(mod);
                } else {
                    rl_feetinch.setVisibility(View.VISIBLE);
                    rl_cm.setVisibility(View.GONE);
                    loadFeetAndInchSpinnerValues(mod);
                    Log.e("selectedFeetVal", "call" + selectedFeetVal + "call" + selectedInchValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadFeetAndInchSpinnerValues(final Dialog mod) {
        //feet spinner
        Spinner spin = (Spinner) mod.findViewById(R.id.feetspinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().feetArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        Log.e("selectedFeetVal", "call" + selectedFeetVal);

        if (selectedFeetVal != null) {
            Log.e("tempselectedFeetVal", "call" + selectedFeetVal);
            int spinnerPosition = aa.getPosition(selectedFeetVal);
            Log.e("indextempselectedFe", "call" + spinnerPosition);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFeetVal = PersonalInfoController.getInstance().feetArray.get(i);
                String selectedValue = PersonalInfoController.getInstance().convertFeetToCm(selectedFeetVal + " " + selectedInchValue);
                Log.e("convertCmValue", "call" + selectedValue);
                int index = PersonalInfoController.getInstance().cmArray.indexOf(selectedValue);
                selectedCmValue = PersonalInfoController.getInstance().cmArray.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //inch spinner
        Spinner inchspin = (Spinner) mod.findViewById(R.id.inchspinner);
        ArrayAdapter inchspinaa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().inchArray);
        inchspinaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchspin.setAdapter(inchspinaa);
        Log.e("selectedInchValue", "call" + selectedInchValue);
        if (selectedInchValue != null) {
            Log.e("tempselectedInchValue", "call" + selectedInchValue);
            int spinnerPos = inchspinaa.getPosition(selectedInchValue);
            Log.e("indextempselectedin", "call" + spinnerPos);
            inchspin.setSelection(spinnerPos);
        }
        inchspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedInchValue = PersonalInfoController.getInstance().inchArray.get(i);
                String selectedValue = PersonalInfoController.getInstance().convertFeetToCm(selectedFeetVal + " " + selectedInchValue);
                Log.e("convertCmValue", "call" + selectedValue);
                int index = PersonalInfoController.getInstance().cmArray.indexOf(selectedValue);
                selectedCmValue = PersonalInfoController.getInstance().cmArray.get(index);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadCmSpinnerValues(final Dialog mod) {
        //inch spinner
        Spinner inchspin = (Spinner) mod.findViewById(R.id.spinnercm);
        ArrayAdapter inchspinaa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().cmArray);
        inchspinaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchspin.setAdapter(inchspinaa);
        if (selectedCmValue != null) {
            int spinnerPosition = inchspinaa.getPosition(selectedCmValue);
            inchspin.setSelection(spinnerPosition);
        }
        inchspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCmValue = PersonalInfoController.getInstance().cmArray.get(i);
                String selectedfeeinch = PersonalInfoController.getInstance().convertCmToFeet(selectedCmValue);
                Log.e("selectedCmValue", "call" + selectedfeeinch);
                String feetinch[] = selectedfeeinch.split(" ");
                int index = PersonalInfoController.getInstance().feetArray.indexOf(feetinch[0]);
                int index1 = PersonalInfoController.getInstance().inchArray.indexOf(feetinch[1]);
                selectedFeetVal = PersonalInfoController.getInstance().feetArray.get(index);
                selectedInchValue = PersonalInfoController.getInstance().inchArray.get(index1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void loadWeightMeasuresSpinner() {
        weightMeasures = new ArrayList<>();
        weightMeasures.add("Kg");
        weightMeasures.add("lbs");
        final Dialog mod = new Dialog(PersonalInfoNextActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.activity_weight_picker);
        mod.setCanceledOnTouchOutside(true);
        mod.setCancelable(true);
        mod.show();
        mod.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        RelativeLayout rl_measure = (RelativeLayout) mod.findViewById(R.id.measure);
        final TextView selected_measure = (TextView) mod.findViewById(R.id.selected_type);
        final TextView txt_set = (TextView) mod.findViewById(R.id.set);
        txt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mod.dismiss();
                if (selectedWeight != null && selectedWeight.contains("Kg")) {
                    String array[] = selectedWeight.split(" ");
                    selectedWeigthValue = array[0];
                    textInputWeight.setText("" + selectedWeigthValue + " Kg");
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setWeight(textInputWeight.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                    calculatingBMR();

                } else {
                    String array[] = selectedWeight.split(" ");
                    selectedWeigthValue = array[0];
                    textInputWeight.setText("" + selectedWeigthValue + " Lbs");
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setWeight(textInputWeight.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                    calculatingBMR();


                }

            }
        });
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) mod.findViewById(R.id.spinner1);
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
                selected_measure.setText(selectedMeasure);
                loadWeigthSpinnerValues(mod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadWeigthSpinnerValues(mod);
    }

    private void loadWeigthSpinnerValues(final Dialog mod) {
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) mod.findViewById(R.id.spinner);
        if (selectedMeasure.equals("Kg")) {
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().kgArray);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(aa);
            if (textInputWeight.getText().toString() != null && selectedWeight != null && selectedWeight.contains("Kg")) {
                String array[] = textInputWeight.getText().toString().split(" ");
                selectedWeigthValue = array[0];
                Log.e("textValuekg", "call" + array[0]);
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            } else {
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            }

        } else {
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().lbsArray);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(aa);
            if (textInputWeight.getText().toString() != null && selectedWeight != null && selectedWeight.contains("Lbs")) {
                String array[] = textInputWeight.getText().toString().split(" ");
                selectedWeigthValue = array[0];
                Log.e("textValuekg", "call" + array[0]);
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            } else {
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            }
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
                    selectedWeigthValue = PersonalInfoController.getInstance().lbsArray.get(index);
                    Log.e("selectedWeigth", "call" + selectedWeigthValue);
                } else {
                    String selectedValue = PersonalInfoController.getInstance().lbsArray.get(i);
                    selectedWeight = selectedValue + " " + "Lbs";
                    double kgValue = new Double(PersonalInfoController.getInstance().lbsArray.get(i)) * 0.453592;
                    String selectedValue1 = String.valueOf(Math.round(kgValue));
                    int index = PersonalInfoController.getInstance().kgArray.indexOf(selectedValue1);
                    selectedWeigthValue = PersonalInfoController.getInstance().kgArray.get(index);
                    Log.e("selectedWeigth", "call" + selectedWeigthValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void calculatingBMR() {
        double KgValue = 0.0, cmValue = 0.0;
    /*formula
    For men:	BMR = 10 × weight(kg) + 6.25 × height(cm) - 5 × age  + 5
    For women:	BMR = 10 × weight(kg) + 6.25 × height(cm) - 5 × age  - 161*/
        if (mGender != null && mAge != null && textInputHeight.getText().toString() != null && textInputWeight.getText().toString() != null) {

            if (textInputWeight.getText().toString().contains("Lbs")) {
                String a[] = textInputWeight.getText().toString().split(" ");
                double kgValue = new Double(a[0]) * 0.453592;
                String selectedValue1 = String.valueOf(Math.round(kgValue));
                KgValue = Double.parseDouble(selectedValue1);
                Log.e("bmiWeigth", "call" + KgValue);
            } else {
                if (textInputWeight.getText().toString().length() > 0) {
                    String a[] = textInputWeight.getText().toString().split(" ");
                    KgValue = Double.parseDouble(a[0]);
                    Log.e("bmiWeigth1", "call" + KgValue);
                }
            }

            if (textInputHeight.getText().toString().contains("feet")) {
                String feetVal[] = textInputHeight.getText().toString().split(" ");
                String selectedValue = PersonalInfoController.getInstance().convertFeetToCm(feetVal[0] + " " + feetVal[2]);
                cmValue = Double.parseDouble(selectedValue);
                Log.e("bmiheigth", "call" + cmValue);
            } else {
                if (textInputHeight.getText().toString().length() > 0) {

                    String feetVal[] = textInputHeight.getText().toString().split(" ");
                    cmValue = Double.parseDouble(feetVal[0]);
                    Log.e("bmiHeigth1", "call" + cmValue);
                }
            }

            if (KgValue > 0.0 && cmValue > 0.0) {
                Log.e("kgandcmvalues", "call" + KgValue + cmValue);
                calculateBMIAndBMR(KgValue, cmValue);
            }

        }
    }

    private void calculateBMIAndBMR(double KgValue, double cmValue) {
        ///
        double bmiVal = 0.0;
        double age = Double.parseDouble(mAge);
        Log.e("callage", "call" + age);

        if (mGender.equals("Female")) {
            bmiVal = 10 * KgValue + 6.25 * cmValue - 5 * age - 161;
            Log.e("Femaledbmi", "call" + bmiVal);
            bmrValue = String.valueOf(bmiVal);
        } else {
            bmiVal = 10 * KgValue + 6.25 * cmValue - 5 * age + 5;
            Log.e("Maledbmi", "call" + bmiVal);
            bmrValue = String.valueOf(bmiVal);
        }
        //calculateBMI using Weight in kgs and height in cm
        final double bmi = (KgValue * 10000f) / (cmValue * cmValue);
        Log.e("bmical", "call" + bmi);
        final DecimalFormat df = new DecimalFormat("#0.0");
        final Handler handler = new Handler();
        pieChart.highlightValue(0, -1, false);// disable previous highloght index position
        pieChart.animateX(1000);
        final double finalBmiVal = bmiVal;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PersonalInfoController.getInstance().SetBackColorToLayouts(bmi, mAge, mGender, pieChart, l1, l2, l3, l4, l5, l6, l7, l8);
                PersonalInfoController.getInstance().ChangeText(mGender, Integer.parseInt(mAge), t1, t2, t3, t4, t5, t6, t7, t8);
                pieChart.setCenterText("BMI:" + df.format(bmi)); // set center text to piechart
                bmrResult.setText("" + df.format(finalBmiVal) + " " + "Kcal");
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setBmi(df.format(bmi));
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setBmr(bmrValue);
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
            }
        }, 500);

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
        /*Fade enterTransition = new Fade();
        enterTransition.excludeTarget(android.R.id.statusBarBackground, false);
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));*/
        Transition fade = new Explode();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        return fade;
    }
}
