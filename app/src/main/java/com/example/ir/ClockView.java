package com.example.ir;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import java.util.TimeZone;

public class ClockView extends View {
    private final Handler mHandler = new Handler(); // Handler

    //
    private Time mCalendar;
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mDial;
    private boolean mAttached; //

    //
    private int mDialWidth;

    private int mDialHeight;

    private float mMinute;

    private float mHour;

    private boolean mChanged;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // action

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            onTimeChanged(); //
            invalidate(); // ?onDraw
        }
    };

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources r = context.getResources();
        // ?
        mDial = r.getDrawable(R.drawable.clock_face);
        mHourHand = r.getDrawable(R.drawable.hand_hour);
        mMinuteHand = r.getDrawable(R.drawable.hand_min);
        mCalendar = new Time();
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter(); // ??action
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiver(mIntentReceiver, filter, null,
                    mHandler);
        }
        mCalendar = new Time();
        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver); //
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // ?
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        float hScale = 1.0f;
        float vScale = 1.0f;
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        // ?
        float scale = Math.min(hScale, vScale);
        setMeasuredDimension(
                resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));

    }

    // ?Canvas
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // changed
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        final int mRight = getRight();
        final int mLeft = getLeft();
        final int mTop = getTop();
        final int mBottom = getBottom();
        int availableWidth = mRight - mLeft;
        int availableHeight = mBottom - mTop;
        int x = availableWidth / 2;
        int y = availableHeight / 2;
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;
        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));//
        }
        dial.draw(canvas);
        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);//
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
                    + (h / 2));
        }

        hourHand.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate(mMinute / 60.0f * 360.0f, x, y); // ?
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
                    + (h / 2));

        }
        minuteHand.draw(canvas);
        canvas.restore();
        if (scaled) {
            canvas.restore();
        }
    }

    private void onTimeChanged() { // ?
        mCalendar.setToNow();
        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        mMinute = minute + second / 60.0f;
        mHour = hour + mMinute / 60.0f;
        mChanged = true;
    }
}
