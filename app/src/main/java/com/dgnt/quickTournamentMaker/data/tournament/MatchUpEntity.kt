package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey

const val MATCH_UP_TABLE = "matchUpTable"

@Entity(tableName = MATCH_UP_TABLE)
data class MatchUpEntity(
    @PrimaryKey val epoch: Long,
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val matchUpIndex: String,
    val note: String,
    val color: Int,
    val status: String
)