package com.example.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.movementreminder.MainActivity;
import com.example.movementreminder.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/* NOTES
- 'READ' in CRUD
- This dynamically shows whats in the database locally saved on device (Dynamically updates with user input)
- Able to scroll if too long (Due to card views!)
- ReadCoursesActivity (From Geeks4Geeks CRUD Article with MongoDbRealm & Android Studio)

 */
public class CRUD_ViewDatabasePainJoints extends AppCompatActivity {

    List<PainJointDataModel> painJointModel;

    // creating variables for realm,
    // recycler view, adapter and our list.
    private Realm realm;
    private RecyclerView painJointDatabaseDataEntries; //RecyclerView
    private SetupDatabasePainJointData settingDBPainJointLayout; //Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_view_painjointsdatabase);

        // on below lines we are initializing our variables.
        painJointDatabaseDataEntries = findViewById(R.id.painJointDatabaseEntries);
        realm = Realm.getDefaultInstance();
        painJointModel = new ArrayList<>();

        // calling a method to load
        // our recycler view with data.
        prepareRecyclerView();



        // 'BACK HOME' BUTTON
        Button homeBttn = findViewById(R.id.homeBttn); //for list of exercises popup and pain form input
        homeBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(CRUD_ViewDatabasePainJoints.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void prepareRecyclerView() {
        // on below line we are getting data from realm database in our list.
        painJointModel = realm.where(PainJointDataModel.class).findAll();
        // on below line we are adding our list to our adapter class.
        settingDBPainJointLayout = new SetupDatabasePainJointData(painJointModel, this);
        // on below line we are setting layout manager to our recycler view.
        painJointDatabaseDataEntries.setLayoutManager(new LinearLayoutManager(this));
        // at last we are setting adapter to our recycler view.
        painJointDatabaseDataEntries.setAdapter(settingDBPainJointLayout);
    }
}