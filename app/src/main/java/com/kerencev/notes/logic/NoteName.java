package com.kerencev.notes.logic;

import android.widget.EditText;

public class NoteName {
    public static String setDefaultName(EditText text) {
        String temp = text.getText().toString();
        String[] arr = temp.split(" ", 2);
        String[] arr01 = arr[0].split(",", 2);
        String[] arr02 = arr01[0].split("\n", 2);
        StringBuilder sb = new StringBuilder(arr02[0]);
        sb.setCharAt(0, Character.toUpperCase(arr02[0].charAt(0)));
        return sb.toString();
    }
}
