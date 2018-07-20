package com.vedas.weightloss.Models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by VEDAS on 6/13/2018.
 */

public class WaterObject {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(generatedId = true,columnName = "water_id")
    int id;

    @DatabaseField(columnName = "waterContent")
    String   waterContent;

    @DatabaseField(columnName = "waterUnits")
    String waterUnits;

    @DatabaseField(columnName = "addedTime")
    String addedTime;

    @DatabaseField(columnName = "food_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public DayFoodObject dayFoodObject;


    public DayFoodObject getDayFoodObject() {
        return dayFoodObject;
    }

    public void setDayFoodObject(DayFoodObject dayFoodObject) {
        this.dayFoodObject = dayFoodObject;
    }

    public String getWaterContent() {
        return waterContent;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }

    public void setWaterContent(String waterContent) {
        this.waterContent = waterContent;
    }

    public String getWaterUnits() {
        return waterUnits;
    }

    public void setWaterUnits(String waterUnits) {
        this.waterUnits = waterUnits;
    }

}
