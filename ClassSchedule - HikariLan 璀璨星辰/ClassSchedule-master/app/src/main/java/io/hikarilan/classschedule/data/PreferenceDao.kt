package io.hikarilan.classschedule.data

import androidx.room.*

@Dao
interface PreferenceDao {

    @Query("SELECT * FROM PreferenceEntity")
    fun getAll(): List<PreferenceEntity>

    @Query("SELECT * FROM PreferenceEntity WHERE `key` = :key LIMIT 1")
    fun findByKey(key: String): PreferenceEntity?

    @Update
    fun updatePreferences(vararg preferences: PreferenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: PreferenceEntity)

    @Delete
    fun delete(user: PreferenceEntity)
}