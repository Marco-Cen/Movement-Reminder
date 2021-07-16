package com.example.cruddatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movementreminder.R;
import com.example.setupdatabase.ExerciseDataModel;

import io.realm.Realm;

/* NOTE:
- 'Update' and 'Delete' in CRUD
- This class allows user to EDIT or DELETE entry in database dynamically
- similar to "AddExerciseDatabase"
- UpdateCourseActivity
 */
public class UpdateDeleteExercisesDatabase extends AppCompatActivity {

    // Storing our values from edittext fields.
    private Realm realm;
    private TextView idDisplayField;
    private long id;
    private Button updateBtn, deletebttn, dontChangebttn;

    private EditText exerciseNameField, exerciseDurationField, exerciseDescripField;    // creating variables for our edit text
    private String exerciseName, exerciseDurationString, exerciseDescription; //exerciseDurationString is for validation
    private int exerciseDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_updatedelete_exercises_database);

        // initializing our edittext and buttons
        realm = Realm.getDefaultInstance();
        idDisplayField = findViewById(R.id.idExerciseFieldInUpdatePage);
        exerciseNameField = findViewById(R.id.addExerciseName);
        exerciseDurationField = findViewById(R.id.addExerciseDuration);
        exerciseDescripField = findViewById(R.id.addExerciseNote);
        updateBtn = findViewById(R.id.updateBttnUpdateExercise);
        deletebttn = findViewById(R.id.deleteBttnExercise);
        dontChangebttn = findViewById(R.id.dontChangeBttnUpdateExercise);

        // on below line we are getting data which is passed from intent.
        id = getIntent().getLongExtra("id", 0);
        exerciseName = getIntent().getStringExtra("exerciseName"); //'name' or the first arguemnt in .get__Extra() refers to
        exerciseDuration = getIntent().getIntExtra("exerciseDuration", 0);
        exerciseDescription = getIntent().getStringExtra("exerciseDescription");

        // on below line we are setting data in our edit test fields.
        idDisplayField.setText("[" + String.valueOf(id) + "]");
        exerciseNameField.setText(exerciseName);
        exerciseDurationField.setText(String.valueOf(exerciseDuration));
        exerciseDescripField.setText(exerciseDescription);

        //TEST (working: "\n Duration is: [" + String.valueOf(exerciseDuration) )
        Toast.makeText(UpdateDeleteExercisesDatabase.this, "Looking into ID: [" + String.valueOf(id) + "]", Toast.LENGTH_SHORT).show();

        // 'DONT CHANGE' BUTTON
        dontChangebttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(UpdateDeleteExercisesDatabase.this, ViewExercisesDatabase.class);
                startActivity(i);
            }
        });


        // 'DELETE' BUTTON
        deletebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-- Confirmation text before delete --
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDeleteExercisesDatabase.this);
                builder.setCancelable(true);
                builder.setTitle("DELETE CONFIRMATION");
                builder.setMessage("You sure you wanna DELETE this entry from the database? \n \t \t ID: [" + String.valueOf(id) + "] \n \t \t Exercise Name: [" + exerciseName +"]");

                // 'CONFIRM' selected
                builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Calling a Helper function/method to delete
                            deleteCourse(id);
                            // after deleting we are displaying a toast message as course deleted.
                            Toast.makeText(UpdateDeleteExercisesDatabase.this, "DELETED From Database: \n \t \t ID: [" + String.valueOf(id) + "] \n \t \t Exercise Name: [" + exerciseName +"] \n \n Hope ya meant to do that!", Toast.LENGTH_LONG).show();
                            // after that we are opening a new activity via an intent.
                            Intent i = new Intent(UpdateDeleteExercisesDatabase.this, ViewExercisesDatabase.class);
                            startActivity(i);
                            finish();
                        }
                    });

                // 'CANCEL' selected
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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

                    // on below line we are displaying a toast message when course is updated.
                    Toast.makeText(UpdateDeleteExercisesDatabase.this, "UPDATED! \n \t \t ID: [" + String.valueOf(id) + "] \n \t \t Exercise Name: [" + exerciseName + "]", Toast.LENGTH_LONG).show();

                    // on below line we are opening our activity for read course activity to view updated course.
                    Intent i = new Intent(UpdateDeleteExercisesDatabase.this, ViewExercisesDatabase.class);
                    startActivity(i);
                    finish();
                }
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