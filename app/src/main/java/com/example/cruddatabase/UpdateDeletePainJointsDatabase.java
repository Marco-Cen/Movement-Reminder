package com.example.cruddatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.HelperClass_InputRangeLimiter;
import com.example.movementreminder.R;
import com.example.setupdatabase.PainJointDataModel;

import io.realm.Realm;

/* NOTE:
- 'Update' and 'Delete' in CRUD
- This class allows user to EDIT or DELETE entry in database dynamically
- similar to "AddExerciseDatabase", (Based on CRUD_UpdateDeleteExercisesDatabase.java)
- UpdateCourseActivity
 */
public class UpdateDeletePainJointsDatabase extends AppCompatActivity {

    // Storing our values from edittext fields.
    private Realm realm;
    private TextView idDisplayField;
    private long id;
    private Button updateBtn, deletebttn, dontChangebttn;

    private EditText jointNameField, jointPainValueField; //creating variables for our edit text
    private String jointName, jointPainValueString;
    private int jointPainValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_updatedelete_painjoints_database);

        // initializing our edittext and buttons
        realm = Realm.getDefaultInstance();
        idDisplayField = findViewById(R.id.idPainField);
        jointNameField = findViewById(R.id.updateJointNameField);
        jointPainValueField= findViewById(R.id.updateJointPainValueField);

        updateBtn = findViewById(R.id.updateBttnUpdateJointPain);
        deletebttn = findViewById(R.id.deleteBttnJointPain);
        dontChangebttn = findViewById(R.id.dontChangeBttnUpdateJointPain);

        // on below line we are getting data which is passed from intent.
        id = getIntent().getLongExtra("id", 0);
        jointName = getIntent().getStringExtra("jointName");
        jointPainValue = getIntent().getIntExtra("jointPainLevel", 0); //'name' or the first arguemnt in .get__Extra() refers to the 'key', in 'key, value' pair so can be referenced in new activity sending to [Must use same key name here and new activity to use PASSED information between pages

        // on below line we are setting data in our edit test fields. (SETTING CONTENTS into input fields)
        idDisplayField.setText("[" + String.valueOf(id) + "]");
        jointNameField.setText(jointName);
        jointPainValueField.setText(String.valueOf(jointPainValue));
        jointPainValueField.setFilters(new InputFilter[]{ new HelperClass_InputRangeLimiter("1", "100")}); // (Inclusive) Limits user input to range of 1-100

        //TEST
        Toast.makeText(UpdateDeletePainJointsDatabase.this, "Looking into ID: [" + String.valueOf(id) + "]", Toast.LENGTH_SHORT).show();

        // 'DONT CHANGE' BUTTON
        dontChangebttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(UpdateDeletePainJointsDatabase.this, ViewPainJointsDatabase.class);
                startActivity(i);
            }
        });


        // 'DELETE' BUTTON
        deletebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-- Confirmation text before delete --
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDeletePainJointsDatabase.this);
                builder.setCancelable(true);
                builder.setTitle("DELETE CONFIRMATION");
                builder.setMessage("You sure you wanna DELETE this entry from the database? \n \t \t ID: [" + String.valueOf(id) + "] \n \t \t Joint Name: [" + jointName +"]");

                // 'CONFIRM' selected
                builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Calling a Helper function/method to delete
                        deleteCourse(id);
                        // after deleting we are displaying a toast message as course deleted.
                        Toast.makeText(UpdateDeletePainJointsDatabase.this, "DELETED From Database: \n \t \t ID: [" + String.valueOf(id) + "] \n \t \t Joint Name: [" + jointName +"] \n \n Hope ya meant to do that!", Toast.LENGTH_LONG).show();
                        // after that we are opening a new activity via an intent.
                        Intent i = new Intent(UpdateDeletePainJointsDatabase.this, ViewPainJointsDatabase.class);
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
                jointName = jointNameField.getText().toString();

                jointPainValueString = jointPainValueField.getText().toString();
                if(!TextUtils.isEmpty(jointPainValueString) ) { jointPainValue = Integer.parseInt(jointPainValueString); } //If jointPain value is not empty, convert string to int

                // Input Validation (validating the text fields if empty or not)
                if (TextUtils.isEmpty(jointName)) {
                    jointNameField.setError("Woah! You left the joint NAME blank! You need to know where the pain is coming from!");
                } else if (TextUtils.isEmpty(jointPainValueString)) {
                    jointPainValueField.setError("What's the severity of pain you feel in that joint? (From 1-100 %)");
                } else { //UPDATE
                    // on below line we are getting data from our modal where
                    // the id of the course equals to which we passed previously.
                    final PainJointDataModel painJointDataModel = realm.where(PainJointDataModel.class).equalTo("IDPain", id).findFirst();
                    updateCourse(painJointDataModel, jointName, jointPainValue);

                    // on below line we are displaying a toast message when course is updated.
                    Toast.makeText(UpdateDeletePainJointsDatabase.this, "UPDATED! \n \t \t ID: [" + String.valueOf(id) + "] \n \t \t Joint Name: [" + jointName + "]", Toast.LENGTH_LONG).show();

                    // on below line we are opening our activity for read course activity to view updated course.
                    Intent i = new Intent(UpdateDeletePainJointsDatabase.this, ViewPainJointsDatabase.class);
                    startActivity(i);
                    finish();
                }

            }
        });
    }


    //UPDATE FUNCTION
    private void updateCourse(PainJointDataModel painJointDataModel, String jointName, int jointPainValue) {

        // on below line we are calling
        // a method to execute a transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // on below line we are setting data to our modal class
                // which we get from our edit text fields.
                painJointDataModel.setJointName(jointName);
                painJointDataModel.setPainRating(jointPainValue);

                // inside on execute method we are calling a method to copy
                // and update to real m database from our modal class.
                realm.copyToRealmOrUpdate(painJointDataModel);
            }
        });
    }

    // DELETE FUNCTION
    private void deleteCourse(long id) {
        // Finding data from our modal class by comparing it with the course id.
        PainJointDataModel painJointDataModel = realm.where(PainJointDataModel.class).equalTo("IDPain", id).findFirst();
        // on below line we are executing a realm transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // on below line we are calling a method for deleting this course
                painJointDataModel.deleteFromRealm();
            }
        });
    }


}