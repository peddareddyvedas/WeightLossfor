package com.vedas.weightloss.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.DinnerObject;
import com.vedas.weightloss.Models.ExerciseObject;
import com.vedas.weightloss.Models.LunchObject;
import com.vedas.weightloss.Models.PersonalInfoModel;
import com.vedas.weightloss.Models.RemainderObject;
import com.vedas.weightloss.Models.SnacksObject;
import com.vedas.weightloss.Models.UnitsObject;
import com.vedas.weightloss.Models.User;
import com.vedas.weightloss.Models.WaterObject;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application --
    private static final String DATABASE_NAME = "weightloss.db";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<User, Integer> userDao = null;
    private Dao<PersonalInfoModel, Integer> personalInfoDao = null;
    private Dao<BreakfastObject, Integer> breakfastDao = null;
    private Dao<LunchObject, Integer> lunchDao = null;
    private Dao<DinnerObject, Integer> dinnerDao = null;
    private Dao<SnacksObject, Integer> snacksDao = null;
    private Dao<WaterObject, Integer> waterDao = null;
    private Dao<ExerciseObject, Integer> exerciseDao = null;
    private Dao<DayFoodObject, Integer> dayFoodObjectDao = null;
    private Dao<RemainderObject, Integer> remainderDao = null;
    private Dao<UnitsObject, Integer> unitsDao = null;

    ConnectionSource objConnectionSource;

    public DataBaseHelper(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.e("DBStatus", "OnCreate" + connectionSource);

        try {
            //creating the user table
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, PersonalInfoModel.class);
            TableUtils.createTable(connectionSource, BreakfastObject.class);
            TableUtils.createTable(connectionSource, LunchObject.class);
            TableUtils.createTable(connectionSource, DinnerObject.class);
            TableUtils.createTable(connectionSource, SnacksObject.class);
            TableUtils.createTable(connectionSource,WaterObject.class);
            TableUtils.createTable(connectionSource,ExerciseObject.class);
            TableUtils.createTable(connectionSource,DayFoodObject.class);
            TableUtils.createTable(connectionSource, RemainderObject.class);
            TableUtils.createTable(connectionSource, UnitsObject.class);


            //
            userDao = DaoManager.createDao(connectionSource, User.class);
            personalInfoDao = DaoManager.createDao(connectionSource, PersonalInfoModel.class);
            breakfastDao = DaoManager.createDao(connectionSource, BreakfastObject.class);
            lunchDao = DaoManager.createDao(connectionSource, LunchObject.class);
            dinnerDao = DaoManager.createDao(connectionSource, DinnerObject.class);
            snacksDao = DaoManager.createDao(connectionSource, SnacksObject.class);
            waterDao = DaoManager.createDao(connectionSource, WaterObject.class);
            exerciseDao = DaoManager.createDao(connectionSource, ExerciseObject.class);
            dayFoodObjectDao = DaoManager.createDao(connectionSource, DayFoodObject.class);
            remainderDao = DaoManager.createDao(connectionSource, RemainderObject.class);
            unitsDao = DaoManager.createDao(connectionSource, UnitsObject.class);

            Log.e("userDao", "user table is created");
            objConnectionSource = connectionSource;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.e("DBStatus", "OnUpgrade" + connectionSource);
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, PersonalInfoModel.class, true);
            TableUtils.dropTable(connectionSource, BreakfastObject.class, true);
            TableUtils.dropTable(connectionSource, LunchObject.class, true);
            TableUtils.dropTable(connectionSource, DinnerObject.class, true);
            TableUtils.dropTable(connectionSource, SnacksObject.class, true);
            TableUtils.dropTable(connectionSource, WaterObject.class, true);
            TableUtils.dropTable(connectionSource, ExerciseObject.class, true);
            TableUtils.dropTable(connectionSource, DayFoodObject.class, true);
            TableUtils.dropTable(connectionSource, RemainderObject.class, true);
            TableUtils.dropTable(connectionSource, UnitsObject.class, true);

            onCreate(database, connectionSource);
            objConnectionSource = connectionSource;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //user DAO
    public Dao<User, Integer> getUserDao() {
        if (userDao == null) {
            try {
                //  userDao = DaoManager.createDao(objConnectionSource,User.class);
                userDao = getDao(User.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }

    //personal info DAO
    public Dao<PersonalInfoModel, Integer> getPersonalInfoDao() {
        if (personalInfoDao == null) {
            try {
                personalInfoDao = getDao(PersonalInfoModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return personalInfoDao;
    }

    ////
    public Dao<BreakfastObject, Integer> getbreakfastInfoDao() {
        if (breakfastDao == null) {
            try {
                breakfastDao = getDao(BreakfastObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return breakfastDao;
    }

    public Dao<LunchObject, Integer> getLunchInfoDao() {
        if (lunchDao == null) {
            try {
                lunchDao = getDao(LunchObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lunchDao;
    }

    public Dao<DinnerObject, Integer> getDinnerInfoDao() {
        if (dinnerDao == null) {
            try {
                dinnerDao = getDao(DinnerObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dinnerDao;
    }

    public Dao<SnacksObject, Integer> getSnacksInfoDao() {
        if (snacksDao == null) {
            try {
                snacksDao = getDao(SnacksObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return snacksDao;
    }
    public Dao<WaterObject, Integer> getWaterInfoDao() {
        if (waterDao == null) {
            try {
                waterDao = getDao(WaterObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return waterDao;
    }

    public Dao<ExerciseObject, Integer> getExerciseInfoDao() {
        if (exerciseDao == null) {
            try {
                exerciseDao = getDao(ExerciseObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exerciseDao;
    }
    public Dao<DayFoodObject, Integer> getdayFoodInfoDao() {
        if (dayFoodObjectDao == null) {
            try {
                dayFoodObjectDao = getDao(DayFoodObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dayFoodObjectDao;
    }

    ////remain info DAO
    public Dao<RemainderObject, Integer> getRemainderDao() {
        if (remainderDao == null) {
            try {
                remainderDao = getDao(RemainderObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return remainderDao;
    }

    ////remain info DAO
    public Dao<UnitsObject, Integer> getUnitsDao() {
        if (unitsDao == null) {
            try {
                unitsDao = getDao(UnitsObject.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return unitsDao;
    }
}
