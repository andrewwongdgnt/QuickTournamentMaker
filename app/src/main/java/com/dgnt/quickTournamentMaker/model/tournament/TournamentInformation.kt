package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class TournamentInformation(val title: String, val description: String, val participants: List<Participant>, val tournamentType: TournamentType, val seedType: SeedType, val rankConfig: IRankConfig, val creationDate: Date, val lastModifiedDate: Date? = null) : Parcelable