package com.example.database;

/* NOTE:
- This class represents the DATA MODEL used for the backend implementation
of this app (Database) with relation to 'MongoDB Realm'
- For Exercises (List of Exercises & Exercise selected) related content
- Order of Usage:
    - CRUD_AddExerciseDatabaseEntry.java > activity_crud_add_exercisedatabase.xml
    - CRUD_ViewDatabaseExercises.java > activity_crud_view_exercisesdatabase.xml
    - SetupDatabaseExercisesList.java > setup_database_exerciseslist.xml
    -
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/* NOTE:
- This class is a DATA MODEL OBJECT for Exercise List

 */
public class ExerciseDataModel extends RealmObject {

    //-- Instance Variables --
    @PrimaryKey //This is to connect with MongoDB Realm (Adds Primary Key in ID: creating our variables and using primary key for our id)
    private long IDExercise;
    private String exerciseName;
    private int exerciseTimeRequired; //Time required for each exercise (Minutes)
    private String exerciseNote; //Additional description box user can type into

    //Exercise selected popularity
    int exerciseSelectedCount; //How many times this exercise was selected
    private String exerciseDateRecorded; //date recorded which is datetime.now on when she submitted [DD/MM/YYYY]
    float exerciseRating; //5 star rating user can choose


    //-- creating an empty constructor --
    public ExerciseDataModel() {    }


    //-- Getters/Setters --
    public long getIDExercise() { return IDExercise; }
    public void setIDExercise(long IDExercise) { this.IDExercise = IDExercise; }
    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String name) { this.exerciseName = name; }
    public int getExerciseTimeRequired() { return exerciseTimeRequired; }
    public void setExerciseTimeRequired(int timeMinutes) { this.exerciseTimeRequired = timeMinutes; }
    public String getExerciseNote() { return exerciseNote; }
    public void setExerciseNote(String exerciseNote) { this.exerciseNote = exerciseNote; }

    public int getExerciseSelectedCount() { return exerciseSelectedCount; }
    public void setExerciseSelectedCount(int exerciseSelectedCount) { this.exerciseSelectedCount = exerciseSelectedCount; }
    public String getExerciseDateRecorded() { return exerciseDateRecorded; }
    public void setExerciseDateRecorded(String date) { this.exerciseDateRecorded = date; }
    public float getExerciseRating() { return exerciseRating; }
    public void setExerciseRating(float exerciseRating) { this.exerciseRating = exerciseRating; }

}
