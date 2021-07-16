package com.example.cruddatabase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movementreminder.R;
import com.example.setupdatabase.ExerciseDataModel;

import java.util.List;

/* NOTES:
- This class is what we set/see in showing database exercises list
 - CourseRVAdapter (ADAPTER)
 */
public class CardContentLayout_ExerciseListDB extends RecyclerView.Adapter<CardContentLayout_ExerciseListDB.ViewHolder> {

    // variable for our array list and context
    private List<ExerciseDataModel> exerciseDataModelArrayList;
    private Context context;

    //-- Constructor --
    public CardContentLayout_ExerciseListDB(List<ExerciseDataModel> exerciseList, Context context) {
        this.exerciseDataModelArrayList = exerciseList;
        this.context = context;
    }


    @NonNull
    @Override
    public CardContentLayout_ExerciseListDB.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardcontentlayout_exerciselistdb, parent, false);
        return new ViewHolder(view);
    }

    //-- Sets content INTO tile cards! --
    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private TextView idExerciseField, exerciseNameField, exerciseDurationField, exerciseDescripField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            idExerciseField = itemView.findViewById(R.id.idExerciseField);
            exerciseNameField = itemView.findViewById(R.id.exerciseName);
            exerciseDurationField = itemView.findViewById(R.id.exerciseDuration);
            exerciseDescripField = itemView.findViewById(R.id.exerciseNote);
        }
    }


    //-- (UPDATE) When click on card tile shown! --
    @Override
    public void onBindViewHolder(@NonNull CardContentLayout_ExerciseListDB.ViewHolder holder, int position) {
        ExerciseDataModel modal = exerciseDataModelArrayList.get(position);
        holder.idExerciseField.setText("[ID]: " + modal.getIDExercise());
        holder.exerciseNameField.setText("[Exercise Name]: " + modal.getExerciseName());
        holder.exerciseDurationField.setText("[Duration]: " + Integer.toString(modal.getExerciseTimeRequired()) + " Minute(s)");
        holder.exerciseDescripField.setText("[Note]: " + modal.getExerciseNote());

        //-- UPDATE Function in CRUD [adding on click listener for item of recycler view] --
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are creating a new intent.
                Intent i = new Intent(context, UpdateDeleteExercisesDatabase.class);
                // on below line we are passing all the data to new activity.
                i.putExtra("exerciseName", modal.getExerciseName());
                i.putExtra("exerciseDuration", modal.getExerciseTimeRequired());
                i.putExtra("exerciseDescription", modal.getExerciseNote());
                i.putExtra("id", modal.getIDExercise());
                // on below line we are starting a new activity.
                context.startActivity(i);
            }
        });
    }


    //-- SIZE of list populated --
    @Override
    public int getItemCount() {
        return exerciseDataModelArrayList.size();
    }

}
