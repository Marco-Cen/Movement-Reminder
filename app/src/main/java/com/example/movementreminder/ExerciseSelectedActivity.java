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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

/*
Functions:
- Countdown Timer:
   - Continues countdown timer when running app in background
   - Ability to Set/Start/Pause/Reset buttons of countdown timer
   - Orientation changes wont stop timer (Slight delay in time at most)
Note:
- May be very slight/subtle delay of countdown timer when change orientation of phone during countdown (few millisecond to max 1-2 second)
- Countdown Timer still works in bkgground of app WITHOUT using service
- Need to Manually SET time bc wasnt able to auto set on landing of page (Solution: Show time required below: show exercise entry selected and its data)
    - Initial STARTUP on landing page, not able to SET initial time on timer?? (Alt solution if hardcode int value, NOT pass intent value obtained from past activity: When reset timer, sets dynamically to selected exercise -- view TODO: (Code comment below)????
 */
public class ExerciseSelectedActivity extends AppCompatActivity {

    //-- Instance Variables for Countdown Timer --
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button skipExerciseBttn;
    private Button mButtonSet; //User input set time (minutes) bttn
    private Button mButtonStartPause; //Start/Pause bttn
    private Button mButtonReset; //Reset bttn
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning; //if timer running or not
    private long mStartTimeInMillis; //Starting time for countdown timer (User input)
    private long mTimeLeftInMillis = mStartTimeInMillis;
    private long mEndTime; //FIXES issues of slight delay (Loss of seconds) when change orientation while countdown timer running

    long exerciseId;
    String exerciseName;
    int exerciseTimeReq;
    String exerciseNote;
    TextView IDExerciseSelectedField, exerciseSelectedNameField, exerciseSelectedDurationField, exerciseSelectedDescriptionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_selected);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Exercise Selected!");

        //-- Retrieve info sent from intent in previous page file of: 'ExerciseListActivity.java'
         exerciseId = getIntent().getLongExtra("IDExerciseKey", 0);
         exerciseName = getIntent().getStringExtra("exerciseNameKey");
         exerciseTimeReq = getIntent().getIntExtra("exerciseDurationKey", 0);
         exerciseNote = getIntent().getStringExtra("exerciseNoteKey");

         //Find text fields to set text
        IDExerciseSelectedField = findViewById(R.id.dynamicIDShow);
        exerciseSelectedNameField = findViewById(R.id.dynamicExerciseNameTxt);
        exerciseSelectedDurationField = findViewById(R.id.dynamicExerciseDurationTxt);
        exerciseSelectedDescriptionField = findViewById(R.id.dynamicExerciseDescriptionTxt);

        // Set data passed from previous activity (passed intent) to show to user
        IDExerciseSelectedField.setText("[ID]: " + String.valueOf(exerciseId));
        exerciseSelectedNameField.setText("[Name]: " +  exerciseName);
        exerciseSelectedDurationField.setText("[Time Required]: " + String.valueOf(exerciseTimeReq) + " Minute(s)");
        exerciseSelectedDescriptionField.setText("[Note]: " + exerciseNote);



        // "Skip Exercise" BUTTON
        skipExerciseBttn = findViewById(R.id.skipExerciseBttn);
        skipExerciseBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //TODO: Count amount of times skipped?


                Intent i = new Intent(ExerciseSelectedActivity.this, ExerciseListActivity.class);
                startActivity(i);
            }
        });


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


    //-- Built in functions override --
    //Called After OnCreate method and CHECKS if there EXISTS an saved instance state, if not then create from beginning
    //(This replaces the previous 'onRestoreInstanceState' method (But instead this method works in background of app as well))
    @Override
    protected void onStart() { //Shows actual time thats left on screen of countdown timer
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 60000); //(can set to anything, doesnt matter)
//        mStartTimeInMillis = Long.valueOf(exerciseTimeReq); //TODO: (Alt solution to set timer according to exercise selected -- Need to RESET timer in order to show BUT ONLY WORKS if HARDCODE VALUE (Doesnt work if send in variable of passed intent from last activity?!

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

                Toast.makeText(ExerciseSelectedActivity.this, "Nice! -- Exercise Timer Finished! -- WhooHoo!", Toast.LENGTH_SHORT).show();  //Msg shown to user

                //TODO: NEED OBAIDA'S PAGE TO GET EXERCISE SELECTED INTENT INFO
                Intent starPopupIntent = new Intent(getBaseContext(), starRatingPopupActivity.class);

                //TEST: (TESTING DATA bc waiting on Obaida)
//                RealmList<Float> exampleTestRatingsList = new RealmList<Float> (Arrays.asList("2.00f", "2.00f", "2.00f"));  //{ 2.00f, 2.00f, 2.00f};
//                ArrayList<Float> exampleTestRatingsList = new ArrayList<>(Arrays.asList(2.00f, 2.00f, 2.00f));

                // Passing all the data to new activity. (https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application#:~:text=The%20easiest%20way%20to%20do,sessionId)%3B%20startActivity(intent)%3B)
//                starPopupIntent.putExtra("IDExerciseKey", 3); //curently using testing data in arguements
//                starPopupIntent.putExtra("exerciseAllRatingsKey", 2.00f); //curently using testing data in arguements

                // Starting a new activity with Info passed in
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


    //FIX: To Close keyboard AFTER USER INPUT 'SET TIMER' BUTTON
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}