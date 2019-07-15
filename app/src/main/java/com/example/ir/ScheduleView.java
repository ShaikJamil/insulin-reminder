package com.example.ir;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleView extends View implements SurfaceHolder.Callback {
    final static String TAG = ScheduleView.class.getName();
    final int strockWidth = 1;

    // int thickness; // Chart Thickness
    float roundPx = 20;

    int colorMilk = Color.parseColor("#e83f4d");
    int colorCerels = Color.parseColor("#49b848");

    int line = Color.parseColor("#000000");
    int aWhite = Color.parseColor("#ecf0f1");
    int aGray = Color.parseColor("#95a5a6");
    int Red = Color.parseColor("#e74c3c");
    int Green = Color.parseColor("#1abc9c");
    int Blue = Color.parseColor("#3498db");
    int aBlack = Color.parseColor("#34495e");
    int aMilk = Color.parseColor("#55aaaa55");

    Paint paintFillWhite, paintFillText, paintFillTextGray, paintFillGray,
            paintFillTimes, paintFillSchedule, paintStrokeGray, paintStrokeRed,
            paintFillBlue, paintFillRed, paintFillGreen, paintStrokeDotGray;

    int centerX;
    int centerY;

    int width;
    int height;
    int xPart, yPart;

    int left, top, bottom, right;

    ArrayList<MealInsulin> meals;

    OnScheduleClickListener onScheduleClickListener;
    // public void setValues(int insulinN, int insulinR, int capacity) {
    // this.insulinN = insulinN;
    // this.insulinR = insulinR;
    // this.capacity = capacity;
    //
    // invalidate();
    // }
    int parentWidth, parentHeight;
    OnNowChangedListener onNowChangedListener;

    // int paddingLeft, paddingRight, paddingTop, paddingBottom;
    float startX, startY, deltaX, deltaY;
    float previousTranslateX = 0f;
    float previousTranslateY = 0f;
    boolean isScrolling = false;
    // Timer timer;
    private BroadcastReceiver _broadcastReceiver;
    private Bitmap ic_clock, ic_sms, ic_clock_disabled, ic_sms_disabled;
    private boolean firstRun = true;

    public ScheduleView(Context context) {
        this(context, null);
    }

    public ScheduleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TypedArray a = context.obtainStyledAttributes(attrs,
        // R.styleable.InsulinSyringeView);
        //
        // final int N = a.getIndexCount();
        // for (int i = 0; i < N; ++i) {
        // int attr = a.getIndex(i);
        // switch (attr) {
        // case R.styleable.InsulinSyringeView_barThickness:
        // thickness = a.getInt(attr, 50);
        // break;
        // case R.styleable.InsulinSyringeView_paddingLeft:
        // paddingLeft = a.getInt(attr, 2);
        // break;
        // case R.styleable.InsulinSyringeView_paddingRight:
        // paddingRight = a.getInt(attr, 2);
        // break;
        // case R.styleable.InsulinSyringeView_paddingTop:
        // paddingTop = a.getInt(attr, 2);
        // break;
        // case R.styleable.InsulinSyringeView_paddingBottom:
        // paddingBottom = a.getInt(attr, 2);
        // break;
        // }
        // }
        //
        // a.recycle();

        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    Log.i(TAG, "Time tick!");
                    invalidate();
                }
            }
        };

        Log.i(TAG, "Register Time Tick Receiver...");
        context.registerReceiver(_broadcastReceiver, new IntentFilter(
                Intent.ACTION_TIME_TICK));

    }

    public ScheduleView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        initView();
        invalidate();
    }

    public void setSchedules(ArrayList<MealInsulin> meals) {
        this.meals = meals;
        invalidate();
    }

    public OnScheduleClickListener getOnScheduleClickListener() {
        return onScheduleClickListener;
    }

    public void setOnScheduleClickListener(
            OnScheduleClickListener onScheduleClickListener) {
        this.onScheduleClickListener = onScheduleClickListener;
    }

    private void initView() {
        width = getWidth();
        height = getHeight();

        centerX = (int) (width / 2);
        centerY = (int) (height / 2);

        xPart = width / 5;
        yPart = height / 48;

        left = 0;// paddingLeft;
        top = 0;// paddingTop;
        bottom = height;// - paddingBottom;
        right = width;// - paddingRight;

        // if (thickness == 0)
        // thickness = height / 4;

        // init paints
        paintFillWhite = new Paint();
        paintFillWhite.setColor(Color.WHITE);
        paintFillWhite.setStyle(Paint.Style.FILL);
        paintFillWhite.setAntiAlias(true);

        // paintFillPlunger = new Paint();
        // paintFillPlunger.setShader(new LinearGradient(0, centerY - thickness
        // / 4, 0, centerY + thickness / 4, aGray, aBlack,
        // Shader.TileMode.MIRROR));
        // paintFillPlunger.setStyle(Paint.Style.FILL);
        // paintFillPlunger.setAntiAlias(true);
        //
        // paintFillNeedle = new Paint();
        // paintFillNeedle.setColor(Color.BLACK);
        // paintFillNeedle.setStyle(Paint.Style.FILL);
        // paintFillNeedle.setAntiAlias(true);

        // Typeface tf = Typeface.defaultFromStyle(Typeface.ITALIC);

        // paintFillTextSyringeMeasurement = new Paint();
        // paintFillTextSyringeMeasurement.setColor(Color.BLACK);
        // paintFillTextSyringeMeasurement.setStyle(Paint.Style.FILL);
        // paintFillTextSyringeMeasurement.setAntiAlias(true);
        // paintFillTextSyringeMeasurement.setTextAlign(Align.CENTER);
        // paintFillTextSyringeMeasurement.setTextSize(width / 30);
        // // paintFillTextSyringeMeasurement.setTypeface(tf);
        //
        // paintFillTextInsulin = new Paint();
        // paintFillTextInsulin.setColor(Color.BLACK);
        // paintFillTextInsulin.setStyle(Paint.Style.FILL);
        // paintFillTextInsulin.setAntiAlias(true);
        // paintFillTextInsulin.setTextAlign(Align.LEFT);
        // paintFillTextInsulin.setTextSize(width / 30);
        // // paintFillTextInsulin.setTypeface(tf);

        paintFillText = new Paint();
        paintFillText.setColor(Color.BLACK);
        paintFillText.setStyle(Paint.Style.FILL);
        paintFillText.setAntiAlias(true);
        paintFillText.setTextAlign(Align.LEFT);
        paintFillText.setTextSize(width / 30);
        // paintFillTextTotal.setTypeface(tf);

        paintFillTextGray = new Paint();
        paintFillTextGray.setColor(Color.GRAY);
        paintFillTextGray.setStyle(Paint.Style.FILL);
        paintFillTextGray.setAntiAlias(true);
        paintFillTextGray.setTextAlign(Align.LEFT);
        paintFillTextGray.setTextSize(width / 40);

        paintFillGray = new Paint();
        paintFillGray.setColor(aGray);
        paintFillGray.setStyle(Paint.Style.FILL);
        paintFillGray.setAntiAlias(true);

        paintFillBlue = new Paint();
        paintFillBlue.setColor(Blue);
        paintFillBlue.setStyle(Paint.Style.FILL);
        paintFillBlue.setAntiAlias(true);

        paintFillRed = new Paint();
        paintFillRed.setColor(Red);
        paintFillRed.setStyle(Paint.Style.FILL);
        paintFillRed.setAntiAlias(true);

        paintFillGreen = new Paint();
        paintFillGreen.setColor(Green);
        paintFillGreen.setStyle(Paint.Style.FILL);
        paintFillGreen.setAntiAlias(true);

        paintFillTimes = new Paint();
        // paintFillTimes.setColor(Color.WHITE);
        paintFillTimes
                .setShader(new LinearGradient(xPart, 0, xPart - xPart / 6, 0,
                        aGray, Color.WHITE, Shader.TileMode.CLAMP));
        paintFillTimes.setStyle(Paint.Style.FILL);
        paintFillTimes.setAntiAlias(true);

        paintFillSchedule = new Paint();
        paintFillSchedule.setColor(Color.GRAY);
        paintFillSchedule.setStyle(Paint.Style.FILL);
        paintFillSchedule.setAntiAlias(true);

        // paintFillSyringeBody = new Paint();
        // paintFillSyringeBody.setShader(new LinearGradient(0, centerY
        // - thickness / 3, 0, centerY + thickness / 3, aGray, aWhite,
        // Shader.TileMode.MIRROR));
        // paintFillSyringeBody.setStyle(Paint.Style.FILL);
        // paintFillSyringeBody.setAntiAlias(true);

        // paintStrokeSyringeBody = new Paint();
        // paintStrokeSyringeBody.setColor(aGray);
        // paintStrokeSyringeBody.setStyle(Paint.Style.STROKE);
        // paintStrokeSyringeBody.setStrokeJoin(Paint.Join.MITER);
        // paintStrokeSyringeBody.setStrokeWidth(strokeWidth);
        // paintStrokeSyringeBody.setStyle(Paint.Style.STROKE);
        // paintStrokeSyringeBody.setAntiAlias(true);
        //
        // paintFillInsulinN = new Paint();
        // // paintFillInsulinN.setShader(new LinearGradient(0, centerY -
        // thickness
        // // / 2, 0, centerY + thickness / 2, aWhite, aMilk,
        // // Shader.TileMode.MIRROR));
        // paintFillInsulinN.setColor(aMilk);
        // paintFillInsulinN.setStyle(Paint.Style.FILL);
        // paintFillInsulinN.setAntiAlias(true);
        //
        // paintFillInsulinR = new Paint();
        // paintFillInsulinR.setColor(aWhite);
        // // paintFillInsulinR.setShader(new LinearGradient(0, centerY -
        // thickness
        // // / 2, 0, centerY + thickness / 2, Color.WHITE, aWhite,
        // // Shader.TileMode.MIRROR));
        // paintFillInsulinR.setStyle(Paint.Style.FILL);
        // paintFillInsulinR.setAntiAlias(true);
        //
        // paintStrokeSyringeMeasurement = new Paint();
        // paintStrokeSyringeMeasurement.setColor(Color.BLACK);
        // paintStrokeSyringeMeasurement.setStyle(Paint.Style.STROKE);
        // paintStrokeSyringeMeasurement.setStrokeJoin(Paint.Join.MITER);
        // paintStrokeSyringeMeasurement.setStrokeWidth(strokeWidth);
        // paintStrokeSyringeMeasurement.setAntiAlias(true);

        paintStrokeGray = new Paint();
        paintStrokeGray.setColor(line);
        paintStrokeGray.setColor(aGray);
        paintStrokeGray.setStyle(Paint.Style.STROKE);
        paintStrokeGray.setStrokeJoin(Paint.Join.MITER);
        paintStrokeGray.setStrokeWidth(strockWidth);
        paintStrokeGray.setAntiAlias(true);

        paintStrokeDotGray = new Paint();
        paintStrokeDotGray.setColor(line);
        paintStrokeDotGray.setColor(aGray);
        paintStrokeDotGray.setStyle(Paint.Style.STROKE);
        paintStrokeDotGray.setStrokeJoin(Paint.Join.MITER);
        paintStrokeDotGray.setStrokeWidth(strockWidth);
        paintStrokeDotGray.setAntiAlias(true);
        paintStrokeDotGray.setPathEffect(new DashPathEffect(new float[]{20,
                20,}, 10));

        paintStrokeRed = new Paint();
        paintStrokeRed.setColor(line);
        paintStrokeRed.setColor(Red);
        paintStrokeRed.setStyle(Paint.Style.STROKE);
        paintStrokeRed.setStrokeJoin(Paint.Join.MITER);
        paintStrokeRed.setStrokeWidth(strockWidth * 2);
        paintStrokeRed.setAntiAlias(true);

        int rec = (yPart * 2 + yPart / 2) - (yPart * 2 - yPart / 2) - 5;
        ic_clock = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_clock), rec, rec, true);
        ic_clock_disabled = Bitmap.createScaledBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.ic_clock_disabled),
                rec, rec, true);

        ic_sms = Bitmap
                .createScaledBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.ic_sms), rec, rec, true);
        ic_sms_disabled = Bitmap.createScaledBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.ic_sms_disabled),
                rec, rec, true);

        // int drec = (int) (rec * 1.5);

        if (meals == null)
            meals = new ArrayList<MealInsulin>();

        for (MealInsulin meal : meals) {
            meal.setIcon(Bitmap.createScaledBitmap(meal.getIcon(), rec, rec,
                    true));
        }

        // meals = new ArrayList<MealInsulin>();
        // meals.add(new MealInsulin(MealType.BREAKFAST, breakfastValues,
        // breakfast,
        // MealType.BREAKFAST.getIcon(getContext()), rec, rec,
        // true), breakfastScheduled, breakfastSms));
        // meals.add(new MealInsulin(MealType.LUNCH, lunchValues, lunch, Bitmap
        // .createScaledBitmap(MealType.LUNCH.getIcon(getContext()), rec,
        // rec, true), lunchScheduled, lunchSms));
        // meals.add(new MealInsulin(MealType.DINNER, dinnerValues, dinner,
        // Bitmap.createScaledBitmap(
        // MealType.DINNER.getIcon(getContext()), rec, rec, true),
        // dinnerScheduled, dinnerSms));

        // ic_clock = BitmapFactory.decodeResource(getResources(),
        // R.drawable.ic_clock);
        // if (timer != null)
        // timer.cancel();
        //
        // timer = new Timer();
        // timer.schedule(new TimerTask() {
        //
        // @Override
        // public void run() {
        // Log.i(TAG, "Time tick!");
        // post(new Runnable() {
        // public void run() {
        // invalidate();
        // }
        // });
        // }
        // }, 1000 * 60);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        this.setMeasuredDimension(parentWidth, parentWidth * 4);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawTimes(canvas);
        drawSchedule(canvas);
        writeTexts(canvas);

        drawNow(canvas);
    }

    public void setOnNowChangedListener(
            OnNowChangedListener onNowChangedListener) {
        this.onNowChangedListener = onNowChangedListener;
    }

    private void drawNow(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        float t = h + (float) m / 60;

        float now = (t - 1) * yPart * 2 + yPart;

        canvas.drawLine(xPart - xPart / 10, now, right, now, paintStrokeRed);

        if (onNowChangedListener != null)
            onNowChangedListener.onNowChanged(now);

        scrollToNow((int) now);
    }

    private void scrollToNow(int now) {
        if (firstRun) {
            firstRun = false;

            int yScroll = now - parentHeight / 2;

            if (yScroll <= 0)
                yScroll = 0;

            else if (yScroll >= height - parentHeight)
                yScroll = (height - parentHeight);

            scrollTo(0, yScroll);
        }
    }

    private void writeTexts(Canvas canvas) {
        for (MealInsulin meal : meals) {
            float hour = meal.getTime();
            String text = meal.getMealType().toString() + " ("
                    + meal.getTimeString() + "): "
                    + meal.getValues().toString();

            meal.setLeft(xPart + xPart / 8);
            meal.setTop((int) ((hour - 1) * yPart * 2 + yPart / 2));
            meal.setRight(right - xPart / 8);
            meal.setBottom((int) ((hour) * yPart * 2 - yPart / 2));

            canvas.drawRect(meal.getLeft(), meal.getTop(), meal.getRight(),
                    meal.getBottom(), paintFillGreen);

            canvas.drawText(text, xPart + xPart / 5 + meal.getIcon().getWidth()
                            + 5, (hour - 1) * yPart * 2 + yPart + yPart / 8,
                    paintFillText);

            canvas.drawBitmap(meal.getIcon(), xPart + xPart / 5, (hour - 1)
                    * yPart * 2 + yPart / 2, null);

            if (meal.isScheduled())
                canvas.drawBitmap(ic_clock, right - ic_clock.getWidth()
                        - ic_sms.getWidth() - xPart / 4, ((hour - 1) * yPart
                        * 2 + yPart / 2) + 2, null);
            else
                canvas.drawBitmap(ic_clock_disabled,
                        right - ic_clock.getWidth() - ic_sms.getWidth() - xPart
                                / 4, ((hour - 1) * yPart * 2 + yPart / 2) + 2,
                        null);

            if (meal.isSms())
                canvas.drawBitmap(ic_sms,
                        right - ic_sms.getWidth() - xPart / 4, ((hour - 1)
                                * yPart * 2 + yPart / 2) + 2, null);
            else
                canvas.drawBitmap(ic_sms_disabled, right - ic_sms.getWidth()
                                - xPart / 4, ((hour - 1) * yPart * 2 + yPart / 2) + 2,
                        null);
        }
    }

    private void drawSchedule(Canvas canvas) {
        canvas.drawRect(xPart, top, right, bottom, paintFillWhite);

        for (int i = 0; i < 48; i++) {
            if (i % 2 == 0)
                canvas.drawLine(xPart - xPart / 8, (i + 1) * yPart, right,
                        (i + 1) * yPart, paintStrokeGray);
            else
                canvas.drawLine(xPart, (i + 1) * yPart, right, (i + 1) * yPart,
                        paintStrokeDotGray);
        }

        canvas.drawLine(xPart, top, xPart, bottom, paintStrokeGray);
    }

    private void drawTimes(Canvas canvas) {
        canvas.drawRect(left, top, xPart, bottom, paintFillWhite);
        for (int i = 0; i < 24; i++) {
            String time;
            String halfTime;

            if (i == 23) {
                time = "00:00";
                halfTime = "00:30";
            } else {
                time = i + 1 + ":00";
                halfTime = i + 1 + ":30";
            }
            if (i < 9) {
                time = "0" + time;
                halfTime = "0" + halfTime;
            }

            canvas.drawText(time, left + xPart / 5, (i) * yPart * 2 + yPart
                    + yPart / 8, paintFillText);

            canvas.drawText(halfTime, left + xPart / 4, (i + 1) * yPart * 2
                    + yPart / 8, paintFillTextGray);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // // float x = centerX - event.getX();
    // // float y = centerY - event.getY();
    // //
    // // switch (event.getAction()) {
    // // case MotionEvent.ACTION_DOWN:
    // // case MotionEvent.ACTION_MOVE:
    // // if (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(radiusOuter, 2)) {
    // // makeAllFalse();
    // // if (Math.abs(x) < y)
    // // buttonsSellected[2] = true;
    // // else if (Math.abs(y) < x)
    // // buttonsSellected[1] = true;
    // // else if (Math.abs(y) < Math.abs(x))
    // // buttonsSellected[3] = true;
    // // else if (Math.abs(x) < Math.abs(y))
    // // buttonsSellected[0] = true;
    // // invalidate();
    // // return true;
    // // } else {
    // // makeAllFalse();
    // // }
    // // invalidate();
    // // return super.onTouchEvent(event);
    // //
    // // case MotionEvent.ACTION_UP:
    // // if (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(radiusOuter, 2)) {
    // // if (navigationListener != null) {
    // // if (buttonsSellected[0] && buttonsEnabled[0])
    // // navigationListener.onDownClick(this);
    // // else if (buttonsSellected[1] && buttonsEnabled[1])
    // // navigationListener.onLeftClick(this);
    // // else if (buttonsSellected[2] && buttonsEnabled[2])
    // // navigationListener.onUpClick(this);
    // // else if (buttonsSellected[3] && buttonsEnabled[3])
    // // navigationListener.onRightClick(this);
    // //
    // // makeAllFalse();
    // // invalidate();
    // // return true;
    // // }
    // // } else {
    // // makeAllFalse();
    // // invalidate();
    // // return super.onTouchEvent(event);
    // // }
    // //
    // // }
    // return super.onTouchEvent(event);
    //
    // }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false); // Allows us to use invalidate() to call
        // onDraw()
        postInvalidate();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Log.i(TAG, "Unregister Time Tick Receiver...");
        getContext().unregisterReceiver(_broadcastReceiver); //
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;

                // Log.d(TAG, "Touch X : " + x + " Touch Y : " + y);
                break;
            case MotionEvent.ACTION_UP:
                if (!isScrolling && getTouch(x, y)) {
                    isScrolling = false;
                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
                // Release the scroll.
                isScrolling = false;
                return false;
            case MotionEvent.ACTION_MOVE:
                deltaX = event.getX() - startX;
                deltaY = event.getY() - startY;

                startX = event.getX();
                startY = event.getY();
                // Log.d(TAG, "Move deltaX : " + deltaX + " Move deltaY : " +
                // deltaY);

                int yScroll = getScrollY();

                if (Math.abs(deltaY) > 5 || isScrolling) {
                    isScrolling = true;
                    yScroll -= deltaY;

                    if (yScroll <= 0)
                        yScroll = 0;

                    else if (yScroll >= height - parentHeight)
                        yScroll = (height - parentHeight);

                    scrollTo(0, yScroll);
                }
                break;
        }
        return true;
    }

    private boolean getTouch(int x, int y) {
        for (MealInsulin meal : meals) {
            if (meal.isTouchedInside(x, y + getScrollY())) {
                if (onScheduleClickListener != null)
                    onScheduleClickListener.onMealClick(meal);

                return true;
            }
        }
        return false;
    }

    public interface OnScheduleClickListener {
        public void onMealClick(MealInsulin meal);

    }

    public interface OnNowChangedListener {
        // you can define any parameter as per your requirement
        public void onNowChanged(float now);
    }

}
