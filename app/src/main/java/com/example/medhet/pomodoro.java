package com.example.medhet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class pomodoro extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private static final long START_TIME_IN_MS = 1500000;
    private static final long BREAK_TIME_IN_MS = 300000;
    private TextView txtCountDown;
    private TextView txtStatue;
    private Button btnStartPause;
    private Button btnReset;
    private ProgressBar progressBar;
    private float progress = 0;
    private MediaPlayer startVoice;
    private MediaPlayer breakVoice;
    private boolean firstStart = true;

    private CountDownTimer countDownTimer;

    private boolean timerRunning;

    private boolean working;
    private boolean afterReset = false;

    private long timeLiftInMS = START_TIME_IN_MS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);
       // getSupportActionBar().hide();
        drawerLayout = findViewById(R.id.drawer_layout3);

            txtCountDown = (TextView) findViewById(R.id.textCountDown);
            txtStatue = (TextView) findViewById(R.id.txtStatue);
            btnStartPause = (Button) findViewById(R.id.btnStartPause);
            btnReset = (Button) findViewById(R.id.btnReset);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            working = true;

            btnStartPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(afterReset) {
                        txtStatue.setText("Time to work");
                        firstStart = true;
                        afterReset = false;
                    }
                    if(timerRunning) {pauseTimer();}
                    else {
                        startTimer();
                        if(firstStart) {
                            playStartVoice();
                            firstStart = false;
                        }
                    }
                }
            });

            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTimer();
                }
            });
            updateCountDownTxt();
            updateProgressBar();
        }


        private void startTimer() {
            countDownTimer = new CountDownTimer(timeLiftInMS,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLiftInMS = millisUntilFinished;
                    updateCountDownTxt();
                    updateProgressBar();
                }

                @Override
                public void onFinish() {
                    if(working) {
                        working = false;
                        txtStatue.setText("Time to break!");
                        btnStartPause.setText("Pause");
                        timeLiftInMS = BREAK_TIME_IN_MS;
                        startTimer();
                        playBreakVoice();
                    }
                    else {
                        working = true;
                        txtStatue.setText("Back to work");
                        btnStartPause.setText("Pause");
                        timeLiftInMS = START_TIME_IN_MS;
                        startTimer();
                        playStartVoice();
                    }

                }
            }.start();
            timerRunning = true;
            btnStartPause.setText("Pause");
            btnReset.setVisibility(View.INVISIBLE);
        }

        private void pauseTimer() {
            countDownTimer.cancel();
            timerRunning = false;
            btnStartPause.setText("Start");
            btnReset.setVisibility(View.VISIBLE);

        }

        private void resetTimer() {
            timeLiftInMS = START_TIME_IN_MS;
            updateCountDownTxt();
            updateProgressBar();
            txtStatue.setText("Start & back to work!");
            btnReset.setVisibility(View.INVISIBLE);
            btnStartPause.setVisibility(View.VISIBLE);
            btnStartPause.setText("Start");
            afterReset = true;
        }

        private void updateCountDownTxt() {
            int minutes = (int) timeLiftInMS / 1000 / 60;
            int seconds = (int) (timeLiftInMS / 1000) % 60;

            String timeLiftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
            txtCountDown.setText(timeLiftFormatted);
        }

        private void updateProgressBar() {
            if (working) {
                progress = ((float) START_TIME_IN_MS - (float)timeLiftInMS) / (float) START_TIME_IN_MS *100;
            }
            else {
                progress = ((float) BREAK_TIME_IN_MS - (float)timeLiftInMS) / (float) BREAK_TIME_IN_MS *100;
            }
            progressBar.setProgress(Math.round(progress));
        }

        private void playStartVoice() {
            if(startVoice == null) {
                startVoice = MediaPlayer.create(getApplicationContext(),R.raw.start);
            }
            startVoice.start();
        }

        private void playBreakVoice() {
            if(breakVoice == null) {
                breakVoice = MediaPlayer.create(getApplicationContext(),R.raw.breakv);
            }
            breakVoice.start();
        }
    public void ClickMenu(View view)
    {
        //bug
        MainActivity.openDrawer(drawerLayout);

    }
    public void ClickLogo(View view)
    {
        MainActivity.closeDrawer(drawerLayout);
    }

    public void clickpomodoro(View view){
       recreate();
    }
    public void clickGpa(View view){
        MainActivity.redirectActivity(this,Gpa.class);
    }
    public void clickto_do(View view){
        MainActivity.redirectActivity(this,To_do.class);
    }
    public void clickCalender(View view){
        MainActivity.redirectActivity(this,Calendar.class);
    }
    public void clickLogOut(View view){
        MainActivity.logout(this);
    }

   @Override
    protected void onPause() {
        super.onPause();
       progressBar.setProgress(0);
        MainActivity.closeDrawer(drawerLayout);
    }


}

