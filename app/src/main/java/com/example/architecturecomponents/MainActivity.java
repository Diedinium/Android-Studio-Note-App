package com.example.architecturecomponents;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Constant for the intent request code to add
    public static final int ADD_NOTE_REQUEST = 1;
    // Constant for the intent request code to edit
    public static final int EDIT_NOTE_REQUEST = 2;

    // Member variable for View Model
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference to the floating add note button
        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        // On click listener for the button
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This can be used as context, as it gets the context of this from the onclick listener
                // Intent here is to open the add note activity
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                // using this method allows for results to be returned from activity.
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        // Holds a reference to the recycler view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // Layout manager is what takes care of displaying the items below each other.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Sets the fixed size setting to true, makes recycler view more efficient.
        recyclerView.setHasFixedSize(true);

        // Create noteadapter instance
        final NoteAdapter adapter = new NoteAdapter();
        // Set the recyclerview to use this as the adapter.
        recyclerView.setAdapter(adapter);

        // creates the view model, uses view model providers which will get an existing view model if there already is one
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        // Get LiveData, only retrieves LiveData when activity is in foreground
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // Every timer there is a change, the notes are inserted into the view via the adapter.
                adapter.submitList(notes);

            }
        });

        // Touch helper for handling the swipe to delete functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // uses the view model delete method, passes in note view the view Holder by getting the adapter position
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                // Feedback message
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }

            // Needs to call this method, otherwise will not work if item touch helper is not attached to the recyclerView
        }).attachToRecyclerView(recyclerView);

        // Implementing the setOnItemClickListener, via anonymous inner class. Click is handled within onItemClick.
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                // Open the AddEditNoteActivity via intent, pass MainActivity context, not context of the onItemClick.
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                // Put extra values to the activity, uses note variable from the onItemClick declaration in NoteAdapter
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                // Start the activity, uses edit note request constant for it's request ID.
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    // When the add note activity is closed it will return values, this override handles these results.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the request code is the add note request, and the result code is ok, then true.
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            // Get the extras from the add note activity.
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            // Note instance, passed the values from extras
            Note note = new Note(title, description, priority);
            // Inserted into database via view model insert method.
            noteViewModel.insert(note);

            // Feedback message
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        // if the request code is the edit note request, and the result code is ok, then true.
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            // Get the ID from the returned extra
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            // if ID is equal to -1, something went wrong. Exit method and show toast.
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the other 3 extras
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description =data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            // Create a note
            Note note = new Note(title, description, priority);
            // set the ID of this note, so that correct note can be edited.
            note.setId(id);
            // Update the note via the viewmodel.
            noteViewModel.update(note);

            Toast.makeText(this, "Note had been updated", Toast.LENGTH_SHORT).show();

        // Else is for when result is not ok
        } else {
            // Feedback message
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    // Inserts the triple dot menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Instantiate a MenuInflater instance
        MenuInflater menuInflater = getMenuInflater();
        // use it to inflate the main_menu, using the menu value passed into this method.
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Used to handle when one of the menu options is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch statement on the item ID's
        switch (item.getItemId()) {
            // If delete all notes is selected
            case R.id.delete_all_notes:
                // delete all notes via the view model
                noteViewModel.deleteAllNotes();
                // Feedback message
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
