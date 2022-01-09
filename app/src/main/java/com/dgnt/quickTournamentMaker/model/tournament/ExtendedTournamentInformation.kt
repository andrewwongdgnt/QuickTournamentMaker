package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime


@Parcelize
data class ExtendedTournamentInformation(
    var basicTournamentInformation: TournamentInformation,
    var numRounds: Int,
    var numMatchUps: Int,
    var numMatchUpsWithByes: Int,
    var numParticipants: Int,
) : Parcelable