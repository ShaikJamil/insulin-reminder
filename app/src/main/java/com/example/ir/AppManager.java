package com.example.ir;

import android.content.Context;

import com.example.ir.MealInsulin.Insulin;
import com.example.ir.MealInsulin.MealType;
import com.example.ir.AlarmSetter;

import java.util.ArrayList;


public class AppManager {
    private static final String P_INSULIN_R = "insulin_r_";
    private static final String P_INSULIN_N = "insulin_n_";
    private static final String P_TIME = "time_";
    private static final String P_IS_SCHEDULE = "isSchedule_";
    private static final String P_IS_SMS = "isSms_";
    private static final String P_SMS_NUMBER = "sms_number";
    private static final String P_SYRINGE_CAPACITY = "capacity";
    static int userVolume = 0;

    public static ArrayList<MealInsulin> getMeals(Context context) {
        ArrayList<MealInsulin> result = new ArrayList<MealInsulin>();

        for (MealType mealType : MealType.values()) {
            result.add(getMeal(context, mealType));
        }

        return result;
    }

    public static void setMeal(Context context, MealInsulin meal) {
        MealType mealType = meal.getMealType();

        AppSettings.setValue(context, P_INSULIN_R + mealType.toString(), meal
                .getValues().getR());
        AppSettings.setValue(context, P_INSULIN_N + mealType.toString(), meal
                .getValues().getN());
        AppSettings.setValue(context, P_TIME + mealType.toString(),
                meal.getTimeString());
        AppSettings.setValue(context, P_IS_SCHEDULE + mealType.toString(),
                meal.isScheduled());
        AppSettings.setValue(context, P_IS_SMS + mealType.toString(),
                meal.isSms());

    }

    public static String getSmsNumber(Context context) {
        return AppSettings.getString(context, P_SMS_NUMBER, "");
    }

    public static void setSmsNumber(Context context, String value) {
        AppSettings.setValue(context, P_SMS_NUMBER, value);
    }

    public static void resetAlarms(Context context) {
        AlarmSetter.remove(context);
        AlarmSetter.setAlarm(context);
    }

    public static MealInsulin getMeal(Context context, MealType mealType) {
        int r = AppSettings.getInt(context, P_INSULIN_R + mealType.toString(),
                0);
        int n = AppSettings.getInt(context, P_INSULIN_N + mealType.toString(),
                0);
        Insulin insulin = new Insulin(r, n);

        String timeString = AppSettings.getString(context,
                P_TIME + mealType.toString(), mealType.getDefaulTime());

        boolean isScheduled = AppSettings.getBoolean(context, P_IS_SCHEDULE
                + mealType.toString(), false);
        boolean isSms = AppSettings.getBoolean(context,
                P_IS_SMS + mealType.toString(), false);

        return new MealInsulin(mealType, insulin, timeString,
                mealType.getIcon(context), isScheduled, isSms);
    }

    public static int getCapacity(Context context) {
        return AppSettings.getInt(context, P_SYRINGE_CAPACITY, context
                .getResources().getInteger(R.integer.default_capacity));
    }

    public static void setCapacity(Context context, int value) {
        AppSettings.setValue(context, P_SYRINGE_CAPACITY, value);
    }

    public static int getUserVolume() {
        return userVolume;
    }

    public static void setUserVolume(int currentVolume) {
        AppManager.userVolume = currentVolume;
    }
}
