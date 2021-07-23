package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cruddatabase.AddJointNameEntry;
import com.example.cruddatabase.ViewPainJointsDatabase;
import com.example.setupdatabase.PainJointDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

/* NOTE:
- 'Create' in CRUD (Second alteration if JOINT NAME already exists)
- 'Submit' button enters new entry into database ('next' just goes to list of exercises)
- Input Validation:
    - Dropdown Selected CANT be empty
 */
public class PainFormActivity extends AppCompatActivity {

    //-- Instance Variables --
    private Realm realm;
    private List<PainJointDataModel> painJointDataModel;
    private ArrayList<String> jointNameList;

    private ArrayAdapter<String> dropdownAdapter; //(Describes how the items are displayed)
    private Spinner jointNameDropdownField;
    private String jointNameSelected; //String for storing values from input field
    private TextView emptySelectErrorMsg;

    private TextView painSliderText;
    private SeekBar painSliderField;
    private int jointPainValue; //Get numerical input that will then be converted to string

    private Calendar currentDateTime;
    private SimpleDateFormat dateFormat;
    private String dateRecordedString;

    private Button addEntryBttn, backBttn, viewDbBttn, submitBttn, button_selectionExercise;;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pain_form_crud_add2_painjoint);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Pain Form");

        //-- Initialize Realm --
        realm = Realm.getDefaultInstance();


        // "Add Joint Name" BUTTON
        addEntryBttn = findViewById(R.id.addNewPainJointEntryBttn);
        addEntryBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PainFormActivity.this, AddJointNameEntry.class);
                startActivity(i);
            }
        });

        // "Back" BUTTON
        backBttn = findViewById(R.id.backBttnPainPage);
        backBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PainFormActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        // VIEW DATABASE BUTTON
        viewDbBttn = findViewById(R.id.ViewPainDatabaseBttn); //for list of exercises popup and pain form input
        viewDbBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PainFormActivity.this, ViewPainJointsDatabase.class);
                startActivity(i);
            }
        });

        /// Next button
        button_selectionExercise = findViewById((R.id.nextBttnPain));
        button_selectionExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PainFormActivity.this, ExerciseListActivity.class);
                startActivity(intent);
            }
        });


        //// -- Populate JOINT DROPDOWN MENU (Spinner) --
        jointNameDropdownField = findViewById(R.id.jointDropdownList);

        // Spinner Options (Get ALL entries in PainJointDataModel Realm Db and put in list)
        painJointDataModel = realm.where(PainJointDataModel.class).findAll();

        jointNameList = new ArrayList<String>();
        jointNameList.add(""); //Default initial value selected in dropdown menu
        for (PainJointDataModel entry : painJointDataModel){

            //ONLY adds DISTINCT Joint Names to dropdown menu
            if (!jointNameList.contains(entry.getJointName())) {jointNameList.add(entry.getJointName());}
        }

        //creates adapter (Describes how the items are displayed)
        dropdownAdapter = new ArrayAdapter<>(PainFormActivity.this, android.R.layout.simple_spinner_dropdown_item, jointNameList);

        //set the spinners adapter to the previously created one.
        jointNameDropdownField.setAdapter(dropdownAdapter);



        //// -- PAIN SLIDER -- (setted a change listener on the SeekBar)
        painSliderText = findViewById(R.id.painSliderTxt); //To dynamically change instructions text
        painSliderField = findViewById(R.id.painSlider);
        painSliderField.setOnSeekBarChangeListener(seekBarChangeListener); //HELPER FUNCTION Below



        // SUBMIT Button
        submitBttn = findViewById(R.id.submitBttnPainJointDataEntry);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jointNameSelected = jointNameDropdownField.getSelectedItem().toString(); // Get Data from input field

                // To ensure joint name is NOT null when adding INTO database
                if(TextUtils.isEmpty(jointNameSelected)) {
                    emptySelectErrorMsg = (TextView) jointNameDropdownField.getSelectedView();
                    emptySelectErrorMsg.setError("Dummy text just so icon is added to spinner menu");
                    emptySelectErrorMsg.setTextColor(Color.RED); //Highlight error
                    emptySelectErrorMsg.setText("<< No JOINT NAME Selected! Select OR Add One! >>"); //Changes the selected item text
                }
                else{
                    jointPainValue = painSliderField.getProgress(); // Get Data from input field

                    // Get current date's time on submit
                    currentDateTime = Calendar.getInstance(); //gets exact current date
                    dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //format of date to set
                    dateRecordedString = dateFormat.format(currentDateTime.getTime()); //formats date accordingly
//                Toast.makeText(PainFormActivity.this, "Current Date: " + dateRecordedString, Toast.LENGTH_SHORT).show();  //TEST to see

                    // calling method to ADD data to Realm database..
                    addPainJointDataToDatabase(dateRecordedString, jointNameSelected, jointPainValue);
                    Toast.makeText(PainFormActivity.this, "Joint and Pain Data added to the database!", Toast.LENGTH_SHORT).show();

                    //Reset input fields after submission
                    jointNameDropdownField.setSelection(0);
                }

            }
        });

    }




    //-- Helper Function --

    // SLIDER FUNCTION
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            jointPainValue = seekBar.getProgress();
            painSliderText.setText("Changing Pain Severity to: " + jointPainValue + "%"); //Text shown when changed
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            jointNameSelected = jointNameDropdownField.getSelectedItem().toString(); // Get Data from input field
            jointPainValue = seekBar.getProgress();
            painSliderText.setText("Pain Severity in " +  jointNameSelected  + " is: " + jointPainValue + "%"); //Text shown when changed
        }
    };


    //CREATE [in CRUD] FUNCTION
    private void addPainJointDataToDatabase(String dateSubmitted, String jointName, int jointPain) {
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
        painJointDataModel.setPainDateRecorded(dateSubmitted);
        painJointDataModel.setJointName(jointName);
        painJointDataModel.setPainRating(jointPain);

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