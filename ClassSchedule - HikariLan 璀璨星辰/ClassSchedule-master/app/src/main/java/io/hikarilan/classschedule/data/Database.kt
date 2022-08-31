package io.hikarilan.classschedule.data

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.room.Room

object Database {

    var instance: AppDatabase? = null

    fun deploy(context: ComponentActivity) {
        instance = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "schedule"
        ).allowMainThreadQueries().build()
    }

    fun getAppDatabase(): AppDatabase = instance!!
}