package com.vedas.weightloss.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.plus.model.people.Person;
import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.Constants;
import com.vedas.weightloss.DashBoardModule.DashBoardTabsActivity;
import com.vedas.weightloss.DashBoardModule.MoreFragment;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.LoginModule.SplashScreenViewActivity;
import com.vedas.weightloss.Models.PersonalInfoModel;
import com.vedas.weightloss.MoreModule.ProfilePageActivity;
import com.vedas.weightloss.ServerApis.ServerApisInterface;
import com.vedas.weightloss.ServerObjects.PersonalInfoServerObject;
import com.vedas.weightloss.Settings.WeekGoalNextActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VEDAS on 5/22/2018.
 */
public class PersonalInfoController {
    Context context;
    public static PersonalInfoController myObj;
    public PersonalInfoModel selectedPersonalInfoModel;
    public List<String> feetArray;
    public List<String> inchArray;
    public List<String> cmArray;
    public List<String> lbsArray;
    public List<String> kgArray;
    public HashMap<String, String> personalInfoDict;


    public static PersonalInfoController getInstance() {
        if (myObj == null) {
            myObj = new PersonalInfoController();
            myObj.selectedPersonalInfoModel = new PersonalInfoModel();
        }
        return myObj;
    }

    public void fillContext(Context context1) {
        context = context1;
    }

    public void storePersonalInfoIntoUserDeafaults() {
        personalInfoDict = new HashMap<String, String>();
        if (selectedPersonalInfoModel.getGender() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.gender.toString()), selectedPersonalInfoModel.getGender());
        }
        if (selectedPersonalInfoModel.getBirthday() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.dateOfBirth.toString()), selectedPersonalInfoModel.getBirthday());
        }
        if (selectedPersonalInfoModel.getLocation() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.location.toString()), selectedPersonalInfoModel.getLocation());
        }
        if (selectedPersonalInfoModel.getZipcode() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.zipCode.toString()), selectedPersonalInfoModel.getZipcode());
        }
        if (selectedPersonalInfoModel.getHeight() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.height.toString()), selectedPersonalInfoModel.getHeight());
        }
        if (selectedPersonalInfoModel.getWeight() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.weight.toString()), selectedPersonalInfoModel.getWeight());
        }
        if (selectedPersonalInfoModel.getBmi() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.bmi.toString()), selectedPersonalInfoModel.getBmi());
        }
        if (selectedPersonalInfoModel.getBmr() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.bmr.toString()), selectedPersonalInfoModel.getBmr());
        }
        if (selectedPersonalInfoModel.getActivityLevel() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.activityLavel.toString()), selectedPersonalInfoModel.getActivityLevel());
        }
        if (selectedPersonalInfoModel.getTargetWeight() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.targetWeight.toString()), selectedPersonalInfoModel.getTargetWeight());
        }
        if (selectedPersonalInfoModel.getRecommendedPlan() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.recommendedPlan.toString()), selectedPersonalInfoModel.getRecommendedPlan());
        }
        if (selectedPersonalInfoModel.getTargetCalories() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.targetCalories.toString()), selectedPersonalInfoModel.getTargetCalories());
        }
        if (selectedPersonalInfoModel.getTargetDays() != null) {
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.targetDays.toString()), selectedPersonalInfoModel.getTargetDays());
        }
        /*if (selectedPersonalInfoModel.getMprofilepicturepath()!=null){
            personalInfoDict.put(String.valueOf(Constants.personlInfoKeys.profilePhoto), convertByteArrayTOBitmap(selectedPersonalInfoModel.getMprofilepicturepath()));
        }*/
        saveMap(personalInfoDict);
    }


    private void saveMap(Map<String, String> inputMap) {
        SharedPreferences pSharedPref = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").commit();
            editor.putString("My_map", jsonString);
            editor.commit();
            Log.e("hashmap", "call" + inputMap);
        }
        //   retrivePersonalDataFromUserDefaults();

    }


    public boolean retrivePersonalDataFromUserDefaults() {
        HashMap<String, String> outputMap = new HashMap<String, String>();
        SharedPreferences pSharedPref = context.getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("outpiutmap", "call" + outputMap);

        if (outputMap.size() > 0) {
            Log.e("outpiutmapInside", "call" + outputMap);

            if (outputMap.get(Constants.personlInfoKeys.gender.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.gender.toString());
                Log.e("outputmapgender", "call" + dobString);
                selectedPersonalInfoModel.setGender(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.dateOfBirth.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.dateOfBirth.toString());
                selectedPersonalInfoModel.setBirthday(dobString);
                Log.e("outputmapbirthday", "call" + dobString);

            }
            if (outputMap.get(Constants.personlInfoKeys.height.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.height.toString());
                selectedPersonalInfoModel.setHeight(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.weight.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.weight.toString());
                selectedPersonalInfoModel.setWeight(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.location.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.location.toString());
                selectedPersonalInfoModel.setLocation(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.zipCode.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.zipCode.toString());
                selectedPersonalInfoModel.setZipcode(dobString);

            }
            if (outputMap.get(Constants.personlInfoKeys.bmi.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.bmi.toString());
                selectedPersonalInfoModel.setBmi(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.bmr.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.bmr.toString());
                selectedPersonalInfoModel.setBmr(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.activityLavel.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.activityLavel.toString());
                selectedPersonalInfoModel.setActivityLevel(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.recommendedPlan.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.recommendedPlan.toString());
                selectedPersonalInfoModel.setRecommendedPlan(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.targetCalories.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.targetCalories.toString());
                selectedPersonalInfoModel.setTargetCalories(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.targetWeight.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.targetWeight.toString());
                selectedPersonalInfoModel.setTargetWeight(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.targetDays.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.targetDays.toString());
                selectedPersonalInfoModel.setTargetDays(dobString);
            }
            if (outputMap.get(Constants.personlInfoKeys.profilePhoto.toString()) != null) {
                String dobString = outputMap.get(Constants.personlInfoKeys.profilePhoto.toString());
                Log.e("picstring", "call" + dobString);
               /* byte[] name = Base64.encode(dobString.getBytes(),1);
                Log.e("picstringname","call"+name);
                 selectedPersonalInfoModel.setMprofilepicturepath(name);*/
                // byte[] bytes = dobString.getBytes();
                //  selectedPersonalInfoModel.setMprofilepicturepath(bytes);
            }

            return true;
        }

        return false;

    }

    public void loadImage(String image) {
        SharedPreferences pSharedPref = context.getSharedPreferences("personalImage", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pSharedPref.edit();
        edit.putString("image_data", image);
        Log.e("loadimage", "call" + image);
        edit.commit();
        fetchloadingImage();
    }

    public byte[] fetchloadingImage() {
        SharedPreferences pSharedPref = context.getSharedPreferences("personalImage", Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            String jsonString = pSharedPref.getString("image_data", null);
            Log.e("jsonString", "call" + jsonString);
            byte[] data = Base64.decode(jsonString, Base64.DEFAULT);
            Log.e("jsonStringarray", "call" + data);
            return data;
        } else {
            return null;
        }

    }

    public Bitmap convertByteArrayTOBitmap(byte[] profilePic) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(profilePic);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //RegisterAps////
    public PersonalInfoServerObject[] personalInfoApiExecution(final PersonalInfoModel personalInfoModel, final Context context) {
        RequestBody imageBody = null;

        SharedPreferences pSharedPref = context.getSharedPreferences("personalImage", Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            String jsonString = pSharedPref.getString("image_data", null);
            if (jsonString != null) {
                byte[] data = Base64.decode(jsonString, Base64.DEFAULT);
                if (data.length > 0) {
                    imageBody = RequestBody.create(MediaType.parse("image/*"), data);
                    Log.e("imageInByte", "call" + data);
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setMprofilepicturepath(data);
                }
            }
        }

        final String[] statusCode1 = {null};
        final PersonalInfoServerObject[] user = {new PersonalInfoServerObject()};

        // RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), personalInfoModel.getMprofilepicturepath());
        MultipartBody.Part image1 = MultipartBody.Part.createFormData("userPhoto", "UserPhoto", imageBody);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), UserDataController.getInstance().currentUser.getEmail().trim());
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getGender().trim());
        RequestBody birthDay = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getBirthday().trim());
        RequestBody targetWeight = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getTargetWeight().trim());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), "Anantapur,India");
        RequestBody height = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getHeight().trim());
        RequestBody weight = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getWeight().trim());
        RequestBody bmi = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getBmi().trim());
        RequestBody bmr = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getBmr().trim());
        RequestBody activitylevel = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getActivityLevel().trim());
        RequestBody plan = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getRecommendedPlan().trim());
        RequestBody targetCalories = RequestBody.create(MediaType.parse("text/plain"), personalInfoModel.getTargetCalories().trim());
        RequestBody zip = RequestBody.create(MediaType.parse("text/plain"), "515002");
        RequestBody targetDays = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(characterCount(personalInfoModel.getTargetDays())));

        Log.e("level", "call" + activitylevel);
        Log.e("calories", "" + targetCalories);
        Log.e("plan", "" + plan);
        Log.e("dob", "" + birthDay);
        Log.e("height", "" + height);
        Log.e("weight", "" + weight);
        Log.e("gender", "" + gender);
        Log.e("email", "" + email);
        Log.e("targetweight", "" + targetWeight);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<PersonalInfoServerObject> callable = api.personalinfo(image1, email, gender, birthDay, targetWeight,
                location, height, weight, bmi, bmr, activitylevel, plan, targetCalories, zip, targetDays);
        callable.enqueue(new Callback<PersonalInfoServerObject>() {
            @Override
            public void onResponse(Call<PersonalInfoServerObject> call, Response<PersonalInfoServerObject> response) {

                if (PersonalInfoDataController.getInstance().currentMember != null) {
                    ProfilePageActivity.refreshShowingDialog.hideRefreshDialog();

                } else {
                    WeekGoalNextActivity.refreshShowingDialog.hideRefreshDialog();

                }

                if (response.body() != null) {
                    String statusCode = response.body().result;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    Log.e("photourl", "" + "http://117.247.13.135" + response.body().url);
                    response.body().url = "http://117.247.13.135" + response.body().url;
                    Log.e("imgurl", "" + response.body().url);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    Log.e("user[0]", "" + user[0].result);
                    gettingServerResponse(user, context);
                }
            }

            @Override
            public void onFailure(Call<PersonalInfoServerObject> call, Throwable t) {
                Log.e("message", "onFailure" + t.getMessage());
                if (PersonalInfoDataController.getInstance().currentMember != null) {
                    ProfilePageActivity.refreshShowingDialog.hideRefreshDialog();

                } else {
                    WeekGoalNextActivity.refreshShowingDialog.hideRefreshDialog();

                }
                statusCode1[0] = "Failure";
                user[0] = null;
            }
        });
        return user;
    }

    //define your function
    private int characterCount(String word) {
        Log.e("character", "call" + word);
        //initialize the counter to 0
        int count = 0;
        //loop through the word
        for (int i = 0; i < word.length(); i++) {
            Log.e("cha", "call" + word.indexOf(i));
            //if the character in the word is equal to  the character passed in as a parameter increment count
            if (word.charAt(i) == '1') {
                count++;
            }
        }
        //return the sentence.
        return count;
    }

    private void gettingServerResponse(PersonalInfoServerObject[] userServerObjectResponse, final Context context) {
        if (userServerObjectResponse.length > 0) {
            String statusCode = userServerObjectResponse[0].result;
            String message = userServerObjectResponse[0].message;
            Log.e("statuscode", "call" + statusCode);

            if (statusCode.equals("3")) {
                Log.e("personalinfo", "call" + statusCode);

                Intent intent1 = new Intent();
                if (PersonalInfoDataController.getInstance().currentMember != null) {
                    Toast.makeText(context, "Update Personal information", Toast.LENGTH_SHORT).show();

                   // EventBus.getDefault().post(new SplashScreenViewActivity.MessageEvent("Personalinformation"));


             /*       intent1.setClass(context, MoreFragment.class);
                    context.startActivity(intent1);*/

                } else {
                    intent1.setClass(context, DashBoardTabsActivity.class);
                    context.startActivity(intent1);
                    PersonalInfoDataController.getInstance().insertPersonalInfoData(
                            PersonalInfoController.getInstance().selectedPersonalInfoModel);
                    UserDataController.getInstance().createDayPlan();
                }


            } else if (statusCode.equals("error")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("1")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("0")) {
                new AlertShowingDialog(context, message);
            }
        }
    }


    public void loadHeightValuesArray() {
        feetArray = new ArrayList<String>();
        inchArray = new ArrayList<String>();
        cmArray = new ArrayList<String>();
        for (int i = 3; i <= 8; i++) {
            feetArray.add(String.valueOf(i));
        }
        for (int i = 93; i <= 243; i++) {
            cmArray.add(String.valueOf(i));
        }
        for (int i = 0; i <= 11; i++) {
            inchArray.add(String.valueOf(i));
        }
    }

    public void loadWeightValuesArray() {

        kgArray = new ArrayList<String>();
        lbsArray = new ArrayList<String>();

        Log.e("kg", "call");
        for (int i = 29; i <= 350; i++) {
            kgArray.add(String.valueOf(i));
        }

        for (int j = 63; j <= 772; j++) {
            lbsArray.add(String.valueOf(j));
        }
    }

    public String convertCmToFeet(String cmValue) {

        String[] cmValueArray = cmValue.split(" ");

        Double cmVal = Double.parseDouble(cmValueArray[0]);
        Log.e("cm", "call" + cmVal);
        double inchesValue = cmVal * 0.39370;
        int feetValue = (int) (inchesValue / 12);

        int remainInchesValue = (int) (inchesValue % 12);

        Log.e("requiredFeetString", feetValue + "inch" + remainInchesValue);
        return feetValue + " " + remainInchesValue;

    }

    public String convertFeetToCm(String feetValue) {
        Log.e("convertFeetToCm", "" + feetValue);

        String[] feetStrArray = feetValue.split(" ");

        double totalInches = 0.0;
        double feetInches = Double.parseDouble(feetStrArray[0]);
        double inches = Double.parseDouble(feetStrArray[1]);

        Log.e("feetInches", "" + feetInches + "" + inches);
        totalInches = feetInches * 12 + inches;

        Double feetDouble = new Double(totalInches * 2.54);

        int feetint = (int) Math.round(feetDouble);

        if (feetint < 93) {
            feetint = 93;
        }
        if (feetint > 243) {

            feetint = 243;
        }
        Log.e("feetint", "call" + feetint);
        String strValue = String.valueOf(feetint);
        return strValue;
    }

    public String calculatingAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }

    public void SetBackColorToLayouts(double bmi, String age, String gender, PieChart pieChart,
                                      LinearLayout l1, LinearLayout l2, LinearLayout l3, LinearLayout l4,
                                      LinearLayout l5, LinearLayout l6, LinearLayout l7, LinearLayout l8) {
        pieChart.invalidate();
        l1.setBackgroundColor(Color.rgb(255, 255, 255));
        l2.setBackgroundColor(Color.rgb(255, 255, 255));
        l3.setBackgroundColor(Color.rgb(255, 255, 255));
        l4.setBackgroundColor(Color.rgb(255, 255, 255));
        l5.setBackgroundColor(Color.rgb(255, 255, 255));
        l6.setBackgroundColor(Color.rgb(255, 255, 255));
        l7.setBackgroundColor(Color.rgb(255, 255, 255));
        l8.setBackgroundColor(Color.rgb(255, 255, 255));

        if (age.equals("") || Integer.parseInt(age) == 0 ||
                (Integer.parseInt(age) >= 1 && Integer.parseInt(age) <= 6) ||
                bmi == -1f || bmi == 0f) {
            pieChart.highlightValue(0, -1, false);

        }
        if (age.length() != 0) {

            if (Integer.parseInt(age) == 7) {
                if (gender.equals("Male")) {
                    if (bmi <= 13.6f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));

                    }
                    if (bmi >= 13.7f && bmi <= 19.1f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 19.2f && bmi <= 21.0f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 21.1f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 13.2f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 13.3f && bmi <= 18.1f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 18.2f && bmi <= 23.0f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.1f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 8) {
                if (gender.equals("Male")) {
                    if (bmi <= 14.2f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 14.3f && bmi <= 19.2f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 19.3f && bmi <= 22.5f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 22.6f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 13.2f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 13.3f && bmi <= 18.7f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 18.8f && bmi <= 22.2f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 22.3f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 9) {
                if (gender.equals("Male")) {
                    if (bmi <= 13.7f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 13.8f && bmi <= 19.3f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 19.4f && bmi <= 21.5f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 21.6f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 13.7f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 13.8f && bmi <= 19.7f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 19.8f && bmi <= 23.3f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.4f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 10) {
                if (gender.equals("Male")) {
                    if (bmi <= 14.6f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 14.7f && bmi <= 21.3f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 21.4f && bmi <= 24.9f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 25.0f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 14.2f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 14.3f && bmi <= 20.6f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 20.7f && bmi <= 23.3f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.4f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 11) {
                if (gender.equals("Male")) {
                    if (bmi <= 14.3f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 14.4f && bmi <= 21.1f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 21.2f && bmi <= 22.9f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.0f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 14.7f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 14.8f && bmi <= 20.7f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 20.8f && bmi <= 22.8f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 22.9f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 12) {
                if (gender.equals("Male")) {
                    if (bmi <= 14.8f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 14.9f && bmi <= 21.9f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 22.0f && bmi <= 24.7f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 24.8f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 15.0f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 15.1f && bmi <= 21.4f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 21.5f && bmi <= 23.3f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.4f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 13) {
                if (gender.equals("Male")) {
                    if (bmi <= 16.2f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 16.3f && bmi <= 21.6f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 21.7f && bmi <= 24.4f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 24.5f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 15.6f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 15.7f && bmi <= 21.9f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 22.0f && bmi <= 24.3f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 24.4f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 14) {
                if (gender.equals("Male")) {
                    if (bmi <= 16.7f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 16.8f && bmi <= 22.5f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 22.6f && bmi <= 25.6f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 25.7f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 17.0f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 17.1f && bmi <= 23.1f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.2f && bmi <= 25.9f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 26.0f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 15) {
                if (gender.equals("Male")) {
                    if (bmi <= 17.8f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 17.9f && bmi <= 23.0f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.1f && bmi <= 25.8f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 25.9f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 17.6f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 17.7f && bmi <= 23.1f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.2f && bmi <= 27.5f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 27.6f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }

            if (Integer.parseInt(age) == 16) {
                if (gender.equals("Male")) {
                    if (bmi <= 18.5f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 18.6f && bmi <= 23.6f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.7f && bmi <= 25.9f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 26.0f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 17.8f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 17.9f && bmi <= 22.7f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 22.8f && bmi <= 24.1f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 24.2f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 17) {
                if (gender.equals("Male")) {
                    if (bmi <= 18.6f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 18.7f && bmi <= 23.6f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.7f && bmi <= 25.7f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 25.8f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 17.8f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 17.9f && bmi <= 23.3f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.4f && bmi <= 25.6f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 25.7f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) == 18) {
                if (gender.equals("Male")) {
                    if (bmi <= 18.6f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 18.7f && bmi <= 23.9f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 24.0f && bmi <= 26.7f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 26.8f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
                if (gender.equals("Female")) {
                    if (bmi <= 18.3f && bmi > 0f) {
                        pieChart.highlightValue(0, 0, false);
                        l3.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 18.4f && bmi <= 23.4f) {
                        pieChart.highlightValue(1, 0, false);
                        l4.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 23.5f && bmi <= 24.9f) {
                        pieChart.highlightValue(2, 0, false);
                        l5.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                    if (bmi >= 25.0f) {
                        pieChart.highlightValue(3, 0, false);
                        l6.setBackgroundColor(Color.rgb(220, 220, 220));
                    }
                }
            }
            if (Integer.parseInt(age) >= 19) {
                if (bmi <= 16.0f && bmi > 0f) {
                    pieChart.highlightValue(0, 0, false);
                    l1.setBackgroundColor(Color.rgb(220, 220, 220));
                }
                if (bmi >= 16.0f && bmi <= 16.9f) {
                    pieChart.highlightValue(1, 0, false);
                    l2.setBackgroundColor(Color.rgb(220, 220, 220));
                }
                if (bmi >= 17.0f && bmi <= 18.4f) {
                    pieChart.highlightValue(2, 0, false);
                    l3.setBackgroundColor(Color.rgb(220, 220, 220));
                }
                if (bmi >= 18.5f && bmi <= 24.9f) {
                    pieChart.highlightValue(3, 0, false);
                    l4.setBackgroundColor(Color.rgb(220, 220, 220));
                }
                if (bmi >= 25.0f && bmi <= 29.9f) {
                    pieChart.highlightValue(4, 0, false);
                    l5.setBackgroundColor(Color.rgb(220, 220, 220));
                }
                if (bmi >= 30.0f && bmi <= 34.9f) {
                    pieChart.highlightValue(5, 0, false);
                    l6.setBackgroundColor(Color.rgb(220, 220, 220));
                }
                if (bmi >= 35.0f && bmi <= 39.9f) {
                    pieChart.highlightValue(6, 0, false);
                    l7.setBackgroundColor(Color.rgb(220, 220, 220));
                }
                if (bmi >= 40.0f) {
                    pieChart.highlightValue(7, 0, false);
                    l8.setBackgroundColor(Color.rgb(220, 220, 220));
                }
            }

        }
    }

    public void ChangeText(String selectedGender, int age, TextView t1, TextView t2, TextView t3, TextView t4,
                           TextView t5, TextView t6, TextView t7, TextView t8) {
        t1.setText("-");
        t2.setText("-");
        t3.setText("-");
        t4.setText("-");
        t5.setText("-");
        t6.setText("-");
        t7.setText("-");
        t8.setText("-");

        if (selectedGender.equals("Male")) {
            if (age == 0) {
                t1.setText("<16.0");
                t2.setText("16.0-16.9");
                t3.setText("17.0-18.4");
                t4.setText("18.5-24.9");
                t5.setText("25.0-29.9");
                t6.setText("30.0-34.9");
                t7.setText("35.0-39.9");
                t8.setText(">=40.0");
            } else if (age == 7) {
                t3.setText("<=13.6");
                t4.setText("13.7-19.1");
                t5.setText("19.2-21.0");
                t6.setText(">=21.1");
            } else if (age == 8) {
                t3.setText("<=14.2");
                t4.setText("14.3-19.2");
                t5.setText("19.3-22.5");
                t6.setText(">=22.6");

            } else if (age == 9) {

                t3.setText("<=13.7");
                t4.setText("13.8-19.3");
                t5.setText("19.4-21.5");
                t6.setText(">=21.6");

            } else if (age == 10) {

                t3.setText("<=14.6");
                t4.setText("14.7-21.3");
                t5.setText("21.4-24.9");
                t6.setText(">=25.0");

            } else if (age == 11) {

                t3.setText("<=14.3");
                t4.setText("14.4-21.1");
                t5.setText("21.2-22.9");
                t6.setText(">=23.0");


            } else if (age == 12) {

                t3.setText("<=14.8");
                t4.setText("14.9-21.9");
                t5.setText("22.0-24.7");
                t6.setText(">=24.8");

            } else if (age == 13) {

                t3.setText("<=16.2");
                t4.setText("16.3-21.6");
                t5.setText("21.7-24.4");
                t6.setText(">=24.5");

            } else if (age == 14) {

                t3.setText("<=16.7");
                t4.setText("16.8-22.5");
                t5.setText("22.6-25.6");
                t6.setText(">=25.7");


            } else if (age == 15) {

                t3.setText("<=17.8");
                t4.setText("17.9-23.0");
                t5.setText("23.1-25.8");
                t6.setText(">=25.9");


            } else if (age == 16) {

                t3.setText("<=18.5");
                t4.setText("18.6-23.6");
                t5.setText("23.7-25.9");
                t6.setText(">=26.0");


            } else if (age == 17) {
                t3.setText("<=18.6");
                t4.setText("18.7-23.6");
                t5.setText("23.7-25.7");
                t6.setText(">=25.8");


            } else if (age == 18) {

                t3.setText("<=18.6");
                t4.setText("18.7-23.9");
                t5.setText("24.0-26.7");
                t6.setText(">=26.8");

            } else if (age >= 19) {
                t1.setText("<16.0");
                t2.setText("16.0-16.9");
                t3.setText("17.0-18.4");
                t4.setText("18.5-24.9");
                t5.setText("25.0-29.9");
                t6.setText("30.0-34.9");
                t7.setText("35.0-39.9");
                t8.setText(">=40.0");

            }
        }

        if (selectedGender.equals("Female")) {
            if (age == 0) {
                t1.setText("<16.0");
                t2.setText("16.0-16.9");
                t3.setText("17.0-18.4");
                t4.setText("18.5-24.9");
                t5.setText("25.0-29.9");
                t6.setText("30.0-34.9");
                t7.setText("35.0-39.9");
                t8.setText(">=40.0");
            } else if (age == 7) {

                t3.setText("<=13.2");
                t4.setText("13.3-18.1");
                t5.setText("18.2-23.0");
                t6.setText(">=23.1");


            } else if (age == 8) {

                t3.setText("<=13.2");
                t4.setText("13.3-18.7");
                t5.setText("18.8-22.2");
                t6.setText(">=22.3");


            } else if (age == 9) {

                t3.setText("<=13.7");
                t4.setText("13.8-19.7");
                t5.setText("19.8-23.3");
                t6.setText(">=23.4");


            } else if (age == 10) {

                t3.setText("<=14.2");
                t4.setText("14.3-20.6");
                t5.setText("20.7-23.3");
                t6.setText(">=23.4");


            } else if (age == 11) {

                t3.setText("<=14.7");
                t4.setText("14.8-20.7");
                t5.setText("20.8-22.8");
                t6.setText(">=22.9");


            } else if (age == 12) {

                t3.setText("<=15.0");
                t4.setText("15.1-21.4");
                t5.setText("21.5-23.3");
                t6.setText(">=23.4");


            } else if (age == 13) {

                t3.setText("<=15.6");
                t4.setText("15.7-21.9");
                t5.setText("22.0-24.3");
                t6.setText(">=24.4");


            } else if (age == 14) {

                t3.setText("<=17.0");
                t4.setText("17.1-23.1");
                t5.setText("23.2-25.9");
                t6.setText(">=26.0");

            } else if (age == 15) {

                t3.setText("<=17.6");
                t4.setText("17.7-23.1");
                t5.setText("23.2-27.5");
                t6.setText(">=27.6");


            } else if (age == 16) {

                t3.setText("<=17.8");
                t4.setText("17.9-22.7");
                t5.setText("22.8-24.1");
                t6.setText(">=24.2");


            } else if (age == 17) {

                t3.setText("<=17.8");
                t4.setText("17.9-23.3");
                t5.setText("23.4-25.6");
                t6.setText(">=25.7");


            } else if (age == 18) {

                t3.setText("<=18.3");
                t4.setText("18.4-23.4");
                t5.setText("23.5-24.9");
                t6.setText(">=25.0");

            } else if (age >= 19) {
                t1.setText("<16.0");
                t2.setText("16.0-16.9");
                t3.setText("17.0-18.4");
                t4.setText("18.5-24.9");
                t5.setText("25.0-29.9");
                t6.setText("30.0-34.9");
                t7.setText("35.0-39.9");
                t8.setText(">=40.0");

            }
        }

    }

}
