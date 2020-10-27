package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "participantTable")
data class ParticipantEntity (
    @PrimaryKey val epoch: Long,
    val name: String,
    val seedIndex: Int,
    val displayName: Int,
    val note: String,
    val type: String,
    val color: Int
)