package com.example.cruddatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movementreminder.MainActivity;
import com.example.movementreminder.R;
import com.example.setupdatabase.ExerciseDataModel;

import io.realm.Realm;


/* NOTE:
- 'Create' in CRUD
- Includes Validation on input fields:
    - Exercise Name field CANT be empty
    - Exercise Duration field CANT be empty
 */
public class AddExerciseEntry extends AppCompatActivity {

    //-- Instance Variables --
    // For our edit text
    private EditText exerciseNameFieldInput, exerciseDescriptionFieldInput, exerciseDurationFieldInput;
    private Realm realm;
    // Strings for storing values from edittext fields.
    private String exerciseName, exerciseDescription, exerciseDurationString; //exerciseDurationString string is used for validation in if statement
    private int exerciseDuration;

    private Button backBttn;
    private Button viewDbBttn;
    private Button submitBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_add_exercise_entry);

        // initializing our edittext and buttons
        realm = Realm.getDefaultInstance();
        exerciseNameFieldInput = findViewById(R.id.addExerciseName);
        exerciseDurationFieldInput = findViewById(R.id.addExerciseDuration);
        exerciseDescriptionFieldInput = findViewById(R.id.addExerciseNote);

        // BACK BUTTON
        backBttn = findViewById(R.id.backBttnAddExercise);
        backBttn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(AddExerciseEntry.this, MainActivity.class); //TO BE REPLACED AFTER!
            startActivity(i);
        }
        });

        // VIEW DATABASE BUTTON
        viewDbBttn = findViewById(R.id.ViewExerciseDatabaseBttn); //for list of exercises popup and pain form input
        viewDbBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(AddExerciseEntry.this, ViewExercisesDatabase.class);
                startActivity(i);
            }
        });

        // SUBMIT BUTTON
        submitBttn = findViewById(R.id.submitBttnAddExercise);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting data from edittext fields.
                exerciseName = exerciseNameFieldInput.getText().toString();
                exerciseDurationString = exerciseDurationFieldInput.getText().toString(); //Duration
                if(!TextUtils.isEmpty(exerciseDurationString)) { exerciseDuration = Integer.parseInt(exerciseDurationString); } //If Duration is not empty, Duration convert string to int
                exerciseDescription = exerciseDescriptionFieldInput.getText().toString();

                // Validating the text fields if empty or not.
                if (TextUtils.isEmpty(exerciseName)) {
                    exerciseNameFieldInput.setError("Please enter Exercise Name");
                } else if (TextUtils.isEmpty(exerciseDurationString)) {
                    exerciseDurationFieldInput.setError("Please enter Exercise Duration");
                } else {
                    // calling method to ADD data to Realm database..
                    addDataToDatabase(exerciseName, exerciseDuration, exerciseDescription);
                    Toast.makeText(AddExerciseEntry.this, "Exercise added to the database!", Toast.LENGTH_SHORT).show();

                    //Reset input fields after submission
                    exerciseNameFieldInput.setText("");
                    exerciseDurationFieldInput.setText("");
                    exerciseDescriptionFieldInput.setText("");
                }
            }
        });
    }


    //CREATE [in CRUD]
    private void addDataToDatabase(String exerciseName, int exerciseDuration, String exerciseDescription) {
        // Creating variable for data model class.
        ExerciseDataModel exerciseDataModel = new ExerciseDataModel();

        // Getting id for the exercise which we are storing.
        Number id = realm.where(ExerciseDataModel.class).max("IDExercise");
        long nextId; // creating a variable for our id.

        if (id == null) {   // validating if id is null or not.
            nextId = 1;
        } else {     //id != null
            nextId = id.intValue() + 1; // incrementing it by 1
        }

        // Setting the data entered by user in our modal class.
        exerciseDataModel.setIDExercise(nextId);
        exerciseDataModel.setExerciseName(exerciseName);
        exerciseDataModel.setExerciseTimeRequired(exerciseDuration);
        exerciseDataModel.setExerciseNote(exerciseDescription);

        // Calling a method to execute a transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Method to copy to realm database from our modal class.
                realm.copyToRealm(exerciseDataModel);
            }
        });
    }
}