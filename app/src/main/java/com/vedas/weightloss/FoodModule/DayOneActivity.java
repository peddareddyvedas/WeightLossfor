package com.vedas.weightloss.FoodModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.vedas.weightloss.Alert.Constants;
import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.DataBase.BreakfastDataController;
import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.DinnerDataController;
import com.vedas.weightloss.DataBase.ExerciseDataController;
import com.vedas.weightloss.DataBase.LunchDataController;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.DataBase.SnacksDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.DataBase.WaterDataController;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.Models.LunchObject;
import com.vedas.weightloss.MoreModule.GoogleFitActivity;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DayOneActivity extends BaseDetailActivity {
    RecyclerView addRecyclerView;
    DashBoardAdapter adapter;
    Toolbar toolbar;
    ImageView home, btn_back;
    int type;
    private boolean hideEmpty;
    private boolean showFooters = true;
    public static String foodType = "";
    RelativeLayout rl_addweight, rl_addweightTitle;
    TextView current_weight;
    TextView targetCal, foodCal, exerciseCal, remianCal;
    String selectedPosition = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getExtras().getInt(EXTRA_TYPE);
        setupWindowAnimations();
        setContentView(R.layout.activity_dayone);
        ButterKnife.bind(this);
        setRecyclerView();
        setToolbar();
        DayFoodDataController.getInstance().fetchdayfoodData();
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

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        home = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DayFoodDataController.getInstance().currentFoodObject.targetCalories = remianCal.getText().toString();
                DayFoodDataController.getInstance().updateDayFoodData(DayFoodDataController.getInstance().currentFoodObject);
                // int objectPosition = DayFoodDataController.getInstance().allDayfoodArray.indexOf(DayFoodDataController.getInstance().currentFoodObject);
                // DayFoodDataController.getInstance().allDayfoodArray.set(objectPosition, DayFoodDataController.getInstance().currentFoodObject);
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbar_text);
        toolbartext.append("Day 1");

        rl_addweight = (RelativeLayout) findViewById(R.id.addweighttille);
        rl_addweightTitle = (RelativeLayout) findViewById(R.id.weighttitle);

        current_weight = (TextView) findViewById(R.id.weight1);
    }

    @OnClick({R.id.addweighttille})
    public void addweighttitle() {
        Intent i = new Intent(getApplicationContext(), AddWeightActivity.class);
        transitionTo(i);
    }

    private void setRecyclerView() {
        addRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewbeakfast);
        adapter = new DashBoardAdapter();
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        addRecyclerView.setLayoutManager(horizontalLayoutManager);
        adapter.shouldShowHeadersForEmptySections(hideEmpty);
        adapter.shouldShowFooters(showFooters);
        addRecyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.weighttitle})
    public void addWeightAction() {
        Intent i = new Intent(getApplicationContext(), AddWeightActivity.class);
        transitionTo(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        DayFoodDataController.getInstance().fetchdayfoodData();
        if (DayFoodDataController.getInstance().currentFoodObject != null) {
            if (DayFoodDataController.getInstance().currentFoodObject.weight != null) {
                rl_addweightTitle.setVisibility(View.VISIBLE);
                rl_addweight.setVisibility(View.GONE);
                current_weight.setText(DayFoodDataController.getInstance().currentFoodObject.weight);
            }
        }
        DayFoodController.getInstance().calculatingTotalCaloriesForFood();
        DayFoodController.getInstance().calculatingTotalCaloriesForFoodForHeader();
        DayFoodController.getInstance().calculatingTotalCaloriesForExercise();
        DayFoodController.getInstance().calculatelitersFromWaterobject();
        calculateRemaineCalories();
        adapter.notifyDataSetChanged();

    }

    private void calculateRemaineCalories() {

        targetCal = (TextView) findViewById(R.id.targetCal);
        foodCal = (TextView) findViewById(R.id.foodCal);
        exerciseCal = (TextView) findViewById(R.id.exerciseCal);
        remianCal = (TextView) findViewById(R.id.remaileCal);

        targetCal.setText("" + PersonalInfoDataController.getInstance().currentMember.getTargetCalories());
        foodCal.setText("" + DayFoodController.getInstance().calculatingTotalCaloriesForFood());
        exerciseCal.setText("" + DayFoodController.getInstance().calculatingTotalCaloriesForExercise());

        if (!foodCal.getText().toString().equals("0") || !exerciseCal.getText().toString().equals("0")) {
            Log.e("remainecal", "call");
            String remaineCal = String.valueOf(DayFoodController.getInstance().calculateTotlaRemainingCalories());
            remianCal.setText("" + DayFoodController.getInstance().calculateTotlaRemainingCalories());
            if (remaineCal.contains("-")) {
                remianCal.setTextColor(Color.RED);
            } else {
                remianCal.setTextColor(Color.parseColor("#41BD67"));
            }
        } else {
            remianCal.setText("" + PersonalInfoDataController.getInstance().currentMember.getTargetCalories());
        }
    }

    @Override
    public void onBackPressed() {
        DayFoodDataController.getInstance().currentFoodObject.targetCalories = remianCal.getText().toString();
        DayFoodDataController.getInstance().currentFoodObject.burnedCalories = exerciseCal.getText().toString();
        DayFoodDataController.getInstance().updateDayFoodData(DayFoodDataController.getInstance().currentFoodObject);
        // int objectPosition = DayFoodDataController.getInstance().allDayfoodArray.indexOf(DayFoodDataController.getInstance().currentFoodObject);
        // DayFoodDataController.getInstance().allDayfoodArray.set(objectPosition,DayFoodDataController.getInstance().currentFoodObject);
        finish();
        super.onBackPressed();
    }

    public void deleteFoodDetails(int section, int relativePosition) {
        switch (section) {
            case 0:
                BreakfastDataController.getInstance().deleteBreakfastDataForPosition(relativePosition, DayFoodDataController.getInstance().currentFoodObject);
                break;
            case 1:
                LunchDataController.getInstance().deleteLunchDataForPosition(relativePosition, DayFoodDataController.getInstance().currentFoodObject);
                break;
            case 2:
                DinnerDataController.getInstance().deleteDinnerDataForPosition(relativePosition, DayFoodDataController.getInstance().currentFoodObject);
                break;
            case 3:
                SnacksDataController.getInstance().deleteSnacksDataForPosition(relativePosition, DayFoodDataController.getInstance().currentFoodObject);
                break;
            case 4:
                ExerciseDataController.getInstance().deleteExerciseDataForPosition(relativePosition, DayFoodDataController.getInstance().currentFoodObject);
                break;
            case 5:
                WaterDataController.getInstance().deleteWaterDataForPosition(relativePosition, DayFoodDataController.getInstance().currentFoodObject);
                break;
            default:

        }

        DayFoodController.getInstance().calculatingTotalCaloriesForFood();
        DayFoodController.getInstance().calculatingTotalCaloriesForFoodForHeader();
        DayFoodController.getInstance().calculatingTotalCaloriesForExercise();
        calculateRemaineCalories();
        adapter.notifyDataSetChanged();

    }

    @SuppressLint("DefaultLocale")
    public class DashBoardAdapter extends SectionedRecyclerViewAdapter<DashBoardAdapter.MainVH> {

        @Override
        public int getSectionCount() {
            return 6;
        }

        @Override
        public int getItemCount(int section) {
            DayFoodObject dayFoodObject = DayFoodDataController.getInstance().currentFoodObject;
            if (section == 0) {
                if (BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).size() > 0) {
                    return BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).size();
                }
            }// lunch
            if (section == 1) {
                if (LunchDataController.getInstance().fetchLunchData(dayFoodObject).size() > 0) {
                    return LunchDataController.getInstance().fetchLunchData(dayFoodObject).size();
                }
            }
            // dinner
            if (section == 2) {
                if (DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).size() > 0) {
                    return DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).size();
                }
            }
            // snacks
            if (section == 3) {
                if (SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).size() > 0) {
                    return SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).size();
                }
            }
            if (section == 4) {
                if (ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).size() > 0) {
                    DayFoodController.getInstance().calculatingTotalCaloriesForExercise();
                    return ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).size();
                }
            }
            if (section == 5) {
                if (WaterDataController.getInstance().fetchWaterData(dayFoodObject).size() > 0) {
                    return WaterDataController.getInstance().fetchWaterData(dayFoodObject).size();
                }
            }
            return 1;
        }

        @Override
        public void onBindHeaderViewHolder(MainVH holder, int section, boolean expanded) {
            holder.headertitle.setText(DayFoodController.getInstance().headerArray.get(section).toString());
            switch (section) {
                case 0:
                    DayFoodObject dayObject = DayFoodDataController.getInstance().currentFoodObject;
                    holder.headertitle1.setText(dayObject.breakFastCalories + " Cal");
                    break;
                case 1:
                    DayFoodObject objlunchObject = DayFoodDataController.getInstance().currentFoodObject;
                    holder.headertitle1.setText(objlunchObject.lunchCalories + " Cal");

                    break;
                case 2:
                    DayFoodObject objDinnerObject = DayFoodDataController.getInstance().currentFoodObject;
                    holder.headertitle1.setText(objDinnerObject.dinnerCalories + " Cal");
                    break;
                case 3:
                    DayFoodObject objSnacksObject = DayFoodDataController.getInstance().currentFoodObject;
                    holder.headertitle1.setText(objSnacksObject.snacksCalories + " Cal");

                    break;
                case 4:
                    DayFoodObject objExerciseObject = DayFoodDataController.getInstance().currentFoodObject;
                    holder.headertitle1.setText(objExerciseObject.burnedCalories + " Cal");
                    break;
                case 5:
                    holder.headertitle1.setText(" " + DayFoodController.getInstance().calculatelitersFromWaterobject() + " " + "li");
                    break;
            }
        }

        @Override
        public void onBindFooterViewHolder(MainVH holder, final int section) {
            holder.name.setText(DayFoodController.getInstance().footerArray.get(section).toString());
            holder.footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (section == 0) {
                        BreakfastActivity.selectedObject = null;
                        foodType = Constants.foodKeys.breakfast.toString();
                        Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                        transitionTo(i);
                    } else if (section == 1) {
                        BreakfastActivity.selectedlunchObject = null;
                        foodType = Constants.foodKeys.lunch.toString();
                        Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                        transitionTo(i);
                    } else if (section == 2) {
                        BreakfastActivity.selecteddinnerObject = null;
                        foodType = Constants.foodKeys.dinner.toString();
                        Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                        transitionTo(i);
                    } else if (section == 3) {
                        BreakfastActivity.selectedSnacksObject = null;
                        foodType = Constants.foodKeys.snacks.toString();
                        Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                        transitionTo(i);
                    } else if (section == 4) {
                        ExerciseActivity.selectedExerciseObj = null;
                        Intent i = new Intent(getApplicationContext(), ExerciseActivity.class);
                        transitionTo(i);
                    } else if (section == 5) {
                        AddWatterActivity.selectedWaterObj = null;
                        Intent i = new Intent(getApplicationContext(), AddWatterActivity.class);
                        transitionTo(i);
                    }
                }
            });
        }

        @Override
        public void onBindViewHolder(final MainVH holder, final int section, final int relativePosition, final int absolutePosition) {

            DayFoodController.getInstance().loadTextviewdata(holder, section, relativePosition, absolutePosition);
            if (section == 4) {
                holder.exercrise.setVisibility(View.VISIBLE);
                holder.exercrise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), GoogleFitActivity.class);
                        transitionTo(i);
                    }
                });
            }
            holder.mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DayFoodObject dayFoodObject = DayFoodDataController.getInstance().currentFoodObject;
                    switch (section) {
                        case 0:
                            if (BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).size() > 0) {
                                DayOneActivity.foodType = Constants.foodKeys.breakfast.toString();
                                BreakfastActivity.selectedObject = null;
                                BreakfastActivity.selectedObject = BreakfastDataController.getInstance().fetchBreakfastData(dayFoodObject).get(relativePosition);
                                Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                                transitionTo(i);
                            }
                            break;
                        case 1:
                            if (LunchDataController.getInstance().fetchLunchData(dayFoodObject).size() > 0) {
                                BreakfastActivity.selectedlunchObject = null;
                                BreakfastActivity.selectedlunchObject = LunchDataController.getInstance().fetchLunchData(dayFoodObject).get(relativePosition);
                                DayOneActivity.foodType = Constants.foodKeys.lunch.toString();
                                Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                                transitionTo(i);
                            }
                            break;
                        case 2:
                            if (DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).size() > 0) {
                                BreakfastActivity.selecteddinnerObject = null;
                                BreakfastActivity.selecteddinnerObject = DinnerDataController.getInstance().fetchdinnerData(dayFoodObject).get(relativePosition);
                                DayOneActivity.foodType = Constants.foodKeys.dinner.toString();
                                Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                                transitionTo(i);
                            }
                            break;
                        case 3:
                            if (SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).size() > 0) {
                                BreakfastActivity.selectedSnacksObject = null;
                                BreakfastActivity.selectedSnacksObject = SnacksDataController.getInstance().fetchSnacksData(dayFoodObject).get(relativePosition);
                                DayOneActivity.foodType = Constants.foodKeys.snacks.toString();
                                Intent i = new Intent(getApplicationContext(), BreakfastActivity.class);
                                transitionTo(i);
                            }
                            break;
                        case 4:

                            if (ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).size() > 0) {
                                ExerciseActivity.selectedExerciseObj = null;
                                ExerciseActivity.selectedExerciseObj = ExerciseDataController.getInstance().fetchExerciseData(dayFoodObject).get(relativePosition);
                                Intent i = new Intent(getApplicationContext(), ExerciseActivity.class);
                                transitionTo(i);
                            }
                            break;
                        case 5:
                            if (WaterDataController.getInstance().fetchWaterData(dayFoodObject).size() > 0) {
                                AddWatterActivity.selectedWaterObj = null;
                                AddWatterActivity.selectedWaterObj = WaterDataController.getInstance().fetchWaterData(dayFoodObject).get(relativePosition);
                                Intent i = new Intent(getApplicationContext(), AddWatterActivity.class);
                                transitionTo(i);
                            }
                            break;
                    }
                }
            });
            holder.mainView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DayOneActivity.this);
                    alertDialogBuilder.setMessage("Do you want to delete this item ?")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteFoodDetails(section, relativePosition);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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

                    return true;
                }
            });
        }

        @Override
        public int getItemViewType(int section, int relativePosition, int absolutePosition) {
            return super.getItemViewType(section, relativePosition, absolutePosition);
        }

        @Override
        public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layout = R.layout.list_item_header;
                    break;
                case VIEW_TYPE_ITEM:
                    layout = R.layout.list_item_main;
                    break;
                case VIEW_TYPE_FOOTER:
                    layout = R.layout.list_item_footer;
                    break;
                default:
                    // Our custom item, which is the 0 returned in getItemViewType() above
                    layout = R.layout.list_item_main;
                    break;
            }

            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new MainVH(v, this);
        }

        public class MainVH extends SectionedViewHolder implements View.OnClickListener {

            public TextView name, calories, added_time;
            public RelativeLayout footerView, mainView, exercrise;
            public TextView headertitle, headertitle1;
            // final ImageView caret;
            final DashBoardAdapter adapter;
            public View view;

            MainVH(View itemView, DashBoardAdapter adapter) {
                super(itemView);
                this.name = itemView.findViewById(R.id.title);
                this.calories = itemView.findViewById(R.id.title1);
                this.headertitle = itemView.findViewById(R.id.headertitle);
                this.headertitle1 = itemView.findViewById(R.id.headertitle1);
                this.footerView = itemView.findViewById(R.id.footer);
                this.view = itemView.findViewById(R.id.view);
                this.mainView = itemView.findViewById(R.id.mainView);
                this.added_time = itemView.findViewById(R.id.time);
                this.exercrise = itemView.findViewById(R.id.exercriselayout);
                this.adapter = adapter;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (isFooter()) {
                    Log.e("footer", "call");
                }

            }
        }
    }
}
        /*if (DayFoodController.getInstance().selectedFoodObject.breakfastArrayList.size() > 0) {
            adapter.notifyDataSetChanged();
        }
        if (DayFoodController.getInstance().selectedFoodObject.lunchArrayList.size() > 0) {
            adapter.notifyDataSetChanged();
        }
        if (DayFoodController.getInstance().selectedFoodObject.dinnerArrayList.size() > 0) {
            adapter.notifyDataSetChanged();
        }
        if (DayFoodController.getInstance().selectedFoodObject.snacksArrayList.size() > 0) {
            adapter.notifyDataSetChanged();
        }
        if (DayFoodController.getInstance().selectedFoodObject.weightArrayList.size() > 0) {
            adapter.notifyDataSetChanged();
        }
        if (DayFoodController.getInstance().selectedFoodObject.exerciseArrayList.size() > 0) {
            adapter.notifyDataSetChanged();
        }
        if (DayFoodController.getInstance().selectedFoodObject.waterArrayList.size() > 0) {
            adapter.notifyDataSetChanged();
        }*/




