package com.example.ir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;


public class MealInsulin implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    MealType mealType;
    Insulin values;
    String timeString;
    Bitmap icon;
    boolean isScheduled;
    boolean isSms;
    int left, top, right, bottom;

    public MealInsulin(MealType mealType, Insulin values, String timeString,
                       Bitmap icon, boolean isScheduled, boolean isSms) {
        super();
        this.mealType = mealType;
        this.values = values;
        this.timeString = timeString;
        this.icon = icon;
        this.isScheduled = isScheduled;
        this.isSms = isSms;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public Insulin getValues() {
        return values;
    }

    public void setValues(Insulin values) {
        this.values = values;
    }

    public float getTime() {
        int hour = Integer.parseInt(timeString.split(":")[0]);
        int minutes = Integer.parseInt(timeString.split(":")[1]);
        float time = hour + (float) minutes / 60;
        return time;
    }

    public int getTimeHour() {
        return Integer.parseInt(timeString.split(":")[0]);
    }

    public int getTimeMinute() {
        return Integer.parseInt(timeString.split(":")[1]);
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean isScheduled) {
        this.isScheduled = isScheduled;
    }

    public boolean isSms() {
        return isSms;
    }

    public void setSms(boolean isSms) {
        this.isSms = isSms;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public boolean isTouchedInside(int x, int y) {
        if (x <= right && x >= left && y <= bottom && y >= top)
            return true;
        return false;
    }

    public enum MealType {
        BREAKFAST("Breakfast"), LUNCH("Lunch"), DINNER("Dinner");

        String value;

        MealType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public String getDefaulTime() {
            switch (this) {
                case BREAKFAST:
                    return "7:00";
                case LUNCH:
                    return "12:00";
                case DINNER:
                    return "20:00";
                default:
                    return "00:00";
            }
        }

        public Bitmap getIcon(Context context) {
            switch (this) {
                case BREAKFAST:
                    return BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_breakfast);
                case LUNCH:
                    return BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_lunch);
                case DINNER:
                    return BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_dinner);
                default:
                    return null;
            }
        }

        public int getMaxHour() {
            switch (this) {
                case BREAKFAST:
                    return 10;
                case LUNCH:
                    return 16;
                case DINNER:
                    return 23;
                default:
                    return 0;
            }
        }

        public int getMinHour() {
            switch (this) {
                case BREAKFAST:
                    return 4;
                case LUNCH:
                    return 11;
                case DINNER:
                    return 18;
                default:
                    return 0;
            }
        }

        public int getRequestCode() {
            switch (this) {
                case BREAKFAST:
                    return 1;
                case LUNCH:
                    return 2;
                case DINNER:
                    return 3;
                default:
                    return 0;
            }
        }

    }

    public static class Insulin {
        int r, n;

        public Insulin(int r, int n) {
            super();
            this.r = r;
            this.n = n;
        }

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public String toString() {
            return String.format("R=%d, N=%d", r, n);
        }
    }
}
