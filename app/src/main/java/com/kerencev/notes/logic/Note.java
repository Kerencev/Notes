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
    private boolean isSelected;

    private Date dateForSort;

    private boolean isFixed;

    public Note(String id, String name, String description, String date, int color, Date dateForSort) {
        this.id = id;
        this.name = name;
        Description = description;
        this.date = date;
        this.color = color;
        this.dateForSort = dateForSort;
        this.isSelected = false;
        this.isFixed = false;
    }


    protected Note(Parcel in) {
        name = in.readString();
        Description = in.readString();
        date = in.readString();
        color = in.readInt();
        id = in.readString();
        isSelected = in.readByte() != 0;
        isFixed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(Description);
        dest.writeString(date);
        dest.writeInt(color);
        dest.writeString(id);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isFixed ? 1 : 0));
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return color == note.color && isSelected == note.isSelected && isFixed == note.isFixed && Objects.equals(name, note.name) && Objects.equals(Description, note.Description) && Objects.equals(date, note.date) && Objects.equals(id, note.id) && Objects.equals(dateForSort, note.dateForSort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, Description, date, color, id, isSelected, dateForSort, isFixed);
    }
}

