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

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainActivity extends AppCompatActivity {

    String MongoDBRealmAppID = "makermove_movementreminder-uyrap"; //Provided App ID in MongoDB Realm (Downloaded MongoDB Realm on GitHub Repo)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //MongoDB REALM Setup
        Realm.init(this); //Initializes Realm [MUST be placed whereever application first opens: landing page]
        App mongoRealmAppSync = new App(new AppConfiguration.Builder(MongoDBRealmAppID).build()); //This creates instance of REALM application in Android Studio (Linked with ONLINE realm created)




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


        //-- TEST: Linking Button to redirect to page
        Button bttnExerciseSelected;
        bttnExerciseSelected = findViewById((R.id.exerciseSelectedBttn));
        bttnExerciseSelected.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Functionality to open/redirect to (OTHER) PAGE
                testExerciseSelectedPage();
            }
        });

    }

    //-- Functionality to redirect to pain form page --
    public void openPainFormPage(){
        Intent intent = new Intent(this, PainFormActivity.class);
        startActivity(intent);
    }
    //-- Functionality to redirect to Exercise Selected page (TEST) --
    public void testExerciseSelectedPage(){
        Intent intent = new Intent(this, ExerciseSelectedActivity.class);
        startActivity(intent);
    }


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