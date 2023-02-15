package com.example.timeclock;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity
{

    Button btnPunchIn;
    Button btnPunchOut;
    Button btnCancel;
    Button btnSave;
    Button btnShifts;
    Button btnClear;
    Button btnMenu;
    TextView txtPunchIn;
    TextView txtPunchOut;
    boolean isPunchedIn = false;
    LocalDateTime startTime;
    LocalDateTime quitTime;
    String strStartTime;
    String strQuitTime;
    EditText txtStopWatch;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy hh:mm:ss a");
    Thread thread = new Thread() {

        @Override
        public void run() {
            try {
                while (true) {
                    thread.sleep(100);
                    if (isPunchedIn)
                    {

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                LocalDateTime nowTime = LocalDateTime.now();
                                String strNowTime = formatter.format(nowTime);
                                TimeDifference timeDifference = new TimeDifference();
                                timeDifference.FindDifferenceInTimes(strStartTime, strNowTime);
                                txtStopWatch.setText(timeDifference.getStrDuration());
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                e.getCause();
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        btnPunchIn = findViewById(R.id.btnPunchIn);
        btnPunchOut = findViewById(R.id.btnPunchOut);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnClear = findViewById(R.id.btnClear);
        btnShifts = findViewById(R.id.btnShifts);
        btnMenu = findViewById(R.id.btnMenu);
        txtPunchIn = findViewById(R.id.txtPunchIn);
        txtPunchOut = findViewById(R.id.txtPunchOut);
        txtStopWatch = findViewById(R.id.txtStopWatch);
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks()
        {
            final SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            LocalDateTime savedTime;

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState)
            {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity)
            {
                isPunchedIn = sharedPreferences.getBoolean("isPunchedIn", false);
                if (isPunchedIn)
                {
                    startTime = LocalDateTime.parse(sharedPreferences.getString("PunchTime", null));
                    strStartTime = formatter.format(startTime);
                    txtPunchIn.setText(strStartTime);
                    txtPunchIn.setVisibility(View.VISIBLE);
                    txtPunchOut.setVisibility(View.INVISIBLE);
                    txtStopWatch.setVisibility(View.VISIBLE);
                    btnPunchIn.setEnabled(false);
                    btnPunchOut.setEnabled(true);
                }

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity)
            {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity)
            {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity)
            {
                if (isPunchedIn)
                {
                    savedTime = startTime;
                    editor.putBoolean("isPunchedIn", true);
                    editor.putString("PunchTime", savedTime.toString());
                    editor.apply();
                    Toast.makeText(getBaseContext(), "Punch In Time Saved", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    editor.putBoolean("isPunchedIn", false);
                    editor.remove("PunchTime");
                    editor.commit();
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState)
            {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity)
            {

            }
        });

        btnSave.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnClear.setVisibility(View.INVISIBLE);
        txtPunchIn.setVisibility(View.INVISIBLE);
        txtPunchOut.setVisibility(View.INVISIBLE);
        btnPunchOut.setEnabled(false);
        txtStopWatch.setVisibility(View.INVISIBLE);
        thread.start();

        btnPunchIn.setOnClickListener(v ->
        {
            isPunchedIn = true;
            startTime = LocalDateTime.now();
            strStartTime = formatter.format(startTime);
            txtPunchIn.setText(strStartTime);
            txtPunchIn.setVisibility(View.VISIBLE);
            txtPunchOut.setVisibility(View.INVISIBLE);
            txtStopWatch.setVisibility(View.VISIBLE);
            btnPunchIn.setEnabled(false);
            btnPunchOut.setEnabled(true);
            btnClear.setVisibility(View.VISIBLE);
        });

        btnPunchOut.setOnClickListener(v ->
        {
            isPunchedIn = false;
            quitTime = LocalDateTime.now();
            txtPunchOut.setVisibility(View.VISIBLE);
            strQuitTime = formatter.format(quitTime);
            txtPunchOut.setText(strQuitTime);
            btnPunchOut.setEnabled(false);
            btnPunchIn.setEnabled(false);
            btnCancel.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            btnClear.setVisibility(View.VISIBLE);
        });

        btnSave.setOnClickListener(v ->
        {
            String strSaveTime = (strStartTime + " - " + strQuitTime + "\n");
            try
            {
                FileOutputStream fosWriter = openFileOutput("WorkDays.txt", MODE_APPEND);
                fosWriter.write(strSaveTime.toUpperCase().getBytes());
                fosWriter.close();
                Toast.makeText(getBaseContext(), "Time Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            btnPunchIn.setEnabled(true);
            btnSave.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
            btnClear.setVisibility(View.INVISIBLE);
            txtPunchOut.setVisibility(View.INVISIBLE);
            txtPunchIn.setVisibility(View.INVISIBLE);
            txtStopWatch.setVisibility(View.INVISIBLE);
        });

        btnCancel.setOnClickListener(v ->
        {
            isPunchedIn = true;
            btnPunchOut.setEnabled(true);
            txtPunchOut.setVisibility(View.INVISIBLE);
            btnSave.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
            btnClear.setVisibility(View.VISIBLE);
        });

        btnClear.setOnClickListener(v ->
        {
            startTime = null;
            isPunchedIn = false;
            btnPunchIn.setEnabled(true);
            btnSave.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
            btnClear.setVisibility(View.INVISIBLE);
            txtPunchOut.setVisibility(View.INVISIBLE);
            txtPunchIn.setVisibility(View.INVISIBLE);
            txtStopWatch.setVisibility(View.INVISIBLE);
        });

        btnShifts.setOnClickListener(v ->
        {
            Intent i = new Intent(getApplicationContext(), TimeTableActivity.class);
            startActivity(i);
        });

        btnMenu.setOnClickListener(v ->
        {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
        });
    }
}