package com.example.architecturecomponents;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        // Returns the instance, if one already exists simply returns the already created instance.
        return instance;
    }

    // Database callback method
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        // Runs on create
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Runs the async task to populate the database, takes the instance of the database.
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // Async task used to populate the database upon creation.
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        // Declare instance of NoteDao
        private NoteDao noteDao;

        // Construct noteDao, gets the noteDao from the database instance
        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        // Inserts 3 different notes into the database to start with
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }
    }
}
