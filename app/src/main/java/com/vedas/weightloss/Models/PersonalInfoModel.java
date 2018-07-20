package com.vedas.weightloss.Models;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by VEDAS on 5/25/2018.
 */

public class PersonalInfoModel {

    @DatabaseField(id = true, columnName = "userid")
    private String id;

    @DatabaseField(columnName = "gender")
    private String gender;

    @DatabaseField(columnName = "profilePicture",dataType = DataType.BYTE_ARRAY)
    public byte[] mprofilepicturepath;

    @DatabaseField(columnName = "zipcode")
    private String zipcode;

    @DatabaseField(columnName = "birthday")
    private String birthday;

    @DatabaseField(columnName = "location")
    private String location;

    @DatabaseField(columnName = "height")
    private String height;

    @DatabaseField(columnName = "weight")
    private String weight;

    @DatabaseField(columnName = "bmi")
    private String bmi;

    @DatabaseField(columnName = "bmr")
    private String bmr;

    @DatabaseField(columnName = "activityLevel")
    private String activityLevel;

    @DatabaseField(columnName = "recommendedPlan")
    private String recommendedPlan;

    @DatabaseField(columnName = "targetWeight")
    private String targetWeight;

    @DatabaseField(columnName = "targetCalories")
    private String targetCalories;


    @DatabaseField(columnName = "targetDays")
    private String targetDays;


    @DatabaseField(columnName = "user_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private User user;

    public String getTargetDays() {
        return targetDays;
    }

    public void setTargetDays(String targetDays) {
        this.targetDays = targetDays;
    }
    public void setUser(User user) {
        this.user = user;
    }

    private User getUser() {
        return user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBmr() {
        return bmr;
    }

    public void setBmr(String bmr) {
        this.bmr = bmr;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getRecommendedPlan() {
        return recommendedPlan;
    }

    public void setRecommendedPlan(String recommendedPlan) {
        this.recommendedPlan = recommendedPlan;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(String targetCalories) {
        this.targetCalories = targetCalories;
    }

    public byte[] getMprofilepicturepath() {
        return mprofilepicturepath;
    }

    public void setMprofilepicturepath(byte[] mprofilepicturepath) {
        this.mprofilepicturepath = mprofilepicturepath;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
