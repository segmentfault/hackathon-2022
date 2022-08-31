package io.hikarilan.classschedule.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ClassEntity::class, PreferenceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao
    abstract fun preferenceDao(): PreferenceDao
}