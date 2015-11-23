package com.example.victor.assignmentreminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by victor on 9/8/15.
 */
public class AssignmentRecord {




    public enum Status {
        NOTFINISHED, FINISHED
    };

    //for global variables
    private static SharedPreferences globalVariables;
    private static String sharedPreferenceString= "Application_Variables";
    private static String notifyHour, notifyMinute;
    private static int remindHour, remindMinute;


    public static final String LINE_SEP = System.getProperty("line.separator");


    public final static String ITEM = "item";
    public final static String STATUS = "status";
    public final static String DDATE = "due_date";
    public final static String RDATE = "reminder_date";
    public final static String DATEDDATE = "due date";
    public final static String DATERDATE = "reminder date";
    public final static String REMINDER = "reminder";
    public final static String FILENAME = "filename";


    private String itemName = new String();
    private String notificationTitle = new String();
    private String notificationText = new String();
    private String dueDateString = new String();
    private String remindDateString = new String();
    private Date dueDate = new Date();
    private Date reminderDate = new Date();
    private Status defaultStatus = Status.NOTFINISHED;

    private AlarmManager alarmManager;
    private Intent notificationReceiverIntent;
    private PendingIntent notificationReceiverPendingIntent;
    private static final int mID = 1;


    public final static SimpleDateFormat standardDateformat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    private static void getSettingValues (Context context){

        globalVariables = context.getSharedPreferences(sharedPreferenceString, Context.MODE_PRIVATE);

        notifyHour = "HourVariable";
        notifyMinute = "MinuteVariable";

        int hour = globalVariables.getInt(notifyHour, 10 );
        int minute = globalVariables.getInt(notifyMinute, 0);

        remindHour = hour;
        remindMinute = minute;
    }



    AssignmentRecord(String item, Date duedate, Date reminderdate) {
        this.itemName = item;
        this.dueDate = duedate;
        this.reminderDate = reminderdate;
        //this.currentStatus = status;

    }

    AssignmentRecord (Intent intent){
        itemName = intent.getStringExtra(AssignmentRecord.ITEM);
        //defaultStatus = Status.valueOf(intent.getStringExtra(AssignmentRecord.STATUS));
        dueDateString = intent.getStringExtra(AssignmentRecord.DDATE);
        remindDateString = intent.getStringExtra(AssignmentRecord.RDATE);

        try {
            dueDate = AssignmentRecord.standardDateformat.parse(intent.getStringExtra(AssignmentRecord.DDATE));
        } catch (ParseException e) {
            dueDate = new Date();
        }
        try {
            reminderDate = AssignmentRecord.standardDateformat.parse(intent.getStringExtra(AssignmentRecord.RDATE));
        } catch (ParseException e) {
            reminderDate = new Date();
        }

    }

    public void setReminderNotification (Context context) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //create random request code for pending intent so different pendingIntents don't conflict with each other
        Random random = new Random();
        int var = random.nextInt(1000);

        notificationReceiverIntent = new Intent(context, AlarmReceiver.class);
        notificationReceiverIntent.putExtra("Title", itemName);
        notificationReceiverIntent.putExtra("DueDate", dueDateString);
        notificationReceiverIntent.putExtra("DueDateValue", dueDate);
        notificationReceiverIntent.putExtra("PendingIntentRequestCode", var);

        long date = getRemindDayLong(context);

        Calendar calendar = Calendar.getInstance();


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        long time = calendar.getTimeInMillis();


        notificationReceiverPendingIntent = PendingIntent.getBroadcast(context, var, notificationReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date ,AlarmManager.INTERVAL_DAY,
                notificationReceiverPendingIntent);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, date, notificationReceiverPendingIntent);

    }

    public void cancelNotification ()
    {
        //notificationReceiverPendingIntent.cancel();
        alarmManager.cancel(notificationReceiverPendingIntent);
    }



    // use String values and create Intent

    public static void sendIntent(Intent intent, String itemtitle, String duedate, String reminddate) {

        intent.putExtra(AssignmentRecord.ITEM, itemtitle);
        intent.putExtra(AssignmentRecord.DDATE, duedate);
        intent.putExtra(AssignmentRecord.RDATE, reminddate);
        //intent.putExtra(AssignmentRecord.DATEDDATE, dDueDate.getTime());
        //intent.putExtra(AssignmentRecord.DATERDATE, dRemindDate.getTime());

    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemname) {
        itemName = itemname;
    }
    public Status getCurrentStatus() {
        return defaultStatus;
    }
    public void setStatus(Status status){
        defaultStatus = status;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate ( Date dDate) {
        dueDate = dDate;
    }
    public Date getReminderDate() {
        return reminderDate;
    }
    public void setReminderDate (Date rDate){
        reminderDate = rDate;
    }

    @Override
    public String toString(){
        return itemName + LINE_SEP + standardDateformat.format(dueDate) +
                LINE_SEP + standardDateformat.format(reminderDate);
    }


    //confirmed gives correct time
    public Long getRemindDayLong(Context context) {

        // get values to determine when to send notificatioon
        getSettingValues(context);

        Calendar targetdate = Calendar.getInstance();
        targetdate.setTime (reminderDate);

        targetdate.set(Calendar.HOUR_OF_DAY, remindHour);
        targetdate.set(Calendar.MINUTE, remindMinute);

        return targetdate.getTimeInMillis();

    }


}
