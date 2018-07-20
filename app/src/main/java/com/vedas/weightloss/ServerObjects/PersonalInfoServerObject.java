package com.vedas.weightloss.ServerObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by WAVE on 2/27/2018.
 */

public class PersonalInfoServerObject {

    @SerializedName("userPhoto")
    public String userPhoto;

    @SerializedName("email")
    public String email;

    @SerializedName("gender")
    public String gender;

    @SerializedName("birthday")
    public  String birthday;

    @SerializedName("targetWeight")
    public  String targetWeight;//

    @SerializedName("location")
    public  String location;//

    @SerializedName("height")
    public String height;

    @SerializedName("weight")
    public String weight;

    @SerializedName("bmi")
    public String bmi;

    @SerializedName("bmr")
    public String bmr;

    @SerializedName("activityLevel")
    public String activityLevel;

    @SerializedName("recommendedPlan")//recommendedPlan
    public String recommendedPlan;

    @SerializedName("targetCalories")
    public String targetCalories;

    @SerializedName("zip")
    public String zip;

    @SerializedName("targetDays")
    public String targetDays;

    @SerializedName("response")
    public String result;

    @SerializedName("message")
    public String message;

    @SerializedName("url")
    public String url;


}
