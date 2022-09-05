package com.example.notepad.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notepad.Dao.NoteDao;
import com.example.notepad.Entity.Note;

@Database(entities = {Note.class},version = 1,exportSchema = false)
public abstract class NoteDataBase extends RoomDatabase {

    private static NoteDataBase INSTANCE;

    public static synchronized NoteDataBase getInstance(Context context){
        if (INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteDataBase.class, "notepad")
                    .build();
        }
        return INSTANCE;
    }

    public abstract NoteDao getNoteDao();
}
