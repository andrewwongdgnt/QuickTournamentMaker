package com.dgnt.quickTournamentMaker.service.data.serializable

import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import kotlinx.serialization.Serializable

@Serializable
data class TournamentData(
    val currentVersion: Int,
    val title: String,
    val description: String,
    val type: TournamentType,
    val rankingConfig: String?,
    val seedType: SeedType,
    val creationDate: Long,
    val lastModifiedDate: Long?,
    val participants: List<ParticipantData>,
    val rounds: List<RoundData>,
    val matchUps: List<MatchUpData>,
)
