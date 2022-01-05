package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.LocalDateTime

const val TOURNAMENT_TABLE = "tournamentTable"
@Entity(tableName = TOURNAMENT_TABLE)
data class TournamentEntity(
    @PrimaryKey val epoch: LocalDateTime,
    val lastModifiedTime: Long,
    val name: String,
    val note: String,
    val type: String,
    val rankingConfig: String
)