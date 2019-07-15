package com.example.ir;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class InsulinSyringeView extends View implements SurfaceHolder.Callback {
    final int needle = 2;
    final int tip = 6;
    final int plungerTip = 10;
    final int paddingText = 5;

    final int strokeWidth = 1;

    int insulinN, insulinR = 0;
    int capacity = 100;
    int thickness; // Chart Thickness
//    float roundPx = 20;

//    int colorMilk = Color.parseColor("#e83f4d");
//    int colorCerels = Color.parseColor("#49b848");

    int line = Color.parseColor("#00000000");
    int aWhite = Color.parseColor("#55ffffff");
    int aGray = Color.parseColor("#55555555");
    int aBlack = Color.parseColor("#77000000");

    int aMilk = Color.parseColor("#55aaaa55");

    Paint paintFillWhite, paintFillPlunger, paintFillNeedle,
            paintFillTextSyringeMeasurement, paintFillTextInsulin,
            paintFillTextTotal, paintFillGray, paintFillSyringeBody,
            paintStrokeSyringeBody, paintStrokeSyringeMeasurement,
            paintStrokeGray, paintFillInsulinN, paintFillInsulinR;

    int centerX;
    int centerY;

    int width;
    int height;
    int sixPart, tenPart;

    int left, top, bottom, right;
    int paddingLeft, paddingRight, paddingTop, paddingBottom;
    private Bitmap insulin_r;
    private Bitmap insulin_n;

    public InsulinSyringeView(Context context) {
        this(context, null);
    }

    public InsulinSyringeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.InsulinSyringeView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.InsulinSyringeView_insulin_n:
                    insulinN = a.getInt(attr, 0);
                    break;
                case R.styleable.InsulinSyringeView_insulin_r:
                    insulinR = a.getInt(attr, 0);
                    break;
                case R.styleable.InsulinSyringeView_syringe_capacity:
                    capacity = a.getInteger(attr, 100);
                    break;
                case R.styleable.InsulinSyringeView_barThickness:
                    thickness = a.getInt(attr, 50);
                    break;
                case R.styleable.InsulinSyringeView_paddingLeft:
                    paddingLeft = a.getInt(attr, 2);
                    break;
                case R.styleable.InsulinSyringeView_paddingRight:
                    paddingRight = a.getInt(attr, 2);
                    break;
                case R.styleable.InsulinSyringeView_paddingTop:
                    paddingTop = a.getInt(attr, 2);
                    break;
                case R.styleable.InsulinSyringeView_paddingBottom:
                    paddingBottom = a.getInt(attr, 2);
                    break;
            }
        }

        a.recycle();

    }

    public InsulinSyringeView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        initView();
        invalidate();
    }

    private void initView() {
        width = getWidth();
        height = getHeight();

        centerX = (int) (width / 2);
        centerY = (int) (height / 2);

        sixPart = width / 6;
        tenPart = width / 10;

        left = paddingLeft;
        top = paddingTop;
        bottom = height - paddingBottom;
        right = width - paddingRight;

        if (thickness == 0)
            thickness = height / 4;

        // init paints
        paintFillWhite = new Paint();
        paintFillWhite.setColor(Color.WHITE);
        paintFillWhite.setStyle(Paint.Style.FILL);
        paintFillWhite.setAntiAlias(true);

        paintFillPlunger = new Paint();
        paintFillPlunger.setShader(new LinearGradient(0, centerY - thickness
                / 4, 0, centerY + thickness / 4, aGray, aBlack,
                Shader.TileMode.MIRROR));
        paintFillPlunger.setStyle(Paint.Style.FILL);
        paintFillPlunger.setAntiAlias(true);

        paintFillNeedle = new Paint();
        paintFillNeedle.setColor(Color.BLACK);
        paintFillNeedle.setStyle(Paint.Style.FILL);
        paintFillNeedle.setAntiAlias(true);

        // Typeface tf = Typeface.defaultFromStyle(Typeface.ITALIC);

        paintFillTextSyringeMeasurement = new Paint();
        paintFillTextSyringeMeasurement.setColor(Color.BLACK);
        paintFillTextSyringeMeasurement.setStyle(Paint.Style.FILL);
        paintFillTextSyringeMeasurement.setAntiAlias(true);
        paintFillTextSyringeMeasurement.setTextAlign(Align.CENTER);
        paintFillTextSyringeMeasurement.setTextSize(width / 30);
        // paintFillTextSyringeMeasurement.setTypeface(tf);

        paintFillTextInsulin = new Paint();
        paintFillTextInsulin.setColor(Color.BLACK);
        paintFillTextInsulin.setStyle(Paint.Style.FILL);
        paintFillTextInsulin.setAntiAlias(true);
        paintFillTextInsulin.setTextAlign(Align.LEFT);
        paintFillTextInsulin.setTextSize(width / 30);
        // paintFillTextInsulin.setTypeface(tf);

        paintFillTextTotal = new Paint();
        paintFillTextTotal.setColor(Color.BLACK);
        paintFillTextTotal.setStyle(Paint.Style.FILL);
        paintFillTextTotal.setAntiAlias(true);
        paintFillTextTotal.setTextAlign(Align.LEFT);
        paintFillTextTotal.setTextSize(width / 30);
        // paintFillTextTotal.setTypeface(tf);

        paintFillGray = new Paint();
        paintFillGray.setColor(aGray);
        paintFillGray.setStyle(Paint.Style.FILL);
        paintFillGray.setAntiAlias(true);

        paintFillSyringeBody = new Paint();
        paintFillSyringeBody.setShader(new LinearGradient(0, centerY
                - thickness / 3, 0, centerY + thickness / 3, aGray, aWhite,
                Shader.TileMode.MIRROR));
        paintFillSyringeBody.setStyle(Paint.Style.FILL);
        paintFillSyringeBody.setAntiAlias(true);

        paintStrokeSyringeBody = new Paint();
        paintStrokeSyringeBody.setColor(aGray);
        paintStrokeSyringeBody.setStyle(Paint.Style.STROKE);
        paintStrokeSyringeBody.setStrokeJoin(Paint.Join.MITER);
        paintStrokeSyringeBody.setStrokeWidth(strokeWidth);
        paintStrokeSyringeBody.setStyle(Paint.Style.STROKE);
        paintStrokeSyringeBody.setAntiAlias(true);

        paintFillInsulinN = new Paint();
        // paintFillInsulinN.setShader(new LinearGradient(0, centerY - thickness
        // / 2, 0, centerY + thickness / 2, aWhite, aMilk,
        // Shader.TileMode.MIRROR));
        paintFillInsulinN.setColor(aMilk);
        paintFillInsulinN.setStyle(Paint.Style.FILL);
        paintFillInsulinN.setAntiAlias(true);

        paintFillInsulinR = new Paint();
        paintFillInsulinR.setColor(aWhite);
        // paintFillInsulinR.setShader(new LinearGradient(0, centerY - thickness
        // / 2, 0, centerY + thickness / 2, Color.WHITE, aWhite,
        // Shader.TileMode.MIRROR));
        paintFillInsulinR.setStyle(Paint.Style.FILL);
        paintFillInsulinR.setAntiAlias(true);

        paintStrokeSyringeMeasurement = new Paint();
        paintStrokeSyringeMeasurement.setColor(Color.BLACK);
        paintStrokeSyringeMeasurement.setStyle(Paint.Style.STROKE);
        paintStrokeSyringeMeasurement.setStrokeJoin(Paint.Join.MITER);
        paintStrokeSyringeMeasurement.setStrokeWidth(strokeWidth);
        paintStrokeSyringeMeasurement.setAntiAlias(true);

        paintStrokeGray = new Paint();
        paintStrokeGray.setColor(line);
        paintStrokeGray.setColor(aGray);
        paintStrokeGray.setStyle(Paint.Style.STROKE);
        paintStrokeGray.setStrokeJoin(Paint.Join.MITER);
        paintStrokeGray.setStrokeWidth(strokeWidth);
        paintStrokeGray.setAntiAlias(true);

        int iHeight = height / 3;
        int iWidth = 75 * iHeight / 140;

        insulin_r = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.insulin_r), iWidth, iHeight, true);

        insulin_n = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.insulin_n), iWidth, iHeight, true);

    }

    public void setValues(int insulinN, int insulinR, int capacity) {
        this.insulinN = insulinN;
        this.insulinR = insulinR;
        this.capacity = capacity;

        invalidate();
    }

    public void setValueN(int insulinN) {
        this.insulinN = insulinN;

        invalidate();
    }

    public void setValueR(int insulinR) {
        this.insulinR = insulinR;

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentWidth / 2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawInsulin(canvas);
        drawPlunger(canvas);
        drawSyringe(canvas);
    }

    private void drawSyringe(Canvas canvas) {
        // draw needle
        int startLeft = left;
        int endLeft = startLeft + tenPart;

        canvas.drawRect(startLeft, centerY - needle / 2, endLeft, centerY
                + needle / 2, paintFillNeedle);

        // draw tip
        startLeft = endLeft;
        endLeft += tip;

        canvas.drawRect(startLeft, centerY - tip / 2, endLeft, centerY + tip
                / 2, paintFillGray);

        // draw tip2
        startLeft = endLeft;
        endLeft += tip * 2;

        canvas.drawRect(startLeft, centerY - tip, endLeft, centerY + tip,
                paintFillGray);

        // draw barrel
        startLeft = endLeft;
        endLeft += tenPart * 7;

        canvas.drawRect(startLeft, centerY - thickness / 2, endLeft, centerY
                + thickness / 2, paintFillSyringeBody);
        canvas.drawRect(startLeft, centerY - thickness / 2, endLeft, centerY
                + thickness / 2, paintStrokeSyringeBody);

        // draw measurement
        int factor = capacity / 50 == 0 ? 1 : capacity / 50;

        float measuredCapacity = capacity / factor;

        float unit = (float) (endLeft - startLeft - plungerTip * 2)
                / measuredCapacity;
        float startX = startLeft;
        float startY = centerY - thickness / 2;

        for (int i = 0; i < measuredCapacity; i++) {
            startX = startX + unit;
            float stopX = startX;
            float stopY;

            if ((i + 1) % 10 == 0) {
                stopY = startY + 20;
                canvas.drawText((i + 1) * factor + "", startX, centerY
                        + thickness / 4, paintFillTextSyringeMeasurement);
            } else {
                stopY = startY + 10;
            }

            canvas.drawLine(startX, startY, stopX, stopY,
                    paintStrokeSyringeMeasurement);
        }

        // draw barrel2
        startLeft = endLeft;
        endLeft += 10;

        canvas.drawRect(startLeft, centerY - thickness, endLeft, centerY
                + thickness, paintFillGray);

    }

    private void drawInsulin(Canvas canvas) {
        // draw liquid milk
        int startLeft = left + tenPart + tip * 3;
        int endLeft = startLeft + tenPart * 7;

        int factor = capacity / 50 == 0 ? 1 : capacity / 50;

        float measuredCapacity = capacity / factor;

        float unit = (float) (endLeft - startLeft - plungerTip * 2)
                / measuredCapacity;

        float endRight = startLeft + unit * insulinN / factor;

        if (insulinN > 0) {
            canvas.drawBitmap(insulin_n, left, bottom - insulin_n.getHeight(),
                    null);

            canvas.drawRect(startLeft, centerY - thickness / 2, endRight,
                    centerY + thickness / 2, paintFillInsulinN);

            canvas.drawLine(endRight, centerY + thickness / 2, endRight,
                    centerY + thickness, paintStrokeGray);

            canvas.drawText(insulinN + " N", endRight + paddingText, centerY
                    + thickness, paintFillTextInsulin);
        }

        // draw liquid cerels
        startLeft = (int) endRight;
        endRight += unit * insulinR / factor;

        if (insulinR > 0) {
            canvas.drawBitmap(insulin_r, left + insulin_r.getWidth(), bottom
                    - insulin_r.getHeight(), null);

            canvas.drawRect(startLeft, centerY - thickness / 2, endRight,
                    centerY + thickness / 2, paintFillInsulinR);

            canvas.drawLine(endRight, centerY + thickness / 2, endRight,
                    (float) (centerY + thickness * 1.3), paintStrokeGray);

            canvas.drawText(insulinR + " R", endRight + paddingText,
                    (float) (centerY + thickness * 1.3), paintFillTextInsulin);
        }
    }

    private void drawPlunger(Canvas canvas) {
        // draw plunger tip
        int startLeft = left + tenPart + tip * 3;
        int endLeft = startLeft + tenPart * 7;

        int factor = capacity / 50 == 0 ? 1 : capacity / 50;

        float measuredCapacity = capacity / factor;

        float unit = (float) (endLeft - startLeft - plungerTip * 2)
                / measuredCapacity;

        float endRight = startLeft + unit * (insulinN + insulinR) / factor;

        canvas.drawRect(endRight, centerY - thickness / 2, endRight
                + plungerTip, centerY + thickness / 2, paintFillPlunger);

        // draw plunger measurement
        canvas.drawLine(endRight, centerY - thickness / 2, endRight,
                (float) (centerY - thickness * 1.5), paintStrokeGray);

        canvas.drawText(insulinR + insulinN + " Total", endRight + paddingText,
                (float) (centerY - thickness * 1.3), paintFillTextTotal);

        // draw plunger
        startLeft = (int) (endRight + plungerTip);
        endLeft = startLeft + tenPart * 7 + plungerTip;

        canvas.drawRect(startLeft, centerY - thickness / 6, endLeft, centerY
                + thickness / 6, paintFillPlunger);

        // draw plunger end
        startLeft = endLeft;
        endLeft = startLeft + plungerTip;

        canvas.drawRect(startLeft, centerY - thickness / 2, endLeft, centerY
                + thickness / 2, paintFillPlunger);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

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
    public boolean onTouchEvent(MotionEvent event) {
        // float x = centerX - event.getX();
        // float y = centerY - event.getY();
        //
        // switch (event.getAction()) {
        // case MotionEvent.ACTION_DOWN:
        // case MotionEvent.ACTION_MOVE:
        // if (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(radiusOuter, 2)) {
        // makeAllFalse();
        // if (Math.abs(x) < y)
        // buttonsSellected[2] = true;
        // else if (Math.abs(y) < x)
        // buttonsSellected[1] = true;
        // else if (Math.abs(y) < Math.abs(x))
        // buttonsSellected[3] = true;
        // else if (Math.abs(x) < Math.abs(y))
        // buttonsSellected[0] = true;
        // invalidate();
        // return true;
        // } else {
        // makeAllFalse();
        // }
        // invalidate();
        // return super.onTouchEvent(event);
        //
        // case MotionEvent.ACTION_UP:
        // if (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(radiusOuter, 2)) {
        // if (navigationListener != null) {
        // if (buttonsSellected[0] && buttonsEnabled[0])
        // navigationListener.onDownClick(this);
        // else if (buttonsSellected[1] && buttonsEnabled[1])
        // navigationListener.onLeftClick(this);
        // else if (buttonsSellected[2] && buttonsEnabled[2])
        // navigationListener.onUpClick(this);
        // else if (buttonsSellected[3] && buttonsEnabled[3])
        // navigationListener.onRightClick(this);
        //
        // makeAllFalse();
        // invalidate();
        // return true;
        // }
        // } else {
        // makeAllFalse();
        // invalidate();
        // return super.onTouchEvent(event);
        // }
        //
        // }
        return super.onTouchEvent(event);

    }
}
