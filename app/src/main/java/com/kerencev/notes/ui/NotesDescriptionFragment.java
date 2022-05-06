package com.kerencev.notes.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.StyleOfNotes;
import com.kerencev.notes.ui.dialogFragments.BottomSheetDialogFragment;

import java.util.List;

public class NotesDescriptionFragment extends Fragment {

    private static final String INDEX_NOTE = "INDEX_NOTE";
    private static final String NEW_NAME = "NEW_NAME";

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    private Toolbar toolbar;
    List<Note> notes;

    private NotesAdapter adapter;

    private ConstraintLayout bottomChangeColor;

//    /**
//     * Метод для передачи фрагменту заметки и ее индекса для восстановления после удаления
//     *
//     * @param note        - заметка, удаление которой можно отменить
//     * @param indexOfNote - индекс заметки, для восстановления в том же месте
//     */
//    public static NotesDescriptionFragment newInstance(Note note, int indexOfNote) {
//        Bundle args = new Bundle();
//        args.putParcelable(NotesFragment.ARG_NOTE, note);
//        args.putInt(INDEX_NOTE, indexOfNote);
//        NotesDescriptionFragment fragment = new NotesDescriptionFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    /**
//     * Метод для передачи фрагменту заметки и нового названия
//     *
//     * @param note - заметка, у которой меняем название
//     * @param name - новое название
//     */
//    public static NotesDescriptionFragment newInstance(Note note, String name) {
//        Bundle args = new Bundle();
//        args.putParcelable(NotesFragment.ARG_NOTE, note);
//        args.putString(NEW_NAME, name);
//        NotesDescriptionFragment fragment = new NotesDescriptionFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

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

        bottomChangeColor = view.findViewById(R.id.constraint_bottom_bar);

        initList(view);
        toolbar = view.findViewById(R.id.bar_main_add);
        setOnClickNewNote(view);

        if (requireActivity() instanceof ToolbarHolder) {
            ((ToolbarHolder) requireActivity()).setToolbar(toolbar);
        }

//        if (getArguments() != null && getArguments().containsKey(NotesFragment.ARG_NOTE)) {
//            showSnackBartoCancelRemove(view);
//        }
    }

    private void initList(View view) {

//        if (getArguments() != null && getArguments().containsKey(NEW_NAME)) {
//            changeNoteName(view);
//        }

        RecyclerView recyclerView = view.findViewById(R.id.container);

        RecyclerView.LayoutManager layoutManager = null;

        if (StyleOfNotes.getINSTANCE(requireContext()).getStyle().equals(StyleOfNotes.STYLE_1)) {
            layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(requireContext(), 3);
        }

        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesAdapter();

        recyclerView.setAdapter(adapter);

        Dependencies.getNotesRepository().getAll(new Callback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> data) {
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable exception) {

            }
        });

        notifyAdapter(adapter, view);

        adapter.setOnNoteClicked(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note note) {
                showNote(note);
            }

            @Override
            public void onLongNoteClicked(Note note) {
                BottomSheetDialogFragment.newInstance(note).show(getParentFragmentManager(), "");
            }
        });

    }

    private void notifyAdapter(NotesAdapter adapter, View view) {

        getParentFragmentManager().setFragmentResultListener(Data.KEY_RESULT_CHANGE_RECYCLER, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.containsKey(Data.KEY_BUNDLE_ADD_NEW_NOTE)) {
                    adapter.add(result.getParcelable(Data.KEY_BUNDLE_ADD_NEW_NOTE));
                    adapter.notifyItemInserted(0);
                } else if (result.containsKey(Data.KEY_BUNDLE_DELETE_NOTE)) {
                    int indexOfRemoved = adapter.delete(result.getParcelable(Data.KEY_BUNDLE_DELETE_NOTE));
                    adapter.notifyItemRemoved(indexOfRemoved);
                } else if (result.containsKey(Data.KEY_BUNDLE_UPDATE_NOTE)) {
                    int indexOfRemoved = adapter.updateNote(result.getParcelable(Data.KEY_BUNDLE_UPDATE_NOTE));
                    adapter.notifyItemChanged(indexOfRemoved);
                } else if (result.containsKey(Data.KEY_BUNDLE_SHOW_BOTTOM_BAR)) {
                    bottomChangeColor.setVisibility(View.VISIBLE);
                    setOnClickChangeColor(view, result.getParcelable(Data.KEY_BUNDLE_SHOW_BOTTOM_BAR));
                }
            }
        });
    }

    private void setOnClickChangeColor(View view, Note note) {

        view.findViewById(R.id.action_color_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setColor(R.color.yellow);
                int indexOfChanged = adapter.updateNote(note);
                adapter.notifyItemChanged(indexOfChanged);
            }
        });

        view.findViewById(R.id.action_color_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setColor(R.color.blue);
                int indexOfChanged = adapter.updateNote(note);
                adapter.notifyItemChanged(indexOfChanged);
            }
        });

        view.findViewById(R.id.action_color_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setColor(R.color.green);
                int indexOfChanged = adapter.updateNote(note);
                adapter.notifyItemChanged(indexOfChanged);
            }
        });

        view.findViewById(R.id.action_color_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setColor(R.color.red);
                int indexOfChanged = adapter.updateNote(note);
                adapter.notifyItemChanged(indexOfChanged);
            }
        });

        view.findViewById(R.id.action_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dependencies.getNotesRepository().updateNote(note, note.getName(), note.getDescription(), new Callback<Note>() {
                    @Override
                    public void onSuccess(Note data) {
                        bottomChangeColor.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable exception) {

                    }
                });
            }
        });
    }

    private void setOnClickNewNote(View view) {

        view.findViewById(R.id.action_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note(null, null, null, MyDate.getDate(), StyleOfNotes.COLOR_YELLOW);
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
        getParentFragmentManager().beginTransaction()
                .hide(this)
                .add(R.id.fragment_container, NotesFragment.newInstance(note))
                .addToBackStack("details")
                .commit();
    }

    private void showSnackBartoCancelRemove(View view) {

    }

    private void changeNoteName(View view) {
        Note note = getArguments().getParcelable(NotesFragment.ARG_NOTE);
        String name = getArguments().getString(NEW_NAME);
        for (Note n : notes) {
            if (note.equals(n)) {
                n.setName(name);
            }
        }
        getArguments().clear();
    }
}