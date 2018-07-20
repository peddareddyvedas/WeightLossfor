package com.vedas.weightloss.Models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by VEDAS on 6/13/2018.
 */

public class SnacksObject {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(columnName = "foodname")
    String foodName;

    @DatabaseField(columnName = "servingsize")
    String servingSize;

    @DatabaseField(columnName = "calories")
    String esimatedCalories;

    @DatabaseField(columnName = "addedtime")
    String addedTime;

    @DatabaseField(columnName = "food_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public DayFoodObject dayFoodObject;


    public DayFoodObject getDayFoodObject() {
        return dayFoodObject;
    }

    public void setDayFoodObject(DayFoodObject dayFoodObject) {
        this.dayFoodObject = dayFoodObject;
    }


    public SnacksObject() {

    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }


    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    String units;


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public String getEsimatedCalories() {
        return esimatedCalories;
    }

    public void setEsimatedCalories(String esimatedCalories) {
        this.esimatedCalories = esimatedCalories;
    }


}
