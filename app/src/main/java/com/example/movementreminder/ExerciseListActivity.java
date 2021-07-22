package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cruddatabase.AddExerciseEntry;
import com.example.cruddatabase.ViewExercisesDatabase;
import com.example.cruddatabase.ViewPainJointsDatabase;
import com.example.setupdatabase.ExerciseDataModel;

import java.util.ArrayList;
import java.util.List;

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

        // "VIEW DATABASE" Button
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

//        // "Random Select" Button
//        randomSelectBttn = (Button) findViewById((R.id.randomSelectBttnListofExercisesPage));
//        randomSelectBttn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
        //         //Redirect to page to TIMER page with CONTENTS/INTENT passed through
//                Intent intent = new Intent(ExerciseListActivity.this, AddExerciseEntry.class); //TestingDatabase, testingforuserinput
//                startActivity(intent);


//            }
//        });



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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Toast.makeText(getApplicationContext(),( (TextView) view).getText() + "is clicked",Toast.LENGTH_SHORT).show();



//                //Functionality to open/redirect to (ExerciseSelected) PAGE
//                Intent intent = new Intent(ExerciseListActivity.this, ExerciseSelectedActivity.class);
//                startActivity(intent);

            }
        });

    }
}