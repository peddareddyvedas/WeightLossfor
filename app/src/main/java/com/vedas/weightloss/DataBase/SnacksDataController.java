package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.SnacksObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */

public class SnacksDataController {
    public static SnacksDataController myObj;

    public static SnacksDataController getInstance() {
        if (myObj == null) {
            myObj = new SnacksDataController();
        }

        return myObj;
    }

    public void insertSnacksData(SnacksObject snacksObject,DayFoodObject dayFoodObject) {
        snacksObject.setDayFoodObject(dayFoodObject);
        try {
            UserDataController.getInstance().helper.getSnacksInfoDao().create(snacksObject);
            fetchSnacksData(dayFoodObject);
            Log.e("dinnername","call" + snacksObject.getFoodName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<SnacksObject> fetchSnacksData(DayFoodObject dayFoodObject) {
        if (dayFoodObject!=null){
            ArrayList<SnacksObject> SnacksObjectList = new ArrayList<SnacksObject>(DayFoodDataController.getInstance().currentFoodObject.getSnacksdata());
            Log.e("fetching", "SnacksObjectList successfully" + SnacksObjectList.size());
            return SnacksObjectList;
        }
        return null;
    }

    public void updateSnacksData(SnacksObject snacksObject) {
        try {
            UpdateBuilder<SnacksObject, Integer> updateBuilder = UserDataController.getInstance().helper.getSnacksInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("foodname", snacksObject.getFoodName());
            updateBuilder.updateColumnValue("servingsize", snacksObject.getServingSize());
            updateBuilder.updateColumnValue("calories", snacksObject.getEsimatedCalories());
            updateBuilder.updateColumnValue("addedtime", snacksObject.getAddedTime());
            updateBuilder.where().eq("foodname", snacksObject.getFoodName());
            updateBuilder.update();
            Log.e("update data", "updated dinner sucessfully");
            Log.e("update data", "call"+snacksObject.getFoodName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteSnacksDataForPosition(int position,DayFoodObject dayFoodObject) {
        try {
            SnacksObject breakfastObject=fetchSnacksData(dayFoodObject).get(position);
            UserDataController.getInstance().helper.getSnacksInfoDao().delete(breakfastObject);
            Log.e("deletesnacksdata", "sucessfully");
            fetchSnacksData(dayFoodObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteSnacksData(ArrayList<SnacksObject> dinnerObject) {
        try {
            UserDataController.getInstance().helper.getSnacksInfoDao().delete(dinnerObject);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


