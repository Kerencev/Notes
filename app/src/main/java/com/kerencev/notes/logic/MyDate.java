package com.kerencev.notes.logic;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class MyDate {

    public static String getDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
        java.util.Date date = new java.util.Date();
        return formatter.format(date);
    }
}
