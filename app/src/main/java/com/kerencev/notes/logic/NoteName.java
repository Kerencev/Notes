package com.kerencev.notes.logic;

/**
 * Класс для установки дефолтного имени у заметки, если имя при создании не было введено
 */

public class NoteName {
    public static String setDefaultName(String text) {

        String[] arr = text.split(" ", 2);
        String[] arr01 = arr[0].split(",", 2);
        String[] arr02 = arr01[0].split("\n", 2);
        StringBuilder sb = new StringBuilder(arr02[0]);
        sb.setCharAt(0, Character.toUpperCase(arr02[0].charAt(0)));
        return sb.toString();
    }
}
