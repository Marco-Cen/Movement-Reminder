//package com.example.movementreminder;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.SeekBar;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.example.Database.ExerciseDataModel;
//import com.example.Database.PainDataModel;
//
//import java.util.List;
//
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.mongodb.mongo.MongoClient;
//import io.realm.mongodb.mongo.MongoDatabase;
//
// /* NOTES:
//  - WORKING: INSERT, DELETE, UPDATE (But read not working? unsure why)
//
// */
//public class testingforuserinput extends AppCompatActivity implements View.OnClickListener {
//
//    //-- TEMP for CRUD (List of Exercises Page) --
//    Realm realm;
//    private TextView output;
//
//
//    ///////[Start PAIN FORM]
//    //------ TEMPORARY UNTIL PAIN FORM PAGE UI FINISHED ------
//    TextView tvProgressLabel;
//    MongoDatabase mongoDatabase;
//    MongoClient mongoClient;
//    ///////[End PAIN FORM]
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_testingforuserinput_painform);
//
//        //-- TEMP for CRUD (List of Exercises Page) --
//        RealmConfiguration config = new RealmConfiguration.Builder().allowQueriesOnUiThread(true).allowWritesOnUiThread(true).build();
//        realm = Realm.getInstance(config);
//        Button insert = findViewById(R.id.insert);
//        Button update = findViewById(R.id.update);
//        Button read = findViewById(R.id.read);
//        Button delete = findViewById(R.id.delete);
//        output = findViewById(R.id.show_data); //Show according data on screen depending on button pressed
//
//        insert.setOnClickListener(this);
//        update.setOnClickListener(this);
//        read.setOnClickListener(this);
//        delete.setOnClickListener(this);
//
//
//
//
////        ///////[Start PAIN FORM]
////        //------ TEMPORARY UNTIL PAIN FORM PAGE UI FINISHED (No functionality yet) ------
////        //get the spinner from the xml.
////        Spinner dropdown = findViewById(R.id.spinner1);
////        //create a list of items for the spinner.
////        String[] items = new String[]{"Arm", "LowerBack", "UpperBack"};
////        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
////        //There are multiple variations of this, but this is the basic variant.
////        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
////        //set the spinners adapter to the previously created one.
////        dropdown.setAdapter(adapter);
////
////        //// (SLIDER) set a change listener on the SeekBar
////        SeekBar seekBar = findViewById(R.id.seekBar);
////        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
////
////        int painLevelInput = seekBar.getProgress();
////        tvProgressLabel = findViewById(R.id.textView);
////        tvProgressLabel.setText("Rate the Pain You Feel: " + painLevelInput + "%");
////        ///////[End PAIN FORM]
//
//
//    }
//
/////////[Start PAIN FORM]
//    //-- Helper Function --
//    //(SLIDER) on click [PAIN FORM PAGE]
//    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
//
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            // updated continuously as the user slides the thumb
//            tvProgressLabel.setText("Progress: " + progress);
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            // called when the user first touches the SeekBar
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            // called after the user finishes moving the SeekBar
//        }
//    };
/////////[End PAIN FORM]
//
//
//
//    //[LIST OF EXERCISES: CRUD OPERATIONS]
//    @Override
//    public void onClick(View v) { //Conditions for checking which button is clickde
//        if(v.getId()==R.id.insert){
//            Log.d("Insert","Insert");
//            ShowInsertDialog();
//        }
//        if(v.getId()==R.id.update){
//            showUpdateDialog();
//            Log.d("Update","Update");
//        }
//        if(v.getId()==R.id.delete){
//            Log.d("Delete","Delete");
//            ShowDeleteDialog();
//        }
//        if(v.getId()==R.id.read){
//            showData();
//            Log.d("Read","Read");
//        }
//    }
//
//    //-- Helper functions --
//    //CREATE
//    private void ShowInsertDialog() {
//        AlertDialog.Builder alertD = new AlertDialog.Builder(testingforuserinput.this);
//        View view = getLayoutInflater().inflate(R.layout.add_exercise,null);
//        alertD.setView(view);
//
//        //Accessing input data from fields
//        final EditText addExercise = view.findViewById(R.id.exerciseToAdd);
//        final EditText exerciseTimeReq = view.findViewById(R.id.exerciseTimeRequired);
////        final Spinner jointList = view.findViewById(R.id.jointList);
//        final EditText exerciseNote = view.findViewById(R.id.exerciseNote);
//        Button backBttn = view.findViewById(R.id.backAddExercise);
//        Button submitBttn = view.findViewById(R.id.submitAddExercise);
//        final AlertDialog alertDialog = alertD.show();
//
//        //-- ON SUBMIT, will hide alertDialog and save DataModel into Realm --
//        submitBttn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//
//                final ExerciseDataModel exerciseDataModel = new ExerciseDataModel();
////                final PainDataModel painDataModel = new PainDataModel();
//
//                //-- Keeping track of id number
////                realm = Realm.getDefaultInstance(); //In case null object so no null object ref crash!
//                Number current_id = realm.where(ExerciseDataModel.class).max("IDExercise"); //Accesses the LAST MAX value of id of ExerciseDataModel
//                long nextId;
//                if(current_id==null){
//                    nextId = 1;
//                }
//                else{
//                    nextId = current_id.intValue()+1;
//                }
//
//                exerciseDataModel.setIDExercise(nextId);
//                exerciseDataModel.setExerciseName(addExercise.getText().toString());
//                exerciseDataModel.setExerciseTimeRequired(Integer.parseInt(exerciseTimeReq.getText().toString()));
////                painDataModel.setJointName(jointList.getSelectedItem().toString());
//
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.copyToRealm(exerciseDataModel);
//                    }
//                });
//            }
//        });
//
//    }
//
//    //READ
//    private void showData(){
//        List<ExerciseDataModel> dataModel = realm.where(ExerciseDataModel.class).findAll();
//        output.setText(""); //Reset output each time
//        for(int i = 0; i < dataModel.size(); i++){ //Accessing all list data
//            output.append("ID : " + dataModel.get(i).getIDExercise() + " Exercise : " + dataModel.get(i).getExerciseName() +
//                    " Time Required : " + dataModel.get(i).getExerciseTimeRequired() + " Note : " + dataModel.get(i).getExerciseNote()  + " \n");
//        }
//    }
//
//    //UPDATE
//    private void showUpdateDialog() {
//        final AlertDialog.Builder alertD = new AlertDialog.Builder(testingforuserinput.this);
//        View view = getLayoutInflater().inflate(R.layout.delete_exercise,null);
//        alertD.setView(view);
//
//        final EditText data_id=view.findViewById(R.id.data_id);
//        Button delete=view.findViewById(R.id.delete);
//        final AlertDialog alertDialog = alertD.show();
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//                long id= Long.parseLong(data_id.getText().toString());
//                final ExerciseDataModel exerciseDataModel =realm.where(ExerciseDataModel.class).equalTo("IDExercise",id).findFirst();
//                ShowUpdateDialog(exerciseDataModel);
//            }
//        });
//    }
//
//    private void ShowUpdateDialog(final ExerciseDataModel exerciseDataModel) { //
//        AlertDialog.Builder alertD = new AlertDialog.Builder(testingforuserinput.this);
//        View view = getLayoutInflater().inflate(R.layout.add_exercise,null);
//        alertD.setView(view);
//
//        final EditText exerciseName = view.findViewById(R.id.exerciseToAdd);
//        final EditText exerciseTimeReq = view.findViewById(R.id.exerciseTimeRequired);
//        final EditText exerciseNote = view.findViewById(R.id.exerciseNote);
//        Button backBttn = view.findViewById(R.id.backAddExercise);
//        Button submitBttn = view.findViewById(R.id.submitAddExercise);
//        final AlertDialog alertDialog = alertD.show();
//
//        exerciseName.setText(exerciseDataModel.getExerciseName());
//        exerciseTimeReq.setText(""+ exerciseDataModel.getExerciseTimeRequired());
//        exerciseNote.setText(""+ exerciseDataModel.getExerciseNote());
//
//        submitBttn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        exerciseDataModel.setExerciseTimeRequired(Integer.parseInt(exerciseTimeReq.getText().toString()));
//                        exerciseDataModel.setExerciseName(exerciseName.getText().toString());
//                        exerciseDataModel.setExerciseNote(exerciseNote.getText().toString());
//                        realm.copyToRealmOrUpdate(exerciseDataModel);
//                    }
//                });
//            }
//        });
//
//    }
//
//    //DELETE
//    private void ShowDeleteDialog() {
//        AlertDialog.Builder alertD = new AlertDialog.Builder(testingforuserinput.this);
//        View view = getLayoutInflater().inflate(R.layout.delete_exercise,null);
//        alertD.setView(view);
//
//        final EditText data_id = view.findViewById(R.id.data_id);
//        Button delete = view.findViewById(R.id.delete);
//        final AlertDialog alertDialog = alertD.show();
//
//        //Find id inputted and delete it
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long id = Long.parseLong(data_id.getText().toString());
//                final ExerciseDataModel exerciseDataModel = realm.where(ExerciseDataModel.class).equalTo("IDExercise",id).findFirst();
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        alertDialog.dismiss();
//                        exerciseDataModel.deleteFromRealm();
//                    }
//                });
//            }
//        });
//    }
//
//
//
//}