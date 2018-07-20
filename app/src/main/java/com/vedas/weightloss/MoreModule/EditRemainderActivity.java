package com.vedas.weightloss.MoreModule;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.vedas.weightloss.DataBase.RemainderDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.Models.RemainderObject;
import com.vedas.weightloss.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by Rise on 13/06/2018.
 */
public class EditRemainderActivity extends AppCompatActivity {

    ArrayList<String> remainderArray;
    ArrayList<String> everydayArray;
    String remainder = "Breakfast";
    String everyday = "Every Day";
    TextView textday, texttime, save, texttype;
    EditText editnotes;
    Button button;
    public static RemainderObject remainderobj;
    String notestext, mTime;
    LocalData localData;
    int hour, min;
    ClipboardManager myClipboard;
    RelativeLayout relat;
    ToggleButton toggle;
    Spinner spin, spin1;
    boolean isSpinnerInitialize = false;
    boolean istoggleChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityremainder_editpage);
        ButterKnife.bind(this);
        localData = new LocalData(getApplicationContext());
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        init();
        loadkilogramSpinner();
        loadEverydaySpinner();

        timer();
        hour = localData.get_hour();
        min = localData.get_min();
        if (remainderobj != null) {
            RemainderObject remainderObject = remainderobj;
            textday.setText(remainderObject.getDays());
            texttime.setText(remainderObject.getCurrenttime());
            texttype.setText(remainderObject.getRemaindertype());
            editnotes.setText(remainderObject.getNotes());
            toggle.setChecked(remainderObject.isFromOnline());
            Log.e("nexttimecalled", "" + remainderObject.isFromOnline());

            relat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("bshqb", "call");
                    //showTimePickerDialog(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                    showTimePickerDialog(hour, min);
                }
            });


        } else {
            relat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("bshqb", "call");
                    showTimePickerDialog(localData.get_hour(), localData.get_min());
                }
            });
        }
    }

    @Override
    protected void onResume() {
        if (RemainderDataController.getInstance().currentremainder != null) {
            if (RemainderDataController.getInstance().currentremainder.getRemaindertype().equals("Water")) {
                textday.setText("Every One Hour");
                spin1.setVisibility(View.INVISIBLE);
            } else {
                spin1.setVisibility(View.VISIBLE);
                textday.setText("Every Day");
            }
            if (RemainderDataController.getInstance().currentremainder != null) {
                texttype.setText(RemainderDataController.getInstance().currentremainder.getRemaindertype());
                texttype.setVisibility(View.VISIBLE);
                spin.setVisibility(View.INVISIBLE);
                textday.setVisibility(View.VISIBLE);
                spin1.setVisibility(View.INVISIBLE);

            } else {
                texttype.setVisibility(View.INVISIBLE);
                spin.setVisibility(View.VISIBLE);
                textday.setVisibility(View.INVISIBLE);
                spin1.setVisibility(View.VISIBLE);
            }

        } else {
            /*texttime.setVisibility(View.VISIBLE);
            spin1.setVisibility(View.VISIBLE);
            texttype.setVisibility(View.INVISIBLE);
            spin.setVisibility(View.VISIBLE);
*/
        }

        super.onResume();
    }

    private void init() {
        texttype = (TextView) findViewById(R.id.texttype);
        textday = (TextView) findViewById(R.id.textday);
        texttime = (TextView) findViewById(R.id.texttime);
        editnotes = (EditText) findViewById(R.id.editnotes);
        relat = (RelativeLayout) findViewById(R.id.relat);
        toggle = (ToggleButton) findViewById(R.id.toggle);


        editnotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("not", "" + s.toString());
                notestext = s.toString();
                Log.e("notes", "" + notestext);


            }
        });


        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemainderObject objReminder = new RemainderObject();


                if (remainderobj != null) {
                    objReminder.setNotes(notestext);
                    objReminder.setCurrenttime(texttime.getText().toString());
                    objReminder.setRemaindertype(texttype.getText().toString());
                    objReminder.setDays(textday.getText().toString());
                    if (toggle.isChecked()) {
                        Log.e("togglechecked", "calll" + istoggleChecked);
                        objReminder.setFromOnline(istoggleChecked);
                    } else {
                        Log.e("togglenotchecked", "calll" + remainderobj.isFromOnline());
                        objReminder.setFromOnline(remainderobj.isFromOnline());
                    }
                } else {

                    objReminder.setNotes(notestext);
                    objReminder.setCurrenttime(texttime.getText().toString());
                    objReminder.setRemaindertype(remainder);
                    objReminder.setDays(everyday);
                    objReminder.setFromOnline(istoggleChecked);
                    Log.e("bjfnhjjk", "" + istoggleChecked);
                }
                loadReminderDataIntodb(RemainderDataController.getInstance().allRemainders, objReminder);
                finish();
                startActivity(new Intent(getApplicationContext(), RemindersActivity.class));
            }
        });


        button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        toggle.setChecked(localData.getReminderStatus());

        if (!localData.getReminderStatus())
            relat.setAlpha(0.4f);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //localData.setReminderStatus(isChecked);
                if (isChecked) {
                    istoggleChecked = true;
                    Log.e("conn", "onCheckedChanged: true");
//                    NotificationScheduler.setReminder(getApplicationContext(), AlarmReceiver.class, localData.get_hour(), localData.get_min());
                    relat.setAlpha(1f);
                } else {
                    Log.e("conn", "onCheckedChanged: false");
                    NotificationScheduler.cancelReminder(getApplicationContext(), AlarmReceiver.class);
                    relat.setAlpha(0.4f);
                    istoggleChecked = false;
                }

            }
        });

    }

    private void loadReminderDataIntodb(ArrayList<RemainderObject> arrayList, RemainderObject object) {
        Log.e("methodcall", "call");
        if (remainderobj != null) {
            Log.e("remainderobj", "call");
            if (arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getRemaindertype().equals(remainderobj.getRemaindertype())) {
                        if (remainderobj.getRemaindertype().equals(object.getRemaindertype())) {
                            Log.e("obj", "call" + object.getRemaindertype());
                            Log.e("remobj", "call" + remainderobj.getRemaindertype());
                            Log.e("notsame", "call");

                            RemainderDataController.getInstance().updateRemainderData(object);

                        } else {
                            Log.e("same", "call");

                            RemainderDataController.getInstance().insertRemainderData(object);

                        }
                    }
                }
            }
        } else {
            Log.e("insert", "call");
            RemainderDataController.getInstance().insertRemainderData(object);

        }
    }

    private void showTimePickerDialog(int h, int m) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.timepicker_header, null);

        TimePickerDialog builder = new TimePickerDialog(this, R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        Log.d("jkjkk", "onTimeSet: hour " + hour);
                        Log.d("jvjkk", "onTimeSet: min " + min);
                        localData.set_hour(hour);
                        localData.set_min(min);
                        texttime.setText(getFormatedTime(hour, min));
                        mTime = texttime.getText().toString();
                        Log.e("time", "" + mTime);
                        NotificationScheduler.setReminder(EditRemainderActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());


                    }
                }, h, m, false);

        builder.setCustomTitle(view);
        builder.show();

    }

    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";
        String oldDateString = h + ":" + m;
        String newDateString = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }


    private void loadkilogramSpinner() {
        remainderArray = new ArrayList<>();
        remainderArray.add("Breakfast");
        remainderArray.add("Exercise");
        remainderArray.add("Water");
        remainderArray.add("Lunch");
        remainderArray.add("Dinner");
        remainderArray.add("Snacks");

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin = (Spinner) findViewById(R.id.remainderspinner);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, remainderArray);
        aa.setDropDownViewResource(R.layout.spinner_item);
        spin.setAdapter(aa);
        if (remainder != null) {
            int spinnerPosition = aa.getPosition(remainder);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                remainder = remainderArray.get(position);
                Log.e("remainder", "call" + remainderArray.get(position));

                if (isSpinnerInitialize) {
                    remainder = remainderArray.get(position);
                    Log.e("spnnerinitialize", "call" + remainderArray.get(position));
                    if (remainder.equals("Water")) {
                        texttime.setVisibility(View.VISIBLE);
                        textday.setVisibility(View.VISIBLE);
                        spin1.setVisibility(View.INVISIBLE);
                        relat.setVisibility(View.VISIBLE);
                    } else {
                        texttime.setVisibility(View.VISIBLE);
                        spin1.setVisibility(View.VISIBLE);
                        textday.setVisibility(View.INVISIBLE);
                        texttime.setVisibility(View.VISIBLE);
                        relat.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("elasepart", "call" + isSpinnerInitialize);
                    isSpinnerInitialize = true;
                }

                String item = adapterView.getItemAtPosition(position).toString();
                Log.e("selectedremainder", "call" + remainder);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadEverydaySpinner() {
        everydayArray = new ArrayList<>();
        everydayArray.add("Every Day");
        everydayArray.add("Sunday");
        everydayArray.add("Monday");
        everydayArray.add("Tuesday");
        everydayArray.add("Wednesday");
        everydayArray.add("Thursday");
        everydayArray.add("Friday");
        everydayArray.add("Saturday");

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin1 = (Spinner) findViewById(R.id.eveydayspinner);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_item, everydayArray);
        aa.setDropDownViewResource(R.layout.spinner_item);
        spin1.setAdapter(aa);
        if (remainder != null) {
            int spinnerPosition1 = aa.getPosition(everyday);
            spin1.setSelection(spinnerPosition1);
        }
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                everyday = everydayArray.get(position);
                Log.e("selecteday", "call" + everyday);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void timer() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(d);
        texttime.setText(currentDateTimeString);
    }
}