package com.vedas.weightloss.DataBase;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.User;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */

public class BreakfastDataController  {
    public static BreakfastDataController myObj;

    public static BreakfastDataController getInstance() {
        if (myObj == null) {
            myObj = new BreakfastDataController();
        }

        return myObj;
    }

    public void insertBreakfastData(BreakfastObject breakfastObject, DayFoodObject objFood) {
        breakfastObject.setDayFoodObject(objFood);
        try {
            UserDataController.getInstance().helper.getbreakfastInfoDao().create(breakfastObject);
            fetchBreakfastData(objFood);
            Log.e("breakfastname","call" + breakfastObject.getFoodName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BreakfastObject>  fetchBreakfastData(DayFoodObject foodObject) {
        if (foodObject!=null){
            ArrayList<BreakfastObject> breakFastArray =
                    new ArrayList<BreakfastObject>(foodObject.getBreakfastdata());
            Log.e("breakfastarray","call" + breakFastArray.size());
            return  breakFastArray;
        }
        return null;
    }

    public void updateBreakfastData(BreakfastObject breakfastObject) {
        try {
            UpdateBuilder<BreakfastObject, Integer> updateBuilder = UserDataController.getInstance().helper.getbreakfastInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("foodname", breakfastObject.getFoodName());
            updateBuilder.updateColumnValue("servingsize", breakfastObject.getServingSize());
            updateBuilder.updateColumnValue("calories", breakfastObject.getEsimatedCalories());
            updateBuilder.updateColumnValue("addedtime", breakfastObject.getAddedTime());
            updateBuilder.where().eq("foodname", breakfastObject.getFoodName());
            updateBuilder.update();
            Log.e("update data", "updated breakfast sucessfully");
            Log.e("update data", "call"+breakfastObject.getFoodName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBreakfastData(ArrayList<BreakfastObject> breakfastObjects) {
        try {
            UserDataController.getInstance().helper.getbreakfastInfoDao().delete(breakfastObjects);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteBreakfastDataForPosition(int position,DayFoodObject dayFoodObject) {
        try {
            BreakfastObject breakfastObject=fetchBreakfastData(dayFoodObject).get(position);
            UserDataController.getInstance().helper.getbreakfastInfoDao().delete(breakfastObject);
            Log.e("deletebreakdata", "sucessfully");
            fetchBreakfastData(dayFoodObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


