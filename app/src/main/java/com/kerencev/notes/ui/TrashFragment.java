package com.kerencev.notes.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.logic.memory.FireStoreNotesTrashRepository;
import com.kerencev.notes.logic.memory.StyleOfNotes;
import com.kerencev.notes.ui.dialogFragments.TrashDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Фрагмент для отрисовки корзины
 * Используется тот же RecyclerView.Adapter что и у NotesDescriptionFragment
 */

public class TrashFragment extends Fragment {

    private Toolbar toolbar;

    private NotesAdapter adapter;

    public TrashFragment() {
        super(R.layout.fragment_trash);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);

        if (requireActivity() instanceof ToolbarHolder) {
            ((ToolbarHolder) requireActivity()).setToolbar(toolbar);
        }

        initList(view);

        setOnClickToolbar(adapter);
    }

    private void initialize(View view) {

        toolbar = view.findViewById(R.id.toolbar);
    }

    private void initList(View view) {

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

        Dependencies.getNotesRepository().getAll(FireStoreNotesTrashRepository.TRASH,
                StyleOfNotes.getINSTANCE(requireContext()).getDirection(), new Callback<List<Note>>() {
                    @Override
                    public void onSuccess(List<Note> data) {
                        adapter.setData(data);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable exception) {

                    }
                });

        adapter.setOnNoteClicked(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note note) {
                TrashDialogFragment.newInstance(note).show(getParentFragmentManager(), "");
            }

            @Override
            public void onLongNoteClicked(Note note) {
                TrashDialogFragment.newInstance(note).show(getParentFragmentManager(), "");
            }
        });

        notifyAdapter(adapter);
    }

    private void notifyAdapter(NotesAdapter adapter) {

        getParentFragmentManager().setFragmentResultListener(Data.KEY_RESULT_CHANGE_RECYCLER_FOR_TRASH, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.containsKey(Data.KEY_BUNDLE_DELETE_NOTE_FROM_TRASH)) {
                    int indexOfRemoved = adapter.delete(result.getParcelable(Data.KEY_BUNDLE_DELETE_NOTE_FROM_TRASH));
                    adapter.notifyItemRemoved(indexOfRemoved);
                }
            }
        });
    }

    private void setOnClickToolbar(NotesAdapter adapter) {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.restore_all:

                        List<Note> notes = new ArrayList<>();
                        Dependencies.getNotesRepository().getAll(FireStoreNotesTrashRepository.TRASH,
                                StyleOfNotes.getINSTANCE(requireContext()).getDirection(), new Callback<List<Note>>() {
                                    @Override
                                    public void onSuccess(List<Note> data) {
                                        notes.addAll(data);

                                        for (Note note : notes) {
                                            Dependencies.getNotesRepository().addNote(note.getName(), note.getDescription(), note.getColor(),
                                                    note.getDateForSort(), note.getDate(), new Callback<Note>() {
                                                        @Override
                                                        public void onSuccess(Note data) {

                                                        }

                                                        @Override
                                                        public void onError(Throwable exception) {

                                                        }
                                                    });

                                            Dependencies.getNotesTrashRepository().deleteAll();
                                            adapter.clearData();
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable exception) {

                                    }
                                });

                        return true;

                    case R.id.delete_all:
                        Dependencies.getNotesTrashRepository().deleteAll();
                        adapter.clearData();
                        adapter.notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        });
    }
}
