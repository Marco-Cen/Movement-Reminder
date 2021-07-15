package com.example.database;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movementreminder.R;

import java.util.List;

/* NOTES:
- This class is what we set/see in showing database pain/joints data collected (Based on 'SetupDatabaseExerciseList')
 - CourseRVAdapter (ADAPTER)
 */
public class SetupDatabasePainJointData extends RecyclerView.Adapter<SetupDatabasePainJointData.ViewHolder> {

    // variable for our array list and context
    private List<PainJointDataModel> painJointDataModelArrayList;
    private Context context;

    //-- Constructor --
    public SetupDatabasePainJointData(List<PainJointDataModel> painJointList, Context context) {
        this.painJointDataModelArrayList = painJointList;
        this.context = context;
    }


    @NonNull
    @Override
    public SetupDatabasePainJointData.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setup_database_painjointlist, parent, false);
        return new SetupDatabasePainJointData.ViewHolder(view);
    }

    //-- Sets content INTO tile cards! --
    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private TextView idJointPainField, dateRecordedField, jointNameField, jointPainValueField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views in tile card
            idJointPainField = itemView.findViewById(R.id.idPainField);
            dateRecordedField = itemView.findViewById(R.id.dateRecorded);
            jointNameField = itemView.findViewById(R.id.jointName);
            jointPainValueField = itemView.findViewById(R.id.jointPainLevel);
        }
    }


    //-- (UPDATE) When click on card tile shown! --
    @Override
    public void onBindViewHolder(@NonNull SetupDatabasePainJointData.ViewHolder holder, int position) {
        PainJointDataModel modal = painJointDataModelArrayList.get(position);
        holder.idJointPainField.setText("[ID]: " + modal.getIDPain());
        holder.dateRecordedField.setText("[Date Recorded]: " + modal.getPainDateRecorded());
        holder.jointNameField.setText("[Joint Name]: " + modal.getJointName());
        holder.jointPainValueField.setText("[Joint Pain Level]: " + modal.getPainRating());

        //-- UPDATE Function in CRUD [adding on click listener for item of recycler view] --
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are creating a new intent.
                Intent i = new Intent(context, CRUD_UpdateDeletePainJointsDatabase.class);
                // on below line we are passing all the data to new activity.
                i.putExtra("dateRecorded", modal.getPainDateRecorded());
                i.putExtra("jointName", modal.getJointName());
                i.putExtra("jointPainLevel", modal.getPainRating());
                i.putExtra("id", modal.getIDPain());
                // on below line we are starting a new activity.
                context.startActivity(i);
            }
        });
    }


    //-- SIZE of list populated --
    @Override
    public int getItemCount() {
        return painJointDataModelArrayList.size();
    }

}