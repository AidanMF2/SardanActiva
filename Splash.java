package com.example.ushare.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.ushare.MainActivity;
import com.example.ushare.R;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);

        firstTime = sharedPreferences.getBoolean("firstTime",true);

        if(firstTime){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firstTime = false;
                    editor.putBoolean("firstTime",firstTime);
                    editor.apply();

                    Intent i = new Intent(Splash.this, Tutorial1.class);
                    startActivity(i);
                    finish();
                }
            },5000);
        }
        else{
            Intent i = new Intent(Splash.this, MainActivity.class);
            startActivity(i);
            finish();
        }




    }
}