package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.LocalDateTime

const val PARTICIPANT_TABLE = "participantTable"

@Entity(tableName = PARTICIPANT_TABLE)
data class ParticipantEntity(
    @PrimaryKey val epoch: LocalDateTime,
    val name: String,
    val seedIndex: Int,
    val displayName: Int,
    val note: String,
    val type: String,
    val color: Int
)