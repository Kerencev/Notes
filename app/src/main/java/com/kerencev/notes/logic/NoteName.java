package com.kerencev.notes.logic;

import android.widget.EditText;

public class NoteName {
    public static String setDefaultName(EditText text) {
        String temp = text.getText().toString();
        String[] arr = temp.split(" ", 2);
        String[] arr01 = arr[0].split(",", 2);
        String[] arr02 = arr[0].split("\n", 2);
        return arr02[0];
    }
}
