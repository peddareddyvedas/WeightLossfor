package com.vedas.weightloss.MoreModule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vedas.weightloss.Alert.Constants;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.R;

import butterknife.ButterKnife;

/**
 * Created by Rise on 13/06/2018.
 */

public class DeleteAccountActivity extends AppCompatActivity {
    TextView text, text1, text2;
    Button delete, back;
    EditText password;
    Toolbar toolbar;
    ImageView btn_back;
    LinearLayout rl_delete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletaccount);
        ButterKnife.bind(this);
        setToolbar();
        init();
        if (UserDataController.getInstance().currentUser.getRegisterType().equals(Constants.RegisterTypes.Manual.toString()))
        {
            rl_delete.setVisibility(View.VISIBLE);
        }else {
            rl_delete.setVisibility(View.GONE);
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
        toolbartext.append("Delete Account");
    }
    private void init() {
        text = (TextView) findViewById(R.id.text);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        password = (EditText) findViewById(R.id.password);
        delete = (Button) findViewById(R.id.deleteaccount);
        rl_delete=(LinearLayout)findViewById(R.id.rl_deleteaccount);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
