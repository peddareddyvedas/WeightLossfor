package com.vedas.weightloss.LoginModule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.Constants;
import com.vedas.weightloss.Alert.RefreshShowingDialog;
import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.Controllers.LoginModuleApisController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.Models.User;
import com.vedas.weightloss.R;
import com.vedas.weightloss.ServerApis.LocationTracker;
import com.vedas.weightloss.Settings.PersonalInfoActivity;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class LoginViewActivity extends BaseDetailActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    TextView title;
    RelativeLayout rl_main;
    public static RefreshShowingDialog refreshShowingDialog;
    EditText userNameTextField, textInputPassword;
    ProgressDialog mProgress;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private int RC_SIGN_IN = 100;
    Button fb;
    private CallbackManager callbackManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    String socialmediaLoginEmail;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        ButterKnife.bind(this);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        title = (TextView) findViewById(R.id.weightloss);
        refreshShowingDialog = new RefreshShowingDialog(LoginViewActivity.this);
        userNameTextField = (EditText) findViewById(R.id.edittext1);
        textInputPassword = (EditText) findViewById(R.id.edittext2);

        mProgress = new ProgressDialog(LoginViewActivity.this);
        mProgress.setMessage("Loading...");
        mProgress.setProgress(Color.BLACK);
        mProgress.setCancelable(true);
        mProgress.setCanceledOnTouchOutside(false);

        sharedPreferences = getApplicationContext().getSharedPreferences("socialMediaLoginDetails", Context.MODE_PRIVATE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                Log.e("sdf", "asdfas" + newToken);
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        fb = (Button) findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConn()) {
                    mProgress.show();
                    if (v == fb) {
                        LoginManager.getInstance().logInWithReadPermissions(LoginViewActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
                        LoginManager.getInstance().registerCallback(callbackManager, callback);
                    }
                } else {
                    mProgress.dismiss();
                    new AlertShowingDialog(LoginViewActivity.this, "No Internet connection");
                }
            }
        });
    }

    @OnClick(R.id.google)
    public void google() {
        mProgress.show();
        if (isConn()) {
            mProgress.show();
            signIn();
        } else {
            new AlertShowingDialog(LoginViewActivity.this, "No Internet Connection");
            mProgress.dismiss();

        }
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.e("FBStatus", "onSuccess Called");

            System.out.println("onSuccess");

            String accessToken = loginResult.getAccessToken()
                    .getToken();
            Log.i("accessToken", accessToken);

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            Log.i("LoginActivity", response.toString());
                            try {
                                String id = object.getString("id");
                                String name = object.getString("name");
                                String email = object.getString("email");
                                // String gender = object.getString("gender");
                                if (email != null) {
                                    mProgress.dismiss();
                                    sharedPreferencesEditor = sharedPreferences.edit();
                                    sharedPreferencesEditor.putString("name", name);
                                    Log.e("username", "" + name);
                                    //   sharedPreferencesEditor.putString("gender", gender);
                                    String imageURLString = "http://graph.facebook.com/" + id + "/picture?type=large";
                                    sharedPreferencesEditor.putString("picture", imageURLString);
                                    sharedPreferencesEditor.putString("Type", "Facebook");
                                    //  Log.e("gender", "" + gender);
                                    Log.e("gender", "" + imageURLString);
                                    // Log.e("emailfacebook", "call" + name + "" + email + "" + gender);
                                    socialmediaLoginEmail = email;
                                    sharedPreferencesEditor.commit();
                                    User user = new User();
                                    user.setEmail(socialmediaLoginEmail);
                                    user.setName(name);
                                    user.setLatitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()));
                                    user.setLongitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude()));
                                    user.setVerified(true);
                                    user.setPassword("");
                                    user.setRegisterTime(getCurrentTime());
                                    user.setRegisterType(Constants.RegisterTypes.Facebook.toString());
                                    UserDataController.getInstance().insertUserData(user);

                                    LoginManager.getInstance().logOut();
                                    mProgress.dismiss();
                                    startActivity(new Intent(getApplicationContext(), PersonalInfoActivity.class));

                                } else {
                                    Log.e("emailnull", "call");
                                    LoginManager.getInstance().logOut();
                                    mProgress.dismiss();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields",
                    "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {
            mProgress.dismiss();

            Log.e("FBStatus", "OnCancel Called");
        }

        @Override
        public void onError(FacebookException e) {
            Log.e("FBStatus", "OnCancel Called" + e);
            mProgress.dismiss();

        }
    };

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("requestcode", "" + requestCode + " " + resultCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            Log.e("statuscode", "calldd" + statusCode);

            handleSignInResult(result);
        } else {
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("handleSignInResult", "" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            socialmediaLoginEmail = acct.getEmail();
            String idString = acct.getId();
            String NameString = acct.getDisplayName();
            socialmediaLoginEmail = acct.getEmail();
            Log.e("email", "" + socialmediaLoginEmail);
            Log.e("id", "" + idString);
            Log.e("Name", "" + NameString);
            mProgress.dismiss();
            sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("name", acct.getDisplayName());
            sharedPreferencesEditor.putString("gender", "Female");
            if (acct.getPhotoUrl() == null) {
                //set default image
                Log.e("empptyImag", "call" + acct.getPhotoUrl());
            } else {
                Log.e("pptyImag", "call" + acct.getPhotoUrl().toString());
                sharedPreferencesEditor.putString("picture", acct.getPhotoUrl().toString());
            }

            sharedPreferencesEditor.putString("Type", "Google");

            Log.e("username", "" + acct.getDisplayName());
            Log.e("username1", "" + acct.getEmail());
            sharedPreferencesEditor.commit();

            User user = new User();
            user.setEmail(socialmediaLoginEmail);
            user.setName(NameString);
            user.setLatitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLatitude()));
            user.setLongitude(String.valueOf(LocationTracker.getInstance().currentLocation.getLongitude()));
            user.setVerified(true);
            user.setPassword("");
            user.setRegisterTime(getCurrentTime());
            user.setRegisterType(Constants.RegisterTypes.Google.toString());
            UserDataController.getInstance().insertUserData(user);

            Intent intent1 = new Intent();
            intent1.setClass(getApplicationContext(), PersonalInfoActivity.class);
            intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
            startActivity(intent1);
            //startActivity(new Intent(getApplicationContext(), HomeActivityViewController.class));
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

        } else {

            mProgress.dismiss();
            Toast.makeText(getApplicationContext(), "Failed login", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnCheckedChanged(R.id.chk)
    public void onChecked(boolean checked) {
        if (checked) {
            textInputPassword.setTransformationMethod(null);
        } else {
            textInputPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
        // cursor reset his position so we need set position to the end of text
        textInputPassword.setSelection(textInputPassword.getText().length());
    }

    @OnClick(R.id.button)
    public void buttonAction() {
        mLogin();
    }

    @OnClick({R.id.signup})
    void signupAction() {
        //finish();
        startActivity(new Intent(getApplicationContext(), RegisterViewActivity.class));
    }

    @OnClick({R.id.forgot})
    void forgotAction() {
        moveToForgetPasswordPage(userNameTextField.getText().toString());
    }

    private void moveToForgetPasswordPage(String emailInfo) {

        Intent intent = new Intent(LoginViewActivity.this, ForgotViewActivity.class);
        intent.putExtra("email", emailInfo);
        startActivity(intent);

    }

    //////Validations for loigin////
    public void mLogin() {
        String st_emailandphone, st_password;
        st_emailandphone = userNameTextField.getText().toString();
        st_password = textInputPassword.getText().toString();

        if (st_emailandphone.length() > 0) {
            if (st_password.length() > 0) {
                if (isConn()) {
                    PersonalInfoController.getInstance().fillContext(getApplicationContext());
                    refreshShowingDialog.showAlert();
                    LoginModuleApisController.getInstance().loginApiExecution(st_emailandphone, st_password, this);

                } else {
                    new AlertShowingDialog(LoginViewActivity.this, "No Internet Connection");
                }
            } else {
                new AlertShowingDialog(LoginViewActivity.this, "Please enter your password");
            }
        } else {
            new AlertShowingDialog(LoginViewActivity.this, "Please enter your email");
        }
    }

    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    public String getCurrentTime() {

        long unixTime;
        unixTime = System.currentTimeMillis() / 1L;
        String attempt_time = String.valueOf(System.currentTimeMillis() / 1L);

        Log.e("attem", "" + attempt_time);
        return attempt_time;

    }

    @Override
    public void onBackPressed() {    //when click on phone backbutton
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}