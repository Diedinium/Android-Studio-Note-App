package com.example.architecturecomponents;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity is used to insert the "boilerplate" code that you would normally need to use
// Results in this class being turned into an SQLite table within the database.
// Name is changed to "note_table" to better reflect SQL naming conventions.
@Entity(tableName = "note_table")
public class Note {

    //Used as the primary key for the table
    @PrimaryKey(autoGenerate = true)
    private int id;

    //The note title
    private String title;

    //The note description
    private String description;

    //The priority of the note.
    private  int priority;

    //Constructor for the class
    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    // Setter function for the ID
    public void setId(int id) {
        this.id = id;
    }

    // Get functions to return the values of the table.
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
