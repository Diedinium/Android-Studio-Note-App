package com.example.architecturecomponents;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Declared as a database via the Room annotation, uses the Note.java as the entity and version is set to 1
// Abstract class because I'm not providing the method bodies.
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    // Variable for creating a singleton of this class - singleton means that the class can't be used
    // multiple times, only allows one to exist at a time.
    private static NoteDatabase instance;

    //Used to access the DAO later on, body not provided because Room inserts the code for this.
    // This method will be used to access the Access objects
    public abstract NoteDao noteDao();

    // Synchronized means that only one thread can access this method at a time, this method is used
    // to get the current instance of the database
    public static synchronized NoteDatabase getInstance(Context context) {
        // Only creates the database if there isn't already one.
        if (instance == null) {
            // Rather than call "new NoteDatabase" a builder needs to be used, due to the abstract class
            // Uses the application context, takes the NoteDatabase class, sets the name to "note_database" and
            // then uses fallbackToDestructiveMigration to destroy and recreate the schema, then builds.
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration().build();
        }
        // Returns the instance, if one already exists simply returns the already created instance.
        return instance;
    }
}
