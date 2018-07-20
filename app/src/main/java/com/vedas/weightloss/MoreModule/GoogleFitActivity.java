
package com.vedas.weightloss.MoreModule;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.vedas.weightloss.FoodModule.DayOneActivity;
import com.vedas.weightloss.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ,vm;
 */


public class GoogleFitActivity extends AppCompatActivity implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {


    Toolbar toolbar;
    TextView tool_text;
    ImageView btn_back, home;

    private Button mButtonViewWeek;
    private Button mButtonViewToday;
    private Button mButtonAddSteps;
    private Button mButtonUpdateSteps;
    private Button mButtonDeleteSteps;

    private GoogleApiClient mGoogleApiClient;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_activity);
        initViews();
        setToolbar();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))

                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tool_text = (TextView) toolbar.findViewById(R.id.toolbar_text);
        tool_text.setText("App Connected to Google fit");
        btn_back = (ImageView) toolbar.findViewById(R.id.back);
        home = (ImageView) toolbar.findViewById(R.id.toolbar_icon);
        btn_back.setBackgroundResource(R.drawable.ic_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void initViews() {
        mButtonViewWeek = (Button) findViewById(R.id.btn_view_week);
        mButtonViewToday = (Button) findViewById(R.id.btn_view_today);
        mButtonAddSteps = (Button) findViewById(R.id.btn_add_steps);
        mButtonUpdateSteps = (Button) findViewById(R.id.btn_update_steps);
        mButtonDeleteSteps = (Button) findViewById(R.id.btn_delete_steps);

        mButtonViewWeek.setOnClickListener(this);
        mButtonViewToday.setOnClickListener(this);
        mButtonAddSteps.setOnClickListener(this);
        mButtonUpdateSteps.setOnClickListener(this);
        mButtonDeleteSteps.setOnClickListener(this);
    }

    public void onConnected(@Nullable Bundle bundle) {
        Log.e("HistoryAPI", "onConnected");
    }

    //In use, call this every 30 seconds in active mode, 60 in ambient on watch faces
    private void displayStepDataForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
        showDataSet(result.getTotal());
    }

    private void displayCaloriesForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_CALORIES_EXPENDED).await(1, TimeUnit.MINUTES);
        showDataSet(result.getTotal());
    }

    private void displayDistanceForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_DISTANCE_DELTA).await(1, TimeUnit.MINUTES);
        showDataSet(result.getTotal());
    }

    private void displayTimeForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_ACTIVITY_SEGMENT).await(1, TimeUnit.MINUTES);
        showDataSet(result.getTotal());
    }

    private void displayLastWeeksData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);

        long startTime = cal.getTimeInMillis();

        DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        //Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);

        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
            Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());


            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    showDataSet(dataSet);
                }
            }
        }
        //Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
            Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                showDataSet(dataSet);
            }
        }
    }


    private void showDataSet(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();


        for (final DataPoint dp : dataSet.getDataPoints()) {

            Log.e("History", "Data point:");
            Log.e("History", "\tType: " + dp.getDataType().getName());
            Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            for (final Field field : dp.getDataType().getFields()) {
                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        text = (TextView) findViewById(R.id.noofsteps);
                        // text.setText("Field: " + field.getName() + " Value: " + dp.getValue(field).toString());
                        Log.e("value", "call" + dp.getValue(field).toString());
                        Log.e("names", "call" + field.getName());

                        if (field.getName().equals("steps")) {
                            String stepsVal = dp.getValue(field).toString();
                            text.setText("Field: " + field.getName() + " Value: " + stepsVal);
                        } else if (field.getName().equals("distance")) {
                            String disVal = dp.getValue(field).toString();
                            double disInKm = Double.parseDouble(disVal);
                            DecimalFormat df = new DecimalFormat("0.##");
                            double d = disInKm / 1000;
                            Log.e("disValue", "call" + df.format(d));
                            text.setText("Field: " + field.getName() + " Value: " + df.format(d));


                        } else if (field.getName().equals("calories")) {
                            String calVal = dp.getValue(field).toString();
                            double cal = Double.parseDouble(calVal);
                            DecimalFormat df = new DecimalFormat("###");
                            double d = cal / 1000;
                            Log.e("calValue", "call" + d);

                            text.setText("Field: " + field.getName() + " Value: " + Math.round(cal));


                        } else if (field.getName().equals("duration")) {
                            String timeVal = dp.getValue(field).toString();
                            Log.e("timeval", "" + timeVal);
                            long l = (long) Double.parseDouble(timeVal);
                            l = l / 1000;
                            DecimalFormat df = new DecimalFormat("0");
                            long d = l / 60;
                            Log.e("d", "" + df.format(d));
                            text.setText("Field: " + field.getName() + " Value: " + df.format(d));
                        }
                    }

                });


            }

        }
    }


    private void addStepDataToGoogleFit() {
        //Adds steps spread out evenly from start time to end time
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName("Step Count")
                .setType(DataSource.TYPE_RAW)
                .build();

        int stepCountDelta = 1000000;
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(point);

        Status status = Fitness.HistoryApi.insertData(mGoogleApiClient, dataSet).await(1, TimeUnit.MINUTES);

        if (!status.isSuccess()) {
            Log.e("History", "Problem with inserting data: " + status.getStatusMessage());
        } else {
            Log.e("History", "data inserted");
        }
    }

    private void updateStepDataOnGoogleFit() {
        //If two entries overlap, the new data is dropped when trying to insert. Instead, you need to use update
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.HOUR_OF_DAY, -1);
        long startTime = cal.getTimeInMillis();

        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName("Step Count")
                .setType(DataSource.TYPE_RAW)
                .build();

        int stepCountDelta = 2000000;
        DataSet dataSet = DataSet.create(dataSource);

        DataPoint point = dataSet.createDataPoint()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(point);

        DataUpdateRequest updateRequest = new DataUpdateRequest.Builder().setDataSet(dataSet).setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS).build();
        Fitness.HistoryApi.updateData(mGoogleApiClient, updateRequest).await(1, TimeUnit.MINUTES);
    }

    private void deleteStepDataOnGoogleFit() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataDeleteRequest request = new DataDeleteRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .build();

        Fitness.HistoryApi.deleteData(mGoogleApiClient, request).await(1, TimeUnit.MINUTES);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("HistoryAPI", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HistoryAPI", "onConnectionFailed");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_view_week: {
                new ViewWeekStepCountTask().execute();
                break;
            }
            case R.id.btn_view_today: {
                new ViewTodaysStepCountTask().execute();
                break;
            }
            case R.id.btn_add_steps: {
                new ViewTodaysTimeCountTask().execute();
                break;
            }
            case R.id.btn_update_steps: {
                new ViewTodaysDistanceCountTask().execute();
                break;
            }
            case R.id.btn_delete_steps: {
                new ViewTodaysCaloriesCountTask().execute();
               // moveToDayoneActivity(text.getText().toString().trim());

                break;
            }
        }
    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {
        for (final Field field : dataPoint.getDataType().getFields()) {
            final Value value = dataPoint.getValue(field);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TextView text = (TextView) findViewById(R.id.noofsteps);
                    //text.setText(field.getName()+"Value:" +value);
                    //Toast.makeText(getApplicationContext(), "Field: " + field.getName() + " Value: " + value, Toast.LENGTH_SHORT).show();
                    text.setText("Field: " + field.getName() + " Value: " + value.toString());
                }
            });
        }
    }

    private class ViewWeekStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayLastWeeksData();
            return null;
        }
    }

    private class ViewTodaysStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayStepDataForToday();
            return null;
        }
    }

    private class ViewTodaysCaloriesCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {


            displayCaloriesForToday();
            return null;
        }
    }

    private class ViewTodaysDistanceCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayDistanceForToday();
            return null;
        }
    }

    private class ViewTodaysTimeCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            // addStepDataToGoogleFit();

            displayTimeForToday();
            return null;


        }
    }

    private void moveToDayoneActivity(String caloriestext) {

        Intent intent = new Intent(getApplicationContext(), DayOneActivity.class);
        intent.putExtra("email", caloriestext);
        startActivity(intent);

    }
   /* private class ViewTodaysTimeCountTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Begin by creating the query.
            DataReadRequest readRequest = queryFitnessData();


            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);

            printData(dataReadResult);

            return null;
        }
    }

    public static DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long startTime = cal.getTimeInMillis();

        DataSource ACTIVITY_SEGMENT = new DataSource.Builder()
                .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_activity_segment")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                .bucketByActivitySegment(1, TimeUnit.SECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        return readRequest;
    }

    public static void printData(DataReadResult dataReadResult) {
        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                long activeTime = bucket.getEndTime(TimeUnit.MINUTES) - bucket.getStartTime(TimeUnit.MINUTES);
                Log.e("call", "Active time " + activeTime);
            }
        }

    }
*/

    private class AddStepsToGoogleFitTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            addStepDataToGoogleFit();
            displayLastWeeksData();
            return null;
        }
    }

    private class UpdateStepsOnGoogleFitTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            updateStepDataOnGoogleFit();
            displayLastWeeksData();
            return null;
        }
    }

    private class DeleteYesterdaysStepsTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            deleteStepDataOnGoogleFit();
            displayLastWeeksData();
            return null;
        }
    }

    private class FetchCalorieAsync extends AsyncTask<Object, Object, Long> {
        protected Long doInBackground(Object... params) {
            long total = 0;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_CALORIES_EXPENDED);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : totalSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asInt();
                }
            } else {
                Log.e("call", "There was a problem getting the calories.");
            }
            return total;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            //Total calories burned for that day
            Log.e("call", "Total calories: " + aLong);

        }
    }

    private class FetchStepsAsync extends AsyncTask<Object, Object, Long> {
        protected Long doInBackground(Object... params) {
            long total = 0;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                }
            } else {
                Log.e("call", "There was a problem getting the step count.");
            }
            return total;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            //Total steps covered for that day
            Log.e("call", "Total steps: " + aLong);

        }
    }

}



