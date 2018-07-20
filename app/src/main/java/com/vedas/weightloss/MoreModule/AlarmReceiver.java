package com.vedas.weightloss.MoreModule;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import com.vedas.weightloss.DataBase.RemainderDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.R;


public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    String title, content;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();


        RemainderDataController.getInstance().fetchRemainderData(UserDataController.getInstance().currentUser);
        title = intent.getStringExtra("key1");
        content = intent.getStringExtra("key2");
        Log.e("log", "" + title + "" + content);


        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }
        Log.d(TAG, "onReceive: ");


        //Trigger the notification
        NotificationScheduler.showNotification(context, RemindersActivity.class,
                title, content);

//Trigger the notification
      /*  NotificationScheduler.showNotification(context, RemindersActivity.class,
                "You have 5 unwatched videos", "Watch them now?");
*/
    }


}







