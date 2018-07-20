package com.vedas.weightloss.DataBase;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.RemainderObject;
import com.vedas.weightloss.Models.User;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Rise on 02/07/2018.
 */

public class RemainderDataController extends OrmLiteBaseActivity {
    public ArrayList<RemainderObject> allRemainders = new ArrayList<>();
    public RemainderObject currentremainder;
    //public  Context context;
    public static RemainderDataController myObj;

    public static RemainderDataController getInstance() {
        if (myObj == null) {
            myObj = new RemainderDataController();
        }

        return myObj;
    }

    //insert the userdata into user table
    public void insertRemainderData(RemainderObject remainderdata) {
        remainderdata.setUser(UserDataController.getInstance().currentUser);
        try {
          UserDataController.getInstance().helper.getRemainderDao().create(remainderdata);
            fetchRemainderData(UserDataController.getInstance().currentUser);
            Log.e("fetcrem", "" + allRemainders);
            Log.e("fetcremkk", "" + currentremainder.isFromOnline());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Fetching all the user data
    public ArrayList<RemainderObject> fetchRemainderData(User user) {
        allRemainders = null;
        allRemainders = new ArrayList<>();
        if (user!=null) {
           allRemainders= new ArrayList<RemainderObject>(user.getRemainderobject());
            if (allRemainders.size() > 0) {
                for (int i = 0; i < allRemainders.size(); i++) {
                    currentremainder = allRemainders.get(i);
                    Log.e("togglesttus", "" + currentremainder.isFromOnline());

                }
            }
            Log.e("fetching", "remainder data fectched successfully" + allRemainders.size());
        }
        return allRemainders;
    }

    //updating the userdata
    public void updateRemainderData(RemainderObject remainder)
    {
        Log.e("updateremainderdata", "updated the remainderdata sucessfully"+remainder.getRemaindertype());

        try {
            UpdateBuilder<RemainderObject, Integer> updateBuilder = UserDataController.getInstance().helper.getRemainderDao().updateBuilder();
            updateBuilder.updateColumnValue("notes", remainder.getNotes());
            updateBuilder.updateColumnValue("days", remainder.getDays());
            updateBuilder.updateColumnValue("timestamp", remainder.getTimestamp());
            updateBuilder.updateColumnValue("currenttime", remainder.getCurrenttime());
            updateBuilder.updateColumnValue("ischecked", remainder.isFromOnline());
            updateBuilder.updateColumnValue("remaindertype", remainder.getRemaindertype());
            updateBuilder.where().eq("remaindertype", remainder.getRemaindertype());
            updateBuilder.update();
            //fetchRemainderData();
            Log.e("updateremainderdata", "updated the remainderdata sucessfully"+remainder.getCurrenttime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Deleting all users in database
    public void deleteRemainderData(RemainderObject remainderObject) {
        try {
            UserDataController.getInstance().helper.getRemainderDao().delete(remainderObject);
            Log.e("deleteremainderdata", "sucessfully");
            fetchRemainderData(UserDataController.getInstance().currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
