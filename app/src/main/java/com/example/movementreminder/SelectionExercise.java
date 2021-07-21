package com.example.movementreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectionExercise extends AppCompatActivity {
    GridView gridView;

    static final String[] gridViewValue = new String[]{
            "block1","block2","block3","block4","block5","block6"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_exercise);

        gridView = findViewById(R.id.gridViewSimple);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,gridViewValue);
        gridView.setAdapter(adapter);

        //create toast when clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),((TextView) view).getText()+"is clicked",Toast.LENGTH_SHORT).show();
            }
        });

    }
}