package com.example.ushare.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ushare.R;

public class Tutorial2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);
    }
    public void Tutorial1 (View view){
        Intent tutorial = new Intent(this, Tutorial1.class);
        startActivity(tutorial);
        finish();
    }
    public void Tutorial3 (View view){
        Intent tutorial = new Intent(this, Tutorial3.class);
        startActivity(tutorial);
        finish();
    }
}