package com.vedas.weightloss.Settings;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.Models.PersonalInfoModel;
import com.vedas.weightloss.R;
import com.vedas.weightloss.ServerApis.LocationTracker;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by VEDAS on 5/5/2018.
 */
public class PersonalInfoActivity extends BaseDetailActivity {
    int type;
    TextInputEditText textInputdob, textInputzip;
    EditText textInputloc;
    ArrayList<String> genderArray;

    String selectedGender = "Female";
    SimpleDateFormat df, df_year, weekFormatter;
    String mage;
    CircleImageView profileImage;
    private static final int CAMERA_REQUEST = 1880;
    public static final int PICK_IMAGE = 1889;
    String profileBase64Obj, oldProfielBase64Obj;
    public byte[] imageInByte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);
        setupToolbar();
        textInputdob = (TextInputEditText) findViewById(R.id.dob);
        textInputzip = (TextInputEditText) findViewById(R.id.zip);
        textInputloc = (EditText) findViewById(R.id.loc);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        df_year = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        weekFormatter = new SimpleDateFormat("E,dd", Locale.ENGLISH);
        profileImage = (CircleImageView) findViewById(R.id.imagepic);
        profileImage.setOnClickListener(mProfileListener);
        LocationTracker.getInstance().startLocation();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                textInputdob.setVisibility(View.VISIBLE);
                textInputzip.setVisibility(View.VISIBLE);
                textInputloc.setVisibility(View.VISIBLE);
            }
        }, 500);

        textInputloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(getApplicationContext(), FindDoctorsViewController.class));
            }
        });

        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("personalImage", Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            String jsonString = pSharedPref.getString("picture", null);
            if (jsonString != null) {
                byte[] data = Base64.decode(jsonString, Base64.DEFAULT);
                if (data.length > 0) {
                    profileImage.setImageBitmap(PersonalInfoController.getInstance().convertByteArrayTOBitmap(data));
                }
            } else {
                Log.e("loaddefalule", "call");
                loadDefaultImage();
            }
        }
        loadFbandGoogleData();
        loadtextFeildActions();
        setupWindowAnimations();
        setCurrentUserInfo();
    }

    private void loadFbandGoogleData() {
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences("socialMediaLoginDetails", Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            String jsonStringName = pSharedPref.getString("name", null);
            String jsonStringImage = pSharedPref.getString("picture", null);
            Log.e("picasoimage", "call" + jsonStringImage);
            if (jsonStringImage != null) {
                Log.e("picasoimage123", "call" + jsonStringImage);
                Glide.with(getApplicationContext())
                        .load(jsonStringImage)
                        .asBitmap()
                        .placeholder(R.drawable.ic_defaultprofile)
                        .into(new BitmapImageViewTarget(profileImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                //Play with bitmap
                                super.setResource(resource);
                                Log.e("glideimade", "call" + resource);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                Log.e("encoded", "call" + encoded);
                                PersonalInfoController.getInstance().loadImage(encoded);

                            }
                        });

            }
        } else {
            Log.e("loaddefalulePicaso", "call");
            loadDefaultImage();
        }
    }

    public void loadDefaultImage() {

        profileImage.setImageResource(R.drawable.ic_defaultprofile);
        Bitmap resource = BitmapFactory.decodeResource(getResources(), R.drawable.ic_defaultprofile);
        resource = PersonalInfoController.getInstance().getResizedBitmap(resource, 300);
        convertBitmapToByteArray(resource);
        profileImage.setImageBitmap(resource);
        loadEncoded64ImageStringFromBitmap(resource);

    }

    public void setCurrentUserInfo() {

        if (PersonalInfoController.getInstance().retrivePersonalDataFromUserDefaults()) {
            Log.e("PersonalData", "Called Inside");
            selectedGender = PersonalInfoController.getInstance().selectedPersonalInfoModel.getGender();
            textInputloc.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getLocation());
            textInputdob.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getBirthday());
            textInputzip.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getZipcode());
            loadGenderSpinner();
            if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getBirthday() != null) {
                String selectedDate[] = PersonalInfoController.getInstance().selectedPersonalInfoModel.getBirthday().split("/");
                //convert them to int
                int mDay = Integer.valueOf(selectedDate[0]);
                int mMonth = Integer.valueOf(selectedDate[1]);
                int mYear = Integer.valueOf(selectedDate[2]);
                mage = PersonalInfoController.getInstance().calculatingAge(mYear, mMonth, mDay);
            }

            /*if (PersonalInfoController.getInstance().selectedPersonalInfoModel.getMprofilepicturepath() != null) {
                profileImage.setImageBitmap(convertByteArrayTOBitmap(PersonalInfoController.getInstance().selectedPersonalInfoModel.getMprofilepicturepath()));
            }*/
        } else {
            Log.e("PersonalData", "Called Outside");
            loadCurrentLocationandZipcode();
            loadGenderSpinner();
            PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
        }
    }

    private void loadCurrentLocationandZipcode() {
        if (LocationTracker.getInstance().currentLocationString != null) {
            if (LocationTracker.getInstance().currentLocationZipcode != null) {
                textInputloc.setText("" + LocationTracker.getInstance().currentLocationString);
                textInputzip.setText("" + LocationTracker.getInstance().currentLocationZipcode);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setLocation("" + LocationTracker.getInstance().currentLocationString);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setZipcode(
                        LocationTracker.getInstance().currentLocationZipcode);
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();

            }
        }
    }

    public void onResume() {
        super.onResume();
        // checkIsUserHavingData();
        //  setCurrentUserInfo();
        // loadCurrentLocationandZipcode();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e("sidemenuMessageevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("locationSuccess")) {
            if (PersonalInfoController.getInstance().retrivePersonalDataFromUserDefaults()) {
                Log.e("dblocation", "" + resultData);
                textInputloc.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getLocation());
                textInputzip.setText(PersonalInfoController.getInstance().selectedPersonalInfoModel.getZipcode());
            } else {
                Log.e("locationtracker", "" + resultData);
                loadCurrentLocationandZipcode();
            }
        }
    }

    /*private void checkIsUserHavingData() {
        if (PersonalInfoDataController.getInstance().personalInfoArray.size() > 0) {
            PersonalInfoDataController.getInstance().fetchPersonalInfoData();
            Log.e("selectedmodel", "call" + PersonalInfoDataController.getInstance().personalInfoArray);
            objPersonalInfoModel = PersonalInfoDataController.getInstance().personalInfoArray.get(0);
            selectedGender = objPersonalInfoModel.getGender();
            textInputloc.setText(objPersonalInfoModel.getLocation());
            textInputdob.setText(objPersonalInfoModel.getBirthday());
            textInputzip.setText(objPersonalInfoModel.getZipcode());
            String selectedDate[] = objPersonalInfoModel.getBirthday().split("/");
            //convert them to int
            Log.e("currente", "call" + objPersonalInfoModel.getBirthday());
            int mDay = Integer.valueOf(selectedDate[0]);
            int mMonth = Integer.valueOf(selectedDate[1]);
            int mYear = Integer.valueOf(selectedDate[2]);
            mage = calculatingAge(mYear, mMonth, mDay);
            if (objPersonalInfoModel.getMprofilepicturepath() != null) {
                profileImage.setImageBitmap(convertByteArrayTOBitmap(objPersonalInfoModel.getMprofilepicturepath()));
            }
        }
    }*/

    void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        TextView textView = (TextView) toolbar.findViewById(R.id.title);
        textView.setText("You");

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
                if (textInputdob.getText().toString().length() > 0) {
                    PersonalInfoController.getInstance().selectedPersonalInfoModel.setBirthday(textInputdob.getText().toString());
                    final Intent i = new Intent(PersonalInfoActivity.this, PersonalInfoNextActivity.class);
                    i.putExtra(EXTRA_TYPE, TYPE_PROGRAMMATICALLY);
                    i.putExtra("age", mage);
                    i.putExtra("gender", selectedGender);
                    transitionTo(i);
                } else {
                    final View contextView = findViewById(R.id.context_view);
                    Snackbar.make(contextView, R.string.selectitem, Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.CYAN)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Respond to the click, such as by undoing the modification that caused
                                    // this message to be displayed
                                    //   startActivity(new Intent(getApplicationContext(),LoginViewActivity.class));
                                }

                                ;
                            }).show();
                }
            }
        });
    }

    private void loadtextFeildActions() {
        textInputdob.setShowSoftInputOnFocus(false);
        textInputdob.
                setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasfocus) {
                        if (hasfocus) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(textInputdob.getWindowToken(), 0);
                            /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(i, 1);*/
                            loadDatePicker();
                        }
                    }
                });
        textInputdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(i, 1);*/
                loadDatePicker();
            }
        });
        textInputzip.setShowSoftInputOnFocus(false);
        textInputloc.setShowSoftInputOnFocus(false);
    }

    private void loadDatePicker() {
        int mYear, mMonth, mDay;
        DatePickerDialog datePickerDialog;
        // calender class's instance and get current date , month and year from calender
        if (textInputdob.getText().toString().length() > 0) {
            String selectedDate[] = textInputdob.getText().toString().split("/");
            //convert them to int
            Log.e("currente", "call" + textInputdob.getText().toString());
            mDay = Integer.valueOf(selectedDate[0]);
            mMonth = Integer.valueOf(selectedDate[1]);
            mYear = Integer.valueOf(selectedDate[2]);
            // date picker dialog
            datePickerDialog = new DatePickerDialog(PersonalInfoActivity.this, R.style.DialogTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            String date = dayOfMonth + "/"
                                    + (monthOfYear + 1) + "/" + year;
                            Log.e("currentdate", "call" + date);
                            mage = PersonalInfoController.getInstance().calculatingAge(year, monthOfYear, dayOfMonth);
                            if (mage.equals("0")) {
                                new AlertShowingDialog(PersonalInfoActivity.this, "Your age must be graterthan 0");
                                textInputdob.setText("");
                            } else {
                                textInputdob.setText("" + date);
                                PersonalInfoController.getInstance().selectedPersonalInfoModel.setBirthday(date);
                                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();

                            }
                        }
                    }, mYear, mMonth - 1, mDay);
            datePickerDialog.show();
        } else {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR); // current year
            mMonth = c.get(Calendar.MONTH); // current month
            mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(PersonalInfoActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            String date = dayOfMonth + "/"
                                    + (monthOfYear + 1) + "/" + year;
                            Log.e("currentdate", "call" + date);
                            mage = PersonalInfoController.getInstance().calculatingAge(year, monthOfYear, dayOfMonth);
                            if (mage.equals("0")) {
                                new AlertShowingDialog(PersonalInfoActivity.this, "Your age must be graterthan 0");
                                textInputdob.setText("");
                            } else {
                                textInputdob.setText("" + date);
                                PersonalInfoController.getInstance().selectedPersonalInfoModel.setBirthday(date);
                                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    private void loadGenderSpinner() {
        genderArray = new ArrayList<>();
        genderArray.add("Female");
        genderArray.add("Male");
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinnergender);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedGender != null) {
            int spinnerPosition = aa.getPosition(selectedGender);
            spin.setSelection(spinnerPosition);
            PersonalInfoController.getInstance().selectedPersonalInfoModel.setGender(selectedGender);
            PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGender = genderArray.get(i);
                Log.e("selectedGender", "call" + selectedGender);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setGender(selectedGender);
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        Log.e("onActivityResult", "call");
        if (data == null) {
            return;
        } else if (requestcode == CAMERA_REQUEST && resultcode == Activity.RESULT_OK) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photo = PersonalInfoController.getInstance().getResizedBitmap(photo, 300);
                convertBitmapToByteArray(photo);
                profileImage.setImageBitmap(photo);
                loadEncoded64ImageStringFromBitmap(photo);


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestcode == PICK_IMAGE && resultcode == Activity.RESULT_OK) {
            Log.e("gallary", "call");
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap photo = BitmapFactory.decodeStream(inputStream);
                photo = PersonalInfoController.getInstance().getResizedBitmap(photo, 300);
                convertBitmapToByteArray(photo);
                profileImage.setImageBitmap(photo);
                loadEncoded64ImageStringFromBitmap(photo);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
       /* //// for getting date of birth
        if (requestcode == 1) {
            if (requestcode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
                Date selectedDateOject = null;

                try {
                    selectedDateOject = df.parse(strEditText);
                    textInputdob.setText("" + df.format(selectedDateOject));
                    Log.e("onActivityResult", "call" + df_year.format(selectedDateOject));
                    Log.e("onActivityResult", "call" + weekFormatter.format(selectedDateOject));
                    String birthdate = df.format(selectedDateOject);
                    String[] fullDate = birthdate.split("-");
                    int year = Integer.parseInt(fullDate[0]);
                    int month = Integer.parseInt(fullDate[1]);
                    int day = Integer.parseInt(fullDate[2]);
                    mage = calculatingAge(year, month, day);
                    Log.e("age", "call" + mage);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.e("onActivityResult", "call" + strEditText);

            }
        }*/
    }

    View.OnClickListener mProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(PersonalInfoActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.profile_alert);
            dialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.gravity = Gravity.CENTER;
            lp.windowAnimations = R.style.DialogAnimation1;
            dialog.getWindow().setAttributes(lp);

            dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
            RelativeLayout txtCamera = (RelativeLayout) dialog.findViewById(R.id.camera);
            RelativeLayout txtAlubm = (RelativeLayout) dialog.findViewById(R.id.album);

            TextView photo = (TextView) dialog.findViewById(R.id.textview);
            photo.setText("Set your photo");
            TextView camera = (TextView) dialog.findViewById(R.id.txt_camera);
            TextView album = (TextView) dialog.findViewById(R.id.txt_album);
            TextView cancle = (TextView) dialog.findViewById(R.id.txt_cancle);

            txtCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickImageFromCamera();
                    dialog.dismiss();
                }
            });
            txtAlubm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetImageFromGallery();
                    dialog.dismiss();
                }
            });

            RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.btn_no);
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });

        }
    };

    public void GetImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    public void ClickImageFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);

    }

    public void loadEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageInByte = stream.toByteArray();
        PersonalInfoController.getInstance().selectedPersonalInfoModel.setMprofilepicturepath(imageInByte);
        // PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
        profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + profileBase64Obj);
        PersonalInfoController.getInstance().loadImage(profileBase64Obj);
    }

    private void convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageInByte = stream.toByteArray();
        Log.e("imageInByte", "call" + imageInByte);//[B@64d27a8

    }

    /*public Bitmap convertByteArrayTOBitmap(byte[] profilePic) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(profilePic);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }
    //for reducing the filesize of image

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
*/
    public static class MessageEvent {
        public final String message;

        public MessageEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
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

    private Transition buildEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        return fade;
    }


    @Override
    public void onBackPressed() {    //when click on phone backbutton
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {  // could be in onPause or onStop
        super.onDestroy();
    }
}
