package com.example.victor.assignmentreminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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
    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;
    private Intent notificationReceiverIntent, notificationIntent;
    private PendingIntent notificationReceiverPendingIntent, notificationPendingIntent;
    private static final int mID = 1;


    public final static SimpleDateFormat standardDateformat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);



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

        long date = getRemindDayLong();


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.DAY_OF_MONTH, 15);

        //calendar.set(Calendar.HOUR_OF_DAY, 20);
        //calendar.set(Calendar.MINUTE, 48);
        //calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.AM_PM,Calendar.PM);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        long time = calendar.getTimeInMillis();


        notificationReceiverPendingIntent = PendingIntent.getBroadcast(context, var, notificationReceiverIntent, 0);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,AlarmManager.INTERVAL_DAY,
//                notificationReceiverPendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), notificationReceiverPendingIntent);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time,
//                AlarmManager.INTERVAL_DAY, notificationReceiverPendingIntent);

    }

    public void cancelNotification ()
    {
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
    public Long getRemindDayLong() {
        Calendar targetdate = Calendar.getInstance();
        targetdate.setTime (reminderDate);
        int month = targetdate.get(Calendar.MONTH);
        int day = targetdate.get(Calendar.DAY_OF_MONTH);
        int year = targetdate.get(Calendar.YEAR);

        return targetdate.getTimeInMillis();

    }


}
