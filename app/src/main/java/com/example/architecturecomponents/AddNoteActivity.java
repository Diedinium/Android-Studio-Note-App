package com.example.architecturecomponents;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
    This activity does not communicate with any of the architecture components, it just passes values
    back to the main activity, where they are then input into the database via the view model.
    This keeps things simple.
*/
public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.architecturecomponents.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.architecturecomponents.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.architecturecomponents.EXTRA_PRIORITY";

    // Local member variables to hold the references to these UI widgets.
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        // Store references to the widgets upon create
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        // Set the min and max values that are pickable with the number picker.
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        // Adds the "close button"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        // Changes the title of this activity so that it differs from the default app title.
        setTitle("Add Note");
    }

    private void saveNote() {
        // Get the values from the text/number input fields
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        // If either title or description is empty (uses trim() to remove any leading/trailing whitespace, runs toast.
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            // Informs user they need to insert a title and description
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            // Returns to start of method
            return;
        }

        // Create an intent to pass data into the static strings, so they can be retrieved back in main activity.
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        // Passes back if the input went as expected (for example, was close button pressed?)
        setResult(RESULT_OK, data);
        finish();
    }

    // Creates the save icon on the activity bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Instantiate a MenuInflater instance
        MenuInflater menuInflater = getMenuInflater();
        // use it to inflate the add_note_menu, using the menu value passed into this method.
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    // Runs when save icon is clicked.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Uses switch statement to handle click
        switch (item.getItemId()) {
            // If the ID of the item selected is save_note
            case R.id.save_note:
                // Runs save note method, returns true.
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
