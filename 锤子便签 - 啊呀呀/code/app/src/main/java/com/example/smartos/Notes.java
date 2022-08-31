package com.example.smartos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Notes {
    @PrimaryKey(autoGenerate = true)
    private int ID = 0;

    @ColumnInfo(name="title")
    private String title = "";

    @ColumnInfo(name = "notes")
    private String notes = "";

    @ColumnInfo(name = "date")
    private String date = "";

    @ColumnInfo(name = "group")
    private String group = "";

    @ColumnInfo(name = "textCount")
    private int textCount = 0;
    @Ignore
    public Notes(){}
    @Ignore
    public Notes(String title,String date){
        this.title = title;
        this.date = date;
    }

    public Notes(String title, String notes, String date, String group, int textCount) {
        this.title = title;
        this.notes = notes;
        this.date = date;
        this.group = group;
        this.textCount = textCount;
    }

    public int getID() {
        return ID;
    }
    public void setID(int id) {
        this.ID = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getTextCount() {
        return textCount;
    }

    public void setTextCount(int textCount) {
        this.textCount = textCount;
    }
}

