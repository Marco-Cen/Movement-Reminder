package com.example.setupdatabase;

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

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/* NOTE:
- This class is a DATA MODEL OBJECT for Exercise List
- (REALM database ONLY SUPPORTS RealmList: They dont support any types of lists!)
    - Realm supports the following field types: boolean, byte, short, int, long, float, double, String,
    Date and byte[] as well as the boxed types Boolean, Byte, Short, Integer, Long, Float and Double.
    Subclasses of RealmObject and RealmList<? extends RealmObject> are used to model relationships.
- TODO: (RealmList<Float> exerciseAllRatings;)
    - Need list data type (Ex:'float[]') to store list of all exercise ratings under 1 exercise and get and update avg star rating with each client input
    Alternative: (Overrwrite exercise rating each time)
    Temp Solution?: Maybe create NEW collection in database to store all entries manually instead of using list?
 */
public class ExerciseDataModel extends RealmObject {

    //-- Instance Variables --
    @PrimaryKey //This is to connect with MongoDB Realm (Adds Primary Key in ID: creating our variables and using primary key for our id)
    private long IDExercise;
    private String exerciseName;
    private int exerciseTimeRequired; //Time required for each exercise (Minutes)
    private String exerciseNote; //Additional description box user can type into

    //TODO: Needed Obaida page to test (and ran into some issues with database)
    //Exercise selected popularity
    private int exerciseSelectedCount; //How many times this exercise was selected
    private String exerciseDateRecorded; //date recorded which is datetime.now on when she submitted [DD/MM/YYYY]
    private float exerciseAvgRating; //5 star rating user can choose
    //collection of all user ratings <<Doesnt work accordingly :( >>
    private RealmList<Float> exerciseAllRatings;


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
    public float getExerciseAvgRating() { return exerciseAvgRating; }
    public void setExerciseAvgRating(float exerciseRating) { this.exerciseAvgRating = exerciseRating; }

    //TODO: wasnt able to get List type to work with datamodel with MongoDB Realm db (Only able to use their 'RealmList<>' type)
    public RealmList<Float> getExerciseAllRatings() { return exerciseAllRatings; }
    public void setExerciseAllRatings(RealmList<Float> exerciseAllRatings) { this.exerciseAllRatings = exerciseAllRatings; }

}
