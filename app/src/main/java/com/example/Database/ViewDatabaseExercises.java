package com.example.Database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.movementreminder.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/* NOTES
- ReadCoursesActivity

 */
public class ViewDatabaseExercises extends AppCompatActivity {

    List<ExerciseDataModel> exerciseDataModals;

    // creating variables for realm,
    // recycler view, adapter and our list.
    private Realm realm;
    private RecyclerView exerciseListData; //RecyclerView
    private SetupDatabaseExerciseList settingDBExerciseLayout; //Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database_exercises);

        // on below lines we are initializing our variables.
        exerciseListData = findViewById(R.id.exerciseListInfo);
        realm = Realm.getDefaultInstance();
        exerciseDataModals = new ArrayList<>();

        // calling a method to load
        // our recycler view with data.
        prepareRecyclerView();
    }

    private void prepareRecyclerView() {
        // on below line we are getting data from realm database in our list.
        exerciseDataModals = realm.where(ExerciseDataModel.class).findAll();
        // on below line we are adding our list to our adapter class.
        settingDBExerciseLayout = new SetupDatabaseExerciseList(exerciseDataModals, this);
        // on below line we are setting layout manager to our recycler view.
        exerciseListData.setLayoutManager(new LinearLayoutManager(this));
        // at last we are setting adapter to our recycler view.
        exerciseListData.setAdapter(settingDBExerciseLayout);
    }
}