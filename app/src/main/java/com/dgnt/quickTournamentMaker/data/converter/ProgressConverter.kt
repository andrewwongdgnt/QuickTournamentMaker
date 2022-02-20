package com.dgnt.quickTournamentMaker.data.converter

import androidx.room.TypeConverter
import com.dgnt.quickTournamentMaker.model.tournament.Progress
import org.joda.time.LocalDateTime

class ProgressConverter {
    @TypeConverter
    fun toProgress(value: String): Progress {
        return value.split("/").let { Progress(it[0].toInt(), it[1].toInt()) }
    }

    @TypeConverter
    fun fromProgress(progress: Progress): String {
        return "${progress.matchesCompleted}/${progress.totalMatches}"
    }
}