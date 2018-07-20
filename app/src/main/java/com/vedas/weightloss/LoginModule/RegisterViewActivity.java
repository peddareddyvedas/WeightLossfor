package com.vedas.weightloss.LoginModule;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.RefreshShowingDialog;
import com.vedas.weightloss.Controllers.LoginModuleApisController;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class RegisterViewActivity extends BaseDetailActivity {
    TextView title;
    EditText userEmailTextField, textInputPassword, textInputPassword1,userNameTextField;
   public static RefreshShowingDialog refreshShowingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);
        ButterKnife.bind(this);
        title = (TextView) findViewById(R.id.weightloss);
        refreshShowingDialog = new RefreshShowingDialog(RegisterViewActivity.this);

        userEmailTextField = (EditText) findViewById(R.id.edittext1);
        textInputPassword = (EditText) findViewById(R.id.edittext2);
        textInputPassword1 = (EditText) findViewById(R.id.edittext3);
        userNameTextField=(EditText)findViewById(R.id.edittextname);


    }
    @OnCheckedChanged(R.id.chk)
    public void onChecked1(boolean checked) {
        if (checked) {
            textInputPassword.setTransformationMethod(null);
        } else {
            textInputPassword.setTransformationMethod(new PasswordTransformationMethod());
        }
        // cursor reset his position so we need set position to the end of text
        textInputPassword.setSelection(textInputPassword.getText().length());
    }
    @OnCheckedChanged(R.id.chk1)
    public void onChecked(boolean checked) {
        if (checked) {
            textInputPassword1.setTransformationMethod(null);
        } else {
            textInputPassword1.setTransformationMethod(new PasswordTransformationMethod());
        }
        // cursor reset his position so we need set position to the end of text
        textInputPassword1.setSelection(textInputPassword1.getText().length());
    }
    @OnClick(R.id.button)
    public void buttonAction() {
        mRegisterAction();
    }

    @OnClick({R.id.account})
    void accountAction() {
        // finish();
        startActivity(new Intent(getApplicationContext(), LoginViewActivity.class));
    }

    private void mRegisterAction() {
        String st_email,st_name, st_password, st_confirm_password;

        st_name = userNameTextField.getText().toString();
        st_email = userEmailTextField.getText().toString();
        st_password = textInputPassword.getText().toString();
        st_confirm_password = textInputPassword1.getText().toString();

        if (st_name.length()>0){
        if (st_email.length() > 0) {
            if (isValidEmail(st_email)) {
                if (st_password.length() > 0) {
                    if (isValidPasword(st_password)) {
                        if (st_confirm_password.length() > 0) {
                            if (st_password.equals(st_confirm_password)) {
                                if (isConn()) {
                                    refreshShowingDialog.showAlert();
                                    LoginModuleApisController.getInstance().registerApiExecution(st_name,st_email, st_password, this);
                                } else {
                                    new AlertShowingDialog(RegisterViewActivity.this, "No Internet Connection");
                                }
                            } else {
                                new AlertShowingDialog(RegisterViewActivity.this, "Please enter the same password as above");

                            }

                        } else {
                            new AlertShowingDialog(RegisterViewActivity.this, "Please enter confirm password");
                        }

                    } else {

                        new AlertShowingDialog(RegisterViewActivity.this, "Password must contain at least 1 number, 1 letter, 1 special characters, and minimum of 8 characters in length without space.");
                    }

                } else {
                    new AlertShowingDialog(RegisterViewActivity.this, "Please enter your password");
                }

            } else {

                new AlertShowingDialog(RegisterViewActivity.this, "Please enter a valid email");
            }

        } else {
            new AlertShowingDialog(RegisterViewActivity.this, "Please enter your email");
        }
        }else {
            new AlertShowingDialog(RegisterViewActivity.this, "Please enter your name");
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

    public boolean isValidEmail(String target) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(target).matches();
    }

    // Validate password
    private boolean isValidPasword(String password) {
        boolean isValid = false;

        String expression = "^(?=.*[a-z])(?=.*[$@$#!%*?&])[A-Za-z\\d$@$#!%*?&]{8,}";
        CharSequence inputStr = password;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            System.out.println("if");
            isValid = true;
        } else {
            System.out.println("else");
        }
        return isValid;
    }
    //RegisterAps////
    public String getCurrentTime() {
        String attempt_time = String.valueOf(System.currentTimeMillis() / 1000);
        Log.e("attem", "" + attempt_time);
        return attempt_time;
    }
    @Override
    public void onBackPressed() {    //when click on phone backbutton
        finish();
    }
}