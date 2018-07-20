package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.ExerciseObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */

public class ExerciseDataController {
    public static ExerciseDataController myObj;

    public static ExerciseDataController getInstance() {
        if (myObj == null) {
            myObj = new ExerciseDataController();
        }

        return myObj;
    }

    public void insertExerciseData(ExerciseObject exerciseObject,DayFoodObject dayFoodObject) {
        exerciseObject.setDayFoodObject(dayFoodObject);
        try {
            UserDataController.getInstance().helper.getExerciseInfoDao().create(exerciseObject);
            fetchExerciseData(dayFoodObject);
            Log.e("breakfastname","call" + exerciseObject.getExerciseName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ExerciseObject> fetchExerciseData(DayFoodObject dayFoodObject) {
        if (dayFoodObject!=null){
            ArrayList<ExerciseObject> exerciseModelsList = new ArrayList<ExerciseObject>(DayFoodDataController.getInstance().currentFoodObject.getExercisedata());
            Log.e("fetching", "exerciseModelsList successfully" + exerciseModelsList.size());
            return exerciseModelsList;
        }
        return null;
    }

    public void updateExerciseData(ExerciseObject exerciseObject) {
        try {
            UpdateBuilder<ExerciseObject, Integer> updateBuilder = UserDataController.getInstance().helper.getExerciseInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("exerciseName", exerciseObject.getExerciseName());
            updateBuilder.updateColumnValue("exerciseDuration", exerciseObject.getExerciseDuration());
            updateBuilder.updateColumnValue("calories", exerciseObject.getEstimatedCaloriesBurned());
            updateBuilder.updateColumnValue("addeddate", exerciseObject.getAddedDate());
            updateBuilder.where().eq("exerciseName", exerciseObject.getExerciseName());
            updateBuilder.update();
            Log.e("update data", "updated exercise sucessfully");
            Log.e("update data", "call"+exerciseObject.getEstimatedCaloriesBurned());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteExerciseDataForPosition(int position,DayFoodObject dayFoodObject) {
        try {
            ExerciseObject breakfastObject=fetchExerciseData(dayFoodObject).get(position);
            UserDataController.getInstance().helper.getExerciseInfoDao().delete(breakfastObject);
            Log.e("deleteexercisedata", "sucessfully");
            fetchExerciseData(dayFoodObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteExerciseData(ArrayList<ExerciseObject> exerciseObjects) {
        try {
            UserDataController.getInstance().helper.getExerciseInfoDao().delete(exerciseObjects);
            Log.e("deletebreakdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


