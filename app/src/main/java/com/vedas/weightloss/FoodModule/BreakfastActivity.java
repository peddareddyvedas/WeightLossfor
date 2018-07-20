package com.vedas.weightloss.FoodModule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.weightloss.Alert.Constants;
import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.Controllers.RegionController;
import com.vedas.weightloss.DataBase.BreakfastDataController;
import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.DinnerDataController;
import com.vedas.weightloss.DataBase.LunchDataController;
import com.vedas.weightloss.DataBase.SnacksDataController;
import com.vedas.weightloss.Models.BreakfastObject;
import com.vedas.weightloss.Models.CountryList;
import com.vedas.weightloss.Models.DinnerObject;
import com.vedas.weightloss.Models.LunchObject;
import com.vedas.weightloss.Models.SnacksObject;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class BreakfastActivity extends BaseDetailActivity {
    ArrayList<String> breakfastArray = new ArrayList<>();
    RecyclerView addRecyclerView;
    View view;
    int selectedPosition = -1;
    BreakfastAdapter adapter;
    Toolbar toolbar;
    ImageView home, btn_back;
    com.github.clans.fab.FloatingActionButton fab;
    EditText searchBox;
    ImageView cancel;
    int type;
    String servingSize = "1";
    String units = "Oz";
    public static BreakfastObject selectedObject;
    public static LunchObject selectedlunchObject;
    public static DinnerObject selecteddinnerObject;
    public static SnacksObject selectedSnacksObject;
    public boolean isFav;


    //  String[] spinnerItems = new String[]{"Units","Oz","Grams","Boul"  };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //type = getIntent().getExtras().getInt(EXTRA_TYPE);
        setupWindowAnimations();
        setContentView(R.layout.activity_breakfast);
        ButterKnife.bind(this);
        init();
        setToolbar();
        if (selectedObject != null) {
            Log.e("selectedobject", "call" + selectedObject.getFoodName());
            int index = breakfastArray.indexOf(selectedObject.getFoodName());
            selectedPosition = index;
            Log.e("selectedobject", "call" + selectedObject.getFoodName() + "" + selectedPosition);
            addRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Call smooth scroll
                    addRecyclerView.smoothScrollToPosition(selectedPosition);
                }
            });
        }

        if (selectedlunchObject != null) {
            Log.e("selectedobject", "call" + selectedlunchObject.getFoodName());
            int index = breakfastArray.indexOf(selectedlunchObject.getFoodName());
            selectedPosition = index;
            Log.e("selectedobject", "call" + selectedlunchObject.getFoodName() + "" + selectedPosition);
            addRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Call smooth scroll
                    addRecyclerView.smoothScrollToPosition(selectedPosition);
                }
            });
        }

        if (selecteddinnerObject != null) {
            int index = breakfastArray.indexOf(selecteddinnerObject.getFoodName());
            selectedPosition = index;
            addRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Call smooth scroll
                    addRecyclerView.smoothScrollToPosition(selectedPosition);
                }
            });
        }
        if (selectedSnacksObject != null) {
            int index = breakfastArray.indexOf(selectedSnacksObject.getFoodName());
            selectedPosition = index;
            addRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Call smooth scroll
                    addRecyclerView.smoothScrollToPosition(selectedPosition);
                }
            });
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

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        home = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DayOneActivity.foodType = "";
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbar_text);
        if (DayOneActivity.foodType.equals(Constants.foodKeys.lunch.toString())) {
            toolbartext.append("Lunch");
        } else if (DayOneActivity.foodType.equals(Constants.foodKeys.dinner.toString())) {
            toolbartext.append("Dinner");
        } else if (DayOneActivity.foodType.equals(Constants.foodKeys.snacks.toString())) {
            toolbartext.append("Snacks");
        } else {
            toolbartext.append("Breakfast");
        }
    }

    private void setRecyclerView(ArrayList<String> nameList) {
        addRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewbeakfast);
        adapter = new BreakfastAdapter(nameList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        addRecyclerView.setLayoutManager(horizontalLayoutManager);
        addRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void init() {
        if (DayOneActivity.foodType.equals(Constants.foodKeys.lunch.toString())) {
            breakfastArray.add("Biriyani");
            breakfastArray.add("Rice");
            breakfastArray.add("Dall");
            breakfastArray.add("Rasam");
            breakfastArray.add("Pickel");
            breakfastArray.add("Curd Rice");
            breakfastArray.add("Aalu fry");
            breakfastArray.add("Egg");
        } else if (DayOneActivity.foodType.equals(Constants.foodKeys.dinner.toString())) {
            breakfastArray.add("Biriyani");
            breakfastArray.add("Rice");
            breakfastArray.add("Dall");
            breakfastArray.add("Rasam");
            breakfastArray.add("Pickel");
            breakfastArray.add("Curd Rice");
            breakfastArray.add("Aalu fry");
            breakfastArray.add("Egg");
            // snacksArray = ["bhelpuri","panipuri","gobi","cutlet","noodles","aloopuri","samosa"]
        } else if (DayOneActivity.foodType.equals(Constants.foodKeys.snacks.toString())) {
            breakfastArray.add("bhelpuri");
            breakfastArray.add("panipuri");
            breakfastArray.add("gobi");
            breakfastArray.add("cutlet");
            breakfastArray.add("noodles");
            breakfastArray.add("aloopuri");
            breakfastArray.add("samosa");
        } else {
            breakfastArray.add("Dosa");
            breakfastArray.add("Upma");
            breakfastArray.add("Pasaratu");
            breakfastArray.add("Pongal");
            breakfastArray.add("Samosa");
            breakfastArray.add("Kichidi");
            breakfastArray.add("Panipuri");
            breakfastArray.add("Roti");
            breakfastArray.add("Special Dosa");
            breakfastArray.add("Puri");
            breakfastArray.add("Rihte");
            breakfastArray.add("Idly");
            breakfastArray.add("Vada");
        }
        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent1 = new Intent(getApplicationContext(), AddBreakfastActivity.class);
                intent1.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                transitionTo(intent1);
            }
        });
        searchBox = (EditText) findViewById(R.id.searchBox);
        cancel = (ImageView) findViewById(R.id.img_cancel);

        setRecyclerView(breakfastArray);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString().toLowerCase());
                cancel.setVisibility(View.VISIBLE);
                if (searchBox.getText().toString().isEmpty()) {
                    cancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString().toLowerCase());
                cancel.setVisibility(View.VISIBLE);
                if (searchBox.getText().toString().isEmpty()) {
                    cancel.setVisibility(View.GONE);
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchBox.getText().toString().length() > 0) {
                    searchBox.setText("");
                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<String> nameList = new ArrayList<>();
        for (String name : breakfastArray) {
            if (name.toLowerCase().startsWith(text)) {
                nameList.add(name);
            }
        }
        setRecyclerView(nameList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class BreakfastAdapter extends RecyclerView.Adapter<BreakfastAdapter.ViewHolder> {
        ArrayList<String> arrayList = new ArrayList<>();
        Context ctx;

        public BreakfastAdapter(ArrayList<String> arrayList, Context ctx) {
            this.ctx = ctx;
            this.arrayList = arrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_breakfast, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx, arrayList);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.breakfast_name.setText(arrayList.get(position));
            if (selectedPosition == position) {
                holder.rl_main.setBackgroundColor(Color.parseColor("#E0E0E0"));
                holder.image.setVisibility(View.VISIBLE);
                loadDialogueForAddingCalories(arrayList.get(position));
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        adapter.notifyDataSetChanged();

                    } else {
                        selectedPosition = -1;
                        adapter.notifyDataSetChanged();
                        // holder.image.setVisibility(View.GONE);


                    }

                   /* if (!holder.isFav) {
                        Toast.makeText(ctx,"Selected Row : "+position,Toast.LENGTH_SHORT).show();
                        holder.isFav = true;
                        holder.image.setVisibility(View.VISIBLE);
                    } else {
                        holder.image.setVisibility(View.GONE);
                        holder.isFav = false;
                    }
*/

                }
            });
           /* if (holder.isFav)
                holder.image.setVisibility(View.VISIBLE);
*/
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView breakfast_name, calories;
            ImageView image;
            ArrayList<String> arrayList = new ArrayList<String>();
            Context ctx;
            RelativeLayout rl_main;
            boolean isFav;

            public ViewHolder(View itemView, Context ctx, final ArrayList<String> arrayList) {
                super(itemView);
                this.arrayList = arrayList;
                this.ctx = ctx;
                breakfast_name = (TextView) itemView.findViewById(R.id.itemName);
                calories = (TextView) itemView.findViewById(R.id.calories);
                rl_main = (RelativeLayout) itemView.findViewById(R.id.rl_main);
                image = (ImageView) itemView.findViewById(R.id.image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {


            }
        }
    }

    public void loadDialogueForAddingCalories(final String foodname) {
        final Dialog mod = new Dialog(BreakfastActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.activity_servingsize);
        mod.setCanceledOnTouchOutside(true);
        mod.setCancelable(true);
        mod.show();
        mod.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        RelativeLayout rl_measure = (RelativeLayout) mod.findViewById(R.id.measure);
        final TextView selected_measure = (TextView) mod.findViewById(R.id.selected_type);
        final TextView iteam = (TextView) mod.findViewById(R.id.iteam);
        iteam.setText(foodname);
        final TextView txt_set = (TextView) mod.findViewById(R.id.set);
        //final TextView txt_units = (TextView) mod.findViewById(R.id.txt_units);

        final EditText caloriestext = (EditText) mod.findViewById(R.id.caloriesedittext);
        final Spinner spin1 = (Spinner) mod.findViewById(R.id.spinnerunits);
        Spinner spin = (Spinner) mod.findViewById(R.id.spinnersize);


        txt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("foodname", "ca;l" + iteam.getText().toString());

                if (DayOneActivity.foodType.equals(Constants.foodKeys.lunch.toString())) {
                    LunchObject lunchObject = new LunchObject();
                    lunchObject.setFoodName(iteam.getText().toString());
                    lunchObject.setEsimatedCalories(caloriestext.getText().toString());
                    lunchObject.setServingSize(servingSize);
                    lunchObject.setUnits(units);
                    lunchObject.setAddedTime(DayFoodController.getInstance().gettingCurrentDate());
                    updateLunchData(LunchDataController.getInstance().fetchLunchData(DayFoodDataController.getInstance().currentFoodObject), lunchObject);
                } else if (DayOneActivity.foodType.equals(Constants.foodKeys.dinner.toString())) {
                    DinnerObject dinnerObject = new DinnerObject();
                    dinnerObject.setFoodName(iteam.getText().toString());
                    dinnerObject.setEsimatedCalories(caloriestext.getText().toString());
                    dinnerObject.setServingSize(servingSize);
                    dinnerObject.setUnits(units);
                    dinnerObject.setAddedTime(DayFoodController.getInstance().gettingCurrentDate());
                    updateDinnerData(DinnerDataController.getInstance().fetchdinnerData(DayFoodDataController.getInstance().currentFoodObject), dinnerObject);
                } else if (DayOneActivity.foodType.equals(Constants.foodKeys.snacks.toString())) {
                    SnacksObject dinnerObject = new SnacksObject();
                    dinnerObject.setFoodName(iteam.getText().toString());
                    dinnerObject.setEsimatedCalories(caloriestext.getText().toString());
                    dinnerObject.setServingSize(servingSize);
                    dinnerObject.setUnits(units);
                    dinnerObject.setAddedTime(DayFoodController.getInstance().gettingCurrentDate());
                    updateSnacksData(SnacksDataController.getInstance().fetchSnacksData(DayFoodDataController.getInstance().currentFoodObject), dinnerObject);
                } else {
                    BreakfastObject breakfastObject = new BreakfastObject();
                    breakfastObject.setFoodName(iteam.getText().toString());
                    breakfastObject.setEsimatedCalories(caloriestext.getText().toString());
                    breakfastObject.setServingSize(servingSize);
                    breakfastObject.setUnits(units);
                    breakfastObject.setAddedTime(DayFoodController.getInstance().gettingCurrentDate());
                    updateBreakfastDataIntoDb(BreakfastDataController.getInstance().fetchBreakfastData(DayFoodDataController.getInstance().currentFoodObject), breakfastObject);
                }
                mod.dismiss();
                DayOneActivity.foodType = "";
                finish();
            }
        });
        final List<String> categories = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            categories.add(String.valueOf(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        if (selectedObject != null) {
            spin.setSelection(categories.indexOf(selectedObject.getServingSize()));
        } else {
            spin.setSelection(categories.indexOf(servingSize));
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servingSize = categories.get(position);
                caloriestext.setText("" + Integer.parseInt(servingSize) * 50);
                Log.e("clickcontainer", "call" + servingSize);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //
        final List<String> unitsArray = new ArrayList<String>();
        unitsArray.add("Oz");
        unitsArray.add("Grams");
        unitsArray.add("Bowl");


        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, unitsArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(spinnerArrayAdapter);
        if (selectedObject != null) {
            Log.e("servingsize", "call" + selectedObject.getUnits());
            spin1.setSelection(unitsArray.indexOf(selectedObject.getUnits()));
        } else {
            spin1.setSelection(unitsArray.indexOf(units));
        }
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                units = unitsArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateBreakfastDataIntoDb(ArrayList<BreakfastObject> arrayList, BreakfastObject breakfastObject) {
        if (selectedObject != null) {
            if (arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getFoodName().equals(selectedObject.getFoodName())) {
                        //  int index = arrayList.indexOf(selectedObject);
                        if (!selectedObject.getFoodName().equals(breakfastObject.getFoodName())) {
                            Log.e("notsame", "call");
                            //arrayList.add(breakfastObject);
                            BreakfastDataController.getInstance().insertBreakfastData(breakfastObject, DayFoodDataController.getInstance().currentFoodObject);
                        } else {
                            Log.e("same", "call");
                            // arrayList.set(index, breakfastObject);
                            BreakfastDataController.getInstance().updateBreakfastData(breakfastObject);
                        }
                    }
                }
            }
        } else {
            BreakfastDataController.getInstance().insertBreakfastData(breakfastObject, DayFoodDataController.getInstance().currentFoodObject);
            arrayList.add(breakfastObject);
        }
    }

    private void updateLunchData(ArrayList<LunchObject> arrayList, LunchObject lunchObject) {
        if (selectedlunchObject != null) {
            if (arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getFoodName().equals(selectedlunchObject.getFoodName())) {
                        // int index = arrayList.indexOf(selectedlunchObject);
                        if (!selectedlunchObject.getFoodName().equals(lunchObject.getFoodName())) {
                            Log.e("notsame", "call");
                            // arrayList.add(lunchObject);
                            LunchDataController.getInstance().insertLunchData(lunchObject, DayFoodDataController.getInstance().currentFoodObject);
                        } else {
                            Log.e("same", "call");
                            //arrayList.set(index, lunchObject);
                            LunchDataController.getInstance().updateLunchData(lunchObject);
                        }
                    }
                }
            }
        } else {
            LunchDataController.getInstance().insertLunchData(lunchObject, DayFoodDataController.getInstance().currentFoodObject);
            // arrayList.add(lunchObject);
        }
    }

    private void updateDinnerData(ArrayList<DinnerObject> arrayList, DinnerObject lunchObject) {
        if (selecteddinnerObject != null) {
            if (arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getFoodName().equals(selecteddinnerObject.getFoodName())) {
                        //  int index = arrayList.indexOf(selecteddinnerObject);
                        if (!selecteddinnerObject.getFoodName().equals(lunchObject.getFoodName())) {
                            Log.e("notsame", "call");
                            //arrayList.add(lunchObject);
                            DinnerDataController.getInstance().insertDinnerData(lunchObject, DayFoodDataController.getInstance().currentFoodObject);
                        } else {
                            Log.e("same", "call");
                            // arrayList.set(index, lunchObject);
                            DinnerDataController.getInstance().updateDinnerData(lunchObject);
                        }
                    }
                }
            }
        } else {
            DinnerDataController.getInstance().insertDinnerData(lunchObject, DayFoodDataController.getInstance().currentFoodObject);
            //arrayList.add(lunchObject);
        }
    }

    private void updateSnacksData(ArrayList<SnacksObject> arrayList, SnacksObject lunchObject) {
        if (selectedSnacksObject != null) {
            if (arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getFoodName().equals(selectedSnacksObject.getFoodName())) {
                        // int index = arrayList.indexOf(selectedSnacksObject);
                        if (!selectedSnacksObject.getFoodName().equals(lunchObject.getFoodName())) {
                            Log.e("notsame", "call");
                            // arrayList.add(lunchObject);
                            SnacksDataController.getInstance().insertSnacksData(lunchObject, DayFoodDataController.getInstance().currentFoodObject);
                        } else {
                            Log.e("same", "call");
                            //arrayList.set(index, lunchObject);
                            SnacksDataController.getInstance().updateSnacksData(lunchObject);
                        }
                    }
                }
            }
        } else {
            SnacksDataController.getInstance().insertSnacksData(lunchObject, DayFoodDataController.getInstance().currentFoodObject);
            // arrayList.add(lunchObject);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}


