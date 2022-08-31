package io.hikarilan.classschedule.data

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(primaryKeys = ["week", "class_number"])
data class ClassEntity(
    @ColumnInfo(name = "week") val week: Int,
    @ColumnInfo(name = "class_number") val classNumber: Int,
    @ColumnInfo(name = "class_name") var className: String,
    @ColumnInfo(name = "location") var location: String,
    @ColumnInfo(name = "teacher") var teacher: String,
    @ColumnInfo(name = "availableWeeks") var availableWeeks: String,
    @ColumnInfo(name = "color") var color: Long
) {
    companion object {
        fun fillEntities(
            origin: List<ClassEntity>,
            maxWeek: Int,
            currentClassNumber: Int
        ): List<ClassEntity> {
            val list = mutableListOf<ClassEntity>()
            for (week in 1..maxWeek) {
                origin.find { it.week == week && it.classNumber == currentClassNumber }.let {
                    if (it != null) list.add(it)
                    else list.add(ClassEntity(week, currentClassNumber, "", "", "", "", 0x00FFFFFFF))
                }
            }
            return list
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassEntity

        if (week != other.week) return false
        if (classNumber != other.classNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = week
        result = 31 * result + classNumber
        return result
    }


}
