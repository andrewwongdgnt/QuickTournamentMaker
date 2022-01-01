package com.dgnt.quickTournamentMaker.service.data.serializable

import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import kotlinx.serialization.Serializable

@Serializable
data class TournamentData(
    val currentVersion: Int,
    val title: String,
    val description: String,
    val type: TournamentType
)
