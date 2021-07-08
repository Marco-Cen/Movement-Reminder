package com.example.Database;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;


/* NOTE:
- This class initializes realmDatabase (MongoDB REALM Setup)
 */
public class RealmDbLinkage extends Application {


    @Override
    public void onCreate() {
        super.onCreate();



        //////////////
        //Initializing realm database: [MUST be placed whereever application first opens: landing page]
        Realm.init(this);

        //Setting realm configuration
        RealmConfiguration config = new RealmConfiguration.Builder()
                        //To allow write data to database on ui thread.
                        .allowWritesOnUiThread(true)
                        //To delete realm if migration is needed.
                        .deleteRealmIfMigrationNeeded()
                        //Method to build.
                        .build();
        //Setting configuration to our realm database.
        Realm.setDefaultConfiguration(config);
    }
}

