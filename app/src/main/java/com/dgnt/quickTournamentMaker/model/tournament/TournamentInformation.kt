package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class TournamentInformation(val title: String, val description: String, val tournamentType: TournamentType, val seedType: SeedType, val rankConfig: IRankConfig, val creationDate: Date, val lastModifiedDate: Date? = null) : Parcelable