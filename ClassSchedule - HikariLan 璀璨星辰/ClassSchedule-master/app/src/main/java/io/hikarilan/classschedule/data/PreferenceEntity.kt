package io.hikarilan.classschedule.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

fun getPreferenceByKey(key: String): PreferenceEntity {
    return Database.getAppDatabase().preferenceDao().findByKey(key)!!
}

fun updatePreference(key: String, value: String) {
    val preference = getPreferenceByKey(key)
    preference.value = value
    Database.getAppDatabase().preferenceDao().updatePreferences(preference)
}

@Entity
data class PreferenceEntity(
    @PrimaryKey @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "value") var value: String
)