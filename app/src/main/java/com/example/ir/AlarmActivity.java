package com.example.ir;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ir.MealInsulin.MealType;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class AlarmActivity extends ParentActionBarActivity implements
        OnClickListener {
    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    Handler mHideHandler = new Handler();
    MealType mealType;
    MealInsulin meal;
    TextView tvTitle;
    AudioManager audioManager;
    CountDownTimer countDownTimer;
    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };
    private ImageView ivIcon;

    private TextView tvValues;

    private InsulinSyringeView insulinSyringeView;

    private int n;

    private int r;

    private Button btDone;

    private TextView tvTimer;

    private TextView tvTimerLable;

    private Button btAnimate;
    private int capacity;

    public void onAttachedToWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initForm(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm);

        setupFullScreen();

        mealType = (MealType) getIntent().getExtras().getSerializable("DATA");
        meal = AppManager.getMeal(context, mealType);

        tvTitle = (TextView) findViewById(R.id.tvTitle);

        switch (mealType) {
            case BREAKFAST:
                tvTitle.setText(context.getResources().getString(
                        R.string.msg_breakfast));
                break;

            case LUNCH:
                tvTitle.setText(context.getResources()
                        .getString(R.string.msg_lunch));
                break;

            case DINNER:
                tvTitle.setText(context.getResources().getString(
                        R.string.msg_dinner));
                break;

        }

        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        ivIcon.setImageBitmap(mealType.getIcon(context));

        tvValues = (TextView) findViewById(R.id.tvValues);
        n = meal.getValues().getN();
        r = meal.getValues().getR();
        tvValues.setText(String.format(
                getResources().getString(R.string.insulin_reminder_values), r,
                n));

        capacity = AppManager.getCapacity(context);

        insulinSyringeView = (InsulinSyringeView) findViewById(R.id.insulinSyringeView1);
        insulinSyringeView.setValues(n, r, capacity);

        btDone = (Button) findViewById(R.id.btDone);
        btDone.setOnClickListener(this);

        btAnimate = (Button) findViewById(R.id.btAnimate);
        btAnimate.setOnClickListener(this);

        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvTimerLable = (TextView) findViewById(R.id.tvTimerLable);

        playAlarmSound();

        if (meal.isSms()) {
            startSmsTimer();
        } else {
            tvTimer.setVisibility(View.GONE);
            tvTimerLable.setVisibility(View.GONE);
        }
    }

    private void setContentView(int activity_alarm) {
    }

    private void playAlarmSound() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AppManager.setUserVolume(audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC));
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        Utils.playSound(context, R.raw.door_bell);
    }

    private void startSmsTimer() {
        // 30 min countdown timer
        countDownTimer = new CountDownTimer(1000 * 60 * 30, 1) {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onTick(long millisUntilFinished) {
                DateFormat df = new SimpleDateFormat("mm'':ss'\"'.SSS");
                df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                tvTimer.setText(df.format(new Date(millisUntilFinished)));

                // Bleep every 1 minutes
                if (millisUntilFinished % (60 * 1000) <= 20)
                    Utils.playSound(context, R.raw.bleep);

            }

            @Override
            public void onFinish() {
                tvTimer.setText("Reminder SMS message sent!");
                tvTimer.setTextColor(Color.RED);
                tvTimerLable.setVisibility(View.GONE);
                sendSms();
            }

        }.start();

    }

    private void sendSms() {
        // TODO Auto-generated method stub

    }

    private void setupFullScreen() {
        // final View controlsView =
        // findViewById(R.id.fullscreen_content_controls);
        final View contentView = (View) findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView,
                HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = contentView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            contentView
                                    .animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            contentView.setVisibility(visible ? View.VISIBLE
                                    : View.GONE);
                        }

                    }
                });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(
        // mDelayHideTouchListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btDone:
                if (countDownTimer != null)
                    countDownTimer.cancel();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        AppManager.getUserVolume(),
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                finish();
                break;

            case R.id.btSnooze:

                break;

            case R.id.btAnimate:
                startSyringeAnimation();
                break;
        }
    }

    private void startSyringeAnimation() {
        new Thread(new Runnable() {
            public void run() {
                btAnimate.post(new Runnable() {

                    @Override
                    public void run() {
                        btAnimate.setEnabled(false);
                    }
                });

                if (r > 0) {
                    for (int i = 0; i <= r; i++) {
                        final int value = i;
                        insulinSyringeView.post(new Runnable() {

                            @Override
                            public void run() {
                                insulinSyringeView
                                        .setValues(0, value, capacity);
                            }
                        });

                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (n > 0) {
                    for (int i = 0; i <= n; i++) {
                        final int value = i;
                        insulinSyringeView.post(new Runnable() {

                            @Override
                            public void run() {
                                insulinSyringeView
                                        .setValues(value, r, capacity);
                            }
                        });

                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

                btAnimate.post(new Runnable() {

                    @Override
                    public void run() {
                        btAnimate.setEnabled(true);
                    }
                });

            }
        }).start();
    }

}
