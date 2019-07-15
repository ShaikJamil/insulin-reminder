package com.example.ir;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ir.AppManager;
import com.example.ir.MealInsulin;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmSetter extends BroadcastReceiver {

    // private Context context;

    // public AlarmSetter() {
    //
    // }
    //
    // public AlarmSetter(Context context) {
    // this.context = context;
    // }

    public static void setAlarm(Context context) {
        ArrayList<MealInsulin> meals = AppManager.getMeals(context);

        for (MealInsulin meal : meals) {
            if (meal.isScheduled()) {
                Calendar time = Calendar.getInstance();

                time.set(Calendar.HOUR_OF_DAY, meal.getTimeHour());
                time.set(Calendar.MINUTE, meal.getTimeMinute());
                time.set(Calendar.SECOND, 0);
                time.set(Calendar.MILLISECOND, 0);

                // avoid past time alarm
                if (time.before(Calendar.getInstance()))
                    time.add(Calendar.DAY_OF_MONTH, 1);

                setMealAlarm(context, meal, time);
            }
        }
    }

    private static void setMealAlarm(Context context, MealInsulin meal,
                                     Calendar time) {
        Log.d("ALARM", "setMealAlarm: " + time);

        AlarmManager alarmMgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_REMINDER);
        intent.putExtra("DATA", meal.getMealType());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, meal
                        .getMealType().getRequestCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void remove(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);

        alarmMgr.cancel(pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARM", "onReceived Start");
        try {
            // this.context = context;
            setAlarm(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("ALARM", "onReceived Finished");
    }

}
