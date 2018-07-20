package com.vedas.weightloss.Settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.Models.WeekDaysObject;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.util.ArrayList;

/**
 * Created by VEDAS on 5/5/2018.
 */
public class WeekGoalActivity extends BaseDetailActivity {
    int type;
    RecyclerView recyclerView, daysrecyclerView;
    ActivityLevel activityLevel;
    ArrayList<String> kgArray;
    ArrayList<String> messageArray;
    int selectedPosition = 1;
    EditText textInputKgs;
    public static String calories = "";
    String weekGoalType = "Lose 0.5 kg for week";
    int caloriesGoal;
    //weeks adapter
    WeekDaysAdapter weekDaysAdapter;
    ArrayList<WeekDaysObject> weekDaysModelList;
    String daysStr = "";
    boolean shouldRun = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekgoal);
        setupToolbar();
        setupWindowAnimations();
        loadWeekdaysArray();
       /* if (PersonalInfoDataController.getInstance().personalInfoArray.size() > 0) {
            PersonalInfoDataController.getInstance().fetchPersonalInfoData();
            PersonalInfoModel objModel = PersonalInfoDataController.getInstance().currentMember;
            String objDayArr[] = objModel.getTargetDays().split(",");
            loadWeekdaysArrayFromDb(objDayArr);
        } else {
            loadWeekdaysArray();
        }*/
        loadArrays();
        setUserData();
        setupRecyclerView();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textInputKgs.setVisibility(View.VISIBLE);
            }
        }, 500);

        if (calories.length() > 0) {
            Log.e("calories", "call" + calories);
            calculateCaloriesDependsOnWeeklygoal(calories);
        }
        textInputKgs.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.e("EditText", "call" + textInputKgs.getText().toString());
                String st_emailandphone = textInputKgs.getText().toString();
                if (st_emailandphone.length() > 0) {
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setTargetWeight(textInputKgs.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                }
            }
        });
        textInputKgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        setUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setUserData() {
        if (PersonalInfoController.getInstance().retrivePersonalDataFromUserDefaults()) {
            Log.e("selectedmodel", "call");
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getRecommendedPlan() != null) {
                int index = kgArray.indexOf(PersonalInfoController.getInstance().selectedPersonalInfoModel.getRecommendedPlan());
                selectedPosition = index;
            }
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getTargetWeight() != null) {
                textInputKgs.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getTargetWeight());
            }
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getTargetDays() != null) {
                daysStr = PersonalInfoController.getInstance().selectedPersonalInfoModel.getTargetDays();
                String objDayArr[] = PersonalInfoController.getInstance().selectedPersonalInfoModel.getTargetDays().split(",");
                loadWeekdaysArrayFromDb(objDayArr);
            }

        } else {
            loadWeekdaysArray();
        }
    }

    /*private void checkIsUserHavingData() {
        if (PersonalInfoDataController.getInstance().personalInfoArray.size() > 0) {
            PersonalInfoDataController.getInstance().fetchPersonalInfoData();
            Log.e("selectedmodel", "call" + PersonalInfoDataController.getInstance().personalInfoArray);
            PersonalInfoModel objPersonalInfoModel = PersonalInfoDataController.getInstance().currentMember;
            int index = kgArray.indexOf(objPersonalInfoModel.getRecommendedPlan());
            selectedPosition = index;
            Log.e("getTargetWeight", "call" + objPersonalInfoModel.getTargetWeight());
            Log.e("getTargetdays", "call" + objPersonalInfoModel.getTargetDays());

            textInputKgs.setText("" + objPersonalInfoModel.getTargetWeight());
            calculateCaloriesDependsOnWeeklygoal(calories);
        }
    }
*/
    private void calculateCaloriesDependsOnWeeklygoal(String calories) {
        Double d = Double.parseDouble(calories);
        // String[] mySplit = calories.split("\\.");
        // int calories_activity=Math.round(d);
        Integer calories_activity = d.intValue();
        Log.e("calories_activity", "call" + calories_activity);
        if (weekGoalType.equals("Lose 0.25 kg for week")) {
            caloriesGoal = calories_activity - 250;
        }
        if (weekGoalType.equals("Lose 0.5 kg for week")) {
            caloriesGoal = calories_activity - 500;
        }
        if (weekGoalType.equals("Lose 1 kg per week")) {
            caloriesGoal = calories_activity - 750;
        }
        if (weekGoalType.equals("Lose 2 kg for week")) {
            caloriesGoal = calories_activity - 1000;
        }
        Log.e("caloriesGoal", "call" + caloriesGoal);
        PersonalInfoController.getInstance().selectedPersonalInfoModel.setTargetCalories(String.valueOf(caloriesGoal));
        PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
    }

    void setupToolbar() {
        textInputKgs = (EditText) findViewById(R.id.goal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        textView.setText("Weekly Goal");
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
                if (textInputKgs.getText().toString().length() > 0) {
                    if (daysStr.length() > 0) {
                        Intent i = new Intent(WeekGoalActivity.this, WeekGoalNextActivity.class);
                        i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        transitionTo(i);
                    } else {
                        final View contextView = findViewById(R.id.context_view);
                        Snackbar.make(contextView, R.string.selectdays, Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.CYAN)
                                .setAction(R.string.undo, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }).show();
                    }
                } else {
                    final View contextView = findViewById(R.id.context_view);
                    Snackbar.make(contextView, R.string.selectkgs, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.CYAN)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();
                }

            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        activityLevel = new ActivityLevel(getApplication());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(activityLevel);

        //for weekdays
        daysrecyclerView = (RecyclerView) findViewById(R.id.recylerview1);
        weekDaysAdapter = new WeekDaysAdapter(getApplication());
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        daysrecyclerView.setLayoutManager(horizontalLayoutManager1);
        daysrecyclerView.setAdapter(weekDaysAdapter);
    }

    private void loadArrays() {
        kgArray = new ArrayList<>();
        messageArray = new ArrayList<>();

        kgArray.add("Lose 0.25 kg for week");
        kgArray.add("Lose 0.5 kg for week");
        kgArray.add("Lose 1 kg for week");
        kgArray.add("Lose 2 kg for week");

        messageArray.add("");
        messageArray.add("Recommended");
        messageArray.add("");
        messageArray.add("");

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
                    .inflate(R.layout.activitylevelnext_items, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx);
            return myViewHolder;
        }

        //step 6:-
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt_level.setText(kgArray.get(position));
            holder.txt_message.setText(messageArray.get(position));
            if (selectedPosition == position) {
                Log.e("selectedPosition", "call" + selectedPosition);
                weekGoalType = kgArray.get(selectedPosition);
                holder.radioButton.setChecked(true);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setRecommendedPlan(weekGoalType);
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();

                if (calories.length() > 0) {
                    Log.e("calories", "call" + calories);
                    calculateCaloriesDependsOnWeeklygoal(calories);
                }
            } else {
                holder.radioButton.setChecked(false);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    Log.e("pos", "" + selectedPosition);
                    weekGoalType = kgArray.get(selectedPosition);
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setRecommendedPlan(weekGoalType);
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();

                    notifyDataSetChanged();
                }
            });

        }

        // step 4:-
        @Override
        public int getItemCount() {
            return kgArray.size();
        }


        // Step 2:-
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
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }
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

    // Step 1:-
    public class WeekDaysAdapter extends RecyclerView.Adapter<WeekDaysAdapter.ViewHolder> {
        // step 3:-
        Context ctx;

        public WeekDaysAdapter(Context ctx) {
            this.ctx = ctx;
        }

        // step 5:-
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activityweekdays_items, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx);
            return myViewHolder;
        }

        //step 6:-
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final WeekDaysObject objWeekObject = weekDaysModelList.get(position);
            holder.txt_day.setText(objWeekObject.dayName);
            holder.checkBox.setChecked(objWeekObject.isSelected);

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                    objWeekObject.isSelected = checked;
                    weekDaysModelList.set(position, objWeekObject);
                    boolean result = true;
                    String displayString = "";
                    for (int i = 0; i < weekDaysModelList.size(); i++) {
                        WeekDaysObject objNotifyForCheck = weekDaysModelList.get(i);
                        if (objNotifyForCheck.isSelected) {
                            if (i == 6) {
                                displayString = displayString.concat("1");
                            } else {
                                displayString = displayString.concat("1,");
                            }
                        } else {
                            if (i == 6) {
                                displayString = displayString.concat("0");
                            } else {
                                displayString = displayString.concat("0,");
                            }
                        }
                        result = result && objNotifyForCheck.isSelected;
                    }
                    //Toast.makeText(getApplicationContext(), displayString, Toast.LENGTH_SHORT).show();
                    daysStr = displayString;
                    Log.e("dispstr", "" + daysStr);
                    shouldRun = false;
                    int num = characterCount(displayString);
                    Log.e("countval", "" + num);
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setTargetDays(displayString);
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                }
            });
        }

        //define your function
        private int characterCount(String word) {
            Log.e("character", "call" + word);
            int count = 0;
            for (int i = 0; i < word.length(); i++) {
                Log.e("cha", "call" + word.indexOf(i));
                if (word.charAt(i) == '1') {
                    count++;
                }
            }
            return count;
        }

        // step 4:-
        @Override
        public int getItemCount() {
            return weekDaysModelList.size();

        }

        // Step 2:-
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txt_day;
            Context ctx;
            CheckBox checkBox;

            public ViewHolder(View itemView, Context ctx) {
                super(itemView);
                this.ctx = ctx;
                txt_day = (TextView) itemView.findViewById(R.id.days);
                checkBox = (CheckBox) itemView.findViewById(R.id.check);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    private void loadWeekdaysArray() {
        weekDaysModelList = new ArrayList<WeekDaysObject>();
        for (int i = 0; i <= 6; i++) {

            WeekDaysObject obj = new WeekDaysObject();
            switch (i) {
                case 0:
                    obj.dayName = "Monday";
                    obj.isSelected = false;
                    break;
                case 1:
                    obj.dayName = "Tuesday";
                    obj.isSelected = false;
                    break;

                case 2:
                    obj.dayName = "Wednesday";
                    obj.isSelected = false;
                    break;

                case 3:
                    obj.dayName = "Thursday";
                    obj.isSelected = false;
                    break;
                case 4:
                    obj.dayName = "Friday";
                    obj.isSelected = false;
                    break;
                case 5:
                    obj.dayName = "Saturday";
                    obj.isSelected = false;
                    break;
                case 6:
                    obj.dayName = "Sunday";
                    obj.isSelected = false;
                    break;
            }
            weekDaysModelList.add(obj);
            Log.e("weekDaysModelList", "call" + weekDaysModelList.size());
        }
    }

    private void loadWeekdaysArrayFromDb(String daysArray[]) {
        weekDaysModelList = new ArrayList<WeekDaysObject>();
        Log.e("daysArray", "call" + daysArray.length);
        for (int i = 0; i <= 6; i++) {
            WeekDaysObject obj = new WeekDaysObject();
            switch (i) {
                case 0:
                    if (daysArray[0].equals("1")) {
                        Log.e("otrue", "call");
                        obj.dayName = "Monday";
                        obj.isSelected = true;
                    } else {
                        obj.dayName = "Monday";
                        obj.isSelected = false;
                    }
                    break;
                case 1:
                    if (daysArray[1].equals("1")) {
                        obj.dayName = "Tuesday";
                        obj.isSelected = true;
                    } else {
                        obj.dayName = "Tuesday";
                        obj.isSelected = false;
                    }
                    break;
                case 2:
                    if (daysArray[2].equals("1")) {
                        obj.dayName = "Wednesday";
                        obj.isSelected = true;
                    } else {
                        obj.dayName = "Wednesday";
                        obj.isSelected = false;
                    }

                    break;

                case 3:
                    if (daysArray[3].equals("1")) {
                        obj.dayName = "Thursday";
                        obj.isSelected = true;
                    } else {
                        obj.dayName = "Thursday";
                        obj.isSelected = false;
                    }
                    break;
                case 4:
                    if (daysArray[4].equals("1")) {
                        obj.dayName = "Friday";
                        obj.isSelected = true;
                    } else {
                        obj.dayName = "Friday";
                        obj.isSelected = false;
                    }
                    break;
                case 5:
                    if (daysArray[5].equals("1")) {
                        obj.dayName = "Saturday";
                        obj.isSelected = true;
                    } else {
                        obj.dayName = "Saturday";
                        obj.isSelected = false;
                    }
                    break;
                case 6:
                    if (daysArray[6].equals("1")) {
                        obj.dayName = "Sunday";
                        obj.isSelected = true;
                    } else {
                        obj.dayName = "Sunday";
                        obj.isSelected = false;
                    }
                    break;
            }
            weekDaysModelList.add(obj);
            Log.e("weekDaysModelListdb", "call" + weekDaysModelList.size());
        }

    }

    @Override
    public void onBackPressed() {    //when click on phone backbutton
        finish();
    }

}
