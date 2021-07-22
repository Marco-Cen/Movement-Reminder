package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cruddatabase.AddExerciseEntry;
import com.example.cruddatabase.AddJointNameEntry;
import com.example.cruddatabase.ViewExercisesDatabase;
import com.example.cruddatabase.ViewPainJointsDatabase;
import com.example.setupdatabase.ExerciseDataModel;
import com.example.setupdatabase.PainJointDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

public class Exercises extends AppCompatActivity {

    //-- Instance Variables --
    private Realm realm;
    private List<ExerciseDataModel> exerciseDataModel;
    private ArrayList<String> exerciseNameList;

    private ArrayAdapter<String> dropdownAdapter; //(Describes how the items are displayed)
    private Spinner exerciseNameDropdownField;
    private String exerciseNameSelected; //String for storing values from input field
    private TextView emptySelectErrorMsg;


    private Button addEntryBttn, backBttn, viewDbBttn, submitBttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Exercises");

        //-- Initialize Realm --
        realm = Realm.getDefaultInstance();

        // "Add Joint Name" BUTTON
        addEntryBttn = findViewById(R.id.addNewExerciseEntryBttn);
        addEntryBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Exercises.this, AddExerciseEntry.class);
                startActivity(i);
            }
        });

        // "Back" BUTTON
        backBttn = findViewById(R.id.backBttnExercisePage);
        backBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Exercises.this, PainFormActivity.class);
                startActivity(i);
            }
        });

        // "Next" BUTTON
        backBttn = findViewById(R.id.nextBttnExercise);
        backBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Exercises.this, ExerciseSelectedActivity.class);
                startActivity(i);
            }
        });

        // VIEW DATABASE BUTTON
        viewDbBttn = findViewById(R.id.ViewExerciseDatabaseBttn); //for list of exercises
        viewDbBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Exercises.this, ViewExercisesDatabase.class);
                startActivity(i);
            }
        });

        //// -- Populate exercise DROPDOWN MENU (Spinner) --
        exerciseNameDropdownField = findViewById(R.id.exerciseDropdownList);

        // Spinner Options (Get ALL entries in ExerciseDataModel Realm Db and put in list)
        exerciseDataModel = realm.where(ExerciseDataModel.class).findAll();

        exerciseNameList = new ArrayList<String>();
        exerciseNameList.add(""); //Default initial value selected in dropdown menu
        for (ExerciseDataModel entry : exerciseDataModel){

            //ONLY adds DISTINCT Exercise Names to dropdown menu
            if (!exerciseNameList.contains(entry.getExerciseName())){exerciseNameList.add(entry.getExerciseName());}
        }

        //creates adapter (Describes how the items are displayed)
        dropdownAdapter = new ArrayAdapter<>(Exercises.this, android.R.layout.simple_spinner_dropdown_item, exerciseNameList);

        //set the spinners adapter to the previously created one.
        exerciseNameDropdownField.setAdapter(dropdownAdapter);







    }
}