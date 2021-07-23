package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;

import com.example.setupdatabase.ExerciseDataModel;
import com.example.setupdatabase.PainJointDataModel;

import java.util.ArrayList;

import io.realm.Realm;

/* NOTE:
- Suppose to be: (User Rating put into database is the AVERAGE of all submitted data under that exercise entry)
    BUT Realm DB doesnt support list types so not able to
*/
public class starRatingPopupActivity extends Activity { //changed extends 'AppCompatActivity'

    private Realm realm;
    private Button bttn_skipRating;
    private Button bttn_submitRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_rating_popup);

        realm = Realm.getDefaultInstance();

        //-- Ratings Bar Input (5 stars: Returns Float value)
        RatingBar RateBarInput = (RatingBar) findViewById(R.id.ratingBar);
        Float userRating = RateBarInput.getRating(); // get rating number from a rating bar

        //-- Retrieve info sent from intent in previous page file of: 'ExerciseSelectedActivity.java'
//        long exerciseId = getIntent().getLongExtra("IDExerciseKey", 0);
//        float exerciseAllRatings = getIntent().getFloatExtra("exerciseAllRatingsKey", 0);

        //-- (Was suppose to be for avg rating of starrating of that exercies but list type cant be carried over)
//        //Convert Arraylist<String> to FLoat[]
//        float [] convertRatingsListToFloat = new float[exerciseAllRatings.size()];
//        for (int i = 0; i < exerciseAllRatings.size(); i++) {
//            convertRatingsListToFloat[i] = Float.parseFloat(exerciseAllRatings.get(i));
//        }
//        //Getting Exercise ratings COUNT/SIZE and NEW AVERAGE value!
//        float sumOfAllRatings = 0;
//        int countAllRatings = 0;
//        for (float rating: convertRatingsListToFloat){
//            sumOfAllRatings = sumOfAllRatings + rating;
//            countAllRatings = countAllRatings + 1;
//        }
//        float newAvgRatingsResult = (sumOfAllRatings + userRating) / (countAllRatings + 1); //NEW AVERAGE CALCULATED WITH NEW INPUT RATING


        //-- Buttons on popup --
        // SKIP BUTTON
        bttn_skipRating = (Button) findViewById(R.id.skipRatingBttn);
        bttn_skipRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //Functionality to open/redirect to List of Exercises PAGE
                Intent intent = new Intent(starRatingPopupActivity.this, ExerciseListActivity.class);
                startActivity(intent);
            }
        });

        // SUBMIT BUTTON (Submit rating into database for that specific exercise (Use info from intent passed from prevous page to add into database))
        bttn_submitRating = (Button) findViewById(R.id.submitBttnStarRate);
        bttn_submitRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                //Locate that specific exercise entry in database
//                final ExerciseDataModel exerciseDataModel = realm.where(ExerciseDataModel.class).equalTo("IDExercise", exerciseId).findFirst();
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//
//                        // updating that exercises's rating
//                        exerciseDataModel.setExerciseAvgRating(userRating);
//
//                        // inside on execute method we are calling a method to copy
//                        // and update to real m database from our modal class.
//                        realm.copyToRealmOrUpdate(exerciseDataModel);  //TODO: DOes this just update that one field or OVERWRITE entire entry?
//                    }
//                });

                //Functionality to open/redirect to List of Exercises PAGE
                Intent intent = new Intent(starRatingPopupActivity.this, ExerciseListActivity.class);
                startActivity(intent);
            }
        });



        //-- Popup window setup --
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //POPUP RESOLUTION SIZE
        getWindow().setLayout(
            (int) (width*.9), (int) (height*.4)
        );

        //Move entire Popup Up vertically
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        //Makes everything beyond POP UP window transparent
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

}