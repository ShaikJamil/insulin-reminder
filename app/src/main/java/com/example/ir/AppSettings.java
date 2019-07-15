package com.example.ir;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;

public class AppSettings {
    // Constants
    public static final String LANGUAGE = "language";
    private final static String tag = AppSettings.class.getSimpleName();
    // Boolean Values
    public static HashMap<String, Boolean> booleanValues;
    // Float Values
    public static HashMap<String, Float> floatValues;
    // Long Values
    public static HashMap<String, Long> longValues;
    // String Values
    public static HashMap<String, String> stringValues;
    // Integer Values
    public static HashMap<String, Integer> intValues;

    public static Boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, null);
    }

    public static Boolean getBoolean(Context context, String key,
                                     Boolean defaultValue) {
        Log.i(tag, "Get " + key);
        if (booleanValues == null)
            booleanValues = new HashMap<String, Boolean>();

        if (!booleanValues.containsKey(key))
            booleanValues.put(key,
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .getBoolean(key, defaultValue));

        return booleanValues.get(key);
    }

    public static void setValue(Context context, String key, Boolean value) {
        Log.i(tag, "Set " + key + ":" + value);

        if (booleanValues == null)
            booleanValues = new HashMap<String, Boolean>();

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putBoolean(key, value);
        editor.commit();

        booleanValues.put(key, value);
    }

    public static Float getFloat(Context context, String key) {
        return getFloat(context, key, null);
    }

    public static Float getFloat(Context context, String key, Float defaultValue) {
        Log.i(tag, "Get " + key);
        if (floatValues == null)
            floatValues = new HashMap<String, Float>();

        if (!floatValues.containsKey(key))
            floatValues.put(key,
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .getFloat(key, defaultValue));

        return floatValues.get(key);
    }

    public static void setValue(Context context, String key, Float value) {
        Log.i(tag, "Set " + key + ":" + value);

        if (floatValues == null)
            floatValues = new HashMap<String, Float>();

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putFloat(key, value);
        editor.commit();

        floatValues.put(key, value);
    }

    public static Long getLong(Context context, String key) {
        return getLong(context, key, null);
    }

    public static Long getLong(Context context, String key, Long defaultValue) {
        Log.i(tag, "Get " + key);
        if (longValues == null)
            longValues = new HashMap<String, Long>();

        if (!longValues.containsKey(key))
            longValues.put(key,
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .getLong(key, defaultValue));

        return longValues.get(key);
    }

    public static void setValue(Context context, String key, Long value) {
        Log.i(tag, "Set " + key + ":" + value);

        if (longValues == null)
            longValues = new HashMap<String, Long>();

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putFloat(key, value);
        editor.commit();

        longValues.put(key, value);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key,
                                   String defaultValue) {
        Log.i(tag, "Get " + key);
        if (stringValues == null)
            stringValues = new HashMap<String, String>();

        if (!stringValues.containsKey(key))
            stringValues.put(key,
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .getString(key, defaultValue));

        return stringValues.get(key);
    }

    public static void setValue(Context context, String key, String value) {
        Log.i(tag, "Set " + key + ":" + value);

        if (stringValues == null)
            stringValues = new HashMap<String, String>();

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putString(key, value);
        editor.commit();

        stringValues.put(key, value);
    }

    public static Integer getInt(Context context, String key) {
        return getInt(context, key, null);
    }

    public static Integer getInt(Context context, String key,
                                 Integer defaultValue) {
        Log.i(tag, "Get " + key);
        if (intValues == null)
            intValues = new HashMap<String, Integer>();

        if (!intValues.containsKey(key))
            intValues.put(key,
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .getInt(key, defaultValue));

        return intValues.get(key);
    }

    public static void setValue(Context context, String key, Integer value) {
        Log.i(tag, "Set " + key + ":" + value);

        if (intValues == null)
            intValues = new HashMap<String, Integer>();

        Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        editor.putInt(key, value);
        editor.commit();

        intValues.put(key, value);
    }

}
