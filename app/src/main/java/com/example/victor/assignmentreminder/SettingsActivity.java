package com.example.victor.assignmentreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by victor on 11/8/15.
 */
public class SettingsActivity extends Activity {

    //"global" variables
    private static SharedPreferences globalVariables;
    private static String sharedPreferenceString= "Application_Variables";
    private static String studyTime, shortBreak, longBreak, notifyHour, notifyMinute;
    private static int studyVal, sBreakVal, lBreakVal, hourVal, minuteVal;


    private final int highlightColor = 0x9900CCFF;
    private final int backgroundCOlor = 0x000000;

    private static TextView studyValue, sBreakValue, lBreakValue, notificationTimeValue;
   // private static String studyText, sBreakText, lBreakText;

    private RelativeLayout studyButton, shortBreakButton, longBreakButton, notifyTimeButton;
    //private TextView studyTime, shortBreakTime, longBreakTime;
    private Button restoreDefaultButton, saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        studyTime = "StudyTimeVariable";
        shortBreak = "ShortBTimeVariable";
        longBreak = "LongBTimeVariable";
        notifyHour = "HourVariable";
        notifyMinute = "MinuteVariable";

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

                DialogFragment dialogFragment = new studyPickerDialog();
                dialogFragment.show(getFragmentManager(), "numberPicker");

            }
        });

        //clickable event for short break time
        shortBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new sBreakPickerDialog();
                dialogFragment.show(getFragmentManager(), "number2Picker");

            }
        });

        //clickable event for long break time
        longBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new lBreakPickerDialog();
                dialogFragment.show(getFragmentManager(), "number3Picker");

            }
        });

        notifyTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new timePickerDialog();
                dialogFragment.show(getFragmentManager(), "timePicker");
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
                saveChanges(getApplicationContext());
                finish();
            }
        });


    }

    public static void setDefaultValues(Context context){

        int defaultStudy = 25;
        int defaultSBreak = 5;
        int defaultLBreak = 15;
        int defaultHour = 10;
        int defaultMinute = 0;

        String defaultStudyString = defaultStudy + " minutes";
        String defaultSBreakString = defaultSBreak + " minutes";
        String defaultLBreakString = defaultLBreak + " minutes";
        String defaultNotifyString = "10:00 AM";

        // set TextViews to default
        studyValue.setText(defaultStudyString);
        sBreakValue.setText(defaultSBreakString);
        lBreakValue.setText(defaultLBreakString);
        notificationTimeValue.setText(defaultNotifyString);

        studyVal = defaultStudy;
        lBreakVal = defaultLBreak;
        sBreakVal = defaultSBreak;
        hourVal = defaultHour;
        minuteVal = defaultMinute;

        globalVariables = context.getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
        SharedPreferences.Editor editor = globalVariables.edit();
        editor.putInt(studyTime, defaultStudy);
        editor.putInt(shortBreak, defaultSBreak);
        editor.putInt(longBreak, defaultLBreak);
        editor.putInt(notifyHour, defaultHour);
        editor.putInt(notifyMinute, defaultMinute);
        editor.commit();
    }

    public static void setCurrentValues(Context context){


        globalVariables = context.getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);

        //int studyVar = globalVariables.getInt(studyTime, 25 );
        studyVal = globalVariables.getInt(studyTime, 25);
        //int sBreakVar = globalVariables.getInt(shortBreak, 5);
        sBreakVal = globalVariables.getInt(shortBreak, 5);
        //int lBreakVar = globalVariables.getInt(longBreak, 15);
        lBreakVal = globalVariables.getInt(longBreak, 15);
        //int hourValue = globalVariables.getInt(notifyHour, 10);
        hourVal = globalVariables.getInt(notifyHour,10);
        //int minuteValue = globalVariables.getInt(notifyMinute,0);
        minuteVal = globalVariables.getInt(notifyMinute, 0);

        String defaultStudyString = studyVal + " minutes";
        String defaultSBreakString = sBreakVal + " minutes";
        String defaultLBreakString = lBreakVal + " minutes";
        String defaultNotifyString = getNotifyTimeString(hourVal, minuteVal);

        // set TextViews to default
        studyValue.setText(defaultStudyString);
        sBreakValue.setText(defaultSBreakString);
        lBreakValue.setText(defaultLBreakString);
        notificationTimeValue.setText(defaultNotifyString);

    }

    public static void saveChanges (Context context){
        globalVariables = context.getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
        SharedPreferences.Editor editor = globalVariables.edit();
        editor.putInt(studyTime, studyVal);
        editor.putInt(longBreak, lBreakVal);
        editor.putInt(shortBreak, sBreakVal);
        editor.putInt(notifyMinute, minuteVal);
        editor.putInt(notifyHour, hourVal);
        editor.commit();

    }



    //dialog used for picking study time duration
    public static class studyPickerDialog extends DialogFragment
            implements NumberPicker.OnValueChangeListener {

        private int tempValue;


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
                            setTimerValues(tempValue, 1);
                            //save to global variables
                            globalVariables = getActivity().getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
                            SharedPreferences.Editor editor = globalVariables.edit();
                            editor.putInt(studyTime, tempValue);
                            editor.commit();
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
            tempValue = newVal;
            /*
            setTimerValues(newVal, 1);
            //save to global variables
            globalVariables = getActivity().getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
            SharedPreferences.Editor editor = globalVariables.edit();
            editor.putInt(studyTime, newVal);
            editor.commit();
            */
        }
    }

    //dialog used for choosing short break duration
    public static class sBreakPickerDialog extends DialogFragment
            implements NumberPicker.OnValueChangeListener {

        private int tempValue;


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

                            setTimerValues(tempValue, 2);
                            //save to global variables
                            globalVariables = getActivity().getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
                            SharedPreferences.Editor editor = globalVariables.edit();
                            editor.putInt(shortBreak, tempValue);
                            editor.commit();
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
            tempValue = newVal;

        }
    }


    // dialog for choosing long break
    public static class lBreakPickerDialog extends DialogFragment
            implements NumberPicker.OnValueChangeListener {
        private int tempValue;


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
                            setTimerValues(tempValue, 3);
                            //save to global variables
                            globalVariables = getActivity().getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
                            SharedPreferences.Editor editor = globalVariables.edit();
                            editor.putInt(longBreak, tempValue);
                            editor.commit();
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
            tempValue = newVal;

        }
    }


    public static void setTimerValues (int time, int timertype){
        String timeValue;

        switch (timertype){
            case 1:
                timeValue = time + " minutes";
                studyValue.setText(timeValue);
                studyVal = time;
                break;

            case 2:
                timeValue = time + " minutes";
                sBreakValue.setText(timeValue);
                sBreakVal = time;
                break;

            case 3:
                timeValue = time + " minutes";
                lBreakValue.setText(timeValue);
                lBreakVal = time;
                break;

        }

    }


    //dialog used to pick reminder notification TIME
    public static class timePickerDialog extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            //final Calendar c = Calendar.getInstance();
            //int hour = c.get(Calendar.HOUR_OF_DAY);
            //int minute = c.get(Calendar.MINUTE);

            int hour = hourVal;
            int minute = minuteVal;

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hourVal = hourOfDay;
            minuteVal = minute;
            String timeText = getNotifyTimeString(hourOfDay, minute);
            notificationTimeValue.setText(timeText);

            globalVariables = getActivity().getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
            SharedPreferences.Editor editor = globalVariables.edit();
            editor.putInt(notifyMinute, minuteVal);
            editor.putInt(notifyHour, hourVal);
            editor.commit();



        }

    }

    private static String getNotifyTimeString (int hourOfDay, int minute) {
        String hourString = ""+hourOfDay;
        String minuteString = ""+minute;
        String timeString;
        Boolean isPM = false;

        if (hourOfDay<10){
            hourString = "0" + hourString;
        } else if (hourOfDay >= 12) {
            if (hourOfDay == 12) {
            }else {
                int adjustedHour = hourOfDay - 12;
                hourString = "0" + adjustedHour;
            }

            isPM = true;
        }
        if (hourOfDay == 0){
            hourString="12";
        }
        if (minute < 10){
            minuteString = "0" + minuteString;
        }

        if (isPM){
            timeString = hourString+":"+minuteString + " PM";
        } else {
            timeString = hourString+":"+minuteString + " AM";
        }

        return timeString;

    }


}
