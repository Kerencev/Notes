package com.kerencev.notes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.Query;
import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.FireStoreNotesRepository;
import com.kerencev.notes.logic.memory.StyleOfNotes;
import com.kerencev.notes.ui.dialogFragments.BottomSheetDialogFragment;

import java.util.Date;
import java.util.List;

/**
 * Главный фрагмент заметок
 * Отрисовывается при заходе в приложение
 */

public class NotesDescriptionFragment extends Fragment {

    private Toolbar toolbar;

    private NotesAdapter adapter;

    private ConstraintLayout bottomChangeColor;

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

        setOnSortClick();
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

        Dependencies.getNotesRepository().getAll(FireStoreNotesRepository.NOTES,
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

    /**
     * Метод для изменения данных и передачи адаптеру информации об изменении
     */
    private void notifyAdapter(NotesAdapter adapter, View view) {

        getParentFragmentManager().setFragmentResultListener(Data.KEY_RESULT_CHANGE_RECYCLER, getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.containsKey(Data.KEY_BUNDLE_ADD_NEW_NOTE)) {

                    if (StyleOfNotes.getINSTANCE(requireContext()).getDirection().equals(Query.Direction.DESCENDING)) {
                        adapter.addFirst(result.getParcelable(Data.KEY_BUNDLE_ADD_NEW_NOTE));
                        adapter.notifyItemInserted(0);
                    } else {
                        int index = adapter.addLast(result.getParcelable(Data.KEY_BUNDLE_ADD_NEW_NOTE));
                        adapter.notifyItemInserted(index);
                    }

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

    /**
     * Метод для отрисовки изменения цвета у заметки и сохранения цвета
     */
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s.toString());
                return false;
            }
        });
    }


    private void setOnClickNewNote(View view) {

        view.findViewById(R.id.action_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note(null, null, null, MyDate.getDate(), StyleOfNotes.COLOR_YELLOW, new Date());
                showNote(note);
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


    /**
     * Метод для установки слушателя на кнопку сортировки
     */
    private void setOnSortClick() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_sort:

                        adapter.clearData();
                        switchDirection(item);

                        Dependencies.getNotesRepository().getAll(FireStoreNotesRepository.NOTES,
                                StyleOfNotes.getINSTANCE(requireContext()).getDirection(), new Callback<List<Note>>() {
                                    @Override
                                    public void onSuccess(List<Note> data) {
                                        adapter.setData(data);
                                        adapter.notifyDataSetChanged();
                                        makeToast();
                                    }

                                    @Override
                                    public void onError(Throwable exception) {
                                    }
                                });
                        return true;
                }
                return false;
            }
        });
    }

    private void makeToast() {
        if (StyleOfNotes.getINSTANCE(requireContext()).getDirection().equals(Query.Direction.ASCENDING)) {
            Toast.makeText(requireContext(), "По возрастанию", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "По убыванию", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchDirection(MenuItem item) {
        if (StyleOfNotes.getINSTANCE(requireContext()).getDirection().equals(Query.Direction.ASCENDING)) {
            StyleOfNotes.getINSTANCE(requireContext()).setDirection(Query.Direction.DESCENDING);
            item.setIcon(R.drawable.ic_baseline_trending_down_24);
        } else {
            StyleOfNotes.getINSTANCE(requireContext()).setDirection(Query.Direction.ASCENDING);
            item.setIcon(R.drawable.ic_baseline_trending_up_24);
        }
    }
}