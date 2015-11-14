package com.example.victor.assignmentreminder;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends ListActivity {

    private static final int ADD_ASSIGNMENT = 0;
    private static final String FILE_DATA = "saveFile.tmp";

    private static final int MENU_DEL = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;


    private AlarmManager alarmManager;
    private Intent notifcationReceiverIntent;
    private PendingIntent notificationReceiverPendingIntent;

    AssignmentAdapter aAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aAdapter = new AssignmentAdapter(getApplicationContext(), this);



        getListView().setHeaderDividersEnabled(true);


        LinearLayout headerView = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_main, getListView(), false);

        getListView().addHeaderView(headerView);


        if (null == headerView){
            return;
        }

        final Button addButton = (Button)findViewById(R.id.AddItemButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start intent to AddAssignmentActivity
                Intent intent = new Intent(getApplicationContext(),AddAssignmentActivity.class);
                startActivityForResult(intent, ADD_ASSIGNMENT);

            }
        });

        final Button timerButton = (Button)findViewById(R.id.TimerButton);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog to choose due date;
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(intent);

                ;
            }
        });

        setListAdapter(aAdapter);



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Log.i(TAG, "Entered onActivityResult()");

        if (requestCode == ADD_ASSIGNMENT) {
            if (resultCode == RESULT_OK) {
                AssignmentRecord newdata = new AssignmentRecord(data);
                aAdapter.addItem(newdata);

                newdata.setReminderNotification(getApplicationContext());


                //sync_calendar(newdata.getItemName());

            }
        }
    }


    @Override
    protected void onResume(){
        //load previous shit
        if (aAdapter.getCount() == 0) {
            load_data();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        save_data();
        super.onPause();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (true) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void sync_calendar(String Title) {

        Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
        calendarIntent.setType("vnd.android.cursor.item/event");
        calendarIntent.putExtra(CalendarContract.Events.TITLE, "Title");
        calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Finish this Assignment!");

        GregorianCalendar duedate = new GregorianCalendar( 2015, 7, 15);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        startActivity(calendarIntent);


    }





    private void load_data(){
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_DATA);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            Date duedate = null;
            Date reminddate = null;

            while (null != (title = reader.readLine())){
                duedate = AssignmentRecord.standardDateformat.parse(reader.readLine());


                reminddate = AssignmentRecord.standardDateformat.parse(reader.readLine());
                aAdapter.addItem(new AssignmentRecord(title, duedate, reminddate));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }

    private void save_data(){
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_DATA, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int i = 0; i < aAdapter.getCount(); i++) {

                writer.println(aAdapter.getItem(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (null != writer)
                writer.close();
        }

    }




}
