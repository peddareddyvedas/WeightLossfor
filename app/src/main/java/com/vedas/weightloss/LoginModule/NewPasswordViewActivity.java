package com.vedas.weightloss.LoginModule;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.RefreshShowingDialog;
import com.vedas.weightloss.Controllers.LoginModuleApisController;
import com.vedas.weightloss.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class NewPasswordViewActivity extends AppCompatActivity {
    TextView title;
    String emailString;
    EditText passwordTextField;
    public static RefreshShowingDialog refreshShowingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpassword_view);
        ButterKnife.bind(this);
        title = (TextView) findViewById(R.id.weightloss);
        passwordTextField = (EditText) findViewById(R.id.edittext);
        refreshShowingDialog=new RefreshShowingDialog(NewPasswordViewActivity.this);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            emailString = (String) bd.get("email");
            Log.e("emialstring", "call" + emailString);

        }
    }
    @OnCheckedChanged(R.id.chk)
    public void onChecked1(boolean checked) {
        if (checked) {
            passwordTextField.setTransformationMethod(null);
        } else {
            passwordTextField.setTransformationMethod(new PasswordTransformationMethod());
        }
        // cursor reset his position so we need set position to the end of text
        passwordTextField.setSelection(passwordTextField.getText().length());
    }
    @OnClick(R.id.button)
    public void buttonAction() {
        mConformpassword();
    }

    public void mConformpassword() {
        String st_password;
        st_password = passwordTextField.getText().toString();

        if (st_password.length() > 0) {
            if (isValidPasword(st_password)) {
                if (isConn()) {
                    refreshShowingDialog.showAlert();
                    LoginModuleApisController.getInstance().newPasswordApiExecution(emailString, st_password, this);
                } else {
                    new AlertShowingDialog(NewPasswordViewActivity.this, "No Internet Connection");
                }


            } else {
                new AlertShowingDialog(NewPasswordViewActivity.this, "Password must contain at least 1 number, 1 letter, 1 special characters, and minimum of 8 characters in length without space.");

            }
        } else {

            new AlertShowingDialog(NewPasswordViewActivity.this, "Please enter your password");

        }
    }

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

    public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}