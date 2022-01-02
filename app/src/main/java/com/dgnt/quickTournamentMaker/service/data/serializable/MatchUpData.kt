package com.dgnt.quickTournamentMaker.service.data.serializable

import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import kotlinx.serialization.Serializable

@Serializable
data class MatchUpData(
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val matchUpIndex: Int,
    val title: String,
    val useTitle: Boolean,
    val note: String,
    val color: Int,
    val status: MatchUpStatus,
)
