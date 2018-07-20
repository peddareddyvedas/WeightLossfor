package com.vedas.weightloss.Models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by VEDAS on 6/13/2018.
 */

public class UnitsObject {

    @DatabaseField(generatedId = true,columnName = "unitsid")
    int id;

    @DatabaseField(columnName = "weightUnits")
    String weightUnits;

    @DatabaseField(columnName = "heightUnits")
    String heightUnits;

    @DatabaseField(columnName = "waterUnits")
    String waterUnits;

    @DatabaseField(columnName = "distanceUnits")
    String distanceUnits;

    @DatabaseField(columnName = "userid", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeightUnits() {
        return weightUnits;
    }

    public void setWeightUnits(String weightUnits) {
        this.weightUnits = weightUnits;
    }

    public String getHeightUnits() {
        return heightUnits;
    }

    public void setHeightUnits(String heightUnits) {
        this.heightUnits = heightUnits;
    }

    public String getWaterUnits() {
        return waterUnits;
    }

    public void setWaterUnits(String waterUnits) {
        this.waterUnits = waterUnits;
    }

    public String getDistanceUnits() {
        return distanceUnits;
    }

    public void setDistanceUnits(String distanceUnits) {
        this.distanceUnits = distanceUnits;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
