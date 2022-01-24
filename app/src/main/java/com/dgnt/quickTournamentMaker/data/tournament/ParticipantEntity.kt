package com.dgnt.quickTournamentMaker.data.tournament

import android.os.Parcelable
import androidx.room.Entity
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime

const val PARTICIPANT_TABLE = "participantTable"

@Parcelize
@Entity(tableName = PARTICIPANT_TABLE, primaryKeys = ["epoch", "name", "seedIndex"])
data class ParticipantEntity(
    val epoch: LocalDateTime,
    val name: String,
    val seedIndex: Int,
    val displayName: String,
    val note: String,
    val type: ParticipantType,
    val color: Int
) : Parcelable