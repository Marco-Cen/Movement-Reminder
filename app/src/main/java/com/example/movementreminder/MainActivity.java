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

public class MainActivity extends AppCompatActivity {

    //For Realm Sync
//    String MongoDBRealmAppID = "makermove_movementreminder-uyrap"; //Provided App ID in MongoDB Realm (Downloaded MongoDB Realm on GitHub Repo)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Hi Maddie!");


        //-- Linking Buttons to redirect to other pages --
        Button bttnPainForm;
        bttnPainForm = findViewById((R.id.PainFormBttn));
        bttnPainForm.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               //Functionality to open/redirect to (OTHER) PAGE
               openPainFormPage(); //Pain Form Page
           }
        });



        //////////// [START]  TEST: Linking Button to redirect to page
        Button bttnExerciseSelected;
        bttnExerciseSelected = findViewById((R.id.exerciseSelectedBttn));
        bttnExerciseSelected.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Functionality to open/redirect to (OTHER) PAGE
                testExerciseSelectedPage();
            }
        });

        Button bttnTestCRUD; //for list of exercises popup and pain form input
        bttnTestCRUD = (Button) findViewById((R.id.CRUDTestPage));
        bttnTestCRUD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Functionality to open/redirect to (OTHER) PAGE
                testCRUD();
            }
        });
        //////////// [End]

    }

    //-- Functionality to redirect to pain form page --
    public void openPainFormPage(){
        Intent intent = new Intent(this, PainFormActivity.class);
        startActivity(intent);
    }

    //////////// [START]  TEST: Linking Button to redirect to page
    //-- Functionality to redirect to Exercise Selected page (TEST) --
    public void testExerciseSelectedPage(){
        Intent intent = new Intent(this, ExerciseSelectedActivity.class);
        startActivity(intent);
    }
    //-- Functionality to redirect to Testing User Input CRUD page (TEST) --
    public void testCRUD(){
        Intent intent = new Intent(this, AddExerciseDatabase.class); //TestingDatabase, testingforuserinput
        startActivity(intent);
    }
    //////////// [End]


    ////-- Linking Settings Button from res > menu --
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