package com.vedas.weightloss.MoreModule;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.vedas.weightloss.DataBase.RemainderDataController;
import com.vedas.weightloss.DataBase.UserDataController;
import com.vedas.weightloss.Models.RemainderObject;
import com.vedas.weightloss.R;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;
import static com.vedas.weightloss.R.layout.activity_main;
import static com.vedas.weightloss.R.layout.calendar;


public class NotificationScheduler {
    public static final int DAILY_REMINDER_REQUEST_CODE = 100;

    public static void setReminder(Context context, Class<?> cls, int hour, int min) {
        Log.e("amnsamsn", "ssm");
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);
        // cancel already scheduled reminders
        cancelReminder(context, cls);
        if (setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE, 1);
        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        if (RemainderDataController.getInstance().allRemainders.size() > 0) {

            for (int i = 0; i < RemainderDataController.getInstance().allRemainders.size(); i++) {
                RemainderObject remainderObject = RemainderDataController.getInstance().allRemainders.get(i);
                if (remainderObject.getRemaindertype().equals("Water")) {
                    Log.e("water", "ssm");

                    Intent intent1 = new Intent(context, cls);
                    intent1.putExtra("key1", RemainderDataController.getInstance().currentremainder.getRemaindertype());
                    intent1.putExtra("key2", RemainderDataController.getInstance().currentremainder.getNotes());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    long triggerTime = SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    long repeatInterval = AlarmManager.INTERVAL_HOUR;
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                  /*  am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            triggerTime, repeatInterval, pendingIntent);
*/
                    am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            setcalendar.getTimeInMillis(), 10 * 1000, pendingIntent);
                    Toast.makeText(context, " onwater ", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("breakfast", "ssm");
                    Intent myIntent = new Intent(context, cls);
                    myIntent.putExtra("key1", RemainderDataController.getInstance().currentremainder.getRemaindertype());
                    myIntent.putExtra("key2", RemainderDataController.getInstance().currentremainder.getNotes());
                    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // am.setRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                    am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                    Toast.makeText(context, " on break ", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    public static void cancelReminder(Context context, Class<?> cls) {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotification(Context context, Class<?> cls, String title, String content) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_appicon)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
    }
}

