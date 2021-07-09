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

public class PainFormActivity extends AppCompatActivity {

    //-- Instance Variables --
    TextView painSliderText, jointDropDownText;
    Button backBttn;
    int painLevelInput;
    String dropdownSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pain_form);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Pain Form");

        // "Back" BUTTON
        backBttn = findViewById(R.id.backBttnPainPage);
        backBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(PainFormActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        //// -- JOINT DROPDOWN MENU -- <TODO>
        Spinner dropdown = findViewById(R.id.jointDropdownList);
        //create a list of items for the spinner.
        String[] items = new String[]{"Arm", "LowerBack", "UpperBack"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdownSelected = dropdown.getSelectedItem().toString();

        // Get Dropdown Input and Show Text Dynamically
//        String dropdownSelected = dropdown.getSelectedItem().toString();
//        jointDropDownText = findViewById(R.id.jointDropdownTxt);
//        painSliderText.setText("Which Joint: " + dropdownSelected);



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
            painSliderText.setText("Pain Severity in " +  dropdownSelected  + " is: " + painLevelInput + "%"); //Text shown when changed
        }
    };


}