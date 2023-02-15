package com.example.timeclock;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState)
            {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity)
            {

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
                saveMenu();
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
        setContentView(R.layout.activity_options);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    private void saveMenu()
    {

    }
}
