package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.ExerciseObject;
import com.vedas.weightloss.Models.LunchObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */

public class LunchDataController {
    public static LunchDataController myObj;

    public static LunchDataController getInstance() {
        if (myObj == null) {
            myObj = new LunchDataController();
        }

        return myObj;
    }

    public void insertLunchData(LunchObject lunchObject,DayFoodObject objFood) {
        lunchObject.setDayFoodObject(objFood);
        try {
            UserDataController.getInstance().helper.getLunchInfoDao().create(lunchObject);
            fetchLunchData(objFood);
            Log.e("breakfastname","call" + lunchObject.getFoodName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LunchObject> fetchLunchData(DayFoodObject dayFoodObject) {
        if (dayFoodObject!=null){
            ArrayList<LunchObject> exerciseModelsList = new ArrayList<LunchObject>(DayFoodDataController.getInstance().currentFoodObject.getLunchdata());
            Log.e("fetching", "exerciseModelsList successfully" + exerciseModelsList.size());
            return exerciseModelsList;
        }
        return null;
    }
    public void updateLunchData(LunchObject lunchObject) {
        try {
            UpdateBuilder<LunchObject, Integer> updateBuilder = UserDataController.getInstance().helper.getLunchInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("foodname", lunchObject.getFoodName());
            updateBuilder.updateColumnValue("servingsize", lunchObject.getServingSize());
            updateBuilder.updateColumnValue("calories", lunchObject.getEsimatedCalories());
            updateBuilder.updateColumnValue("addedtime", lunchObject.getAddedTime());
            updateBuilder.where().eq("foodname", lunchObject.getFoodName());
            updateBuilder.update();
            Log.e("update data", "updated lunch sucessfully");
            Log.e("update data", "call"+lunchObject.getFoodName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteLunchDataForPosition(int position,DayFoodObject dayFoodObject) {
        try {
            LunchObject breakfastObject=fetchLunchData(dayFoodObject).get(position);
            UserDataController.getInstance().helper.getLunchInfoDao().delete(breakfastObject);
            Log.e("deletebreakdata", "sucessfully");
            fetchLunchData(dayFoodObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteLunchData(ArrayList<LunchObject> lunchObjects) {
        try {
            UserDataController.getInstance().helper.getLunchInfoDao().delete(lunchObjects);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


