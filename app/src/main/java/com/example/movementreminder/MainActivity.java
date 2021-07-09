package com.example.movementreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Database.AddExerciseDatabase;
import com.example.Database.ViewDatabaseExercises;

public class MainActivity extends AppCompatActivity {

    //For Realm Sync
//    String MongoDBRealmAppID = "makermove_movementreminder-uyrap"; //Provided App ID in MongoDB Realm (Downloaded MongoDB Realm on GitHub Repo)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Hi Maddie!");


        // "VIEW DATABASE: Exercises" BUTTON
        Button viewDbBttn = findViewById(R.id.ViewExerciseDatabaseBttn); //for list of exercises popup and pain form input
        viewDbBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, ViewDatabaseExercises.class);
                startActivity(i);
            }
        });


        // "VIEW DATABASE: Pain" BUTTON



        //-- Linking Buttons to redirect to other pages --
        Button bttnPainForm;
        bttnPainForm = findViewById((R.id.PainFormBttn));
        bttnPainForm.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               //Functionality to open/redirect to (PAIN FORM) PAGE
               Intent intent = new Intent(MainActivity.this, PainFormActivity.class);
               startActivity(intent);
           }
        });


        //////////// [START]  TEST: Linking Button to redirect to page
        Button bttnExerciseSelected;
        bttnExerciseSelected = findViewById((R.id.exerciseSelectedBttn));
        bttnExerciseSelected.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Functionality to open/redirect to (ExerciseSelected) PAGE
                Intent intent = new Intent(MainActivity.this, ExerciseSelectedActivity.class);
                startActivity(intent);
            }
        });

        Button bttnTestCRUD; //for list of exercises popup and pain form input
        bttnTestCRUD = (Button) findViewById((R.id.CRUDTestPage));
        bttnTestCRUD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Functionality to open/redirect to (AddExercise) PAGE
                Intent intent = new Intent(MainActivity.this, AddExerciseDatabase.class); //TestingDatabase, testingforuserinput
                startActivity(intent);
            }
        });
        //////////// [End]

    }



    ////-- Linking Settings Button from res > menu -- [SETTINGS]
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
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

            return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    ////////



}