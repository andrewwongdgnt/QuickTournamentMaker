package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ExtendedTournamentInformation(
    val basicTournamentInformation: TournamentInformation,
    val numRounds: Int,
    val numOpenMatchUps: Int,
    val numMatchUpsWithByes: Int,
    val numParticipants: Int,
    val progress: Progress
) : Parcelable