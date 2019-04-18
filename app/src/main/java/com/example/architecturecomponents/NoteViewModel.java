package com.example.architecturecomponents;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    // Declare variables for instances of the NoteRepository and LiveData.
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    //Constructor for the View Model which takes the application as context
    public NoteViewModel(@NonNull Application application) {
        super(application);
        // repository instance is created using NoteRepository class, passed the application context
        repository = new NoteRepository(application);
        // All notes are populated into LiveData component
        allNotes = repository.getAllNotes();
    }

    // Inserts a note via the repository
    public void insert(Note note) {
        repository.insert(note);
    }

    // Updates a note via the repository
    public void update(Note note) {
        repository.update(note);
    }

    // Deletes note via the repository
    public void delete(Note note) {
        repository.delete(note);
    }

    // Deletes all notes via the repository (doesn't need to be passed a note)
    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    // Returns LiveData
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
