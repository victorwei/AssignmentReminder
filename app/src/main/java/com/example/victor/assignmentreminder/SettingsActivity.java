package com.example.victor.assignmentreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by victor on 11/8/15.
 */
public class SettingsActivity extends Activity {

    //"global" variables
    private static SharedPreferences globalVariables;
    private static String sharedPreferenceString= "Application_Variables";
    private static String studyTime, shortBreak, longBreak;
    private static int studyVal, sBreakVal, lBreakVal;

    private final int highlightColor = 0x9900CCFF;
    private final int backgroundCOlor = 0x000000;

    private static TextView studyValue, sBreakValue, lBreakValue, notificationTimeValue;
    private static String studyText, sBreakText, lBreakText;

    private RelativeLayout studyButton, shortBreakButton, longBreakButton, notifyTimeButton;
    //private TextView studyTime, shortBreakTime, longBreakTime;
    private Button restoreDefaultButton, saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        studyButton = (RelativeLayout)findViewById(R.id.studyTimeSetting);
        shortBreakButton = (RelativeLayout)findViewById(R.id.shortBreakSetting);
        longBreakButton = (RelativeLayout)findViewById(R.id.longBreakSetting);
        notifyTimeButton = (RelativeLayout)findViewById(R.id.ReceiveTimeSetting);

        studyValue = (TextView)findViewById(R.id.StudyTimeValue);
        sBreakValue = (TextView)findViewById(R.id.shortBreakValue);
        lBreakValue = (TextView)findViewById(R.id.longBreakValue);
        notificationTimeValue = (TextView)findViewById(R.id.NotificationTimeValue);

        restoreDefaultButton = (Button)findViewById(R.id.defaultButton);
        saveButton = (Button)findViewById(R.id.saveButton);

        //set the Current Values from global context
        setCurrentValues(getApplicationContext());

        //create "pressed button effect" by highlighting the view
        studyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        studyButton.setBackgroundColor(highlightColor);
                        break;
                    case MotionEvent.ACTION_UP:
                        studyButton.setBackgroundColor(backgroundCOlor);
                }
                return false;
            }
        });

        shortBreakButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        shortBreakButton.setBackgroundColor(highlightColor);
                        break;
                    case MotionEvent.ACTION_UP:
                        shortBreakButton.setBackgroundColor(backgroundCOlor);
                }
                return false;
            }
        });

        longBreakButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        longBreakButton.setBackgroundColor(highlightColor);
                        break;
                    case MotionEvent.ACTION_UP:
                        longBreakButton.setBackgroundColor(backgroundCOlor);
                }
                return false;
            }
        });

        //clickable event for study time
        studyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new numberPickerDialog();
                dialogFragment.show(getFragmentManager(), "numberPicker");

            }
        });

        //clickable event for short break time
        shortBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new sBreakPickerDialog();
                dialogFragment.show(getFragmentManager(), "numberPicker");

            }
        });

        //clickable event for long break time
        longBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new lBreakPickerDialog();
                dialogFragment.show(getFragmentManager(), "numberPicker");

            }
        });

        notifyTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        restoreDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultValues(getApplicationContext());

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public static void setDefaultValues(Context context){

        int defaultStudy = 25;
        int defaultSBreak = 5;
        int defaultLBreak = 15;

        String defaultStudyString = defaultStudy + " minutes";
        String defaultSBreakString = defaultSBreak + " minutes";
        String defaultLBreakString = defaultLBreak + " minutes";
        String defaultNotifyString = "10:00 AM";

        // set TextViews to default
        studyValue.setText(defaultStudyString);
        sBreakValue.setText(defaultSBreakString);
        lBreakValue.setText(defaultLBreakString);
        notificationTimeValue.setText(defaultNotifyString);

        globalVariables = context.getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
        SharedPreferences.Editor editor = globalVariables.edit();
        editor.putInt(studyTime, defaultStudy);
        editor.putInt(shortBreak, defaultSBreak);
        editor.putInt(longBreak, defaultLBreak);
        editor.commit();
    }

    public static void setCurrentValues(Context context){


        globalVariables = context.getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);

        int studyVar = globalVariables.getInt(studyTime, 25 );
        studyVal = globalVariables.getInt(studyTime, 25);
        int sBreakVar = globalVariables.getInt(shortBreak, 5);
        sBreakVal = globalVariables.getInt(shortBreak, 5);
        int lBreakVar = globalVariables.getInt(longBreak, 15);
        lBreakVal = globalVariables.getInt(longBreak, 15);

        String defaultStudyString = studyVar + " minutes";
        String defaultSBreakString = sBreakVar + " minutes";
        String defaultLBreakString = lBreakVar + " minutes";
        String defaultNotifyString = "10:00 AM";

        // set TextViews to default
        studyValue.setText(defaultStudyString);
        sBreakValue.setText(defaultSBreakString);
        lBreakValue.setText(defaultLBreakString);
        notificationTimeValue.setText(defaultNotifyString);

    }



    public static class numberPickerDialog extends DialogFragment
            implements NumberPicker.OnValueChangeListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final NumberPicker picker = new NumberPicker(getActivity());
            picker.setMinValue(1);
            picker.setMaxValue(60);
            picker.setValue(studyVal);
            picker.setWrapSelectorWheel(false);

            picker.setOnValueChangedListener(this);


            builder.setMessage("Set Study Time")
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cancel
                        }
                    });

            final FrameLayout parent = new FrameLayout(getActivity());
            parent.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));
            builder.setView(parent);

            Dialog dialog = builder.create();
            return dialog;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            setTimerValues(newVal, 1);
            globalVariables = getActivity().getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
            SharedPreferences.Editor editor = globalVariables.edit();
            editor.putInt(studyTime, newVal);
            editor.commit();

        }
    }

    public static class sBreakPickerDialog extends DialogFragment
            implements NumberPicker.OnValueChangeListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final NumberPicker picker = new NumberPicker(getActivity());
            picker.setMinValue(1);
            picker.setMaxValue(60);
            picker.setValue(sBreakVal);
            picker.setWrapSelectorWheel(false);

            picker.setOnValueChangedListener(this);


            builder.setMessage("Set Short Break")
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cancel
                        }
                    });

            final FrameLayout parent = new FrameLayout(getActivity());
            parent.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));
            builder.setView(parent);

            Dialog dialog = builder.create();
            return dialog;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            setTimerValues(newVal, 2);
        }
    }



    public static class lBreakPickerDialog extends DialogFragment
            implements NumberPicker.OnValueChangeListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final NumberPicker picker = new NumberPicker(getActivity());
            picker.setMinValue(1);
            picker.setMaxValue(60);
            picker.setValue(lBreakVal);
            picker.setWrapSelectorWheel(false);

            picker.setOnValueChangedListener(this);


            builder.setMessage("Set Long Break")
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //cancel
                        }
                    });

            final FrameLayout parent = new FrameLayout(getActivity());
            parent.addView(picker, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER));
            builder.setView(parent);

            Dialog dialog = builder.create();
            return dialog;
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            setTimerValues(newVal, 3);

        }
    }


    public static void setTimerValues (int time, int timertype){
        String timeValue;

        switch (timertype){
            case 1:
                timeValue = time + " minutes";
                studyValue.setText(timeValue);
                break;

            case 2:
                timeValue = time + " minutes";
                sBreakValue.setText(timeValue);
                break;

            case 3:
                timeValue = time + " minutes";
                lBreakValue.setText(timeValue);
                break;

        }

    }


}
