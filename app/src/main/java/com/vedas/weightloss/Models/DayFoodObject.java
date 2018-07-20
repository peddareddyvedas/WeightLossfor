package com.vedas.weightloss.Models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
/**
 * Created by VEDAS on 6/13/2018.
 */
public class DayFoodObject {

    public int getDayid() {
        return dayid;
    }

    public void setDayid(int dayid) {
        this.dayid = dayid;
    }

    @DatabaseField(generatedId = true)
    int dayid;

    @DatabaseField(columnName = "dayName")
    public String dayName;

    @DatabaseField(columnName = "date")
    public String date;

    @DatabaseField(columnName = "breakFastCalories")
    public String breakFastCalories;

    @DatabaseField(columnName = "lunchCalories")
    public String lunchCalories;

    @DatabaseField(columnName = "calories")
    public String dinnerCalories;

    @DatabaseField(columnName = "snacksCalories")
    public String snacksCalories;

    @DatabaseField(columnName = "targetCalories")
    public String targetCalories;

    @DatabaseField(columnName = "burnedCalories")
    public String burnedCalories;

    @DatabaseField(columnName = "weight")
    public String weight;


    @DatabaseField(columnName = "user_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private User user;

    @ForeignCollectionField
    private ForeignCollection<BreakfastObject> breakfastdata;

    @ForeignCollectionField
    private ForeignCollection<LunchObject> lunchdata;

    @ForeignCollectionField
    private ForeignCollection<DinnerObject> dinnerdata;

    @ForeignCollectionField
    private ForeignCollection<SnacksObject> snacksdata;

    @ForeignCollectionField
    private ForeignCollection<ExerciseObject> exercisedata;

    @ForeignCollectionField
    private ForeignCollection<WaterObject> waterdata;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ForeignCollection<BreakfastObject> getBreakfastdata() {
        return breakfastdata;
    }

    public ForeignCollection<LunchObject> getLunchdata() {
        return lunchdata;
    }

    public ForeignCollection<DinnerObject> getDinnerdata() {
        return dinnerdata;
    }

    public ForeignCollection<SnacksObject> getSnacksdata() {
        return snacksdata;
    }

    public ForeignCollection<ExerciseObject> getExercisedata() {
        return exercisedata;
    }

    public ForeignCollection<WaterObject> getWaterdata() {
        return waterdata;
    }


   /* public ArrayList<BreakfastObject> breakfastArrayList;
    public ArrayList<BreakfastObject> lunchArrayList;
    public ArrayList<BreakfastObject> dinnerArrayList;
    public ArrayList<BreakfastObject> snacksArrayList;
    public ArrayList<WeightObject> weightArrayList;
    public ArrayList<ExerciseObject> exerciseArrayList;
    public ArrayList<WaterObject> waterArrayList;*/
}
