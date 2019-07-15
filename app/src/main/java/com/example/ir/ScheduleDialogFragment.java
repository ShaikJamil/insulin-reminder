package com.example.ir;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;

public class ScheduleDialogFragment extends DialogFragment implements
        OnClickListener, TimePickerDialog.OnTimeSetListener {
    public static final String TIMEPICKER_TAG = "timepicker";
    MealInsulin meal;
    ImageView ivIcon;
    TimePickerDialog timePickerDialog;
    private TextView tvMealType, tvTime;
    private EditText etValueN, etValueR, etSmsNumber;
    private CheckBox cbAlarm, cbSms;
    private Button btUpdate, btCancel;
    private InsulinSyringeView insulinSyringeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        meal = (MealInsulin) getArguments().getSerializable("DATA");

        getDialog().requestWindowFeature(STYLE_NO_TITLE);

        View rootView = inflater.inflate(R.layout.fragment_schedule_dialog,
                container, false);

        ivIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        ivIcon.setImageBitmap(meal.getMealType().getIcon(getActivity()));

        tvMealType = (TextView) rootView.findViewById(R.id.tvMealType);
        tvMealType.setText(meal.getMealType().toString());

        insulinSyringeView = (InsulinSyringeView) rootView
                .findViewById(R.id.insulinSyringeView1);
        insulinSyringeView.setValues(meal.getValues().getN(), meal.getValues()
                .getR(), AppManager.getCapacity(getActivity()));

        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        tvTime.setText(meal.getTimeString());
        tvTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                timePickerDialog = TimePickerDialog.newInstance(
                        ScheduleDialogFragment.this, Integer.parseInt(tvTime
                                .getText().toString().split(":")[0]), Integer
                                .parseInt(tvTime.getText().toString()
                                        .split(":")[1]), false, false);
                timePickerDialog.setVibrate(true);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getFragmentManager(), TIMEPICKER_TAG);
            }
        });

        etValueN = (EditText) rootView.findViewById(R.id.etValueN);
        etValueN.setText(meal.getValues().getN() + "");
        etValueN.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    insulinSyringeView.setValueN(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    insulinSyringeView.setValueN(0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etValueR = (EditText) rootView.findViewById(R.id.etValueR);
        etValueR.setText(meal.getValues().getR() + "");
        etValueR.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                try {
                    insulinSyringeView.setValueR(Integer.parseInt(s.toString()));
                } catch (Exception e) {
                    insulinSyringeView.setValueR(0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbAlarm = (CheckBox) rootView.findViewById(R.id.cbAlarm);
        cbAlarm.setChecked(meal.isScheduled());

        cbSms = (CheckBox) rootView.findViewById(R.id.cbSms);
        cbSms.setChecked(meal.isSms());
        cbSms.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                etSmsNumber.setEnabled(isChecked);
            }
        });

        etSmsNumber = (EditText) rootView.findViewById(R.id.etSmsNumber);
        etSmsNumber.setText(AppManager.getSmsNumber(getActivity()));
        etSmsNumber.setEnabled(meal.isSms());

        btUpdate = (Button) rootView.findViewById(R.id.btUpdate);
        btUpdate.setOnClickListener(this);

        btCancel = (Button) rootView.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(this);

        return rootView;
    }

    private Context getActivity() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btUpdate:
                if (checkFields()) {
                    updateMeal();
                    dismiss();
                }
                break;

            case R.id.btCancel:
                dismiss();
                break;
        }
    }

    private boolean checkFields() {
        Boolean result = true;

        etSmsNumber.setError(null);
        etValueN.setError(null);
        etValueR.setError(null);
        tvTime.setError(null);

        int r = 0;
        int n = 0;

        View focusView = null;

        if (cbSms.isChecked()
                && TextUtils.isEmpty(etSmsNumber.getText().toString().trim())) {
            etSmsNumber
                    .setError(getString(R.string.msg_value_sms_number_required));
            focusView = etSmsNumber;
            result = false;
        } else if (cbSms.isChecked()
                && etSmsNumber.getText().toString().trim().length() < 5) {
            etSmsNumber
                    .setError(getString(R.string.msg_value_sms_number_error));
            focusView = etSmsNumber;
            result = false;
        }

        if (TextUtils.isEmpty(etValueR.getText().toString().trim())) {
            etValueR.setError(getString(R.string.msg_value_r_required));
            focusView = etValueN;
            result = false;
        } else {
            try {
                r = Integer.parseInt(etValueR.getText().toString());
                if (r < 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                etValueR.setError(getString(R.string.msg_value_r_error));
                focusView = etValueR;
                result = false;
            }
        }

        if (TextUtils.isEmpty(etValueN.getText().toString().trim())) {
            etValueN.setError(getString(R.string.msg_value_n_required));
            focusView = etValueN;
            result = false;
        } else {
            try {
                n = Integer.parseInt(etValueN.getText().toString());
                if (n < 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                etValueN.setError(getString(R.string.msg_value_n_error));
                focusView = etValueN;
                result = false;
            }
        }

        if (TextUtils.isEmpty(tvTime.getText().toString().trim())) {
            tvTime.setError(getString(R.string.msg_value_time_required));
            focusView = tvTime;
            result = false;
        } else {
            try {
                int hour = Integer.parseInt(tvTime.getText().toString().trim()
                        .split(":")[0]);
                // int minute = Integer.parseInt(tvTime.getText().toString()
                // .trim().split(":")[1]);

                if (hour > meal.getMealType().getMaxHour()
                        || hour < meal.getMealType().getMinHour())
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                String errorMessage = String.format(
                        getString(R.string.msg_value_time_error), meal
                                .getMealType().toString(), meal.getMealType()
                                .getMinHour(), meal.getMealType().getMaxHour());
                tvTime.setError(errorMessage);
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG)
                        .show();
                focusView = tvTime;
                result = false;
            }
        }

        if (result && cbAlarm.isChecked() && n + r == 0) {
            String errorMessage = getResources().getString(
                    R.string.msg_values_error);

            etValueR.setError(errorMessage);
            etValueN.setError(errorMessage);

            focusView = etValueR;
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG)
                    .show();
            result = false;
        } else if (result && cbAlarm.isChecked()
                && n + r > AppManager.getCapacity(getActivity())) {
            String errorMessage = getResources().getString(
                    R.string.msg_capacity_error);

            etValueR.setError(errorMessage);
            etValueN.setError(errorMessage);

            focusView = etValueR;
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG)
                    .show();
            result = false;
        }

        if (result == false) {
            focusView.performClick();
            focusView.requestFocus();
        }

        return result;
    }

    private void updateMeal() {
        meal.setTimeString(tvTime.getText().toString());
        meal.getValues().setN(
                Integer.parseInt(etValueN.getText().toString().trim()));
        meal.getValues().setR(
                Integer.parseInt(etValueR.getText().toString().trim()));
        meal.setScheduled(cbAlarm.isChecked());
        meal.setSms(cbSms.isChecked());
        AppManager.setMeal(getActivity(), meal);
        AppManager.setSmsNumber(getActivity(), etSmsNumber.getText().toString()
                .trim());

        AppManager.resetAlarms(getActivity());
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        tvTime.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "")
                + ":" + (minute < 10 ? "0" + minute : minute));
    }

    public void setArguments(Bundle args) {
    }
}
