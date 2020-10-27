package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tournamentTable")
data class TournamentEntity(
    @PrimaryKey val epoch: Long,
    val lastModifiedTime: Long,
    val name: String,
    val note: String,
    val type: String,
    val rankingConfig: String
)