package com.vedas.weightloss.MoreModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.FoodModule.DayOneActivity;
import com.vedas.weightloss.Models.PersonalInfoModel;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Settings.PersonalInfoActivity;
import com.vedas.weightloss.Settings.PersonalInfoNextActivity;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rise on 12/06/2018.
 */

public class GoalsActivity extends BaseDetailActivity {
    TextView startingkg, currentweight, goalweight, weeklygoal,
            activitylevel, caloriesperday;
    Toolbar toolbar;
    ImageView btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        ButterKnife.bind(this);
        setToolbar();
        init();
    }
    @OnClick({R.id.set})
    public void updateaction()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GoalsActivity.this);
        alertDialogBuilder.setMessage("Do you want to update your goal plan ?")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        final Intent i = new Intent(GoalsActivity.this, PersonalInfoActivity.class);
                        transitionTo(i);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#FF0B8B42"));

        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#FF0012"));

    }
    @Override
    public void onResume() {
        super.onResume();
        loadGoalsdataFromDb();
    }
    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbartext.append("Goals");
    }
    private void init() {
        startingkg = (TextView) findViewById(R.id.startingkg);
        currentweight = (TextView) findViewById(R.id.currentweight);
        goalweight = (TextView) findViewById(R.id.goalweight);
        weeklygoal = (TextView) findViewById(R.id.weeklygoal);
        activitylevel = (TextView) findViewById(R.id.activitylevel);
        caloriesperday = (TextView) findViewById(R.id.caloriesperday);
    }
    private void loadGoalsdataFromDb() {
        if (PersonalInfoDataController.getInstance().currentMember != null) {
            PersonalInfoModel objModel=PersonalInfoDataController.getInstance().currentMember;
            startingkg.setText(""+objModel.getWeight());
            currentweight.setText(""+objModel.getWeight());
            goalweight.setText(""+objModel.getTargetWeight()+" "+"Kg");
            weeklygoal.setText(""+objModel.getRecommendedPlan());
            activitylevel.setText(""+objModel.getActivityLevel());
            caloriesperday.setText(""+objModel.getTargetCalories()+" "+"cal");
        }

    }
}
