package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cruddatabase.AddExerciseEntry;
import com.example.cruddatabase.UpdateDeleteExercisesDatabase;
import com.example.cruddatabase.ViewExercisesDatabase;
import com.example.cruddatabase.ViewPainJointsDatabase;
import com.example.setupdatabase.ExerciseDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;

public class ExerciseListActivity extends AppCompatActivity {

    //-- Instance Variables --
    private Realm realm;
    private List<ExerciseDataModel> exerciseDataModals;

    private GridView gridView;
    private List<String> gridViewValue;

    Button backBttn, viewDbBttn, addExerciseBttn, randomSelectBttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        //-- Sets title of application in Action Menu (Bar at very top) --
        setTitle("Exercise Selection");

        //-- Initialize Variables --
        realm = Realm.getDefaultInstance();
        exerciseDataModals = new ArrayList<>();
        gridViewValue = new ArrayList<String>();


        // "Back" Button
        backBttn = findViewById(R.id.backBttnListExercisePage);
        backBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ExerciseListActivity.this, PainFormActivity.class);
                startActivity(i);
            }
        });

        // "VIEW DATABASE: Exercises" Button
        viewDbBttn = findViewById(R.id.viewListExerciseDbBttnListExercisePage); //for list of exercises popup and pain form input
        viewDbBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ExerciseListActivity.this, ViewExercisesDatabase.class);
                startActivity(i);
            }
        });

        // "ADD EXERCISE" Button
        addExerciseBttn = (Button) findViewById((R.id.addExerciseBttnListOfExercisePage));
        addExerciseBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ExerciseListActivity.this, AddExerciseEntry.class); //TestingDatabase, testingforuserinput
                startActivity(intent);
            }
        });


        //-- Logic in DISPLAY CONTENTS on SCREEN DYNAMICALLY from DB (Exercises)
        //Exercise Data Model
        exerciseDataModals = realm.where(ExerciseDataModel.class).findAll();

        //Contents to show on page (List)
        for(ExerciseDataModel entry : exerciseDataModals){
            gridViewValue.add(entry.getExerciseName());
        }
        gridView = findViewById(R.id.gridViewSimple);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gridViewValue);
        gridView.setAdapter(adapter);

        //UPON CLICK on EXERCISE BLOCK/CARD Tile
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // GET POSITION OF CLICKED ITEM IN GRIDVIEW (Position starts at 0 ; L --> R)
                ExerciseDataModel entrySelected = exerciseDataModals.get(position); //WITH GRID SELECTED, MATCHES WITH ITEM SELECTED

                //Show Feedback Toast to USER (Centered)
                Toast feedbackMsg = Toast.makeText(getApplicationContext(),"Selected: \n ID: [" + String.valueOf(position+1)
                                                                                  + "] \n Exercise Name: [" + ((TextView) view).getText() + "]", Toast.LENGTH_LONG);
                feedbackMsg.setGravity(Gravity.CENTER, 0, 0);
                feedbackMsg.show();

               // on below line we are creating a new intent to PASS all data to new activity/page location
                Intent i = new Intent(getApplicationContext(), ExerciseSelectedActivity.class);
                i.putExtra("IDExerciseKey", entrySelected.getIDExercise()); //Exercise ID
                i.putExtra("exerciseNameKey", entrySelected.getExerciseName()); //Exercise name
                i.putExtra("exerciseDurationKey", entrySelected.getExerciseTimeRequired()); //Time Required Value for timer
                i.putExtra("exerciseNoteKey", entrySelected.getExerciseNote()); //Exercise Note
                getApplicationContext().startActivity(i); //start activity
            }
        });


        // "Random Select" Button
        randomSelectBttn = (Button) findViewById((R.id.randomSelectBttnListofExercisesPage));
        randomSelectBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(gridViewValue.size() == 0) { //ERROR NULL Checking (So app doesnt crash)
                    //Show Feedback Toast to USER (Centered)
                    Toast nullListMsg = Toast.makeText(getApplicationContext(), "There is NO exercises to randomly choose from! Add one!", Toast.LENGTH_SHORT);
                    nullListMsg.setGravity(Gravity.CENTER, 0, 0);
                    nullListMsg.show();
                }
                else { //IF LIST IS NOT EMPTY
                    //Set Random Interval Range so can randomly select from list of exercises in Database (0 - gridViewValue.size())
                    int randomExerciseID = new Random().nextInt(gridViewValue.size() - 0) + 0; //0 (inclusive) and sizeCountOfExerciseListShown (exclusive)  [Accounts for ALL elements shown on screen]

                    // Find Random Exercise Entry selected in db
                    ExerciseDataModel entrySelected = exerciseDataModals.get(randomExerciseID); //WITH GRID SELECTED, MATCHES WITH ITEM SELECTED

                    //Show Feedback Toast to USER (Centered)
                    Toast feedbackMsg = Toast.makeText(getApplicationContext(), "Selected: \n ID: [" + String.valueOf(entrySelected.getIDExercise())
                            + "] \n Exercise Name: [" + entrySelected.getExerciseName() + "]", Toast.LENGTH_SHORT);
                    feedbackMsg.setGravity(Gravity.CENTER, 0, 20);
                    feedbackMsg.show();

                    // on below line we are creating a new intent to PASS all data to new activity/page location
                    Intent i = new Intent(getApplicationContext(), ExerciseSelectedActivity.class);
                    i.putExtra("IDExerciseKey", entrySelected.getIDExercise()); //Exercise ID
                    i.putExtra("exerciseNameKey", entrySelected.getExerciseName()); //Exercise name
                    i.putExtra("exerciseDurationKey", entrySelected.getExerciseTimeRequired()); //Time Required Value for timer
                    i.putExtra("exerciseNoteKey", entrySelected.getExerciseNote()); //Exercise Note
                    getApplicationContext().startActivity(i); //start activity
                }
            }
        });

    }
}