package com.example.notepad.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notepad.Entity.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note... notes);

    @Update
    void updateNote(Note... notes);

    @Delete
    void deleteNote(Note... notes);

    @Query("select * from note where title like '%' || :condition || '%' or content like '%' || :condition || '%'")
    List<Note> queryByCondition(String... condition);

    @Query("select * from note order by date desc")
    LiveData<List<Note>> getAllNotes();
}
