package io.hikarilan.classschedule.data

import java.util.*

data class DateEntity(val year: Int, val month: Int, val dayOfMonth: Int) {

    companion object {
        fun DateEntity.toCalender(): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
        }

        fun Calendar.toDateEntity(): DateEntity {
            return DateEntity(
                get(Calendar.YEAR),
                get(Calendar.MONTH) + 1,
                get(Calendar.DAY_OF_MONTH)
            )
        }
    }

}
