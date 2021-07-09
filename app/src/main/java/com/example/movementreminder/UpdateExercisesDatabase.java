package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Database.ExerciseDataModel;
import com.example.Database.ViewDatabaseExercises;

import io.realm.Realm;

/* NOTE:
- UpdateCourseActivity
- similar to "AddExerciseDatabase"
 */
public class UpdateExercisesDatabase extends AppCompatActivity {

    // creating variables for our edit text
    private EditText exerciseNameField, exerciseDurationField, exerciseDescripField;

    // Storing our values from edittext fields.
    private String exerciseName, exerciseDurationString, exerciseDescription;
    private int exerciseDuration;
    private long id;
    private Button updateBtn, dontChangebttn;
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
        Toast.makeText(UpdateExercisesDatabase.this, "ID Selected is: [" + String.valueOf(id) + "]", Toast.LENGTH_SHORT).show();

        // 'DONT CHANGE' BUTTON
        dontChangebttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(UpdateExercisesDatabase.this, ViewDatabaseExercises.class);
                startActivity(i);
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

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(exerciseName)) {
                    exerciseNameField.setError("Please enter Exercise Name");
                } else if (TextUtils.isEmpty(exerciseDurationString)) {
                    exerciseDurationField.setError("Please enter Exercise Duration");
                } else { //UPDATE
                    // on below line we are getting data from our modal where
                    // the id of the course equals to which we passed previously.
                    final ExerciseDataModel exerciseDataModel = realm.where(ExerciseDataModel.class).equalTo("IDExercise", id).findFirst();
                    updateCourse(exerciseDataModel, exerciseName, exerciseDuration, exerciseDescription);
                }

                // on below line we are displaying a toast message when course is updated.
                Toast.makeText(UpdateExercisesDatabase.this, "Exercise Successfully Updated!", Toast.LENGTH_SHORT).show();

                // on below line we are opening our activity for read course activity to view updated course.
                Intent i = new Intent(UpdateExercisesDatabase.this, ViewDatabaseExercises.class);
                startActivity(i);
                finish();
            }
        });
    }

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


}