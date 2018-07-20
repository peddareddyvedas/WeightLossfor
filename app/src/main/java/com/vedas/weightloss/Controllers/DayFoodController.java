package com.vedas.weightloss.Controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.vedas.weightloss.DataBase.BreakfastDataController;
import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.DinnerDataController;
import com.vedas.weightloss.DataBase.ExerciseDataController;
import com.vedas.weightloss.DataBase.LunchDataController;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.DataBase.SnacksDataController;
import com.vedas.weightloss.DataBase.WaterDataController;
import com.vedas.weightloss.FoodModule.DayOneActivity;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.DinnerObject;
import com.vedas.weightloss.Models.ExerciseObject;
import com.vedas.weightloss.Models.LunchObject;
import com.vedas.weightloss.Models.SnacksObject;
import com.vedas.weightloss.Models.WaterObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by VEDAS on 6/13/2018.
 */

public class DayFoodController {
    public static DayFoodController myObj;
    Context context;
    // public DayFoodObject selectedFoodObject;
    SimpleDateFormat timeformatter = new SimpleDateFormat("hh:mm a");
    public ArrayList<String> footerArray;
    public ArrayList<String> headerArray;

    public static DayFoodController getInstance() {
        if (myObj == null) {
            myObj = new DayFoodController();
        }
        return myObj;
    }

    public void fillContext(Context context1) {
        context = context1;
    }

    public void loadHeaderFooterArray() {
        footerArray = new ArrayList<>();
        headerArray = new ArrayList<>();
        Log.e("headerView", "call");
        headerArray.add("Breakfast");
        headerArray.add("Lunch");
        headerArray.add("Dinner");
        headerArray.add("Snacks");
        headerArray.add("Exercises");
        headerArray.add("Water");
        ////
        footerArray.add("Add Breakfast");
        footerArray.add("Add Lunch");
        footerArray.add("Add Dinner");
        footerArray.add("Add Snacks");
        footerArray.add("Add Exercise");
        footerArray.add("Add Water");

    }

    public int calculatingTotalCaloriesForFood() {
        DayFoodObject dayFoodObject = DayFoodDataController.getInstance().currentFoodObject;
        int selectedCalories = 0;

        if (BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).size() > 0) {
            for (BreakfastObject breakfastObject : BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                selectedCalories = selectedCalories + Integer.parseInt(breakfastCalories);
            }
        }
        if (LunchDataController.getInstance().fetchLunchData(dayFoodObject).size() > 0) {
            for (LunchObject breakfastObject : LunchDataController.getInstance().fetchLunchData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                selectedCalories = selectedCalories + Integer.parseInt(breakfastCalories);
            }
        }
        if (DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).size() > 0) {
            for (DinnerObject breakfastObject : DinnerDataController.getInstance().fetchdinnerData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                selectedCalories = selectedCalories + Integer.parseInt(breakfastCalories);
            }

        }

        if (SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).size() > 0) {
            for (SnacksObject breakfastObject : SnacksDataController.getInstance().fetchSnacksData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                selectedCalories = selectedCalories + Integer.parseInt(breakfastCalories);
            }
        }

        return selectedCalories;
    }

    public void calculatingTotalCaloriesForFoodForHeader() {
        DayFoodObject dayFoodObject = DayFoodDataController.getInstance().currentFoodObject;
        int breakfast = 0;
        int luncch = 0;
        int dinner = 0;
        int snacks = 0;
        int burnedCalories = 0;
        if (BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).size() > 0) {
            for (BreakfastObject breakfastObject : BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                breakfast = breakfast + Integer.parseInt(breakfastCalories);
            }
        }
        if (LunchDataController.getInstance().fetchLunchData(dayFoodObject).size() > 0) {
            for (LunchObject breakfastObject : LunchDataController.getInstance().fetchLunchData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                luncch = luncch + Integer.parseInt(breakfastCalories);
            }
        }
        if (DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).size() > 0) {
            for (DinnerObject breakfastObject : DinnerDataController.getInstance().fetchdinnerData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                dinner = dinner + Integer.parseInt(breakfastCalories);
            }
        }

        if (SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).size() > 0) {
            for (SnacksObject breakfastObject : SnacksDataController.getInstance().fetchSnacksData(dayFoodObject)) {
                String breakfastCalories = breakfastObject.getEsimatedCalories();
                snacks = snacks + Integer.parseInt(breakfastCalories);
            }
        }
        if (ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).size() > 0) {

            for (ExerciseObject exerciseObject : ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject)) {
                String breakfastCalories = exerciseObject.getEstimatedCaloriesBurned();
                burnedCalories = burnedCalories + Integer.parseInt(breakfastCalories);
            }
        }
        DayFoodDataController.getInstance().currentFoodObject.breakFastCalories = String.valueOf(breakfast);
        DayFoodDataController.getInstance().currentFoodObject.lunchCalories = String.valueOf(luncch);
        DayFoodDataController.getInstance().currentFoodObject.dinnerCalories = String.valueOf(dinner);
        DayFoodDataController.getInstance().currentFoodObject.snacksCalories = String.valueOf(snacks);
        DayFoodDataController.getInstance().currentFoodObject.burnedCalories = String.valueOf(burnedCalories);
        DayFoodDataController.getInstance().updateDayFoodData(DayFoodDataController.getInstance().currentFoodObject);

    }

    public double calculatelitersFromWaterobject() {
        double totalWater = 0.0;
        for (int i = 0; i <=WaterDataController.getInstance().fetchWaterData(DayFoodDataController.getInstance().currentFoodObject).size(); i++) {
            ArrayList<WaterObject> water = WaterDataController.getInstance().fetchWaterData(DayFoodDataController.getInstance().currentFoodObject);
            if (water.size()>0){
                for (WaterObject breakfastObject : water) {
                    String breakfastCalories = breakfastObject.getWaterUnits();
                    Log.e("breakfastCalories", "call" + breakfastCalories);
                    if (breakfastObject.getWaterUnits().equals("Milliliter")) {
                        double value = Double.parseDouble(breakfastObject.getWaterContent());
                        Log.e("value", "call" + value);
                        double doubleValue = value /1000.0;
                        Log.e("doubleValue", "call" + doubleValue);
                        totalWater = totalWater + doubleValue;
                        Log.e("Milliliters", "call" + totalWater);
                    } else {
                        Log.e("liters", "call" + totalWater);
                        double value     = Double.parseDouble(breakfastObject.getWaterContent());
                        totalWater = totalWater + value;
                    }
                }
                Log.e("totalvalue", "call" + totalWater);
                return totalWater;
            }

        }
        return  0.0;
    }

    public int calculatingTotalCaloriesForExercise() {
        int burnedCalories = 0;
        if (ExerciseDataController.getInstance().fetchExerciseData(DayFoodDataController.getInstance().currentFoodObject).size() > 0) {
            for (ExerciseObject exerciseObject : ExerciseDataController.getInstance().fetchExerciseData(DayFoodDataController.getInstance().currentFoodObject)) {
                String breakfastCalories = exerciseObject.getEstimatedCaloriesBurned();
                burnedCalories = burnedCalories + Integer.parseInt(breakfastCalories);
            }
        }
        return burnedCalories;
    }

    public int calculateTotlaRemainingCalories() {
        int remaineCalories = 0;
        int targetCalories = Integer.parseInt(PersonalInfoDataController.getInstance().currentMember.getTargetCalories());
        int foodCalories = calculatingTotalCaloriesForFood();
        int execciseCalories = calculatingTotalCaloriesForExercise();
        remaineCalories = targetCalories - foodCalories;
        remaineCalories = remaineCalories + execciseCalories;
        return remaineCalories;
    }

    public String gettingCurrentDate() {
        String currentTIme = "";
        // String currentDate = "";
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime); // convert your date to Calendar object
        calendar.add(Calendar.DATE, -1);
        currentTime = calendar.getTime(); // again get back your date object
        currentTIme = timeformatter.format(currentTime);
        // currentTIme = formatter.format(currentTime);
        return currentTIme;
    }

    public void loadTextviewdata(DayOneActivity.DashBoardAdapter.MainVH holder, int section, int relativePosition, int absolutePosition) {
        DayFoodObject dayFoodObject = DayFoodDataController.getInstance().currentFoodObject;
        if (section == 0) {
            if (BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).size() > 0) {
                holder.view.setVisibility(View.VISIBLE);
                holder.name.setText(
                        String.format(BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).get(relativePosition).getFoodName(), section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format(BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).get(relativePosition).getEsimatedCalories(), section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format(BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).get(relativePosition).getAddedTime(), section, relativePosition, absolutePosition));

            } else {
                holder.view.setVisibility(View.GONE);
                holder.name.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format("", section, relativePosition, absolutePosition));
            }
        }

        if (section == 1) {
            if (LunchDataController.getInstance().fetchLunchData(dayFoodObject).size() > 0) {
                holder.view.setVisibility(View.VISIBLE);
                holder.name.setText(
                        String.format(LunchDataController.getInstance().fetchLunchData(dayFoodObject).get(relativePosition).getFoodName(), section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format(LunchDataController.getInstance().fetchLunchData(dayFoodObject).get(relativePosition).getEsimatedCalories(), section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format(LunchDataController.getInstance().fetchLunchData(dayFoodObject).get(relativePosition).getAddedTime(), section, relativePosition, absolutePosition));
            } else {
                holder.view.setVisibility(View.GONE);
                holder.name.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format("", section, relativePosition, absolutePosition));

            }
        }
        if (section == 2) {
            if (DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).size() > 0) {
                holder.view.setVisibility(View.VISIBLE);
                holder.name.setText(
                        String.format(DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).get(relativePosition).getFoodName(), section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format(DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).get(relativePosition).getEsimatedCalories(), section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format(DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).get(relativePosition).getAddedTime(), section, relativePosition, absolutePosition));

            } else {
                holder.view.setVisibility(View.GONE);
                holder.name.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format("", section, relativePosition, absolutePosition));

            }
        }
        if (section == 3) {
            if (SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).size() > 0) {
                holder.view.setVisibility(View.VISIBLE);
                holder.name.setText(
                        String.format(SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).get(relativePosition).getFoodName(), section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format(SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).get(relativePosition).getEsimatedCalories(), section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format(SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).get(relativePosition).getAddedTime(), section, relativePosition, absolutePosition));

            } else {
                holder.view.setVisibility(View.GONE);
                holder.name.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format("", section, relativePosition, absolutePosition));

            }
        }
        //exercise
        if (section == 4) {
            if (ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).size() > 0) {
                holder.view.setVisibility(View.VISIBLE);
                holder.name.setText(
                        String.format(ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).get(relativePosition).getExerciseName(), section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format(ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).get(relativePosition).getEstimatedCaloriesBurned(), section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format(ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).get(relativePosition).getAddedDate(), section, relativePosition, absolutePosition));
            } else {
                holder.view.setVisibility(View.GONE);
                holder.name.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format("", section, relativePosition, absolutePosition));
            }
        }
        //water
        if (section == 5) {
            if (WaterDataController.getInstance().fetchWaterData(dayFoodObject).size() > 0) {
                holder.view.setVisibility(View.VISIBLE);
                /*holder.name.setText(
                        String.format(DayFoodController.getInstance().selectedFoodObject.waterArrayList.get(relativePosition).getWaterContent(), section, relativePosition, absolutePosition));
              */
                holder.name.setText(
                        String.format("Water", section, relativePosition, absolutePosition));
                String water = WaterDataController.getInstance().fetchWaterData(dayFoodObject).get(relativePosition).getWaterContent() + " " + WaterDataController.getInstance().fetchWaterData(dayFoodObject).get(relativePosition).getWaterUnits();
                holder.calories.setText(
                        String.format(water, section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format(WaterDataController.getInstance().fetchWaterData(dayFoodObject).get(relativePosition).getAddedTime(), section, relativePosition, absolutePosition));

            } else {
                holder.view.setVisibility(View.GONE);
                holder.name.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.calories.setText(
                        String.format("", section, relativePosition, absolutePosition));
                holder.added_time.setText(
                        String.format("", section, relativePosition, absolutePosition));

            }
        }
    }
}
