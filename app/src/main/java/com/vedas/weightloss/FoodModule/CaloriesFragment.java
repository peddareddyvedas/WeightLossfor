package com.vedas.weightloss.FoodModule;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.vedas.weightloss.R;

import java.util.ArrayList;


/**
 * Created by Vedas on 11/10/2016.
 */

public class CaloriesFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calories_fragment, container, false);
        init();

        return view;
    }

    private void init() {


        PieChart pieChart = (PieChart) view.findViewById(R.id.piechart);
        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(8f, 0));
        yvalues.add(new PieEntry(15f, 1));
        yvalues.add(new PieEntry(12f, 2));
        yvalues.add(new PieEntry(25f, 3));
        yvalues.add(new PieEntry(23f, 4));
        yvalues.add(new PieEntry(17f, 5));


        PieDataSet dataSet = new PieDataSet(yvalues, "sbabsansb");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
        xVals.add("April");
        xVals.add("May");
        xVals.add("June");

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setUsePercentValues(false);

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setCenterTextSize(18f);
        pieChart.setCenterText("299" + "\n" + "Calories");
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
        pieChart.setRotationAngle(135f);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateX(1000);
        pieChart.invalidate();

    }

    /*@Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.selectedmlVal("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.selectedmlVal("PieChart", "nothing selected");
    }*/

}