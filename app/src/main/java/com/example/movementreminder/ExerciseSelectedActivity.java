package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/*
Functions:
- Countdown Timer:
   - Continues countdown timer when running app in background
   - Ability to Set/Start/Pause/Reset buttons of countdown timer
   - Orientation changes wont stop timer (Slight delay in time at most)
Note:
- May be very slight/subtle delay of countdown timer when change orientation of phone during countdown (few millisecond to max 1-2 second)
- Countdown Timer still works in bkgground of app WITHOUT using service
 */
public class ExerciseSelectedActivity extends AppCompatActivity {

    //-- Instance Variables for Countdown Timer --
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button mButtonSet; //User input set time (minutes) bttn
    private Button mButtonStartPause; //Start/Pause bttn
    private Button mButtonReset; //Reset bttn
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning; //if timer running or not
    private long mStartTimeInMillis; //Starting time for countdown timer (User input)
    private long mTimeLeftInMillis = mStartTimeInMillis;
    private long mEndTime; //FIXES issues of slight delay (Loss of seconds) when change orientation while countdown timer running


///////////////
    //------ TEMPORARY UNTIL PAIN FORM PAGE UI FINISHED ------
    TextView tvProgressLabel;

    //(SLIDER) on click
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            tvProgressLabel.setText("Progress: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
///////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selected);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Exercise Selected!");


///////////////
        //------ TEMPORARY UNTIL PAIN FORM PAGE UI FINISHED ------
        //get the spinner from the xml.
                Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
                String[] items = new String[]{"Arm", "LowerBack", "UpperBack"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
                dropdown.setAdapter(adapter);

        // (SLIDER) set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int painLevelInput = seekBar.getProgress();
        tvProgressLabel = findViewById(R.id.textView);
        tvProgressLabel.setText("Rate the Pain You Feel: " + painLevelInput + "%");
///////////////






        //-- Countdown timer --
        //Finds button on screen via their ID
        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        //Set Button Pressed
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(ExerciseSelectedActivity.this, "Heyo! Input set is EMPTY!", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(ExerciseSelectedActivity.this, "Oii! Its gotta be a positive number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Set time
                mStartTimeInMillis = millisInput;
                resetTimer();
                closeKeyboard();

                mEditTextInput.setText("");
            }
        });

        //Start Button Pressed
        mButtonStartPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mTimerRunning){

                    //Pauses Timer
                    mCountDownTimer.cancel();
                    mTimerRunning = false;
                    updateButtons();
                }
                else{ //If timer not running, START THE TIMER

                    //Start the timer
                    startTimer(); //Includes on finish as well

                    mTimerRunning = true;
                    updateButtons();
                }
            }
        });

        //Reset Button Pressed
        mButtonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //Reset Timer
                resetTimer();
            }
        });

    }




    //-- Helper Functions FOR ABOVE CODE --
    //Starts the Timer
    private void startTimer(){
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis; //Ensures we have consistent time when rotating/change orientation of device

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) { //(length in milisecton, value of interval to update onTick)
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished; //if cancel this countdown timer and create new one, start whereever we stopped

                //Updates Countdown text
                updateCountDownText();
            }

            //Once countdown timer finishes!!
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateButtons();

                //Popup of star rating
                Toast.makeText(ExerciseSelectedActivity.this, "Nice! -- Exercise Timer Finished! -- WhooHoo!", Toast.LENGTH_SHORT).show();
                Intent starPopupIntent = new Intent(getApplicationContext(), starRatingPopupActivity.class);
                startActivity(starPopupIntent);
            }
        }.start();  //ALlows for immediately start timer
    }
    //UpdateCountDownText
    private void updateCountDownText(){
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600; //No need for hours as unlikely use for that long
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600)  / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60; //What is left after 60 (Mod % shows whats left)
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);  // Takes minutes and seconds adn convert to time string
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        mTextViewCountDown.setText(timeLeftFormatted); //updates coundtown text
    }
    //UpdateButtons (Show which buttons when: visible/invisible)
    private void updateButtons(){
        if (mTimerRunning){
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
             mButtonReset.setVisibility(View.INVISIBLE);
             mButtonStartPause.setText("Pause");
        }
        else{ //Timer not running
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000){ //matches countdown interval (1sec)
                mButtonStartPause.setVisibility(View.INVISIBLE);
            }
            else{
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis){
                mButtonReset.setVisibility(View.VISIBLE);
            }
            else{
                mButtonReset.setVisibility(View.INVISIBLE); //Make reset button disappear after reset
            }
        }
    }
    //Reset Timer
    private void resetTimer(){
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText(); //Resets Text shown on screen as well
        updateButtons();
    }

    //-- FIX in order to have Countdown Timer still works/run in background of app WITHOUT use of services
    //When change orientation of phone, saves state of timer (FIXES ISSUE OF RESETTING TIMER WHEN CHANGE ORIENTATION OF PHONE)
    //(This replaces the previous 'onSaveInstanceState' method (But instead this method works in background of app as well))
    @Override
    protected void onStop() { //Note: string values here should be CONSTANTS, not hardcoded for better practice
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit(); //Saves our data
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    //Called After OnCreate method and CHECKS if there EXISTS an saved instance state, if not then create from beginning
    //(This replaces the previous 'onRestoreInstanceState' method (But instead this method works in background of app as well))
    @Override
    protected void onStart() { //Shows actual time thats left on screen of countdown timer
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 30000); //represents default value we see when we run app for the first time (can set to anything, doesnt matter)
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis(); //to fix delay and ensure in realtime
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false; //finish countdown timer
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

    //FIX: To Close keyboard AFTER USER INPUT 'SET TIMER' BUTTON
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}