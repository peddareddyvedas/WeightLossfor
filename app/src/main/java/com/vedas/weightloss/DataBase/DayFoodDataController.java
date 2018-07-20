package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.ExerciseObject;
import com.vedas.weightloss.Models.User;
import com.vedas.weightloss.Models.WaterObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */

public class DayFoodDataController {
    public ArrayList<DayFoodObject> allDayfoodArray ;
    public DayFoodObject currentFoodObject;

    public static DayFoodDataController myObj;

    public static DayFoodDataController getInstance() {
        if (myObj == null) {
            myObj = new DayFoodDataController();
        }

        return myObj;
    }

    public void insertDayFoodData(DayFoodObject dayFoodObject, User currentUser) {
        dayFoodObject.setUser(currentUser);
        try {
            UserDataController.getInstance().helper.getdayFoodInfoDao().create(dayFoodObject);
            fetchdayfoodData();
            Log.e("insertfood", "call" + dayFoodObject.date);
            Log.e("fetc", "" + allDayfoodArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DayFoodObject> fetchdayfoodData() {
        allDayfoodArray = null;
        allDayfoodArray = new ArrayList<>();

        if (UserDataController.getInstance().currentUser!=null){
            allDayfoodArray = new ArrayList<DayFoodObject>
                    (UserDataController.getInstance().currentUser.getDayFooddata());
            /*if (allDayfoodArray.size() > 0) {
                currentFoodObject = allDayfoodArray.get(0);
                Log.e("currentfood", "call" + currentFoodObject.weight);
            }*/
        }
        Log.e("fetching", "dayfood fectched successfully" + allDayfoodArray.size());
        return allDayfoodArray;
    }

    public void updateDayFoodData(DayFoodObject dayFoodObject) {
        try {
            UpdateBuilder<DayFoodObject, Integer> updateBuilder = UserDataController.getInstance().helper.getdayFoodInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("dayName", dayFoodObject.dayName);
            updateBuilder.updateColumnValue("date", dayFoodObject.date);
            updateBuilder.updateColumnValue("breakFastCalories", dayFoodObject.breakFastCalories);
            updateBuilder.updateColumnValue("lunchCalories", dayFoodObject.lunchCalories);
            updateBuilder.updateColumnValue("calories", dayFoodObject.dinnerCalories);
            updateBuilder.updateColumnValue("snacksCalories", dayFoodObject.snacksCalories);
            updateBuilder.updateColumnValue("targetCalories", dayFoodObject.targetCalories);
            updateBuilder.updateColumnValue("burnedCalories", dayFoodObject.burnedCalories);
            updateBuilder.updateColumnValue("weight", dayFoodObject.weight);
            updateBuilder.where().eq("date", dayFoodObject.date);
            updateBuilder.update();

            Log.e("updatefood", "updated dayfood sucessfully");
            Log.e("updatefood", "call"+dayFoodObject.date);
            Log.e("updatefood", "call"+dayFoodObject.breakFastCalories);
            Log.e("updatefood", "call"+dayFoodObject.weight);
            fetchdayfoodData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletedayfooddataData(ArrayList<DayFoodObject> dayFoodObjects) {
        try {
            UserDataController.getInstance().helper.getdayFoodInfoDao().delete(dayFoodObjects);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletedayfooddataDataForPosition(ArrayList<DayFoodObject> dayFoodObjects) {
        try {
            UserDataController.getInstance().helper.getdayFoodInfoDao().delete(dayFoodObjects);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


