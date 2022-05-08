package com.kerencev.notes.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.kerencev.notes.logic.memory.StyleOfNotes;

import java.util.Date;
import java.util.Objects;

public class Note implements Parcelable {

    private String name;
    private String Description;
    private final String date;
    private int color;
    private String id;

    private Date dateForSort;

    public Note(String id, String name, String description, String date, int color, Date dateForSort) {
        this.id = id;
        this.name = name;
        Description = description;
        this.date = date;
        this.color = color;
        this.dateForSort = dateForSort;
    }


    protected Note(Parcel in) {
        name = in.readString();
        Description = in.readString();
        date = in.readString();
        color = in.readInt();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(Description);
        dest.writeString(date);
        dest.writeInt(color);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(name.charAt(0)));
        this.name = sb.toString();
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getDateForSort() {
        return dateForSort;
    }

    public void setDateForSort(Date dateForSort) {
        this.dateForSort = dateForSort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return color == note.color && Objects.equals(name, note.name) && Objects.equals(Description, note.Description) && Objects.equals(date, note.date) && Objects.equals(id, note.id) && Objects.equals(dateForSort, note.dateForSort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, Description, date, color, id, dateForSort);
    }
}

