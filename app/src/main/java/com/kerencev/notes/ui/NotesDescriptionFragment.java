package com.kerencev.notes.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.InMemoryNotesRepository;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NotesDescriptionFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    private Toolbar toolbar;
    private LinearLayout layoutView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        toolbar = view.findViewById(R.id.bar_main_add);
        setAddToolbar(view);

        if (requireActivity() instanceof ToolbarHolder) {
            ((ToolbarHolder) requireActivity()).setToolbar(toolbar);
        }
    }

    private void initList(View view) {

        List<Note> notes = InMemoryNotesRepository.getINSTANCE(requireContext()).getAll();
        layoutView = view.findViewById(R.id.container);

        for (Note note : notes) {
            View itemView = getLayoutInflater().inflate(R.layout.item_note, layoutView, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showNote(note);
                }
            });

            TextView name = itemView.findViewById(R.id.title);
            TextView text = itemView.findViewById(R.id.text);
            TextView date = itemView.findViewById(R.id.date);


            name.setText(note.getName());
            text.setText(note.getDescription());
            date.setText(note.getDate());

            layoutView.addView(itemView);
        }
    }

    private void setAddToolbar(View view) {
        Note note = new Note(null, null, null);

        view.findViewById(R.id.action_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNote(note);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search) {
                    Toast.makeText(requireContext(), "search", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void showNote(Note note) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_PARAM2, note);
            getParentFragmentManager()
                    .setFragmentResult(ARG_PARAM1, bundle);
        } else {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NotesFragment.newInstance(note))
                    .addToBackStack("details")
                    .commit();
        }
    }
}