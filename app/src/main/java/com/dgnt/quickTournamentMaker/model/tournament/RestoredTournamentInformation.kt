package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestoredTournamentInformation(
    val extendedTournamentInformation: ExtendedTournamentInformation,
    val foundationalTournamentEntities: FoundationalTournamentEntities
) : Parcelable