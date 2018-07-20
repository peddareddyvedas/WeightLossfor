package com.vedas.weightloss.LoginModule;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.RefreshShowingDialog;
import com.vedas.weightloss.Controllers.LoginModuleApisController;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotViewActivity extends BaseDetailActivity {
    TextView title;
    EditText userNameTextField;
    public static RefreshShowingDialog refreshShowingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_view);
        //setupWindowAnimations();
        ButterKnife.bind(this);
        title=(TextView)findViewById(R.id.weightloss) ;
        userNameTextField = (EditText) findViewById(R.id.edittext1);
        refreshShowingDialog=new RefreshShowingDialog(ForgotViewActivity.this);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            String emailString  = (String) bd.get("email");
            userNameTextField.setText(emailString);

        }

    }
    @OnClick(R.id.button)
    public void buttonAction(){
        mLogin();
    }
    //////Validations for loigin////
    public void mLogin() {
        String st_email;

        st_email = userNameTextField.getText().toString();

        if (st_email.length() > 0)
        {
            if (isValidEmail(st_email))
            {
                if (isConn()) {
                    refreshShowingDialog.showAlert();
                    LoginModuleApisController.getInstance().forgotApiExecution(st_email,this);

                } else {
                    new AlertShowingDialog(ForgotViewActivity.this,"No Internet Connection");
                }
            } else {
                new AlertShowingDialog(ForgotViewActivity.this,"Please enter a valid email");
            }
        } else {
            new AlertShowingDialog(ForgotViewActivity.this,"Please enter your email");
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


     /*private Transition buildEnterTransition() {
        Slide enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setSlideEdge(Gravity.LEFT);
        return enterTransition;
    }*/
    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        return enterTransition;
    }
}