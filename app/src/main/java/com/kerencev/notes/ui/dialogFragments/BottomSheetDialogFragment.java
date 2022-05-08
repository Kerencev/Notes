package com.kerencev.notes.ui.dialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.card.MaterialCardView;
import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.logic.memory.StyleOfNotes;
import com.kerencev.notes.ui.NotesFragment;

/**
 * Фрагмент появляется при удержании одной из заметок
 */

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

        MaterialCardView edit = view.findViewById(R.id.action_edit);
        MaterialCardView delete = view.findViewById(R.id.action_delete);
        MaterialCardView rename = view.findViewById(R.id.action_rename);
        MaterialCardView color = view.findViewById(R.id.action_color);

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

                FragmentManager fragmentManager = getParentFragmentManager();

                Dependencies.getNotesRepository().removeNote(note, new Callback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Data.KEY_BUNDLE_DELETE_NOTE, note);

                        fragmentManager.setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);
                    }

                    @Override
                    public void onError(Throwable exception) {

                    }
                });

                // Проверка, нужно ли добавлять заметку в корзину
                if (StyleOfNotes.getINSTANCE(requireContext()).getIsSaveNotes()) {
                    Dependencies.getNotesTrashRepository().addNote(note);
                }

                dismiss();
            }
        });

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ChangeNameDialogFragment.newInstance(note).show(getParentFragmentManager(), "");
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Data.KEY_BUNDLE_SHOW_BOTTOM_BAR, note);

                getParentFragmentManager().setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);
                dismiss();
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
}
