package com.example.ir;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SyringeFragment extends Fragment implements OnClickListener {
    final static String TAG = SyringeFragment.class.getName();

    public static EditText etN, etR;
    public static Button btAnimate;
    public static Spinner spCapacity;
    public static InsulinSyringeView insulinSyringeView;

    public SyringeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_syringe, container,
                false);
        // TextView textView = (TextView)
        // rootView.findViewById(R.id.section_label);
        // textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        etR = (EditText) rootView.findViewById(R.id.etR);
        etN = (EditText) rootView.findViewById(R.id.etN);
        btAnimate = (Button) rootView.findViewById(R.id.btAnimate);
        spCapacity = (Spinner) rootView.findViewById(R.id.spCapacity);
        insulinSyringeView = (InsulinSyringeView) rootView
                .findViewById(R.id.syringeView1);

        spCapacity.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                final int capacity = Integer.parseInt(spCapacity
                        .getSelectedItem().toString());
                insulinSyringeView.setValues(0, 0, capacity);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                insulinSyringeView.setValues(0, 0, 100);
            }

        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btAnimate:
                try {
                    final int insulinMilk = Integer.parseInt(etN.getText()
                            .toString());
                    final int insulinCerels = Integer.parseInt(etR.getText()
                            .toString());

                    final int capacity = Integer.parseInt(spCapacity
                            .getSelectedItem().toString());

                    if (insulinMilk + insulinCerels > capacity) {
                        throw new NumberFormatException();
                    } else {
                        new Thread(new Runnable() {
                            public void run() {
                                btAnimate.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        btAnimate.setEnabled(false);
                                        etN.setEnabled(false);
                                        etR.setEnabled(false);
                                        spCapacity.setEnabled(false);
                                    }
                                });

                                if (insulinCerels > 0) {
                                    for (int i = 0; i <= insulinCerels; i++) {
                                        final int value = i;
                                        insulinSyringeView.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                insulinSyringeView.setValues(0, value,
                                                        capacity);
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

                                if (insulinMilk > 0) {
                                    for (int i = 0; i <= insulinMilk; i++) {
                                        final int value = i;
                                        insulinSyringeView.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                insulinSyringeView.setValues(value,
                                                        insulinCerels, capacity);
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
                                        etN.setEnabled(true);
                                        etR.setEnabled(true);
                                        spCapacity.setEnabled(true);
                                    }
                                });

                            }
                        }).start();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), "Wrong Numbers!!!",
                            Toast.LENGTH_LONG).show();
                }

                break;

            default:
                break;
        }
    }
}
