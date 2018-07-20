package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.DinnerObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */

public class DinnerDataController {
    public static DinnerDataController myObj;

    public static DinnerDataController getInstance() {
        if (myObj == null) {
            myObj = new DinnerDataController();
        }

        return myObj;
    }

    public void insertDinnerData(DinnerObject lunchObject, DayFoodObject objFood) {
        lunchObject.setDayFoodObject(objFood);
        try {
            UserDataController.getInstance().helper.getDinnerInfoDao().create(lunchObject);
            fetchdinnerData(objFood);
            Log.e("dinnername","call" + lunchObject.getFoodName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DinnerObject> fetchdinnerData(DayFoodObject dayFoodObject) {

        if (dayFoodObject!=null){
            ArrayList<DinnerObject> bdinnerModelsList = new ArrayList<DinnerObject>(dayFoodObject.getDinnerdata());
            Log.e("calldinnerlist", "call" + bdinnerModelsList.size());
            return  bdinnerModelsList;
        }
        return null;
    }

    public void updateDinnerData(DinnerObject lunchObject) {
        try {
            UpdateBuilder<DinnerObject, Integer> updateBuilder = UserDataController.getInstance().helper.getDinnerInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("foodname", lunchObject.getFoodName());
            updateBuilder.updateColumnValue("servingsize", lunchObject.getServingSize());
            updateBuilder.updateColumnValue("calories", lunchObject.getEsimatedCalories());
            updateBuilder.updateColumnValue("addedtime", lunchObject.getAddedTime());
            updateBuilder.where().eq("foodname", lunchObject.getFoodName());
            updateBuilder.update();
            Log.e("update data", "updated dinner sucessfully");
            Log.e("update data", "call"+lunchObject.getFoodName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteDinnerDataForPosition(int position,DayFoodObject dayFoodObject) {
        try {
            DinnerObject breakfastObject=fetchdinnerData(dayFoodObject).get(position);
            UserDataController.getInstance().helper.getDinnerInfoDao().delete(breakfastObject);
            Log.e("deletebreakdata", "sucessfully");
            fetchdinnerData(dayFoodObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteDinnerData(ArrayList<DinnerObject> dinnerObject) {
        try {
            UserDataController.getInstance().helper.getDinnerInfoDao().delete(dinnerObject);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


