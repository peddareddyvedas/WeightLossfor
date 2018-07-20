package com.vedas.weightloss.MoreModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vedas.weightloss.DataBase.UnitsDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.Models.UnitsObject;
import com.vedas.weightloss.R;

import java.util.ArrayList;

/**
 * Created by Rise on 12/06/2018.
 */

public class UnitsActivity extends AppCompatActivity {
    ArrayList<String> kilogramArray;
    String kilogram = "Kilogram";
    ArrayList<String> feetArray;
    String feet = "Feet&Inch";
    ArrayList<String> kilometerArray;
    String kilometer = "Kilometer";
    ArrayList<String> literArray;
    String liter = "ml";
    Toolbar toolbar;
    ImageView btn_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units);
        setToolbar();
        loadkilogramSpinner();
        loadfeetSpinner();
        loadkilometerSpinner();
        loadliterSpinner();

    }
    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnitsObject objUnits=new UnitsObject();
                objUnits.setWaterUnits(liter);
                objUnits.setHeightUnits(feet);
                objUnits.setWeightUnits(kilogram);
                objUnits.setDistanceUnits(kilometer);
                objUnits.setId(UnitsDataController.getInstance().fetchUnitsData(UserDataController.getInstance().currentUser).size()+1);
                UnitsDataController.getInstance().insertunitsData(objUnits,UserDataController.getInstance().currentUser);
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbartext.append("Units");
    }

    /////// load Kilogram Spimnner//////////
    private void loadkilogramSpinner() {
        kilogramArray = new ArrayList<>();
        kilogramArray.add("Kilogram");
        kilogramArray.add("Grams");
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, kilogramArray);
        aa.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(aa);
        if (kilogram != null) {
            int spinnerPosition = aa.getPosition(kilogram);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kilogram = kilogramArray.get(i);
                // text.setText(selectedGender);
                Log.e("selectedkg", "call" + kilogram);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //////////// load feet spinner////
    private void loadfeetSpinner() {
        feetArray = new ArrayList<>();
        feetArray.add("Feet&Inch");
        feetArray.add("Cm");
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, feetArray);
        aa.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(aa);
        if (feet != null) {
            int spinnerPosition = aa.getPosition(feet);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                feet = feetArray.get(i);
                // text.setText(selectedGender);
                Log.e("selectefeet", "call" + feet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    ////////////// load kilometer spinner/////////////
    private void loadkilometerSpinner() {
        kilometerArray = new ArrayList<>();
        kilometerArray.add("Kilometer");
        kilometerArray.add("Meter");
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, kilometerArray);
        aa.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(aa);
        if (kilometer != null) {
            int spinnerPosition = aa.getPosition(kilometer);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kilometer = kilometerArray.get(i);
                // text.setText(selectedGender);
                Log.e("selectekilometer", "call" + kilometer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void loadliterSpinner() {
        literArray = new ArrayList<>();
        literArray.add("ml");
        literArray.add("liters");
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, literArray);
        aa.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(aa);
        if (liter != null) {
            int spinnerPosition = aa.getPosition(liter);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                liter = literArray.get(i);
                Log.e("selecteliter", "call" + liter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
