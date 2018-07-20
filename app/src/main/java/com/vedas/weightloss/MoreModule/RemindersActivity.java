package com.vedas.weightloss.MoreModule;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedas.weightloss.DataBase.RemainderDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.Models.RemainderObject;
import com.vedas.weightloss.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * Created by Rise on 13/06/2018.
 */

public class RemindersActivity extends AppCompatActivity {
    RecyclerView remainderRecyclerView;
    Remainder adapter;
    Toolbar toolbar;
    ImageView btn_back;
    com.github.clans.fab.FloatingActionButton fab;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        ButterKnife.bind(this);
        setToolbar();
        RemainderDataController.getInstance().allRemainders.size();
        init();


       /* RemainderDataController.getInstance().fetchRemainderData(UserDataController.getInstance().currentUser);

        if (RemainderDataController.getInstance().allRemainders.size() > 0) {
            for (int i = 1; i <= RemainderDataController.getInstance().allRemainders.size() - 1; i++) {
                RemainderObject objReminder = RemainderDataController.getInstance().allRemainders.get(i);
                LocalData localData = new LocalData(getApplicationContext());

                Log.e("isfromjfb", "" + objReminder.isFromOnline());

                if (objReminder.isFromOnline()) {
                    Log.e("isfromfor", "" + objReminder.isFromOnline());
                    String a[] = objReminder.getCurrenttime().split(":");
                    String min[] = a[1].split(" ");
                    localData.set_hour(Integer.parseInt(a[0]));
                    localData.set_min(Integer.parseInt(min[0]));
                    Log.e("hvbvbvb", "" + localData.get_hour() + "nsss" + localData.get_min());
                    NotificationScheduler.setReminder(RemindersActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                }
            }

        }*/


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
        toolbartext.append("Reminders");
    }

    @Override
    protected void onResume() {
        super.onResume();
        RemainderDataController.getInstance().fetchRemainderData(UserDataController.getInstance().currentUser);
        adapter.notifyDataSetChanged();
    }

    private void init() {

        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditRemainderActivity.remainderobj = null;
                RemainderDataController.getInstance().currentremainder = null;
                finish();
                startActivity(new Intent(getApplicationContext(), EditRemainderActivity.class));
            }
        });
        remainderRecyclerView = (RecyclerView) findViewById(R.id.remaindrrecyclerview);
        adapter = new Remainder(getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        remainderRecyclerView.setLayoutManager(horizontalLayoutManager);
        remainderRecyclerView.setAdapter(adapter);
        remainderRecyclerView.setHasFixedSize(true);

    }

    public class Remainder extends RecyclerView.Adapter<Remainder.ViewHolder> {
        Context ctx;

        public Remainder(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.remainderlist, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view, ctx);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final RemainderObject objReminderObj = RemainderDataController.getInstance().allRemainders.get(position);

            holder.name.setText(objReminderObj.getRemaindertype());
            holder.texttime.setText(objReminderObj.getCurrenttime());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    RemainderDataController.getInstance().fetchRemainderData(UserDataController.getInstance().currentUser);
                    EditRemainderActivity.remainderobj = null;
                    EditRemainderActivity.remainderobj = RemainderDataController.getInstance().allRemainders.get(position);
                    RemainderDataController.getInstance().currentremainder = RemainderDataController.getInstance().allRemainders.get(position);
                    Log.e("current", "jhjhkk" + RemainderDataController.getInstance().currentremainder.getRemaindertype());
                    startActivity(new Intent(getApplicationContext(), EditRemainderActivity.class));

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.e("DBPOS", "CALL" + position);
                    alertForDeleteDevice(RemainderDataController.getInstance().allRemainders.get(position));
                    return true;
                }


            });

        }

        // step 4:-
        @Override
        public int getItemCount() {
            if (RemainderDataController.getInstance().allRemainders.size() > 0) {
                return RemainderDataController.getInstance().allRemainders.size();
            } else {
                return 0;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name, texttime;
            Context ctx;

            public ViewHolder(View itemView, Context ctx) {
                super(itemView);
                this.ctx = ctx;
                name = (TextView) itemView.findViewById(R.id.textname);
                texttime = (TextView) itemView.findViewById(R.id.texttime);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
            }
        }
    }

    public void alertForDeleteDevice(final RemainderObject remainderObject) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure to delete this reminder");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do your work here
                RemainderDataController.getInstance().deleteRemainderData(remainderObject);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        alert.show();
    }

}
