package com.example.movementreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.NotificationHelper;

public class SettingsActivity extends AppCompatActivity {

    NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notificationHelper = new NotificationHelper(this);

        //setting up the Switch for overall Notification
        Switch switchNotification = findViewById(R.id.NotificationsOverall);
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getApplicationContext(),"notifications on",Toast.LENGTH_SHORT).show();
                    notificationHelper.sendHighPriorityNotification("Movement Reminder","Lest move some joints",MainActivity.class);
                }
            }

        });


        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("SETTINGS");


    }

    ////-- Linking Settings Button to go BACK to Home page ( res > menu )--
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }
    //What to do when select settings menu button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1: //This is all options within settings menu
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    ////////
}