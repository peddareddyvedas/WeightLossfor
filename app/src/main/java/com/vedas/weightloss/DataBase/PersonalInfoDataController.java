package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.PersonalInfoModel;

import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Created by dell on 20-09-2017.
 */
public class PersonalInfoDataController {
    public ArrayList<PersonalInfoModel> personalInfoArray;
    //public  Context context;
    public PersonalInfoModel currentMember;
    public static PersonalInfoDataController myObj;

    public static PersonalInfoDataController getInstance() {
        if (myObj == null) {
            myObj = new PersonalInfoDataController();
        }
        return myObj;
    }

    //insert the userdata into user table
    public void insertPersonalInfoData(PersonalInfoModel userdata) {
        try {
            UserDataController.getInstance().helper.getPersonalInfoDao().create(userdata);
            fetchPersonalInfoData();
            Log.e("fetc", "" + personalInfoArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Fetching all the user data
    public ArrayList<PersonalInfoModel> fetchPersonalInfoData() {
        personalInfoArray = null;
        personalInfoArray = new ArrayList<>();
            ArrayList<PersonalInfoModel> personalInfoModels =
                    new ArrayList<PersonalInfoModel>(UserDataController.getInstance().currentUser.getPersonalInformationdata());
            if (personalInfoModels != null) {
                personalInfoArray = personalInfoModels;
                if (personalInfoArray.size()>0){
                    currentMember = personalInfoArray.get(0);
                }
            }
            Log.e("fetching", "personaainfo data fectched successfully" + personalInfoArray.size());
        return personalInfoArray;
    }
    public boolean updateMemberData(PersonalInfoModel objMember)
    {
        try
        {
            UpdateBuilder<PersonalInfoModel,Integer> updateBuilder = UserDataController.getInstance().helper.getPersonalInfoDao().updateBuilder();
            updateBuilder.updateColumnValue("gender",objMember.getGender());
            updateBuilder.updateColumnValue("profilePicture",objMember.getMprofilepicturepath());
            updateBuilder.updateColumnValue("zipcode",objMember.getZipcode());
            updateBuilder.updateColumnValue("birthday",objMember.getBirthday());
            updateBuilder.updateColumnValue("location",objMember.getLocation());
            updateBuilder.updateColumnValue("height",objMember.getHeight());
            updateBuilder.updateColumnValue("weight",objMember.getWeight());
            updateBuilder.updateColumnValue("bmi",objMember.getBmi());
            updateBuilder.updateColumnValue("bmr",objMember.getBmr());
            updateBuilder.updateColumnValue("activityLevel",objMember.getActivityLevel());
            updateBuilder.updateColumnValue("recommendedPlan",objMember.getRecommendedPlan());
            updateBuilder.updateColumnValue("targetWeight",objMember.getTargetWeight());
            updateBuilder.updateColumnValue("targetCalories",objMember.getTargetCalories());
            updateBuilder.updateColumnValue("targetDays",objMember.getTargetDays());
            updateBuilder.where().eq("bmr",objMember.getBmr());
            updateBuilder.update();
            Log.e("update","memner data updated sucessfully"+objMember.getBirthday());
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
    //Deleting all users in database
    public void deleteProfileData(ArrayList<PersonalInfoModel> user) {
        try {
           // UserDataController.getInstance().helper.getPersonalInfoDao().delete(user);
            UserDataController.getInstance().helper.getPersonalInfoDao().deleteBuilder().delete();
            personalInfoArray.removeAll(personalInfoArray);
            Log.e("delete", "personaainfo successfully" + personalInfoArray.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


