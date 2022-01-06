package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import org.joda.time.LocalDateTime

const val PARTICIPANT_TABLE = "participantTable"

@Entity(tableName = PARTICIPANT_TABLE, primaryKeys = ["epoch", "name", "seedIndex"])
data class ParticipantEntity(
    val epoch: LocalDateTime,
    val name: String,
    val seedIndex: Int,
    val displayName: String,
    val note: String,
    val type: ParticipantType,
    val color: Int
)