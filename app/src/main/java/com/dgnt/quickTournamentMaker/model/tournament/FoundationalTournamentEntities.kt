package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.tournament.MatchUpEntity
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantEntity
import com.dgnt.quickTournamentMaker.data.tournament.RoundEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoundationalTournamentEntities(
    val roundEntities: List<RoundEntity>,
    val matchUpEntities: List<MatchUpEntity>,
    val participantEntities: List<ParticipantEntity>,
) : Parcelable