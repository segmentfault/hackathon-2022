package com.example.smartos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class groupClass{
    @PrimaryKey(autoGenerate = true)
    private int group_ID = 0;
    @ColumnInfo(name="group_name")
    private String group_name = "";
    public groupClass(String group_name){
        this.group_name = group_name;
    }
    public int getGroup_ID() {
        return group_ID;
    }

    public void setGroup_ID(int group_ID) {
        this.group_ID = group_ID;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }


}
