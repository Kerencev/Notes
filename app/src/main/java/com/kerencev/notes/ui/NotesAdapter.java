package com.kerencev.notes.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.logic.memory.StyleOfNotes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    public static boolean isForSelect = false;


    interface OnNoteClicked {
        void onNoteClicked(Note note);

        void onLongNoteClicked(Note note);
    }

    private OnNoteClicked onNoteClicked;

    private List<Note> data = new ArrayList<>();

    public void setData(Collection<Note> notes) {
        data.addAll(notes);

        setFixedNotes();
    }

    private void setFixedNotes() {

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isFixed()) {
                Note temp = data.get(i);
                data.remove(temp);
                data.add(0, temp);
            }
        }
    }

    public List<Note> getData() {
        return data;
    }

    public void clearData() {
        data.clear();
    }

    public void addFirst(Note note) {
        data.add(0, note);
    }

    public int addLast(Note note) {
        data.add(note);
        return data.size() - 1;
    }

    public int delete(Note note) {

        int index = data.indexOf(note);
        data.remove(note);

        return index;
    }

    public void deleteSelectedNotes(Context context) {

        for (int i = data.size() - 1; i >= 0; i--) {
            if (data.get(i).isSelected()) {

                Dependencies.getNotesRepository().removeNote(data.get(i), new Callback<Void>() {
                    @Override
                    public void onSuccess(Void data) {

                    }

                    @Override
                    public void onError(Throwable exception) {

                    }
                });

                if (StyleOfNotes.getINSTANCE(context).getIsSaveNotes()) {
                    Dependencies.getNotesTrashRepository().addNote(data.get(i));
                }

                data.remove(i);
            }
        }
    }

    public void selectAll() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelected(true);
        }
    }

    public void clearAllSelectedNotes() {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelected(false);
        }
    }

    public int updateNote(Note note) {

        int index = data.indexOf(note);

        data.set(index, note);

        return index;
    }

    public void filter(ArrayList<Note> filteredList) {

        data = filteredList;
        notifyDataSetChanged();
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

        setRightColor(note, holder);

        holder.name.setText(note.getName());
        holder.text.setText(note.getDescription());

        if (!StyleOfNotes.getINSTANCE(holder.context).isIsHasDate()) {
            holder.date.setVisibility(View.GONE);
        } else {
            holder.date.setText(note.getDate());
        }

        if (isForSelect) {
            holder.radioButton.setVisibility(View.VISIBLE);
            if (note.isSelected()) {
                holder.radioButton.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
            } else {
                holder.radioButton.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
            }
        } else {
            holder.radioButton.setVisibility(View.GONE);
        }
    }

    private void setRightColor(Note note, NotesViewHolder holder) {
        switch (note.getColor()) {

            case StyleOfNotes.COLOR_YELLOW:
                setRightBackground(note, holder, StyleOfNotes.COLOR_YELLOW);
                break;

            case StyleOfNotes.COLOR_BLUE:
                setRightBackground(note, holder, StyleOfNotes.COLOR_BLUE);
                break;

            case StyleOfNotes.COLOR_GREEN:
                setRightBackground(note, holder, StyleOfNotes.COLOR_GREEN);
                break;

            case StyleOfNotes.COLOR_RED:
                setRightBackground(note, holder, StyleOfNotes.COLOR_RED);
                break;
        }
    }

    private void setRightBackground(Note note, NotesViewHolder holder, int backNoteFixedNote) {

        holder.constraintLayout.setBackgroundResource(backNoteFixedNote);
        if (note.isFixed()) {
            holder.pin.setVisibility(View.VISIBLE);
        } else {
            holder.pin.setVisibility(View.GONE);
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
        ImageView radioButton;
        ImageView pin;

        ConstraintLayout constraintLayout;

        private void setContext(Context context) {
            this.context = context;
        }

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            constraintLayout = itemView.findViewById(R.id.container_card);
            radioButton = itemView.findViewById(R.id.radio_button);
            pin = itemView.findViewById(R.id.pin);

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
