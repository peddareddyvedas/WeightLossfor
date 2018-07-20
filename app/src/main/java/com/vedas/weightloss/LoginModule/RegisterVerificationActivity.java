package com.vedas.weightloss.LoginModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;

import com.goodiebag.pinview.Pinview;
import com.vedas.weightloss.Alert.RefreshShowingDialog;
import com.vedas.weightloss.Controllers.LoginModuleApisController;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterVerificationActivity extends BaseDetailActivity {
    int type;
    View contextView;
    Pinview pinview;
    String email, name, regTime, password;
    public static RefreshShowingDialog refreshShowingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        type = getIntent().getExtras().getInt(EXTRA_TYPE);
        setupWindowAnimations();
        ButterKnife.bind(this);
        contextView = findViewById(R.id.context_view);
        refreshShowingDialog = new RefreshShowingDialog(RegisterVerificationActivity.this);

        pinview = (Pinview) findViewById(R.id.pinview);
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                //  Toast.makeText(getApplicationContext(), pinview.getValue(), Toast.LENGTH_SHORT).show();
                //loadPinView(pinview);
            }
        });

        SharedPreferences tokenPreferences = getApplicationContext().getSharedPreferences("regDetails", Context.MODE_PRIVATE);
        email = tokenPreferences.getString("email", null);
        name = tokenPreferences.getString("name", null);
        regTime = tokenPreferences.getString("regtime", null);
        password = tokenPreferences.getString("password", null);
        Log.e("registerdetails", "call " + email + "ids" + regTime + password + "callname" + name);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadPinView(Pinview pinview) {
        if (pinview.getValue().length() >= 4) {
            refreshShowingDialog.showAlert();
            LoginModuleApisController.getInstance().verifyApiExecution(name, email, pinview.getValue(), this);
        } else {
            Snackbar.make(contextView, R.string.item_set_pin, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.CYAN)
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }

    }

    private void setupWindowAnimations() {
        Transition transition;

        if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        } else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.slide);
        }
        getWindow().setEnterTransition(transition);
    }
    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        return enterTransition;
    }

    @OnClick({R.id.next_btn})
    void newtAction() {
        loadPinView(pinview);
    }

    @OnClick({R.id.resend})
    void resendActon() {
        refreshShowingDialog.showAlert();
        LoginModuleApisController.getInstance().resendApiExecution(email, password, this);
    }
}