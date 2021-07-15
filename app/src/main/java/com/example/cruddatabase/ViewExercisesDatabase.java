package com.example.cruddatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.movementreminder.MainActivity;
import com.example.movementreminder.R;
import com.example.setupdatabase.ExerciseDataModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/* NOTES
- 'READ' in CRUD
- This dynamically shows whats in the database locally saved on device (Dynamically updates with user input)
- Able to scroll if too long (Due to card views!)
- ReadCoursesActivity

 */
public class ViewExercisesDatabase extends AppCompatActivity {

    List<ExerciseDataModel> exerciseDataModals;

    // creating variables for realm,
    // recycler view, adapter and our list.
    private Realm realm;
    private RecyclerView exerciseListData; //RecyclerView
    private CardContentLayout_ExerciseListDB settingDBExerciseLayout; //Adapter

    private Button homeBttn;
    private Button addExerciseEntryBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_view_exercises_database);

        // on below lines we are initializing our variables.
        exerciseListData = findViewById(R.id.exerciseListInfo);
        realm = Realm.getDefaultInstance();
        exerciseDataModals = new ArrayList<>();

        // calling a method to load
        // our recycler view with data.
        prepareRecyclerView();



        // 'HOME' BUTTON
        homeBttn = findViewById(R.id.homeBttn); //for list of exercises popup and pain form input
        homeBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ViewExercisesDatabase.this, MainActivity.class);
                startActivity(i);
            }
        });

        // "Add Entry" BUTTON
        addExerciseEntryBttn = findViewById(R.id.addExerciseEntryBttn);
        addExerciseEntryBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ViewExercisesDatabase.this, AddExerciseEntry.class);
                startActivity(i);
            }
        });

    }

    private void prepareRecyclerView() {
        // on below line we are getting data from realm database in our list.
        exerciseDataModals = realm.where(ExerciseDataModel.class).findAll();
        // on below line we are adding our list to our adapter class.
        settingDBExerciseLayout = new CardContentLayout_ExerciseListDB(exerciseDataModals, this);
        // on below line we are setting layout manager to our recycler view.
        exerciseListData.setLayoutManager(new LinearLayoutManager(this));
        // at last we are setting adapter to our recycler view.
        exerciseListData.setAdapter(settingDBExerciseLayout);
    }
}