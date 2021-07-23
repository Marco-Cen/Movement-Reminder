package com.example.cruddatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.HelperClass_InputRangeLimiter;
import com.example.movementreminder.PainFormActivity;
import com.example.movementreminder.R;
import com.example.setupdatabase.PainJointDataModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;

/* NOTE:
- 'Create' in CRUD
- Creates NEW entry in database (Needed in order to add NEW Joint Name that DIDNT exist in dropdown menu in pain form page)
- Includes Validation on input fields:
    - Joint Name field CANT be empty
- Referenced From "AddExerciseEntry.java" and "UpdateDeletePainJointsDatabase.java" & "PainFormActivity.java"
 */
public class AddJointNameEntry extends AppCompatActivity {

    //-- Instance Variables --
    private Realm realm;

    private EditText jointNameFieldInput, jointPainValueFieldInput, jointPainNotesFieldInput;
    private String jointName, jointPainValueString, jointPainNotesString; //jointPainValueString string is used for validation in if statement
    private int jointPainValue;

    private Calendar currentDateTime;
    private SimpleDateFormat dateFormat;
    private String dateRecordedString;

    private Button backBttn, addBttn, viewDbBttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_add_jointnamepain_entry);

        // initializing our edittext and buttons
        realm = Realm.getDefaultInstance();
        jointNameFieldInput = findViewById(R.id.addJointNameFieldID);
        jointPainValueFieldInput = findViewById(R.id.addJointPainRateFieldID);
        jointPainValueFieldInput.setFilters(new InputFilter[]{ new HelperClass_InputRangeLimiter("1", "100")}); // (Inclusive) Limits user input to range of 1-100
        jointPainNotesFieldInput = findViewById(R.id.addPainJointNoteField);

        // BACK BUTTON
        backBttn = findViewById(R.id.backToPainFormPageBttn);
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddJointNameEntry.this, PainFormActivity.class);
                startActivity(i);
            }
        });

        // VIEW DATABASE BUTTON
        viewDbBttn = findViewById(R.id.viewPainDatabaseBttnAddingPage); //for list of exercises popup and pain form input
        viewDbBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(AddJointNameEntry.this, ViewPainJointsDatabase.class);
                startActivity(i);
            }
        });

        // Add BUTTON
        addBttn = findViewById(R.id.addJointNameEntry);
        addBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting data from edittext fields.
                jointName = jointNameFieldInput.getText().toString();

                jointPainValueString = jointPainValueFieldInput.getText().toString();
                if(!TextUtils.isEmpty(jointPainValueString) ) { jointPainValue = Integer.parseInt(jointPainValueString); } //If jointPain value is not empty, convert string to int

                jointPainNotesString = jointPainNotesFieldInput.getText().toString();

                // Input Validation (validating the text fields if empty or not)
                if (TextUtils.isEmpty(jointName)) {
                    jointNameFieldInput.setError("Slow down! You forgot to put a JOINT NAME! \n (This'll be added into the dropdown menu on the Pain Form Page)");
                } else if (TextUtils.isEmpty(jointPainValueString)) {
                    jointPainValueFieldInput.setError("What's the severity of pain you feel in that joint? (From 1-100 %)");
                } else {
                    //Get current date upon SUBMISSION
                    currentDateTime = Calendar.getInstance(); //gets exact current date
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //format of date to set
                    dateRecordedString = dateFormat.format(currentDateTime.getTime()); //formats date accordingly

                    // calling method to ADD data to Realm database..
                    addDataToDatabase(jointName, jointPainValue, dateRecordedString, jointPainNotesString);

                    Toast.makeText(AddJointNameEntry.this, "New Joint/Pain Entry added to the database!", Toast.LENGTH_SHORT).show();

                    //Reset input fields after submission
                    jointNameFieldInput.setText("");
                    jointPainValueFieldInput.setText("");
                    jointPainNotesFieldInput.setText("");

                    Intent i = new Intent(AddJointNameEntry.this, PainFormActivity.class);
                    startActivity(i);
                }

            }
        });
    }


    //CREATE [in CRUD]
    private void addDataToDatabase(String jointName, int jointPainValue, String dateSubmitted, String NotesDescription) {
        // Creating variable for data model class.
        PainJointDataModel painJointDataModel = new PainJointDataModel();

        // Getting id for the exercise which we are storing.
        Number id = realm.where(PainJointDataModel.class).max("IDPain");
        long nextId; // creating a variable for our id.

        if (id == null) {   // validating if id is null or not.
            nextId = 1;
        } else {     //id != null
            nextId = id.intValue() + 1; // incrementing it by 1
        }

        // Setting the data entered by user in our modal class.
        painJointDataModel.setIDPain(nextId);
        painJointDataModel.setJointName(jointName);
        painJointDataModel.setPainRating(jointPainValue);
        painJointDataModel.setPainDateRecorded(dateSubmitted);
        painJointDataModel.setPainJointNotes(NotesDescription);

        // Calling a method to execute a transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Method to copy to realm database from our modal class.
                realm.copyToRealm(painJointDataModel);
            }
        });
    }
}