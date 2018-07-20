package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.ExerciseObject;
import com.vedas.weightloss.Models.WaterObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */

public class WaterDataController {
    public static WaterDataController myObj;

    public static WaterDataController getInstance() {
        if (myObj == null) {
            myObj = new WaterDataController();
        }

        return myObj;
    }

    public void insertWaterData(WaterObject waterObject,DayFoodObject dayFoodObject) {
        waterObject.setDayFoodObject(dayFoodObject);
        try {
            UserDataController.getInstance().helper.getWaterInfoDao().create(waterObject);
            fetchWaterData(dayFoodObject);
            Log.e("watername","call" + waterObject.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<WaterObject> fetchWaterData(DayFoodObject dayFoodObject) {
        if (dayFoodObject!=null){
          ArrayList<WaterObject>  allWaterArray = new ArrayList<WaterObject>
                  (dayFoodObject.getWaterdata());
            return  allWaterArray;
        }
        return null;
    }

    public void updateWaterData(WaterObject waterObject) {
        try {
            UpdateBuilder<WaterObject, Integer> updateBuilder = UserDataController.getInstance().helper.getWaterInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("waterContent", waterObject.getWaterContent());
            updateBuilder.updateColumnValue("waterUnits", waterObject.getWaterUnits());
            updateBuilder.updateColumnValue("addedTime", waterObject.getAddedTime());
            updateBuilder.where().eq("water_id", waterObject.getId());
            updateBuilder.update();
            Log.e("update data", "updated water sucessfully");
            Log.e("update data", "call"+waterObject.getWaterUnits());
            Log.e("update data", "call"+waterObject.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteWaterDataForPosition(int position,DayFoodObject dayFoodObject) {
        try {
            WaterObject breakfastObject=fetchWaterData(dayFoodObject).get(position);
            UserDataController.getInstance().helper.getWaterInfoDao().delete(breakfastObject);
            Log.e("deleteexercisedata", "sucessfully");
            fetchWaterData(dayFoodObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteWaterData(ArrayList<WaterObject> waterObjects) {
        try {
            UserDataController.getInstance().helper.getWaterInfoDao().delete(waterObjects);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


