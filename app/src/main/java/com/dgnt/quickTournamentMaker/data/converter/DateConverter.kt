package com.dgnt.quickTournamentMaker.data.converter

import androidx.room.TypeConverter
import org.joda.time.LocalDateTime

class DateConverter {
    @TypeConverter
    fun toDateTime(value: Long): LocalDateTime {
        return LocalDateTime(value)
    }

    @TypeConverter
    fun fromDateTime(date: LocalDateTime): Long {
        return date.toDateTime().millis
    }
}