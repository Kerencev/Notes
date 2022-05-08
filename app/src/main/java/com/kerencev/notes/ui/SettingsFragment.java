package com.kerencev.notes.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kerencev.notes.R;
import com.kerencev.notes.logic.memory.StyleOfNotes;

/**
 * Фрагмент с настройками
 */

public class SettingsFragment extends Fragment {

    private SwitchMaterial switchMaterialShowDate;
    private SwitchMaterial switchMaterialSaveNotes;

    private Toolbar toolbar;

    private MaterialCardView style1;
    private MaterialCardView style2;

    private ImageView buttonStyle1;
    private ImageView buttonStyle2;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setRightButtonForStyle();

        if (requireActivity() instanceof ToolbarHolder) {
            ((ToolbarHolder) requireActivity()).setToolbar(toolbar);
        }

        style1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleOfNotes.getINSTANCE(requireContext()).setStyle(StyleOfNotes.STYLE_1);
                changeRadioButtonForStyles(buttonStyle1, buttonStyle2);
            }
        });

        style2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleOfNotes.getINSTANCE(requireContext()).setStyle(StyleOfNotes.STYLE_2);
                changeRadioButtonForStyles(buttonStyle2, buttonStyle1);
            }
        });

        switchMaterialShowDate.setChecked(StyleOfNotes.getINSTANCE(requireContext()).isIsHasDate());

        switchMaterialShowDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StyleOfNotes.getINSTANCE(requireContext()).setIsHasDate(b);
            }
        });

        switchMaterialSaveNotes.setChecked(StyleOfNotes.getINSTANCE(requireContext()).getIsSaveNotes());

        switchMaterialSaveNotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StyleOfNotes.getINSTANCE(requireContext()).setIsSaveNotes(b);
            }
        });
    }

    private void init(View view) {
        switchMaterialShowDate = view.findViewById(R.id.switch_has_date);
        switchMaterialSaveNotes = view.findViewById(R.id.switch_is_save_trash);

        toolbar = view.findViewById(R.id.toolbar);

        style1 = view.findViewById(R.id.style1);
        style2 = view.findViewById(R.id.style2);

        buttonStyle1 = view.findViewById(R.id.button_style1);
        buttonStyle2 = view.findViewById(R.id.button_style2);
    }

    private void changeRadioButtonForStyles(ImageView btn1, ImageView btn2) {
        btn1.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
        btn2.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
    }

    private void setRightButtonForStyle() {
        if (StyleOfNotes.getINSTANCE(requireContext()).getStyle().equals(StyleOfNotes.STYLE_1)) {
            changeRadioButtonForStyles(buttonStyle1, buttonStyle2);
        } else {
            changeRadioButtonForStyles(buttonStyle2, buttonStyle1);
        }
    }
}
