package com.example.smartos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface groupDao {
    @Insert
    void insertGroup(groupClass...groupClasses);
    @Update
    void updateGroup(groupClass...groupClasses);
    @Delete
    void deleteGroup(groupClass...groupClasses);

    @Query("select * from groupClass")
    LiveData<List<groupClass>>getAllGroupName();

    @Query("select group_name from groupClass where group_ID = :id")
    String getGroupNameById(int id);
}
