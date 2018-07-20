package com.vedas.weightloss.MoreModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vedas.weightloss.Alert.AlertShowingDialog;
import com.vedas.weightloss.Alert.RefreshShowingDialog;
import com.vedas.weightloss.Controllers.PersonalInfoController;
import com.vedas.weightloss.DashBoardModule.MoreFragment;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.LoginModule.SplashScreenViewActivity;
import com.vedas.weightloss.Models.PersonalInfoModel;
import com.vedas.weightloss.R;
import com.vedas.weightloss.Settings.PersonalInfoActivity;
import com.vedas.weightloss.Settings.PersonalInfoNextActivity;
import com.vedas.weightloss.Settings.WeekGoalNextActivity;
import com.vedas.weightloss.Transition.BaseDetailActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by VEDAS on 6/12/2018.
 */

public class ProfilePageActivity extends BaseDetailActivity {
    TextView age, dob, name, height, weight, bmi, email, save;
    CircleImageView circleImageView;
    ImageView back;
    private static final int CAMERA_REQUEST = 1880;
    public static final int PICK_IMAGE = 1889;
    String profileBase64Obj;
    public byte[] imageInByte;
    int mRepeatMode = 0;

    ArrayList<String> genderArray;

    String selectedGender = "Female";
    Spinner gender;
    String mage;


    ArrayList<String> weightMeasures = new ArrayList<>();
    String selectedMeasure = "Kg";
    String selectedWeigthValue = "29";
    String selectedWeight;

    ArrayList<String> heightMeasures = new ArrayList<>();
    String selectedHeightMeasure = "Cm";
    String selectedFeetVal = "3", selectedInchValue = "0", selectedCmValue = "93";
    public static RefreshShowingDialog refreshShowingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        PersonalInfoDataController.getInstance().fetchPersonalInfoData();
        PersonalInfoController.getInstance().loadWeightValuesArray();
        PersonalInfoController.getInstance().loadHeightValuesArray();
        loadIds();
        setCurrentUserData();
        loadGenderSpinner();
        refreshShowingDialog = new RefreshShowingDialog(ProfilePageActivity.this);

    }


    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SplashScreenViewActivity.MessageEvent event) {
        Log.e("Personalinformation", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("Personalinformation")) {
            Log.e("Personalinformation", "call" + event.message);
            Intent intent = new Intent(getApplicationContext(), MoreFragment.class);
            startActivity(intent);

        } else if (resultData.equals("Personalinformation")) {

        }
    }

    private void loadIds() {
        circleImageView = (CircleImageView) findViewById(R.id.project_image);
        gender = (Spinner) findViewById(R.id.spinnergender);
        age = (TextView) findViewById(R.id.ageedit);
        dob = (TextView) findViewById(R.id.editdob);
        name = (TextView) findViewById(R.id.name);
        height = (TextView) findViewById(R.id.editheight);
        weight = (TextView) findViewById(R.id.editweight);
        bmi = (TextView) findViewById(R.id.editbmi);
        email = (TextView) findViewById(R.id.editemail);
        back = (ImageView) findViewById(R.id.back);
        save = (TextView) findViewById(R.id.profilesave);

        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                }
        );
        circleImageView.setOnClickListener(mProfileListener);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatePicker();
            }
        });
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHeightMeasureSpinner();
            }
        });
        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWeightMeasuresSpinner();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("updatepersonal", "call");
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setUser(UserDataController.getInstance().currentUser);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setTargetWeight(PersonalInfoDataController.getInstance().currentMember.getTargetWeight());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setBmi(PersonalInfoDataController.getInstance().currentMember.getBmi());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setBmr(PersonalInfoDataController.getInstance().currentMember.getBmr());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setActivityLevel(PersonalInfoDataController.getInstance().currentMember.getActivityLevel());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setRecommendedPlan(PersonalInfoDataController.getInstance().currentMember.getRecommendedPlan());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setTargetCalories(PersonalInfoDataController.getInstance().currentMember.getTargetCalories());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setZipcode(PersonalInfoDataController.getInstance().currentMember.getZipcode());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setTargetDays(PersonalInfoDataController.getInstance().currentMember.getTargetDays());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setBirthday(dob.getText().toString());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setHeight(height.getText().toString());
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setWeight(weight.getText().toString());
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
                refreshShowingDialog.showAlert();
                PersonalInfoController.getInstance().personalInfoApiExecution(PersonalInfoController.getInstance().selectedPersonalInfoModel, getApplicationContext());
                PersonalInfoDataController.getInstance().updateMemberData(PersonalInfoController.getInstance().selectedPersonalInfoModel);
            }
        });
    }

    private void loadDatePicker() {
        int mYear, mMonth, mDay;
        DatePickerDialog datePickerDialog;
        // calender class's instance and get current date , month and year from calender
        if (dob.getText().toString().length() > 0) {
            String selectedDate[] = dob.getText().toString().split("/");
            //convert them to int
            Log.e("currente", "call" + dob.getText().toString());
            mDay = Integer.valueOf(selectedDate[0]);
            mMonth = Integer.valueOf(selectedDate[1]);
            mYear = Integer.valueOf(selectedDate[2]);
            // date picker dialog
            datePickerDialog = new DatePickerDialog(ProfilePageActivity.this, R.style.DialogTheme,
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
                                new AlertShowingDialog(ProfilePageActivity.this, "Your age must be graterthan 0");
                                dob.setText("");
                            } else {
                                dob.setText("" + date);
                               /* PersonalInfoController.getInstance().selectedPersonalInfoModel.setBirthday(date);
                                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();*/

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
            datePickerDialog = new DatePickerDialog(ProfilePageActivity.this,
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
                                new AlertShowingDialog(ProfilePageActivity.this, "Your age must be graterthan 0");
                                dob.setText("");
                            } else {
                                dob.setText("" + date);
                               /* PersonalInfoController.getInstance().selectedPersonalInfoModel.setBirthday(date);
                                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();*/
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
                // selectedGender = adapterView.getItemAtPosition(i).toString();

                Log.e("selectedGender", "call" + selectedGender);
                PersonalInfoController.getInstance().selectedPersonalInfoModel.setGender(selectedGender);
                PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCurrentUserData() {
        if (PersonalInfoDataController.getInstance().personalInfoArray.size() > 0) {
            PersonalInfoModel objPersonalInfo = PersonalInfoDataController.getInstance().currentMember;
            name.setText("" + UserDataController.getInstance().currentUser.getName());
            selectedGender = objPersonalInfo.getGender();
            dob.setText(objPersonalInfo.getBirthday());
            height.setText(objPersonalInfo.getHeight());
            weight.setText(objPersonalInfo.getWeight());
            bmi.setText(objPersonalInfo.getBmi());
            email.setText(UserDataController.getInstance().currentUser.getEmail());
            if (objPersonalInfo.getBirthday() != null) {
                String selectedDate[] = objPersonalInfo.getBirthday().split("/");
                //convert them to int
                int mDay = Integer.valueOf(selectedDate[0]);
                int mMonth = Integer.valueOf(selectedDate[1]);
                int mYear = Integer.valueOf(selectedDate[2]);
                String mage = PersonalInfoController.getInstance().calculatingAge(mYear, mMonth, mDay);
                age.setText(mage);
            }
            if (objPersonalInfo.getMprofilepicturepath() != null) {
                circleImageView.setImageBitmap(convertByteArrayTOBitmap(objPersonalInfo.getMprofilepicturepath()));

            }
        }
    }

    public Bitmap convertByteArrayTOBitmap(byte[] profilePic) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(profilePic);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    private static final String[] REPEAT_MODES =
            new String[]{"Camera", "Album"};

    private AlertDialog createRepeatDialog() {
        return new AlertDialog.Builder(ProfilePageActivity.this)
                .setTitle("Set Photo")
                .setSingleChoiceItems(REPEAT_MODES, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mRepeatMode = i;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .show();
    }

    View.OnClickListener mProfileListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog(ProfilePageActivity.this);
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
            photo.setText("Are you want to change photo");
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
                circleImageView.setImageBitmap(photo);
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
                circleImageView.setImageBitmap(photo);
                loadEncoded64ImageStringFromBitmap(photo);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageInByte = stream.toByteArray();
        Log.e("imageInByte", "call" + imageInByte);//[B@64d27a8

    }

    public void loadEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageInByte = stream.toByteArray();
        PersonalInfoController.getInstance().selectedPersonalInfoModel.setMprofilepicturepath(imageInByte);
        profileBase64Obj = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
        Log.e("base64Image", "call" + profileBase64Obj);
        PersonalInfoController.getInstance().loadImage(profileBase64Obj);
        PersonalInfoDataController.getInstance().currentMember.setMprofilepicturepath(imageInByte);
        PersonalInfoDataController.getInstance().updateMemberData(PersonalInfoDataController.getInstance().currentMember);
    }


    private void loadHeightMeasureSpinner() {
        heightMeasures = new ArrayList<>();
        heightMeasures.add("Cm");
        heightMeasures.add("Feets & Inches");

        final Dialog mod = new Dialog(ProfilePageActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.activity_height_picker);
        mod.setCanceledOnTouchOutside(true);
        mod.setCancelable(true);
        mod.show();
        mod.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        final RelativeLayout rl_feetinch = (RelativeLayout) mod.findViewById(R.id.rl_feetinch);
        final RelativeLayout rl_cm = (RelativeLayout) mod.findViewById(R.id.rl_cm);
        final TextView txt_set = (TextView) mod.findViewById(R.id.set);
        txt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mod.dismiss();
                if (selectedHeightMeasure.equals("Cm")) {
                    height.setText("" + selectedCmValue + " cm");

                    /*PersonalInfoController.getInstance().selectedPersonalInfoModel.setHeight(height.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();*/
                    // calculatingBMR();
                } else {
                    height.setText("" + selectedFeetVal + " feet " + selectedInchValue + " inch");
                    /*PersonalInfoController.getInstance().selectedPersonalInfoModel.setHeight(height.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();*/
                    // calculatingBMR();
                }

            }
        });
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) mod.findViewById(R.id.spinnerHeight);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, heightMeasures);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedHeightMeasure != null) {
            int spinnerPosition = aa.getPosition(selectedHeightMeasure);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedHeightMeasure = heightMeasures.get(i);

                Log.e("selectedHeightMeasure", "call" + selectedHeightMeasure);
                if (selectedHeightMeasure.equals("Cm")) {
                    rl_feetinch.setVisibility(View.GONE);
                    rl_cm.setVisibility(View.VISIBLE);
                    loadCmSpinnerValues(mod);
                } else {
                    rl_feetinch.setVisibility(View.VISIBLE);
                    rl_cm.setVisibility(View.GONE);
                    loadFeetAndInchSpinnerValues(mod);
                    Log.e("selectedFeetVal", "call" + selectedFeetVal + "call" + selectedInchValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadFeetAndInchSpinnerValues(final Dialog mod) {
        //feet spinner
        Spinner spin = (Spinner) mod.findViewById(R.id.feetspinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().feetArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        Log.e("selectedFeetVal", "call" + selectedFeetVal);

        if (selectedFeetVal != null) {
            Log.e("tempselectedFeetVal", "call" + selectedFeetVal);
            int spinnerPosition = aa.getPosition(selectedFeetVal);
            Log.e("indextempselectedFe", "call" + spinnerPosition);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFeetVal = PersonalInfoController.getInstance().feetArray.get(i);
                String selectedValue = PersonalInfoController.getInstance().convertFeetToCm(selectedFeetVal + " " + selectedInchValue);
                Log.e("convertCmValue", "call" + selectedValue);
                int index = PersonalInfoController.getInstance().cmArray.indexOf(selectedValue);
                selectedCmValue = PersonalInfoController.getInstance().cmArray.get(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //inch spinner
        Spinner inchspin = (Spinner) mod.findViewById(R.id.inchspinner);
        ArrayAdapter inchspinaa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().inchArray);
        inchspinaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchspin.setAdapter(inchspinaa);
        Log.e("selectedInchValue", "call" + selectedInchValue);
        if (selectedInchValue != null) {
            Log.e("tempselectedInchValue", "call" + selectedInchValue);
            int spinnerPos = inchspinaa.getPosition(selectedInchValue);
            Log.e("indextempselectedin", "call" + spinnerPos);
            inchspin.setSelection(spinnerPos);
        }
        inchspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedInchValue = PersonalInfoController.getInstance().inchArray.get(i);
                String selectedValue = PersonalInfoController.getInstance().convertFeetToCm(selectedFeetVal + " " + selectedInchValue);
                Log.e("convertCmValue", "call" + selectedValue);
                int index = PersonalInfoController.getInstance().cmArray.indexOf(selectedValue);
                selectedCmValue = PersonalInfoController.getInstance().cmArray.get(index);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadCmSpinnerValues(final Dialog mod) {
        //inch spinner
        Spinner inchspin = (Spinner) mod.findViewById(R.id.spinnercm);
        ArrayAdapter inchspinaa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().cmArray);
        inchspinaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inchspin.setAdapter(inchspinaa);
        if (selectedCmValue != null) {
            int spinnerPosition = inchspinaa.getPosition(selectedCmValue);
            inchspin.setSelection(spinnerPosition);
        }
        inchspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCmValue = PersonalInfoController.getInstance().cmArray.get(i);
                String selectedfeeinch = PersonalInfoController.getInstance().convertCmToFeet(selectedCmValue);
                Log.e("selectedCmValue", "call" + selectedfeeinch);
                String feetinch[] = selectedfeeinch.split(" ");
                int index = PersonalInfoController.getInstance().feetArray.indexOf(feetinch[0]);
                int index1 = PersonalInfoController.getInstance().inchArray.indexOf(feetinch[1]);
                selectedFeetVal = PersonalInfoController.getInstance().feetArray.get(index);
                selectedInchValue = PersonalInfoController.getInstance().inchArray.get(index1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void loadWeightMeasuresSpinner() {
        weightMeasures = new ArrayList<>();
        weightMeasures.add("Kg");
        weightMeasures.add("lbs");
        final Dialog mod = new Dialog(ProfilePageActivity.this);
        mod.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mod.setContentView(R.layout.activity_weight_picker);
        mod.setCanceledOnTouchOutside(true);
        mod.setCancelable(true);
        mod.show();
        mod.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        RelativeLayout rl_measure = (RelativeLayout) mod.findViewById(R.id.measure);
        final TextView selected_measure = (TextView) mod.findViewById(R.id.selected_type);
        final TextView txt_set = (TextView) mod.findViewById(R.id.set);
        txt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mod.dismiss();
                if (selectedWeight != null && selectedWeight.contains("Kg")) {
                    String array[] = selectedWeight.split(" ");
                    selectedWeigthValue = array[0];
                    weight.setText("" + selectedWeigthValue + " Kg");
                    /*PersonalInfoController.getInstance().selectedPersonalInfoModel.setWeight(weight.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();*/
                    //calculatingBMR();

                } else {
                    String array[] = selectedWeight.split(" ");
                    selectedWeigthValue = array[0];
                    weight.setText("" + selectedWeigthValue + " Lbs");
                    /*PersonalInfoController.getInstance().selectedPersonalInfoModel.setWeight(weight.getText().toString());
                    PersonalInfoController.getInstance().storePersonalInfoIntoUserDeafaults();*/
                    //calculatingBMR();


                }

            }
        });
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) mod.findViewById(R.id.spinner1);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, weightMeasures);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        if (selectedMeasure != null) {
            int spinnerPosition = aa.getPosition(selectedMeasure);
            spin.setSelection(spinnerPosition);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMeasure = weightMeasures.get(i);
                Log.e("selectedMeasure", "call" + selectedMeasure);
                selected_measure.setText(selectedMeasure);
                loadWeigthSpinnerValues(mod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadWeigthSpinnerValues(mod);
    }

    private void loadWeigthSpinnerValues(final Dialog mod) {
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) mod.findViewById(R.id.spinner);
        if (selectedMeasure.equals("Kg")) {
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().kgArray);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(aa);
            if (weight.getText().toString() != null && selectedWeight != null && selectedWeight.contains("Kg")) {
                String array[] = weight.getText().toString().split(" ");
                selectedWeigthValue = array[0];
                Log.e("textValuekg", "call" + array[0]);
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            } else {
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            }

        } else {
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, PersonalInfoController.getInstance().lbsArray);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(aa);
            if (weight.getText().toString() != null && selectedWeight != null && selectedWeight.contains("Lbs")) {
                String array[] = weight.getText().toString().split(" ");
                selectedWeigthValue = array[0];
                Log.e("textValuekg", "call" + array[0]);
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            } else {
                int spinnerPosition = aa.getPosition(selectedWeigthValue);
                spin.setSelection(spinnerPosition);
            }
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedMeasure.equals("Kg")) {
                    String selectedValue = PersonalInfoController.getInstance().kgArray.get(i);
                    selectedWeight = selectedValue + " " + "Kg";
                    Log.e("selectedWeigthValue", "call" + selectedWeight);
                    double lbsValue = new Double(PersonalInfoController.getInstance().kgArray.get(i)) * 2.2046;
                    String selectedValue1 = String.valueOf(Math.round(lbsValue));
                    int index = PersonalInfoController.getInstance().lbsArray.indexOf(selectedValue1);
                    selectedWeigthValue = PersonalInfoController.getInstance().lbsArray.get(index);
                    Log.e("selectedWeigth", "call" + selectedWeigthValue);
                } else {
                    String selectedValue = PersonalInfoController.getInstance().lbsArray.get(i);
                    selectedWeight = selectedValue + " " + "Lbs";
                    double kgValue = new Double(PersonalInfoController.getInstance().lbsArray.get(i)) * 0.453592;
                    String selectedValue1 = String.valueOf(Math.round(kgValue));
                    int index = PersonalInfoController.getInstance().kgArray.indexOf(selectedValue1);
                    selectedWeigthValue = PersonalInfoController.getInstance().kgArray.get(index);
                    Log.e("selectedWeigth", "call" + selectedWeigthValue);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}
