package com.vedas.weightloss.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.PersonalInfoModel;
import com.vedas.weightloss.Models.UnitsObject;
import com.vedas.weightloss.Models.User;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by dell on 20-09-2017.
 */
public class UnitsDataController {

    public static UnitsDataController myObj;

    public static UnitsDataController getInstance() {
        if (myObj == null) {
            myObj = new UnitsDataController();
        }
        return myObj;
    }

    public void insertunitsData(UnitsObject objUnits, User user) {
        objUnits.setUser(user);
        try {
            UserDataController.getInstance().helper.getUnitsDao().create(objUnits);
            fetchUnitsData(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Fetching all the user data
    public ArrayList<UnitsObject> fetchUnitsData(User user) {
        if (user!=null)
        {
            ArrayList<UnitsObject> unitsModels = new ArrayList<UnitsObject>(user.getUnitsdata());
            Log.e("fetching", "units successfully" + unitsModels.size());
            return unitsModels;
        }
        return null;

    }
    public boolean updateUnitsData(UnitsObject objUnits)
    {
        try
        {
            UpdateBuilder<UnitsObject,Integer> updateBuilder = UserDataController.getInstance().helper.getUnitsDao().updateBuilder();
            updateBuilder.updateColumnValue("weightUnits", objUnits.getWeightUnits());
            updateBuilder.updateColumnValue("heightUnits", objUnits.getHeightUnits());
            updateBuilder.updateColumnValue("waterUnits", objUnits.getWaterUnits());
            updateBuilder.updateColumnValue("distanceUnits", objUnits.getDistanceUnits());
            updateBuilder.updateColumnValue("unitsid", objUnits.getId());
            updateBuilder.where().eq("unitsid", objUnits.getId());
            updateBuilder.update();
            Log.e("update","units updated sucessfully"+ objUnits.getDistanceUnits());
            return  true;
        } catch (SQLException e) {
            e.printStackTrace();
            return  false;
        }
    }
    public void deleteUnitsData(ArrayList<UnitsObject> unitsObjects) {
        try {
            UserDataController.getInstance().helper.getUnitsDao().delete(unitsObjects);
            Log.e("deleteunitsdata", "sucessfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


