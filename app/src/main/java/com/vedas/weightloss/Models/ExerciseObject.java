package com.vedas.weightloss.Models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by VEDAS on 6/13/2018.
 */

public class ExerciseObject {
    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(columnName = "exerciseName")
    String exerciseName;

    @DatabaseField(columnName = "exerciseDuration")
    String exerciseDuration;

    @DatabaseField(columnName = "calories")
    String estimatedCaloriesBurned;

    @DatabaseField(columnName = "addeddate")
    String addedDate;

    @DatabaseField(columnName = "food_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    public DayFoodObject dayFoodObject;


    public DayFoodObject getDayFoodObject() {
        return dayFoodObject;
    }

    public void setDayFoodObject(DayFoodObject dayFoodObject) {
        this.dayFoodObject = dayFoodObject;
    }


    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseDuration() {
        return exerciseDuration;
    }

    public void setExerciseDuration(String exerciseDuration) {
        this.exerciseDuration = exerciseDuration;
    }

    public String getEstimatedCaloriesBurned() {
        return estimatedCaloriesBurned;
    }

    public void setEstimatedCaloriesBurned(String estimatedCaloriesBurned) {
        this.estimatedCaloriesBurned = estimatedCaloriesBurned;
    }

}
