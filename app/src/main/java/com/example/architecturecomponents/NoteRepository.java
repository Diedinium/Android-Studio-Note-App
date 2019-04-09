package com.example.architecturecomponents;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

// Class is used as a repository for data sources, provides View Model with clean access to the methods/data.
public class NoteRepository {
    // Declare the note Dao and the LiveData variables
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    //Constructor for this repository class, takes the application context in order to get the database instance.
    public NoteRepository(Application application) {
        // Gets the database instance, taking the application context.
        NoteDatabase database = NoteDatabase.getInstance(application);
        // Gets the Data Access Object, is able to call function because Room creates the code for this function.
        noteDao = database.noteDao();
        // Gets the notes that are stored within the database via the DAO
        allNotes = noteDao.getAllNotes();
    }

    // Calls the Async task and executes it, passing in the noteDao and taking in the note.
    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    // Calls the Async task and executes it, passing in the noteDao and taking in the note.
    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    // Calls the Async task and executes it, passing in the noteDao and taking in the note.
    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    // Calls the Async task and executes it, passing in the noteDao and taking in the note.
    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    // Returns the LiveData of the notes, executes on the background thread.
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // Async task used to insert a note
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        // Declare an instance of the NoteDao
        private NoteDao noteDao;

        // Construct method, passing in the noteDao
        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        // Perform task in background
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    // Async task used to update a note
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        // Declare an instance of the NoteDao
        private NoteDao noteDao;

        // Construct method, passing in the noteDao
        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        // Perform task in background
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    // Async task used to delete a note
    private static class DeleteNoteAsyncTask extends android.os.AsyncTask<Note, Void, Void> {
        // Declare an instance of the NoteDao
        private NoteDao noteDao;

        // Construct method, passing in the noteDao
        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        // Perform task in background
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    // Async task used to delete all notes
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        // Declare an instance of the NoteDao
        private NoteDao noteDao;

        // Construct method, passing in the noteDao
        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        // Perform task in background
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
