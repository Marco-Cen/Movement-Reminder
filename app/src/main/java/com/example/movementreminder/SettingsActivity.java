package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("SETTINGS");

    }
}