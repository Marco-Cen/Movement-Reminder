package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movementreminder.R;

import io.realm.Realm;

/* NOTE:
- This class allows user to EDIT or DELETE entry in database dynamically
- similar to "AddExerciseDatabase"
- UpdateCourseActivity
 */
public class UpdateExercisesDatabase extends AppCompatActivity {

    // creating variables for our edit text
    private EditText exerciseNameField, exerciseDurationField, exerciseDescripField;

    // Storing our values from edittext fields.
    private String exerciseName, exerciseDurationString, exerciseDescription;
    private int exerciseDuration;
    private long id;
    private Button updateBtn, deletebttn, dontChangebttn;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_exercises_database);

        // initializing our edittext and buttons
        realm = Realm.getDefaultInstance();
        exerciseNameField = findViewById(R.id.addExerciseName);
        exerciseDurationField = findViewById(R.id.addExerciseDuration);
        exerciseDescripField = findViewById(R.id.addExerciseNote);
        updateBtn = findViewById(R.id.updateBttnUpdateExercise);
        deletebttn = findViewById(R.id.deleteBttnExercise);
        dontChangebttn = findViewById(R.id.dontChangeBttnUpdateExercise);

        // on below line we are getting data which is passed from intent.
        exerciseName = getIntent().getStringExtra("exerciseName");
        exerciseDuration = getIntent().getIntExtra("exerciseDuration", 0);
        exerciseDescription = getIntent().getStringExtra("exerciseDescription");
        id = getIntent().getLongExtra("id", 0);

        // on below line we are setting data in our edit test fields.
        exerciseNameField.setText(exerciseName);
        exerciseDurationField.setText(String.valueOf(exerciseDuration));
        exerciseDescripField.setText(exerciseDescription);

        //TEST (working: "\n Duration is: [" + String.valueOf(exerciseDuration) )
        Toast.makeText(UpdateExercisesDatabase.this, "Looking into ID: [" + String.valueOf(id) + "]", Toast.LENGTH_SHORT).show();

        // 'DONT CHANGE' BUTTON
        dontChangebttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(UpdateExercisesDatabase.this, ViewDatabaseExercises.class);
                startActivity(i);
            }
        });


        // 'DELETE' BUTTON
        deletebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are calling a method to delete course.
                deleteCourse(id);
                // after deleting we are displaying a toast message as course deleted.
                Toast.makeText(UpdateExercisesDatabase.this, "DELETED From Database: \n \t \t ID: [" + String.valueOf(id) + "] \n \t \t Exercise Name: [" + exerciseName +"] \n \n Hope ya meant to do that!", Toast.LENGTH_LONG).show();
                // after that we are opening a new activity via an intent.
                Intent i = new Intent(UpdateExercisesDatabase.this, ViewDatabaseExercises.class);
                startActivity(i);
                finish();
            }
        });


        // 'UPDATE' BUTTON [adding on click listener for update button]
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                exerciseName = exerciseNameField.getText().toString();
                exerciseDurationString = exerciseDurationField.getText().toString(); //Duration
                if(!TextUtils.isEmpty(exerciseDurationString)) { exerciseDuration = Integer.parseInt(exerciseDurationString); } //If Duration is not empty, Duration convert string to int
                exerciseDescription = exerciseDescripField.getText().toString();

                // Input Validation (validating the text fields if empty or not)
                if (TextUtils.isEmpty(exerciseName)) {
                    exerciseNameField.setError("Woah! You gotta enter a NAME for your Exercise!");
                } else if (TextUtils.isEmpty(exerciseDurationString)) {
                    exerciseDurationField.setError("Enter the Exercise's DURATION in Minutes");
                } else { //UPDATE
                    // on below line we are getting data from our modal where
                    // the id of the course equals to which we passed previously.
                    final ExerciseDataModel exerciseDataModel = realm.where(ExerciseDataModel.class).equalTo("IDExercise", id).findFirst();
                    updateCourse(exerciseDataModel, exerciseName, exerciseDuration, exerciseDescription);
                }

                // on below line we are displaying a toast message when course is updated.
                Toast.makeText(UpdateExercisesDatabase.this, "[" + exerciseName + "] was UPDATED :)", Toast.LENGTH_SHORT).show();

                // on below line we are opening our activity for read course activity to view updated course.
                Intent i = new Intent(UpdateExercisesDatabase.this, ViewDatabaseExercises.class);
                startActivity(i);
                finish();
            }
        });
    }


    //UPDATE FUNCTION
    private void updateCourse(ExerciseDataModel exerciseDataModel, String exerciseName, int exerciseDuration, String exerciseDescrip) {

        // on below line we are calling
        // a method to execute a transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // on below line we are setting data to our modal class
                // which we get from our edit text fields.
                exerciseDataModel.setExerciseName(exerciseName);
                exerciseDataModel.setExerciseTimeRequired(exerciseDuration);
                exerciseDataModel.setExerciseNote(exerciseDescrip);

                // inside on execute method we are calling a method to copy
                // and update to real m database from our modal class.
                realm.copyToRealmOrUpdate(exerciseDataModel);
            }
        });
    }

    // DELETE FUNCTION
    private void deleteCourse(long id) {
        // Finding data from our modal class by comparing it with the course id.
        ExerciseDataModel exerciseDataModel = realm.where(ExerciseDataModel.class).equalTo("IDExercise", id).findFirst();
        // on below line we are executing a realm transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // on below line we are calling a method for deleting this course
                exerciseDataModel.deleteFromRealm();
            }
        });
    }


}