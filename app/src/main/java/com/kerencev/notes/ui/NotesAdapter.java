package com.kerencev.notes.ui;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.StyleOfNotes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    interface OnNoteClicked {
        void onNoteClicked(Note note);

        void onLongNoteClicked(Note note);
    }

    private OnNoteClicked onNoteClicked;

    private List<Note> data = new ArrayList<>();

    public void setData(Collection<Note> notes) {
        data.addAll(notes);
    }

    public void add(Note note) {
        data.add(0, note);
    }

    public int delete(Note note) {

        int index = data.indexOf(note);
        data.remove(note);

        return index;
    }

    public int updateNote(Note note) {

        int index = data.indexOf(note);

        data.set(index, note);

        return index;
    }

    public OnNoteClicked getOnNoteClicked() {
        return onNoteClicked;
    }

    public void setOnNoteClicked(OnNoteClicked onNoteClicked) {
        this.onNoteClicked = onNoteClicked;
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;

        if (StyleOfNotes.getINSTANCE(parent.getContext()).getStyle().equals(StyleOfNotes.STYLE_1)) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_grid, parent, false);
        }

        NotesViewHolder holder = new NotesViewHolder(itemView);
        holder.setContext(parent.getContext());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        Note note = data.get(position);

        holder.name.setText(note.getName());
        holder.text.setText(note.getDescription());
        
        if (!StyleOfNotes.getINSTANCE(holder.context).isIsHasDate()) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setText(note.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView name;
        TextView text;
        TextView date;

        private void setContext(Context context) {
            this.context = context;
        }

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);

            if (StyleOfNotes.getINSTANCE(context).getStyle().equals(StyleOfNotes.STYLE_1)) {
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (onNoteClicked != null) {
                            int clickedPosition = getAdapterPosition();
                            onNoteClicked.onNoteClicked(data.get(clickedPosition));
                        }
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (onNoteClicked != null) {
                            int clickedPosition = getAdapterPosition();
                            onNoteClicked.onLongNoteClicked(data.get(clickedPosition));
                            return true;
                        }
                        return false;
                    }
                });
            } else {

                itemView.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (onNoteClicked != null) {
                            int clickedPosition = getAdapterPosition();
                            onNoteClicked.onNoteClicked(data.get(clickedPosition));
                        }
                    }
                });

                itemView.findViewById(R.id.card).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (onNoteClicked != null) {
                            int clickedPosition = getAdapterPosition();
                            onNoteClicked.onLongNoteClicked(data.get(clickedPosition));
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }
}
