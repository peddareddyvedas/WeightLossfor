package com.vedas.weightloss.MoreModule;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.weightloss.Controllers.RegionController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.Models.CountryList;
import com.vedas.weightloss.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Rise on 14/06/2018.
 */
public class RegionActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView btn_back;
    EditText searchBox;
    ImageView cancel;
    RecyclerView regionRecyclerView;
    Region adapter;
    RelativeLayout rl_mainlayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        ButterKnife.bind(this);
        init();
        setToolbar();
    }
    @Override
    public void onResume() {
        super.onResume();
        loadSelectedCountry();
    }
    public void loadSelectedCountry() {
        if (RegionController.getInstance().regionlist != null) {
            final int selectedPosition = RegionController.getInstance().regionlist.indexOf(RegionController.getInstance().selectedCountry);
            Log.e("selectedPosition", "call" + selectedPosition);
            regionRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    // Call smooth scroll
                   // regionRecyclerView.smoothScrollToPosition(selectedPosition);
                }
            });
        }
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
        toolbartext.append("Region");
    }
    private void init() {
        searchBox = (EditText) findViewById(R.id.searchBox);
        cancel = (ImageView) findViewById(R.id.img_cancel);
        rl_mainlayout=(RelativeLayout)findViewById(R.id.mainlayout) ;
        regionRecyclerView(RegionController.getInstance().regionlist);
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
                loadSelectedCountry();
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

    private void filter(String s) {
        ArrayList<CountryList> con = new ArrayList<CountryList>();
        Log.e("tyoetext", "call" + s.toLowerCase());
        for (CountryList d : RegionController.getInstance().regionlist) {
            if (d.getName().toLowerCase().startsWith(s.toLowerCase())) {
                Log.e("dbtetx", "call" + d.getName().toLowerCase());
                con.add(d);
            }
            regionRecyclerView(con);
            adapter.notifyDataSetChanged();
        }
    }

    private void regionRecyclerView(ArrayList<CountryList> nameList) {
        regionRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewregion);
        adapter = new Region(nameList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        regionRecyclerView.setLayoutManager(horizontalLayoutManager);
        regionRecyclerView.setAdapter(adapter);
        regionRecyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }

    // Step 1:-
    public class Region extends RecyclerView.Adapter<Region.ViewHolder> {
        ArrayList<CountryList> regionArrayList = new ArrayList<>();
        Context ctx;

        public Region(ArrayList<CountryList> arrayList, Context ctx) {
            this.ctx = ctx;
            this.regionArrayList = arrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.regionlist, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            CountryList objCountry = regionArrayList.get(position);
            holder.name.setText("" + objCountry.getName());

            if (objCountry.getName().equals(RegionController.getInstance().selectedCountry.getName())) {
                holder.imageView.setVisibility(View.VISIBLE);
               // RelativeLayout relative = (RelativeLayout) holder.itemView.findViewById(R.id.relativesetting);
               // relative.setBackgroundColor(Color.parseColor("#d7f1ed"));
                holder.name.setTextColor(Color.parseColor("#FF0012"));
            } else {
                holder.imageView.setVisibility(View.GONE);
                holder.name.setTextColor(Color.parseColor("#000000"));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rl_mainlayout.getWindowToken(), 0);
                    RegionController.getInstance().selectedCountry = regionArrayList.get(position);
                    UserDataController.getInstance().currentUser.setRegion(RegionController.getInstance().selectedCountry.getName());
                    UserDataController.getInstance().updateUserData(UserDataController.getInstance().currentUser);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return regionArrayList.size();
        }

        // Step 2:-
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name;
            ImageView imageView;
            Context ctx;

            public ViewHolder(View itemView, Context ctx) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.regionname);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
            }
        }
    }
}

