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

import com.example.database.ExerciseDataModel;
import com.example.database.PainJointDataModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class PainFormActivity extends AppCompatActivity {

    //-- Instance Variables --
    private Realm realm;
    private Spinner jointDropdown;
    private List<PainJointDataModel> painJointDataModel;
    private String[] jointList;
    private TextView painSliderText;
    private Button backBttn;
    private int painLevelInput;
    private String jointDropDownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pain_form);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Pain Form");

        //-- Initialize Variables --
        realm = Realm.getDefaultInstance();
        painJointDataModel = new ArrayList<>();


        // "Back" BUTTON
        backBttn = findViewById(R.id.backBttnPainPage);
        backBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PainFormActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        //// -- JOINT DROPDOWN MENU --
        jointDropdown = findViewById(R.id.jointDropdownList);
        // Spinner Options (Get Pain/Joint List Data from Realm Database & Loop thru)
        painJointDataModel = realm.where(PainJointDataModel.class).findAll();
        //TODO (Setup PainJointDataModel in db & loop thru in dropdown menu)
        //TODO ('READ' options into database on specific field of 'jointName')
        //TODO ('add/create' entry into database of painJoint Model)


        jointList = new String[]{"Arm", "LowerBack", "UpperBack"}; //TEMPORARY

        //creates adapter (Describes how the items are displayed)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PainFormActivity.this, android.R.layout.simple_spinner_dropdown_item, jointList);
        //set the spinners adapter to the previously created one.
        jointDropdown.setAdapter(adapter);
        jointDropDownText = jointDropdown.getSelectedItem().toString();



        //// -- PAIN SLIDER -- (setted a change listener on the SeekBar)
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener); //calls helper function below
        // Get Slider Input and Show Text Dynamically
        painLevelInput = seekBar.getProgress();
        painSliderText = findViewById(R.id.painSliderTxt);
    }


    //-- Helper Function --
    // SLIDER FUNCTION
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            painLevelInput = seekBar.getProgress();
            painSliderText.setText("Changing Pain Severity to: " + painLevelInput + "%"); //Text shown when changed
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            painLevelInput = seekBar.getProgress();
            painSliderText.setText("Pain Severity in " +  jointDropDownText  + " is: " + painLevelInput + "%"); //Text shown when changed
        }
    };


}