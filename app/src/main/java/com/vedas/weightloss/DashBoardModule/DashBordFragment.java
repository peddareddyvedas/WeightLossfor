package com.vedas.weightloss.DashBoardModule;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.DayFoodController;
import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.FoodModule.DayOneActivity;
import com.vedas.weightloss.Models.DayFoodObject;
import com.vedas.weightloss.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

import static com.vedas.weightloss.Transition.BaseDetailActivity.EXTRA_TYPE;
import static com.vedas.weightloss.Transition.BaseDetailActivity.TYPE_PROGRAMMATICALLY;

public class DashBordFragment extends Fragment {
    RecyclerView addRecyclerView;
    DashboardAdapter adapter;
    View view;
    Toolbar toolbar;
    TextView tool_text;
    ImageView img_bell;
    String currentDate = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashbord_fragment, container, false);

        ButterKnife.bind(this, view);
        init();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        tool_text = (TextView) toolbar.findViewById(R.id.toolbar_text);
        img_bell = (ImageView) toolbar.findViewById(R.id.img_share);
        img_bell.setBackgroundResource(R.drawable.ic_bell);
        tool_text.setText("DashBoard");


        return view;
    }

    private void init() {

        addRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewcalories);
        adapter = new DashboardAdapter(getActivity());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        addRecyclerView.setLayoutManager(horizontalLayoutManager);
        addRecyclerView.setAdapter(adapter);
        addRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
        Context ctx;

        public DashboardAdapter(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.devicecardlayout, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            DayFoodObject dayObject = DayFoodDataController.getInstance().allDayfoodArray.get(position);
            final Date date = new Date();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            currentDate = dateFormat.format(date);

            if (currentDate.equals(dayObject.date)) {
                holder.relative.setBackgroundColor(Color.parseColor("#848415"));
                holder.foodCalories.setTextColor(Color.BLACK);
                holder.exerciseCalories.setTextColor(Color.BLACK);
                holder.remainingCal.setTextColor(Color.BLACK);
                holder.date.setTextColor(Color.BLACK);

            } else {
                holder.relative.setBackgroundColor(Color.parseColor("#D3D3D3"));
                holder.foodCalories.setTextColor(Color.parseColor("#cfcfcf"));
                holder.exerciseCalories.setTextColor(Color.parseColor("#cfcfcf"));
                holder.remainingCal.setTextColor(Color.parseColor("#cfcfcf"));
                holder.date.setTextColor(Color.parseColor("#cfcfcf"));
            }
            int totalCal = Integer.parseInt(dayObject.breakFastCalories) + Integer.parseInt(dayObject.lunchCalories) + Integer.parseInt(dayObject.dinnerCalories) + Integer.parseInt(dayObject.snacksCalories);
            holder.foodCalories.setText("" + totalCal + " Kcal");
            holder.exerciseCalories.setText(dayObject.burnedCalories + " Kcal");
            holder.remainingCal.setText(dayObject.targetCalories + " Calories remaining");
            holder.date.setText(dayObject.date);
            holder.DayCount.setText("Day " + dayObject.dayName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DayFoodDataController.getInstance().currentFoodObject = DayFoodDataController.getInstance().allDayfoodArray.get(position);
                        Date date1 = dateFormat.parse(DayFoodDataController.getInstance().currentFoodObject.date);
                        if (date1.before(date) || date.equals(date1)) {
                            Intent i = new Intent(getActivity(), DayOneActivity.class);
                            i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                            startActivity(i);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    /*if (currentDate.equals(DayFoodDataController.getInstance().currentFoodObject.date)) {
                        Intent i = new Intent(getActivity(), DayOneActivity.class);
                        i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                        startActivity(i);
                    }*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return DayFoodDataController.getInstance().allDayfoodArray.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView foodCalories, exerciseCalories, DayCount, remainingCal, date;
            RelativeLayout relative;
            ImageView image;
            Context ctx;

            public ViewHolder(View itemView, Context ctx) {
                super(itemView);
                this.ctx = ctx;
                foodCalories = (TextView) itemView.findViewById(R.id.itemName);
                date = (TextView) itemView.findViewById(R.id.date);
                exerciseCalories = (TextView) itemView.findViewById(R.id.calories);
                relative = (RelativeLayout) itemView.findViewById(R.id.relative);
                DayCount = (TextView) itemView.findViewById(R.id.textView);
                remainingCal = (TextView) itemView.findViewById(R.id.caloriesremaining);
                image = (ImageView) itemView.findViewById(R.id.image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }
        }
    }
}
