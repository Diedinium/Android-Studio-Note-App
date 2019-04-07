package com.example.architecturecomponents;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

// This class is the Data Access Object for the Note entity
@Dao
public interface NoteDao {
    // Due to being an interface, no method body is provided.
    // @ insert the relevant functionality from the room component.
    @Insert
    void insert(Note note);

    // Used to update the notes
    @Update
    void update(Note note);

    // Used to delete a note
    @Delete
    void delete(Note note);

    // Used to delete all notes, uses a custom query to do so. Note the SQL highlighting thanks to
    // the room component.
    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    // Gets all the notes by order of priority, in descending order
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    //Returns Notes as a list via the getAllNotes() function
    // Live data can be implemented via Room, which means any changes will automatically be noticed and applied.
    LiveData<List<Note>> getAllNotes();
}
