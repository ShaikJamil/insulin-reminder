package com.example.ir;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;

public abstract class ParentActionBarActivity extends ActionBarActivity
        implements ListView.OnItemClickListener {

    protected final String TAG = this.getClass().getName();
    protected final Context context = this;

    @Override
    protected void onStart() {
        super.onStart();

        EasyTracker.getInstance(this).activityStart(this); // Google Analytic
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Log.i("tag", "Change language to setting...");
        // Utils.changeLanguage(context,
        // AppSettings.getString(context, AppSettings.LANGUAGE, "fa"));

        initForm(savedInstanceState);

    }

    protected abstract void initForm(Bundle savedInstanceState);

    @Override
    protected void onResume() {
        super.onResume();

        // Utils.changeLanguage(context,
        // AppSettings.getString(context, AppSettings.LANGUAGE, "fa"));
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Utils.exit(context);
        try {
            NavUtils.navigateUpFromSameTask(this);
        } catch (Exception e) {
            // e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        EasyTracker.getInstance(this).activityStop(this); // Google Analytic
    }

}
