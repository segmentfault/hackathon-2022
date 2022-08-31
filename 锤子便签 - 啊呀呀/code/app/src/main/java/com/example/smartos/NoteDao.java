package com.example.smartos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insertnotes(Notes... notes);

    @Update
    void updatenotes(Notes... notes);

    @Delete
    void deletenote(Notes...notes);

    @Query("SELECT * from NOTES order by ID DESC")
//    List<Notes> getAllnotes();
    LiveData<List<Notes>>getAllnotesLive();

    @Query("select title from Notes where id = :op")
   String getNotesbyID(int op);

    @Query("select * from Notes where id = :op")
    Notes findAllNotesByID(int op);

    @Query("select date from Notes where id = :op")
    String getDatabyID(int op);

    @Query("select `group` from Notes where id = :op")
    String getGroupbyID(int op);

    @Query("select * From Notes where title like :patten Order By ID DESC")
    LiveData<List<Notes>>findNotesByPatten(String patten);

    @Query("select * from Notes where `group` like :patten Order By ID DESC")
    LiveData<List<Notes>>findNotesByGroup(String patten);

    @Query("select * from Notes where notes = 'delete' Order By ID DESC")
    LiveData<List<Notes>>findNotesInDelete();

    @Query("select ID from Notes where notes = 'delete' Order By ID DESC")
    List<Integer> getDeleteID();
}
