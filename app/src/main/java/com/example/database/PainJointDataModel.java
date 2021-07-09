package com.example.database;

/* NOTE:
- This class represents the DATA MODEL used for the backend implementation
of this app (Database) with relation to 'MongoDB Realm'
- For Pain related content
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/* NOTE:
- This class is a DATA MODEL OBJECT for her PAIN x Joint List

 */
public class PainJointDataModel extends RealmObject {

    //-- Instance Variables --
    @PrimaryKey //This is to connect with MongoDB Realm (Adds Primary Key in ID: creating our variables and using primary key for our id)
    private long IDPain; //This is id identifier
    private String jointName;
    private int painRating; //Rating out of 100 (%)
    private String painDateRecorded; //date recorded which is datetime.now on when she submitted [DD/MM/YYYY]


    //-- creating an empty constructor --
    public PainJointDataModel() {    }


    //-- Getters/Setters --
    public long getIDPain() { return IDPain; }
    public void setIDPain(long id) { this.IDPain = id; }
    public String getJointName() { return jointName; }
    public void setJointName(String name) { this.jointName = name; }
    public int getPainRating() { return painRating; }
    public void setPainRating(int painRating) { this.painRating = painRating; }
    public String getPainDateRecorded() { return painDateRecorded; }
    public void setPainDateRecorded(String date) { this.painDateRecorded = date; }

}
