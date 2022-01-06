package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import org.joda.time.LocalDateTime

const val MATCH_UP_TABLE = "matchUpTable"

@Entity(tableName = MATCH_UP_TABLE, primaryKeys = ["epoch", "roundGroupIndex", "roundIndex", "matchUpIndex"])
data class MatchUpEntity(
    val epoch: LocalDateTime,
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val matchUpIndex: Int,
    val useTitle: Boolean,
    val name: String,
    val note: String,
    val color: Int,
    val status: MatchUpStatus
)