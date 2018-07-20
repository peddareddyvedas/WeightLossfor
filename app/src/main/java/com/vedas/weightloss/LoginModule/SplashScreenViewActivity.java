package com.vedas.weightloss.LoginModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.Controllers.NewsFeedController;
import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.Controllers.LoginModuleApisController;
import com.vedas.weightloss.Controllers.RegionController;
import com.vedas.weightloss.DashBoardModule.DashBoardTabsActivity;
import com.vedas.weightloss.DataBase.BreakfastDataController;
import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.DinnerDataController;
import com.vedas.weightloss.DataBase.ExerciseDataController;
import com.vedas.weightloss.DataBase.LunchDataController;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.DataBase.SnacksDataController;
import com.vedas.weightloss.DataBase.UnitsDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.DataBase.WaterDataController;
import com.vedas.weightloss.R;
import com.vedas.weightloss.ServerApis.LocationTracker;
import com.vedas.weightloss.Settings.PersonalInfoActivity;

import java.security.MessageDigest;

import butterknife.ButterKnife;

public class SplashScreenViewActivity extends AppCompatActivity {
    TextView title;
    Handler handler;
    final static int REQUEST_LOCATION = 199;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST = 112;
    Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splachscreen);
        ButterKnife.bind(this);
        title = (TextView) findViewById(R.id.weightloss);

        handler = new Handler();

        LocationTracker.getInstance().fillContext(getApplicationContext());
        LocationTracker.getInstance().startLocation();
        LoginModuleApisController.getInstance().fillContext(getApplicationContext());
        PersonalInfoController.getInstance().fillContext(getApplicationContext());
        DayFoodController.getInstance().fillContext(getApplicationContext());
        NewsFeedController.getInstance().fillCOntext(getApplicationContext());
        DayFoodController.getInstance().loadHeaderFooterArray();
        RegionController.getInstance().fillCOntext(getApplicationContext());
        //
        UserDataController.getInstance().fillContext(getApplicationContext());
        UserDataController.getInstance().fetchUserData();
        if (UserDataController.getInstance().allUsers.size() > 0) {
            PersonalInfoDataController.getInstance().fetchPersonalInfoData();
            UnitsDataController.getInstance().fetchUnitsData(UserDataController.getInstance().currentUser);
        }


        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            Log.e("Version", "Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
            String[] PERMISSIONS = {
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };


            if (!hasPermissions(mContext, PERMISSIONS)) {
                Log.d("TAG", "@@@ IN IF hasPermissions");
                enableLoc();
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
            } else {
                Log.d("TAG", "@@@ IN ELSE hasPermissions");
                //enableLoc();
                checker();
            }
        } else {
            Log.d("TAG", "@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
            // enableLoc();
            checker();

        }


    }

    private void checkIfUserHavingData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserDataController.getInstance().allUsers.size() > 0) {
                    if (PersonalInfoDataController.getInstance().personalInfoArray.size() > 0) {
                        NewsFeedController.getInstance().fetchnews();
                        DayFoodDataController.getInstance();
                        DayFoodDataController.getInstance().fetchdayfoodData();
                        BreakfastDataController.getInstance();
                        BreakfastDataController.getInstance().fetchBreakfastData(DayFoodDataController.getInstance().currentFoodObject);
                        LunchDataController.getInstance();
                        LunchDataController.getInstance().fetchLunchData(DayFoodDataController.getInstance().currentFoodObject);
                        DinnerDataController.getInstance();
                        DinnerDataController.getInstance().fetchdinnerData(DayFoodDataController.getInstance().currentFoodObject);
                        SnacksDataController.getInstance();
                        SnacksDataController.getInstance().fetchSnacksData(DayFoodDataController.getInstance().currentFoodObject);
                        ExerciseDataController.getInstance();
                        ExerciseDataController.getInstance().fetchExerciseData(DayFoodDataController.getInstance().currentFoodObject);
                        WaterDataController.getInstance();
                        WaterDataController.getInstance().fetchWaterData(DayFoodDataController.getInstance().currentFoodObject);
                        startActivity(new Intent(getApplicationContext(), DashBoardTabsActivity.class));
                    } else {
                        NewsFeedController.getInstance().fetchnews();
                        startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginViewActivity.class));
                }
            }

        }, 1000);

    }
////////////Multiple Premession and alerts//////

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "@@@ PERMISSIONS grant");
                    // callNextActivity();
                    checkIfUserHavingData();


                } else {
                    Log.d("TAG", "@@@ PERMISSIONS Denied");
                    Toast.makeText(mContext, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checker() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            checkIfUserHavingData();

        } else {
            enableLoc();
        }

    }



    ///////////
    public static class MessageEvent {
        public final String message;

        public MessageEvent(String message)
        {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }


    //// Below 6.0.1 Laction can be on /////
    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            // checkIfUserHavingData();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true); //this is the key ingredient

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {


                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(SplashScreenViewActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;
                    }
                    case Activity.RESULT_OK: {
                        // The user was asked to change settings, but chose not to
                        checkIfUserHavingData();

                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }

    }


    @Override
    public void onBackPressed() {    //when click on phone backbutton
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    /*public static void printHashKey(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("AppLog", "key:" + hashKey );
            }
        } catch (Exception e) {
            Log.e("AppLog", "error:", e);
        }
    }*/
}