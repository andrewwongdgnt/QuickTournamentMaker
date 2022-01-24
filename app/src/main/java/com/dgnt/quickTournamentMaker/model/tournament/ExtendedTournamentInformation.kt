package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime


@Parcelize
data class ExtendedTournamentInformation(
    val basicTournamentInformation: TournamentInformation,
    val numRounds: Int,
    val numMatchUps: Int,
    val numMatchUpsWithByes: Int,
    val numParticipants: Int,
) : Parcelable