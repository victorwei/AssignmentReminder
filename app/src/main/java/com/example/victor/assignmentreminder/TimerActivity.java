package com.example.victor.assignmentreminder;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class TimerActivity extends Activity
{

    private static SharedPreferences globalVariables;
    private static String sharedPreferenceString= "Application_Variables";
    private static String studyTime, shortBreak, longBreak;


    private Button btnStart, btnStop, btnFinish;
    //private Uri breakRingtonezz, workRingtonezz;
    private TextView textViewTimer, textViewTimerType;
    private String studyTimeText, shortBreakText, longBreakText;
    private RelativeLayout activityBackground;

    private static Integer studytime = 10000;        // 3 minutes
    private static Integer breaktime = 5000;
    private static Integer longbreaktime = 15000;
    private static Integer ticktime = 1000;         // 1 second interval
    private int counter = 1;
    private CounterClass WorkTimer, ShortBreakTimer, LongBreakTimer;
    private static MediaPlayer breakRingtone, workRingtone;

    private static void getSettingValues (Context context){

        globalVariables = context.getSharedPreferences(sharedPreferenceString, MODE_PRIVATE);
        studyTime = "StudyTimeVariable";
        shortBreak = "ShortBTimeVariable";
        longBreak = "LongBTimeVariable";

        int studyVar = globalVariables.getInt(studyTime, 25 );
        int sBreakVar = globalVariables.getInt(shortBreak, 5);
        int lBreakVar = globalVariables.getInt(longBreak, 15);

        studytime = studyVar * 60000;
        breaktime = sBreakVar * 60000;
        longbreaktime = lBreakVar * 60000;
    }

    public static void playBreakRingtone(Context context){
        /*
        breakRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtoneSound = RingtoneManager.getRingtone(getApplicationContext(), breakRingtone);
        if (ringtoneSound != null) {
            ringtoneSound.play();
        }
        */

        breakRingtone = MediaPlayer.create(context, R.raw.mario_break_ringtone);
        breakRingtone.start();
    }


    public void playWorkRingtone(){
        workRingtone = MediaPlayer.create(TimerActivity.this, R.raw.mario_break_ringtone);
        workRingtone.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        activityBackground = (RelativeLayout)findViewById(R.id.timerActivity);

        btnStart = (Button)findViewById(R.id.btnStart);
        //btnStop = (Button)findViewById(R.id.btnStop);
        btnFinish = (Button)findViewById(R.id.btnBack);
        textViewTimer = (TextView)findViewById(R.id.textViewTimer);
        textViewTimerType = (TextView)findViewById(R.id.timerCurrentStatus);
        //textDialog = (TextView)findViewById(R.id.textDialog);

        getSettingValues(getApplicationContext());


        //set string values to display initial timer text
        String initialTimerText = "";
        int initialHourText = studytime/60000;
        if (initialHourText < 10){
            initialTimerText = "0"+initialHourText;
        } else {
            initialTimerText = ""+initialHourText;
        }
        studyTimeText =initialTimerText + ":00";

        initialHourText = breaktime/60000;
        if (initialHourText < 10){
            initialTimerText = "0"+initialHourText;
        } else {
            initialTimerText = ""+initialHourText;
        }
        shortBreakText = initialTimerText + ":00";

        initialHourText = longbreaktime/60000;
        if (initialHourText < 10){
            initialTimerText = "0"+initialHourText;
        } else {
            initialTimerText = ""+initialHourText;
        }
        longBreakText = initialTimerText + ":00";


        textViewTimer.setText(studyTimeText);
        textViewTimerType.setText("Work Timer");



        startTimer(counter);

        //final CounterClass countertimer = new CounterClass(studytime,ticktime);


        //  Button Start

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((counter % 2) == 1) {
                    WorkTimer.start();
                } else if ((counter % 8) == 0 ) {
                    LongBreakTimer.start();
                } else if ((counter % 2) == 0 ){
                    ShortBreakTimer.start();
                }

            }
        });

        /*
        //Button Stop
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((counter % 2) == 1) {
                    WorkTimer.cancel();
                } else if ((counter % 8) == 0 ) {
                    LongBreakTimer.cancel();
                } else if ((counter % 2) == 0 ){
                    ShortBreakTimer.cancel();
                }
            }
        });
        */


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }



    public void startTimer (int counterID) {

        if ((counterID % 2) == 1) {
            //WorkTimer = new CounterClass(studytime, ticktime);
            WorkTimer = new CounterClass(8000, ticktime);
        } else if ((counterID % 8) == 0 ) {
            LongBreakTimer = new CounterClass(10000, ticktime);        //long break time

        } else if ((counterID % 2) == 0 ){
            ShortBreakTimer = new CounterClass(4000, ticktime);

        }



    }

    public void createBreakDialog (){
        FragmentManager fm = getFragmentManager();
        breakTimeDialog breakDialog = new breakTimeDialog();
        breakDialog.show(fm, "breakdialog");
    }

    public void createWorkDialog (){
        FragmentManager fm = getFragmentManager();
        workTimeDialog workDialog = new workTimeDialog();
        workDialog.show(fm, "workdialog");
    }


    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hourminsec = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));


/*
            String hourminsec = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
*/

            System.out.println(hourminsec);
            textViewTimer.setText(hourminsec);

        }

        //  1  2  3  4  5  6  7  8

        @Override
        public void onFinish() {
            if ((counter % 7) ==0 ) {
                textViewTimer.setText(longBreakText);
                createWorkDialog();
                textViewTimerType.setText("Long Break");
                counter++;
                activityBackground.setBackgroundResource(R.drawable.timer_break);
                startTimer(counter);

            } else if (counter % 2 == 1){
                textViewTimer.setText(shortBreakText);
                createBreakDialog();
                textViewTimerType.setText("Short Break");
                activityBackground.setBackgroundResource(R.drawable.timer_break);
                counter++;
                startTimer(counter);

            } else if ((counter % 2) == 0) {
                textViewTimer.setText(studyTimeText);
                createWorkDialog();
                textViewTimerType.setText("Work Timer");
                counter ++;
                activityBackground.setBackgroundResource(R.drawable.timer_size);
                startTimer(counter);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class breakTimeDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction

            breakRingtone = MediaPlayer.create(getActivity(), R.raw.mario_break_ringtone);
            breakRingtone.start();

            String alertMessage = "Good work! Take a twix";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(alertMessage)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast newtoast = Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT);
                            newtoast.show();
                            breakRingtone.stop();


                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    public static class workTimeDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction

            workRingtone = MediaPlayer.create(getActivity(), R.raw.mario_work_ringtone);
            workRingtone.start();

            String alertMessage = "Time to get back to work!";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(alertMessage)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast newtoast = Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT);
                            newtoast.show();
                            workRingtone.stop();


                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}
