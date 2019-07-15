package com.example.ir;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class Utils {
    public static void changeLanguage(Context context, String language_code) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code);
        res.updateConfiguration(conf, dm);
    }

    public static int getAndroidVersionInt() {
        // Android version
        return Build.VERSION.SDK_INT;
    }

    public static void playSound(Context context, int rawID) {
        MediaPlayer mp = MediaPlayer.create(context, rawID);
        mp.start();
    }

    public static void playNotificationSound(Context context) {
        try {
            Uri notification = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
        }
    }
}
