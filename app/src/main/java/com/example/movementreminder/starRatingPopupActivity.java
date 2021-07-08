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

public class starRatingPopupActivity extends Activity { //changed extends 'AppCompatActivity'

    Button bttn_skipRating;
    Button bttn_submitRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_rating_popup);

        //-- Ratings Bar Input (5 stars: Returns Float value)
        RatingBar RateBarInput = (RatingBar) findViewById(R.id.ratingBar);
        Float userRating = RateBarInput.getRating(); // get rating number from a rating bar


        //-- Buttons on popup --
        bttn_skipRating = (Button) findViewById(R.id.skipRatingBttn);
        bttn_skipRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Need to submit into database
                //(Input user data database here)


                //Functionality to open/redirect to List of Exercises PAGE
                Intent intent = new Intent(starRatingPopupActivity.this, MainActivity.class); //List of Exercises Page
                startActivity(intent);
            }
        });
        bttn_submitRating = (Button) findViewById(R.id.submitBttnStarRate);
        bttn_submitRating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Need to submit into database
                //(Input user data database here)
// userRating



                //Functionality to open/redirect to List of Exercises PAGE
                Intent intent = new Intent(starRatingPopupActivity.this, MainActivity.class); //List of Exercises Page
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