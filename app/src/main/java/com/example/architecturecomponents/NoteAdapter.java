package com.example.architecturecomponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

// Pass the note holder inner class, so that the adaptor knows about the note holder it needs to use.
// List adapter is used due to ability this adds for animations etc
public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    //By passing in the note holder class above, it is automatically implemented into hte 3 override methods

    // Member variable for OnItemClickListener
    private OnItemClickListener listener;

    // Constructor, just uses Diff callback variable.
    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    // DiffUtil comparison tool on the notes in the ListAdapter
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            // Only returns true if the ID's of the new item are the same
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            // Only returns true if all 3 of the values (title, description, priority) are the same.
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getPriority()
                    == newItem.getPriority();
        }
    };

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
        Note currentNote = getItem(position);
        // Set the text in the text views based on the data in the current note data calls
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    // Used to get note from certain positions in the adapter list
    public Note getNoteAt(int position) {
        // Returns the note that is at the position that is passed into this method.
        return getItem(position);
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

            // On click listener for recycler view items
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get the position
                    int position = getAdapterPosition();
                    // On an item click gets the position of the note in the note array.
                    // Only runs if listener is not null and is not an invalid array position.
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    // Implementing an interface which contains onItemClick
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    // Method to set an on click listener.
    public void setOnItemClickListener(OnItemClickListener listener) {
        // Sets the listener member variable to be the listener that is passed in.
        this.listener = listener;
    }
}
