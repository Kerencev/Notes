package com.kerencev.notes.logic;

import java.text.SimpleDateFormat;

public class MyDate {

    public static String getDate() {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yy HH:mm");
        java.util.Date date = new java.util.Date();
        return formater.format(date);
    }
}
