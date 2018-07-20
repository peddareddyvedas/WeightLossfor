package com.vedas.weightloss.MoreModule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.RefreshShowingDialog;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.R;
import com.vedas.weightloss.ServerApis.ServerApisInterface;
import com.vedas.weightloss.ServerObjects.FeedbackServerObject;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by VEDAS on 6/13/2018.
 */
public class FeedbackActivity extends BaseDetailActivity {
    Toolbar toolbar;
    ImageView btn_back;

    EditText edit;
    Button feedback;
    String edit_discription;
    RefreshShowingDialog refreshShowingDialog;
    ArrayList<String> name = new ArrayList<>();
    int selectedPosition = 0;
    RecyclerView feedRecyclerView;
    String selectedType = "It Was Great";

    FeedBackAdapter adapter;
    View view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        refreshShowingDialog = new RefreshShowingDialog(FeedbackActivity.this);

        UserDataController.getInstance().fetchUserData();
        setToolbar();
        init();
        toISO8601UTC();

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
        toolbartext.append("Feedback");
    }

    private void init() {


        name.add("It Was Great");
        name.add("It did the job");
        name.add("I am disappointed");

        edit = (EditText) findViewById(R.id.editdiscription);
        feedback = (Button) findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackAction();
            }
        });


        feedRecyclerView = (RecyclerView) findViewById(R.id.feedbackrecyclerview);
        adapter = new FeedBackAdapter(name, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        feedRecyclerView.setLayoutManager(horizontalLayoutManager);
        feedRecyclerView.setAdapter(adapter);
        feedRecyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();


    }

    public void feedbackAction() {

        edit_discription = edit.getText().toString();
        if (isConn()) {

            FeedbackApiExecution(selectedType);
        } else {
            new AlertShowingDialog(FeedbackActivity.this, " No Internet Connection");

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


    // Step 1:-
    public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {

        // step 3:-
        ArrayList<String> arrayList = new ArrayList<>();
        Context ctx;

        public FeedBackAdapter(ArrayList<String> arrayList, Context ctx) {
            this.ctx = ctx;
            this.arrayList = arrayList;
        }

        // step 5:-
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.feedbacklist, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx, arrayList);
            return myViewHolder;
        }

        //step 6:-
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {


            holder.userName.setText(name.get(position));


            if (selectedPosition == position) {
                selectedType = name.get(selectedPosition);
                holder.image.setBackgroundResource(R.drawable.ic_good_on);


                if (position == 0) {
                    holder.image.setBackgroundResource(R.drawable.ic_good_on);

                }

                if (position == 1) {
                    holder.image.setBackgroundResource(R.drawable.ic_average_on);

                }
                if (position == 2) {
                    holder.image.setBackgroundResource(R.drawable.ic_bad_on);

                }

            } else {

                if (position == 0) {
                    holder.image.setBackgroundResource(R.drawable.ic_good_off);

                }
                if (position == 1) {
                    holder.image.setBackgroundResource(R.drawable.ic_average_off);

                }
                if (position == 2) {
                    holder.image.setBackgroundResource(R.drawable.ic_bad_off);
                }

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPosition != position) {
                        selectedPosition = position;
                        Log.e("pos", "" + selectedPosition);
                        adapter.notifyDataSetChanged();

                    } else {
                        selectedPosition = -1;
                        adapter.notifyDataSetChanged();
                    }

                }

            });
        }

        // step 4:-
        @Override
        public int getItemCount() {

            Log.e("listarray", "" + arrayList.size());
            return arrayList.size();
        }

        // Step 2:-
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            TextView userName;

            ImageView image;
            ArrayList<String> arrayList = new ArrayList<String>();
            Context ctx;

            public ViewHolder(View itemView, Context ctx, final ArrayList<String> arrayList) {
                super(itemView);

                this.arrayList = arrayList;
                this.ctx = ctx;
                userName = (TextView) itemView.findViewById(R.id.name);
                image = (ImageView) itemView.findViewById(R.id.image);

                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {

            }
        }
    }

    public static void toISO8601UTC() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        DateFormat df = new SimpleDateFormat("2018-06-14'T'18:08:22.450'Z'");
        df.setTimeZone(tz);
        Log.e("pedda", "" + df.getCalendar());
        //return df.format();
    }

    //FeedbackAps////

    private void FeedbackApiExecution(String selectedType) {

        final FeedbackServerObject requestBody = new FeedbackServerObject();
        requestBody.mailid = UserDataController.getInstance().currentUser.getEmail();
        requestBody.additionalcomment = edit_discription.trim();
        requestBody.feelsupport = selectedType;
        requestBody.timestamp = "08:30";


        Log.e("mailid", "" + requestBody.mailid);
        Log.e("password", "" + requestBody.additionalcomment);
        Log.e("feeltype", "" + requestBody.feelsupport);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<FeedbackServerObject> callable = api.feedback(requestBody);
        callable.enqueue(new Callback<FeedbackServerObject>() {
            @Override
            public void onResponse(Call<FeedbackServerObject> call, retrofit2.Response<FeedbackServerObject> response) {
                refreshShowingDialog.hideRefreshDialog();
                String statuscode = response.body().result;
                String message = response.body().message;

                Log.e("codefor3", "call" + statuscode);
                Log.e("message", "call" + message);


                if (!statuscode.equals(null)) {
                    if (statuscode.equals("success")) {

                        finish();
                        Toast.makeText(getApplicationContext(), "Feedback  Success", Toast.LENGTH_SHORT).show();


                    } else {
                        new AlertShowingDialog(FeedbackActivity.this, message);


                    }
                }
            }

            @Override
            public void onFailure(Call<FeedbackServerObject> call, Throwable t) {
                refreshShowingDialog.hideRefreshDialog();
            }
        });
    }

}
