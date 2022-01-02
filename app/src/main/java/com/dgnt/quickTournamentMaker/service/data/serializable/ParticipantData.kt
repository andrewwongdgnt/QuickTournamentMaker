package com.dgnt.quickTournamentMaker.service.data.serializable

import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantData(
    val name: String,
    val displayName: String,
    val note: String,
    val type: ParticipantType,
    val color: Int,
)
