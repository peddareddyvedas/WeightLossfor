package com.vedas.weightloss.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.Constants;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.LoginModule.ForgotPasswordVerificationActivity;
import com.vedas.weightloss.LoginModule.ForgotViewActivity;
import com.vedas.weightloss.LoginModule.LoginViewActivity;
import com.vedas.weightloss.LoginModule.NewPasswordViewActivity;
import com.vedas.weightloss.LoginModule.RegisterVerificationActivity;
import com.vedas.weightloss.LoginModule.RegisterViewActivity;
import com.vedas.weightloss.Models.User;
import com.vedas.weightloss.ServerApis.LocationTracker;
import com.vedas.weightloss.ServerApis.ServerApisInterface;
import com.vedas.weightloss.ServerObjects.UserServerObject;
import com.vedas.weightloss.Settings.PersonalInfoActivity;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by VEDAS on 5/24/2018.
 */

public class LoginModuleApisController extends BaseDetailActivity {
    public static LoginModuleApisController myObj;
    Context context;

    public static LoginModuleApisController getInstance() {
        if (myObj == null) {
            myObj = new LoginModuleApisController();
        }
        return myObj;
    }

    public void fillContext(Context context1) {
        context = context1;
    }

    public String getCurrentTime() {
        String attempt_time = String.valueOf(System.currentTimeMillis() / 1000);
        Log.e("attem", "" + attempt_time);
        return attempt_time;
    }

    //RegisterAps////
    public UserServerObject[] registerApiExecution(final String name, String str_email, String str_password, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();
        String latitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude());
        if (latitude == null) {
            latitude = "0.0";
        }
        String longitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());
        if (latitude == null) {
            longitude = "0.0";
        }
        requestBody.mailid = str_email.trim();
        requestBody.password = str_password.trim();
        requestBody.latitude = latitude;
        requestBody.longitude = longitude;
        requestBody.register_time = getCurrentTime();
        requestBody.type = "Manual";


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("longitude", "" + requestBody.longitude + "longi" + longitude);
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.password);
        Log.e("type", "" + requestBody.type);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.register(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                RegisterViewActivity.refreshShowingDialog.hideRefreshDialog();
                if (response.body() != null) {

                    SharedPreferences sharedPreferences = context.getSharedPreferences("regDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString("name", name);
                    sharedPreferencesEditor.putString("email", requestBody.mailid);
                    sharedPreferencesEditor.putString("password", requestBody.password);
                    sharedPreferencesEditor.putString("latitude", requestBody.latitude);
                    sharedPreferencesEditor.putString("longitude", requestBody.longitude);
                    sharedPreferencesEditor.putString("regtime", requestBody.register_time);
                    sharedPreferencesEditor.putString("regtype", requestBody.type);
                    sharedPreferencesEditor.commit();

                    String statusCode = response.body().response;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    //userServerObjectResponse =response;
                    Log.e("user[0]", "" + user[0].response);
                    gettingServerResponse(user, context);
                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                Log.e("message", "onFailure" + t.getMessage());
                RegisterViewActivity.refreshShowingDialog.hideRefreshDialog();
                statusCode1[0] = "Failure";
                user[0] = null;
            }
        });
        return user;
    }

    private void gettingServerResponse(UserServerObject[] userServerObjectResponse, final Context context) {
        if (userServerObjectResponse.length > 0) {
            String statusCode = userServerObjectResponse[0].response;
            String message = userServerObjectResponse[0].message;
            Log.e("statuscode", "call" + statusCode);

            if (statusCode.equals("3")) {
                Intent intent1 = new Intent();
                intent1.setClass(context, RegisterVerificationActivity.class);
                intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                context.startActivity(intent1);

            } else if (statusCode.equals("error")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("1")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("0")) {
                new AlertShowingDialog(context, message);
            }else if (statusCode.equals("5")) {
                new AlertShowingDialog(context, message);
            }else if (statusCode.equals("6")) {
                new AlertShowingDialog(context, message);
            }
        }
    }

    //RegisterAps////
    public UserServerObject[] verifyApiExecution(final String name, String str_email, String otp, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();
        String latitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude());
        if (latitude == null) {
            latitude = "0.0";
        }
        String longitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());
        if (latitude == null) {
            longitude = "0.0";
        }
        requestBody.mailid = str_email.trim();
        requestBody.pin = otp;
        requestBody.attempt_time = getCurrentTime();


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.pin);
        Log.e("type", "" + requestBody.attempt_time);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.verification(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                RegisterVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                if (response.body() != null) {

                    String statusCode = response.body().result;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    //userServerObjectResponse =response;
                    Log.e("user[0]", "" + user[0].response);
                    User currentUser = new User();
                    currentUser.setName(name);
                    currentUser.setEmail(requestBody.mailid);
                    currentUser.setPassword(requestBody.password);
                    currentUser.setLatitude(requestBody.latitude);
                    currentUser.setLongitude(requestBody.longitude);
                    currentUser.setRegisterTime(requestBody.register_time);
                    currentUser.setRegisterType(Constants.RegisterTypes.Manual.toString());
                    currentUser.setVerified(true);
                    gettingVerificationServerResponse(user, context, currentUser);
                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                RegisterVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                Log.e("message", "onFailure" + t.getMessage());
                statusCode1[0] = "Failure";
                user[0] = null;
            }
        });
        return user;
    }

    private void gettingVerificationServerResponse(UserServerObject[] userServerObjectResponse, final Context context, User currentUser) {
        if (userServerObjectResponse.length > 0) {
            String statusCode = userServerObjectResponse[0].result;
            String message = userServerObjectResponse[0].message;
            Log.e("statuscode", "call" + statusCode);

            if (statusCode.equals("success")) {
                UserDataController.getInstance().insertUserData(currentUser);
                Intent intent1 = new Intent();
                intent1.setClass(context, PersonalInfoActivity.class);
                intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                context.startActivity(intent1);

            } else if (statusCode.equals("error")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("1")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("0")) {
                new AlertShowingDialog(context, message);
            }
        }
    }

    //RegisterAps////
    public UserServerObject[] resendApiExecution(String str_email, String str_password, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();
        String latitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude());
        if (latitude == null) {
            latitude = "0.0";
        }
        String longitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());
        if (latitude == null) {
            longitude = "0.0";
        }
        requestBody.mailid = str_email.trim();
        requestBody.password = str_password.trim();
        requestBody.latitude = latitude;
        requestBody.longitude = longitude;
        requestBody.register_time = getCurrentTime();
        requestBody.type = "Manual";


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("longitude", "" + requestBody.longitude + "longi" + longitude);
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.password);
        Log.e("type", "" + requestBody.type);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.register(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                RegisterVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                if (response.body() != null) {

                    String statusCode = response.body().response;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    Log.e("resendstatuscode", "call" + statusCode);

                    if (statusCode.equals("3")) {
                        new AlertShowingDialog(context, "Otp send successfully");
                    } else if (statusCode.equals("5")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("1")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("0")) {
                        new AlertShowingDialog(context, message);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                RegisterVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                Log.e("message", "onFailure" + t.getMessage());
                statusCode1[0] = "Failure";
                user[0] = null;
            }
        });
        return user;
    }

    //RegisterAps////
    public UserServerObject[] loginApiExecution(String str_email, String password, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();

        requestBody.mailid = str_email.trim();
        requestBody.password = password;


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.loginAPiExection(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                if (response.body() != null) {
                   LoginViewActivity.refreshShowingDialog.hideRefreshDialog();
                    String statusCode = response.body().response;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    //userServerObjectResponse =response;
                    Log.e("user[0]", "" + user[0].response);
                    Log.e("loginstatuscode", "call" + statusCode);

                    if (statusCode.equals("3")) {
                        User currentUser = new User();
                        currentUser.setName("");
                        currentUser.setEmail(requestBody.mailid);
                        currentUser.setPassword(requestBody.password);
                        currentUser.setLatitude(requestBody.latitude);
                        currentUser.setLongitude(requestBody.longitude);
                        currentUser.setRegisterTime(requestBody.register_time);
                        currentUser.setVerified(true);
                        currentUser.setRegisterType(Constants.RegisterTypes.Manual.toString());
                        UserDataController.getInstance().insertUserData(currentUser);
                        ////
                        Intent intent1 = new Intent();
                        intent1.setClass(context, PersonalInfoActivity.class);
                        intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        context.startActivity(intent1);

                    } else if (statusCode.equals("error")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("1")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("0")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("2")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("6")) {
                        new AlertShowingDialog(context, message);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                Log.e("message", "onFailure" + t.getMessage());
                statusCode1[0] = "Failure";
               LoginViewActivity.refreshShowingDialog.hideRefreshDialog();
                user[0] = null;
            }
        });
        return user;
    }

    //RegisterAps////
    public UserServerObject[] forgotApiExecution(String str_email, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();

        requestBody.mailid = str_email.trim();
        requestBody.register_time = getCurrentTime();


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.register_time);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.forgotAPiExection(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                if (response.body() != null) {
                    ForgotViewActivity.refreshShowingDialog.hideRefreshDialog();
                    String statusCode = response.body().result;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    //userServerObjectResponse =response;
                    Log.e("forgotstatuscode", "call" + statusCode);

                    if (statusCode.equals("success")) {
                        Intent intent1 = new Intent();
                        intent1.setClass(context, ForgotPasswordVerificationActivity.class);
                        intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        intent1.putExtra("email", requestBody.mailid.trim());
                        context.startActivity(intent1);

                    } else if (statusCode.equals("error")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("1")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("0")) {
                        new AlertShowingDialog(context, message);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                Log.e("message", "onFailure" + t.getMessage());
                statusCode1[0] = "Failure";
                ForgotViewActivity.refreshShowingDialog.hideRefreshDialog();
                user[0] = null;
            }
        });
        return user;
    }
    //RegisterAps////
    public UserServerObject[] forgotpswverifyApiExecution( String str_email, String otp, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();
        String latitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude());
        if (latitude == null) {
            latitude = "0.0";
        }
        String longitude = String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude());
        if (latitude == null) {
            longitude = "0.0";
        }
        requestBody.mailid = str_email.trim();
        requestBody.pin = otp;
        requestBody.attempt_time = getCurrentTime();


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.pin);
        Log.e("type", "" + requestBody.attempt_time);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.verification(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                ForgotPasswordVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                if (response.body() != null) {
                    String statusCode = response.body().result;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    forgotpswVerificationServerResponse(user, context,requestBody.mailid);
                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                ForgotPasswordVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                Log.e("message", "onFailure" + t.getMessage());
                statusCode1[0] = "Failure";
                user[0] = null;
            }
        });
        return user;
    }
    private void forgotpswVerificationServerResponse(UserServerObject[] userServerObjectResponse, final Context context,String mailid) {
        if (userServerObjectResponse.length > 0) {
            String statusCode = userServerObjectResponse[0].result;
            String message = userServerObjectResponse[0].message;
            Log.e("statuscode", "call" + statusCode);

            if (statusCode.equals("success")) {
                Intent intent1 = new Intent();
                intent1.setClass(context, NewPasswordViewActivity.class);
                intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                intent1.putExtra("email", mailid.trim());
                context.startActivity(intent1);

            } else if (statusCode.equals("error")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("1")) {
                new AlertShowingDialog(context, message);
            } else if (statusCode.equals("0")) {
                new AlertShowingDialog(context, message);
            }
        }
    }
    public UserServerObject[] forgotresendApiExecution(String str_email, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();

        requestBody.mailid = str_email.trim();
        requestBody.register_time = getCurrentTime();


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.register_time);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.forgotAPiExection(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                if (response.body() != null) {
                    ForgotPasswordVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                    String statusCode = response.body().result;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    //userServerObjectResponse =response;
                    Log.e("forgotstatuscode", "call" + statusCode);

                    if (statusCode.equals("success")) {
                        new AlertShowingDialog(context, "Otp send successfully");
                    } else if (statusCode.equals("error")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("1")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("0")) {
                        new AlertShowingDialog(context, message);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                Log.e("message", "onFailure" + t.getMessage());
                statusCode1[0] = "Failure";
                ForgotPasswordVerificationActivity.refreshShowingDialog.hideRefreshDialog();
                user[0] = null;
            }
        });
        return user;
    }
    //RegisterAps////
    public UserServerObject[] newPasswordApiExecution(String str_email, String password, final Context context) {
        final String[] statusCode1 = {null};
        final UserServerObject[] user = {new UserServerObject()};
        final UserServerObject requestBody = new UserServerObject();

        requestBody.mailid = str_email.trim();
        requestBody.password = password;


        Log.e("st_emailorphone", "call" + str_email.trim());
        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<UserServerObject> callable = api.newPasswordAPiExection(requestBody);
        callable.enqueue(new Callback<UserServerObject>() {
            @Override
            public void onResponse(Call<UserServerObject> call, Response<UserServerObject> response) {
                if (response.body() != null) {
                    NewPasswordViewActivity.refreshShowingDialog.hideRefreshDialog();
                    String statusCode = response.body().result;
                    String message = response.body().message;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("message", "" + message);
                    statusCode1[0] = statusCode;
                    user[0] = response.body();
                    Log.e("user[0]", "" + user[0].result);
                    Log.e("newpawstatuscode", "call" + statusCode);

                    if (statusCode.equals("success")) {
                        Intent intent1 = new Intent();
                        intent1.setClass(context, LoginViewActivity.class);
                        intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        context.startActivity(intent1);

                    } else if (statusCode.equals("error")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("1")) {
                        new AlertShowingDialog(context, message);
                    } else if (statusCode.equals("0")) {
                        new AlertShowingDialog(context, message);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserServerObject> call, Throwable t) {
                Log.e("message", "onFailure" + t.getMessage());
                statusCode1[0] = "Failure";
                NewPasswordViewActivity.refreshShowingDialog.hideRefreshDialog();
                user[0] = null;
            }
        });
        return user;
    }

}
