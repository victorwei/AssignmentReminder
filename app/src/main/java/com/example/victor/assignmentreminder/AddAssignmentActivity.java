package com.example.victor.assignmentreminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by victor on 9/14/15.
 */
public class AddAssignmentActivity extends Activity {

    //set notification variables
    private AlarmManager alarmManager;
    private Intent notifcationReceiverIntent;
    private PendingIntent notificationReceiverPendingIntent;


    //variables
    private static String dueDateString;
    private static String reminderDateString;
    private static TextView dueDateView;
    private static TextView reminderDateView;
    private static Calendar remindDate, dueDate;

    // variables for
    private Date aDate;
    //private Calendar rDate, dDate;
    private EditText aAssignmentText;
    private RadioButton aDefaultRbutton;
    private RadioGroup aStatusRGroup;
    private RelativeLayout dueDateButton, remindDateButton;

    private final int highlightColor = 0x9900CCFF;
    private final int backgroundCOlor = 0x000000;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_assignment);



        // SET UP VIEW
        //aDefaultRbutton = (RadioButton)findViewById(R.id.statusNo);
        //aStatusRGroup = (RadioGroup)findViewById(R.id.statusGroup);
        aAssignmentText = (EditText)findViewById(R.id.assignment_title);
        dueDateView = (TextView)findViewById(R.id.duedate);
        reminderDateView = (TextView)findViewById(R.id.remindDate);
        dueDateButton = (RelativeLayout)findViewById(R.id.dueDateLayout);
        remindDateButton = (RelativeLayout)findViewById(R.id.remindDateLayout);


        dueDateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dueDateButton.setBackgroundColor(highlightColor);
                        break;
                    case MotionEvent.ACTION_UP:
                        dueDateButton.setBackgroundColor(backgroundCOlor);
                }
                return false;
            }
        });

        remindDateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        remindDateButton.setBackgroundColor(highlightColor);
                        break;
                    case MotionEvent.ACTION_UP:
                        remindDateButton.setBackgroundColor(backgroundCOlor);
                }
                return false;
            }
        });

        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dueDateDialog();
            }
        });

        remindDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderDateDialog();
            }
        });



        // button for cancel button

        final Button cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // button for submit button
        final Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String assignmentTitle = aAssignmentText.getText().toString();

                //Status status = Status.NOTFINISHED;

                String duedate = dueDateString;
                String reminderdate = reminderDateString;


                if (reminderdate == null || duedate == null || assignmentTitle == null){

                    String toastMessage = "Oops! You forgot to fill out a field";
                    Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
                    toast.show();

                }else {
                    //set up new intent and package data and send
                    Intent intent = new Intent();

                    AssignmentRecord.sendIntent(intent, assignmentTitle, duedate, reminderdate);

                    setResult(RESULT_OK, intent);


                    //setCalendarEvent();

                    finish();
                }

            }
        });

    }


    // set default date


    public void setDefaultDate() {

        aDate = new Date();

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        setDueDateString(month, day, year);
        setReminderDateString(month, day, year);


    }


    // date picker dialog fragment

    public static class dueDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar currentdate = Calendar.getInstance();

            int month = currentdate.get(Calendar.MONTH);
            int day = currentdate.get(Calendar.DAY_OF_MONTH);
            int year = currentdate.get(Calendar.YEAR);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dueDate = Calendar.getInstance();
            dueDate.set(Calendar.MONTH, monthOfYear);
            dueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dueDate.set(Calendar.YEAR, year);




            setDueDateString(monthOfYear, dayOfMonth, year);

            dueDateView.setText(dueDateString);
        }
    }

    public static class reminderDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar currentdate = Calendar.getInstance();

            int month = currentdate.get(Calendar.MONTH);
            int day = currentdate.get(Calendar.DAY_OF_MONTH);
            int year = currentdate.get(Calendar.YEAR);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {


            remindDate = Calendar.getInstance();
            remindDate.set(Calendar.MONTH, monthOfYear);
            remindDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            remindDate.set(Calendar.YEAR, year);

            if (remindDate.after(dueDate)){
                String toastMessage = "Reminder date must be before due date";
                System.out.println("Date1 is after Date2");
                Toast toast = Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                setReminderDateString(monthOfYear, dayOfMonth, year);
                reminderDateView.setText(reminderDateString);
            }

        }
    }



    private void dueDateDialog() {
        DialogFragment dialogFragment = new dueDatePickerFragment();
        dialogFragment.show(getFragmentManager(), "dueDatePicker");
    }

    private void reminderDateDialog() {
        DialogFragment dialogFragment = new reminderDatePickerFragment();
        dialogFragment.show(getFragmentManager(), "reminderDatePicker");
    }

    private static void setDueDateString (int Month, int Day, int Year ) {

        Month++;
        String monthString = "" + Month;
        String dayString = "" + Day;
//        String monthString = Integer.toString(Month);
//        String dayString = Integer.toString(Day);
//        String yearString = Integer.toString(Year);

        if (Month < 10) {
            monthString = "0"+monthString;
        }
        if (Day < 10) {
            dayString = "0"+dayString;
        }

        dueDateString = monthString + "-" + dayString + "-" + Year;


    }

    private static void setReminderDateString (int Month, int Day, int Year ) {

        /*
        remindDate = Calendar.getInstance();
        remindDate.set(Calendar.MONTH, Month);
        remindDate.set(Calendar.DAY_OF_MONTH, Day);
        remindDate.set(Calendar.YEAR, Year);
        */

        Month++;

        String monthString = "" + Month;
        String dayString = "" + Day;
        //String monthString = Integer.toString(Month);
        //String dayString = Integer.toString(Day);
        //String yearString = Integer.toString(Year);

        if (Month < 10) {
            monthString = "0"+monthString;
        }
        if (Day < 10) {
            dayString = "0"+dayString;
        }

        reminderDateString = monthString + "-" + dayString + "-" + Year;
    }




    private void setCalendarEvent ( ){
        ContentResolver contentResolver = this.getContentResolver();
        ContentValues contentValues = new ContentValues();

        String assignmentTitle = aAssignmentText.getText().toString();

        contentValues.put(CalendarContract.Events.DTSTART, reminderDateString);
        contentValues.put(CalendarContract.Events.TITLE, assignmentTitle);
        contentValues.put(CalendarContract.Events.DESCRIPTION, "Have you completed" + assignmentTitle);

        TimeZone timeZone = TimeZone.getDefault();
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

        contentValues.put(CalendarContract.Events.CALENDAR_ID, 1);

        contentValues.put(CalendarContract.Events.RRULE, "FREQ=DAILY;UNTIL="
                + dueDateString);

        contentValues.put(CalendarContract.Events.DURATION, "+P1H");

        contentValues.put(CalendarContract.Events.HAS_ALARM, 1);

        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);

    }





}
