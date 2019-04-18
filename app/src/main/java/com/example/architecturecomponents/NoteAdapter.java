package com.example.architecturecomponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Pass the note holder inner class, so that the adaptor knows about the note holder it needs to use.
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    //By passing in the note holder class above, it is automatically implemented into hte 3 override methods

    // Member variable declared, list to hold the notes. Initialised as an array list to save having to perform null checks
    private List<Note> notes = new ArrayList<>();

    //This method is where a NoteHolder is created and returned.
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Layout inflater takes the note item layout using the parent context and then returns this as a new NoteHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    // This method is used to get the data from a Java Note object, and pass it into the views of the note holder.
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        // Get the current note, based on position.
        Note currentNote = notes.get(position);
        // Set the text in the text views based on the data in the current note data calls
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    // Returns how many notes there are currently in the list
    @Override
    public int getItemCount() {
        return notes.size();
    }

    // Used to get list of notes into the recycler view
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        // Not the most efficient method, not very granular.
        notifyDataSetChanged();
    }

    // Used to get note from certain positions in the adapter list
    public Note getNoteAt(int position) {
        // Returns the note that is at the position that is passed into this method.
        return notes.get(position);
    }

    // Class that holds the views in single recycler view items.
    class NoteHolder extends RecyclerView.ViewHolder {
        // Three private variables to hold the changeable text for each item.
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        // Constructor for the class, passes the item
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            // Assigns the text views within the passed item to the local variables
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}
