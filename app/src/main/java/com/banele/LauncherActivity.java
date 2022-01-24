package com.banele;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.banele.WeatherApp.ui.main.MainActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int splash_time = 4000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LauncherActivity.this.startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                LauncherActivity.this.finish();
            }
        }, splash_time);setContentView(R.layout.activity_launcher);

    }
}