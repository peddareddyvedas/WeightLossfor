package com.vedas.weightloss.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by VEDAS on 5/5/2018.
 */
public class ActivityLevelActivity extends BaseDetailActivity {
    int type;
    RecyclerView recyclerView;
    ActivityLevel activityLevel;
    ArrayList<String> levelArray;
    ArrayList<String> messageArray;
    int selectedPosition = 0;
    private final static int FADE_DURATION = 1000;
    String activityType = "Not very Active";
    String calories="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitylevel);
        setupToolbar();
        loadArrays();
        setupRecyclerView();
        //setupWindowAnimations();
       // checkIsUserHavingData();
        setUserData();
    }
    @Override
    public void onResume() {
        super.onResume();
        //checkIsUserHavingData();
    }
    private void setUserData(){
        if (PersonalInfoController.getInstance().retrivePersonalDataFromUserDefaults()){

            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmr() !=null ){
                calculateCaloriesFromBmr(Double.parseDouble(PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmr()));
            }
            Log.e("selectedmodel","call");
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getActivityLevel() !=null )
            {
                int index=levelArray.indexOf(PersonalInfoController.getInstance().selectedPersonalInfoModel.getActivityLevel());
                selectedPosition=index;
            }
        }
    }
    /*private void checkIsUserHavingData(){
        if (PersonalInfoDataController.getInstance().personalInfoArray.size() > 0) {
            PersonalInfoDataController.getInstance().fetchPersonalInfoData();
            Log.e("selectedmodel", "call" + PersonalInfoDataController.getInstance().personalInfoArray);
            PersonalInfoModel objPersonalInfoModel = PersonalInfoDataController.getInstance().currentMember;
            int index=levelArray.indexOf(objPersonalInfoModel.getActivityLevel());
            selectedPosition=index;
            calculateCaloriesFromBmr(Double.parseDouble(objPersonalInfoModel.getBmr()));
        }
    }*/
    void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        textView.setText("Activity Level");
        Button btn_back = (Button) toolbar.findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.centerImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLevelActivity.this, WeekGoalActivity.class);
                i.putExtra(calories,"calories");
                i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                transitionTo(i);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        activityLevel = new ActivityLevel(getApplication());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(activityLevel);
    }

    private void loadArrays() {
        levelArray = new ArrayList<>();
        messageArray = new ArrayList<>();

        levelArray.add("Not very Active");
        levelArray.add("Lightly Active");
        levelArray.add("Active");
        levelArray.add("Very Active");

        messageArray.add("Spend most of the day sitting(e.g.bank teller,desk job)");
        messageArray.add("Spend a good part of the day on your feet(e.g.teacher,salesperson)");
        messageArray.add("Spend a good part of the day doing some physical activity(e.g.food server,postal carrier)");
        messageArray.add("Spend most of the day doing physical activity(bike messenger,carpenter)");
    }

    // Step 1:-
    public class ActivityLevel extends RecyclerView.Adapter<ActivityLevel.ViewHolder> {
        // step 3:-
        Context ctx;
        ImageView button;

        public ActivityLevel(Context ctx) {
            this.ctx = ctx;
        }

        // step 5:-
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activitylevel_items, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx);
            return myViewHolder;
        }

        //step 6:-
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt_level.setText(levelArray.get(position));
            holder.txt_message.setText(messageArray.get(position));
            if (selectedPosition == position) {
                activityType = levelArray.get(selectedPosition);
                Log.e("activityType", "call" + activityType);
                holder.radioButton.setChecked(true);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setActivityLevel(activityType);
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                if (PersonalInfoController.getInstance().retrivePersonalDataFromUserDefaults()){
                    if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmr() !=null ){
                        calculateCaloriesFromBmr(Double.parseDouble(PersonalInfoController.getInstance().selectedPersonalInfoModel.getBmr()));
                    }
                }
            } else {
                holder.radioButton.setChecked(false);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    Log.e("pos", "" + selectedPosition);
                    activityType = levelArray.get(selectedPosition);
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setActivityLevel(activityType);
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                    Log.e("pos", "" + PersonalInfoController.getInstance().selectedPersonalInfoModel.getActivityLevel());
                    notifyDataSetChanged();
                }
            });

        }
        @Override
        public int getItemCount() {
            return levelArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txt_level, txt_message;
            Context ctx;
            RadioButton radioButton;

            public ViewHolder(View itemView, Context ctx) {
                super(itemView);
                this.ctx = ctx;
                txt_level = (TextView) itemView.findViewById(R.id.mode);
                txt_message = (TextView) itemView.findViewById(R.id.txt);
                radioButton = (RadioButton) itemView.findViewById(R.id.radio);
                //setScaleAnimation(itemView);
                //loadAnimatonToRecyclerView();
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    private void calculateCaloriesFromBmr(double bmr) {
        double calories_activity=0.0;
        DecimalFormat df = new DecimalFormat("#0.0");
        if(activityType.equals("Not very Active"))
        {
            calories_activity=bmr*1.2;
        }
        if(activityType.equals("Lightly Active"))
        {
            calories_activity=bmr*1.375;
        }
        if(activityType.equals("Active"))
        {
            calories_activity=bmr*1.55;
        }
        if(activityType.equals("Very Active"))
        {
            calories_activity=bmr*1.725;
        }
        Log.e("calories_activity","call"+calories_activity);
        calories_activity=(int) calories_activity;
        Log.e("calories","call"+df.format(calories_activity)+" kcal");
        calories=String.valueOf(calories_activity);
        WeekGoalActivity.calories=calories;

    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    public void loadAnimatonToRecyclerView() {
        // for adding animatio to recyclerview
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);

    }

    private void setupWindowAnimations() {
        Transition transition;

        if (type == TYPE_PROGRAMMATICALLY) {
            transition = buildEnterTransition();
        } else {
            transition = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        }
        getWindow().setEnterTransition(transition);
    }

    /*private Transition buildEnterTransition() {
        Slide enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setSlideEdge(Gravity.LEFT);
        return enterTransition;
    }*/
    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

    @Override
    public void onBackPressed() {    //when click on phone backbutton
        finish();
    }

}
