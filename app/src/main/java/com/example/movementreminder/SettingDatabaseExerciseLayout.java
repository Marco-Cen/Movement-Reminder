package com.example.movementreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Database.ExerciseDataModel;

import java.util.List;

public class SettingDatabaseExerciseLayout extends RecyclerView.Adapter<SettingDatabaseExerciseLayout.ViewHolder> {

    // variable for our array list and context
    private List<ExerciseDataModel> exerciseDataModelArrayList;
    private Context context;

    public SettingDatabaseExerciseLayout(List<ExerciseDataModel> exerciseList, Context context) {
        this.exerciseDataModelArrayList = exerciseList;
        this.context = context;
    }

    @NonNull
    @Override
    public SettingDatabaseExerciseLayout.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_database_exercise_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingDatabaseExerciseLayout.ViewHolder holder, int position) {
        ExerciseDataModel modal = exerciseDataModelArrayList.get(position);
        holder.exerciseNameField.setText("Exercise Name: " + modal.getExerciseName());
        holder.exerciseDurationField.setText("Duration: " + Integer.toString(modal.getExerciseTimeRequired()) + " Minutes");
        holder.exerciseDescripField.setText("Note: " + modal.getExerciseNote());
    }

    @Override
    public int getItemCount() {
        return exerciseDataModelArrayList.size();
    }


    //-- HELPER CLASS --
    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private TextView exerciseNameField, exerciseDurationField, exerciseDescripField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            exerciseNameField = itemView.findViewById(R.id.exerciseName);
            exerciseDurationField = itemView.findViewById(R.id.exerciseDuration);
            exerciseDescripField = itemView.findViewById(R.id.exerciseNote);
        }
    }
}
