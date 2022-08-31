package io.hikarilan.classschedule.data

import androidx.room.*

@Dao
interface ClassDao {

    @Query("SELECT * FROM ClassEntity")
    fun getAll(): List<ClassEntity>

    @Query("SELECT * FROM ClassEntity WHERE week = :week AND class_number = :classNumber LIMIT 1")
    fun findByWeekAndClassNumber(week: Int, classNumber: Int): ClassEntity?

    @Update
    fun updateClasses(vararg classes: ClassEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg classes: ClassEntity)

    @Delete
    fun delete(user: ClassEntity)
}