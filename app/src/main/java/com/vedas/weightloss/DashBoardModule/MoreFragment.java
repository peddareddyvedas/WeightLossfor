package com.vedas.weightloss.DashBoardModule;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.weightloss.DataBase.DayFoodDataController;
import com.vedas.weightloss.DataBase.PersonalInfoDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.LoginModule.LoginViewActivity;
import com.vedas.weightloss.MoreModule.AboutUsActivity;
import com.vedas.weightloss.MoreModule.DeleteAccountActivity;
import com.vedas.weightloss.MoreModule.GoalsActivity;
import com.vedas.weightloss.MoreModule.GoogleFitActivity;
import com.vedas.weightloss.MoreModule.RegionActivity;
import com.vedas.weightloss.MoreModule.RemindersActivity;
import com.vedas.weightloss.MoreModule.UnitsActivity;
import com.vedas.weightloss.MoreModule.FeedbackActivity;
import com.vedas.weightloss.MoreModule.ProfilePageActivity;
import com.vedas.weightloss.MoreModule.ScrachCardActivity;
import com.vedas.weightloss.MoreModule.ShareandEarnActivity;
import com.vedas.weightloss.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreFragment extends Fragment {
    Toolbar toolbar;
    TextView tool_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.more_fragment, container, false);
        ButterKnife.bind(this, v);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        tool_text = (TextView) toolbar.findViewById(R.id.toolbar_text);
        tool_text.setText("More");
        return v;
    }

    @OnClick({R.id.profile})
    public void profilePage() {
        Intent i = new Intent(getActivity(), ProfilePageActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.rl_googlefit})
    public void googlefit() {
        Intent i = new Intent(getActivity(), GoogleFitActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.share})
    public void share() {
        Intent i = new Intent(getActivity(), ShareandEarnActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.scratch})
    public void scratch() {
        Intent i = new Intent(getActivity(), ScrachCardActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.goals})
    public void goal() {
        Intent i = new Intent(getActivity(), GoalsActivity.class);
        startActivity(i);
    }
///region

    @OnClick({R.id.region})
    public void region() {
        Intent i = new Intent(getActivity(), RegionActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.units})
    public void units() {
        Intent i = new Intent(getActivity(), UnitsActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.reminders})
    public void remainders() {
        Intent i = new Intent(getActivity(), RemindersActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.deleteacc})
    public void deletaccount() {
        Intent i = new Intent(getActivity(), DeleteAccountActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.feedback})
    public void feedback() {
        Intent i = new Intent(getActivity(), FeedbackActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.about})
    public void aboutUspage() {
        Intent i = new Intent(getActivity(), AboutUsActivity.class);
        startActivity(i);
    }

    @OnClick({R.id.logout})
    public void logoutPage() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.logout_alert);
        dialog.show();
        RelativeLayout rl_yes = (RelativeLayout) dialog.findViewById(R.id.rl_yes);
        final TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_cornerbg);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        rl_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                PersonalInfoDataController.getInstance().deleteProfileData(PersonalInfoDataController.getInstance().personalInfoArray);
                PersonalInfoDataController.getInstance().currentMember = null;
                PersonalInfoDataController.getInstance().personalInfoArray.clear();

                DayFoodDataController.getInstance().deletedayfooddataData(DayFoodDataController.getInstance().allDayfoodArray);
                DayFoodDataController.getInstance().currentFoodObject = null;

                UserDataController.getInstance().deleteUserData(UserDataController.getInstance().allUsers);
                UserDataController.getInstance().currentUser = null;

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("personalImage", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear().commit();

                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("personalinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit1 = sharedPreferences1.edit();
                edit1.clear().commit();

                SharedPreferences preferences = getActivity().getSharedPreferences("socialMediaLoginDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginViewActivity.class));
            }
        });
    }

}
