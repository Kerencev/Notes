package com.kerencev.notes.ui.dialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.InMemoryNotesRepository;
import com.kerencev.notes.ui.NotesDescriptionFragment;
import com.kerencev.notes.ui.NotesFragment;

public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    private Note note;

    public static BottomSheetDialogFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(NotesFragment.ARG_NOTE, note);

        BottomSheetDialogFragment fragment = new BottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(NotesFragment.ARG_NOTE)) {
            note = getArguments().getParcelable(NotesFragment.ARG_NOTE);
        }

        LinearLayout edit = view.findViewById(R.id.action_edit);
        LinearLayout delete = view.findViewById(R.id.action_delete);
        LinearLayout rename = view.findViewById(R.id.action_rename);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNote(note);
                dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int indexOfNote = InMemoryNotesRepository.getINSTANCE(requireContext()).delete(note);
                dismiss();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, NotesDescriptionFragment.newInstance(note, indexOfNote))
                        .commit();
            }
        });

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ChangeNameDialogFragment.newInstance(note).show(getParentFragmentManager(), "");
            }
        });
    }

    private void showNote(Note note) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, NotesFragment.newInstance(note))
                .addToBackStack("details")
                .commit();
    }
}
