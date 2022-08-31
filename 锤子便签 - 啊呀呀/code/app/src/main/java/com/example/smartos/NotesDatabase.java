package com.example.smartos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Notes.class,groupClass.class},version = 2,exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase INSTANCE;
    static synchronized NotesDatabase getDatebase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),NotesDatabase.class,"notes_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
    public abstract NoteDao getNoteDao();
    public abstract groupDao getGroupDao();
}
