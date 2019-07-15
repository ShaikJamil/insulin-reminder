package com.example.ir;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.ir.MealInsulin.MealType;
import com.example.ir.R;
import com.example.ir.Utils;
import com.example.ir.AlarmActivity;
import com.example.ir.MainActivity;


public class AlarmReceiver extends BroadcastReceiver {
    final static String ACTION_REMINDER = "com.ShaikJamil.diabetes.reminder.alarm.REMINDER";
    final private String tag = this.getClass().getName();
    int requestCode = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Utils.changeLanguage(context, AppSettings.getLanguage(context));

        if (intent.getAction().equals(ACTION_REMINDER)) {

            Log.d("onReceive", intent.toString());

            final MealType mealType = (MealType) intent
                    .getSerializableExtra("DATA");

            Log.d("onReceive", mealType.toString());
            //
            // ArrayList<MealInsulin> meals = AppManager.getSchedules(context);
            // MealInsulin meal = null;
            //
            // for (MealInsulin item : meals) {
            // if (item.getMealType() == mealType)
            // meal = item;
            // break;
            // }

            // if (meal.isScheduled())
            // showNotification(context, title, text);

            showAlarmActivity(context, mealType);
        }
    }

    private void showAlarmActivity(Context context, MealType mealType) {

        Intent intentAlarm = new Intent(context, AlarmActivity.class);
        intentAlarm.putExtra("DATA", mealType);
        intentAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentAlarm);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Context context, String title, String text) {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Intent notificationIntent = new Intent(context, LogActivity.class);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent
                .setAction(MainActivity.ACTION_LOG_INSULIN_NOTIFICATION);

        int iconRes = R.drawable.ic_launcher;

        CharSequence tickerText = title + " | " + text;
        long when = System.currentTimeMillis();
        CharSequence contentTitle = title;
        CharSequence contentText = text;

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                requestCode, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification;

        int currentVersion = Utils.getAndroidVersionInt();
        int honeycombVersion = Build.VERSION_CODES.HONEYCOMB;
        int jellybeanVersion = Build.VERSION_CODES.JELLY_BEAN;

        if (currentVersion >= honeycombVersion) {
            // Resources res = context.getResources();

            Notification.Builder builder = new Notification.Builder(context);

            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(iconRes);
            // builder.setLargeIcon(BitmapFactory.decodeResource(res, icon));
            builder.setTicker(tickerText);
            builder.setWhen(System.currentTimeMillis());
            builder.setAutoCancel(true);// for new browser method
            builder.setContentTitle(title);
            builder.setContentText(text);
            builder.setDefaults(Notification.DEFAULT_ALL);

            if (currentVersion >= jellybeanVersion)
                notification = builder.build();
            else
                notification = builder.getNotification();

        } else {
            notification = new Notification(iconRes, tickerText,
                    when);

            //TODO: check why I have to remove this line???
//            notification.setLatestEventInfo(context, contentTitle, contentText,
//                    contentIntent);
            notification.flags |= Notification.FLAG_SHOW_LIGHTS
                    | Notification.FLAG_NO_CLEAR
                    | Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_ALL;
        }

        notificationManager.notify(requestCode, notification);
        // Utils.playNotificationSound(context);
    }

    public interface smsListener {

        public void OnSending();

        public void OnSent();

        public void OnNotSent();

        // public void OnDelivered();
        //
        // public void OnNotDelivered();
    }

}
