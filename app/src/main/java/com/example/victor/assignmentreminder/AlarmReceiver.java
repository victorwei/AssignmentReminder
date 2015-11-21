package com.example.victor.assignmentreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by victor on 11/6/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private Calendar currentDate, dueDate, nextDate;
    private Date dueDateDate;
    private AlarmManager alarmManager;




    @Override
    public void onReceive(Context context, Intent intent){

        //create variables
        String notificationText = "Stop procrastinating and get to work!";
        String assignmentTitle = intent.getStringExtra("Title");
        String dueDateString = intent.getStringExtra("DueDate");
        String notificationMessage = "You have an assignment due: " + dueDateString +"\nBe fucking productive!";
        String tickerTitle = "Reminder for " + assignmentTitle;
        int requestCode = intent.getIntExtra("PendingIntentRequestCode",0);


        try {
            dueDateDate = AssignmentRecord.standardDateformat.parse(intent.getStringExtra("DueDate"));
        } catch (ParseException e) {
            dueDateDate = new Date();
        }

        currentDate = Calendar.getInstance();
        nextDate = Calendar.getInstance();
        nextDate.add(Calendar.DATE, 1);
        dueDate = Calendar.getInstance();
        dueDate.setTime(dueDateDate);


        //send another notification in a day if current date is before due date

        if (dueDate.after(currentDate)){
            alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent nextNotificationIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(context,requestCode, nextNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextDate.getTimeInMillis(), notificationPendingIntent);
            //create new notification to go off in 1 day.
        }



        Intent notificationIntent = new Intent (context, TimerActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setTicker(tickerTitle)
                .setContentTitle(assignmentTitle)
                .setContentText(notificationMessage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationMessage))
                .setSmallIcon(R.drawable.notification_template_icon_bg);

        notificationBuilder.setContentIntent(notificationPendingIntent);


        notificationManager.notify(10, notificationBuilder.build());

    }


}
