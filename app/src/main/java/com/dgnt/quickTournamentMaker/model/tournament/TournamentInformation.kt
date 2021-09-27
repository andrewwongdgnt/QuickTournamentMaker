package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class TournamentInformation(var title: String, var description: String, val tournamentType: TournamentType, val seedType: SeedType, val rankConfig: IRankConfig, val creationDate: Date, var lastModifiedDate: Date? = null) : Parcelable