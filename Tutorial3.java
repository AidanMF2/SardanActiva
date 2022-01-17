package com.example.ushare.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ushare.MainActivity;
import com.example.ushare.R;
import com.example.ushare.tutorial.Tutorial2;

public class Tutorial3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial3);
    }
    public void Tutorial2 (View view){
        Intent tutorial = new Intent(this, Tutorial2.class);
        startActivity(tutorial);
        finish();
    }
    public void Acaba (View view){
        Intent tutorial = new Intent(this, MainActivity.class);
        startActivity(tutorial);
        finish();
    }

}