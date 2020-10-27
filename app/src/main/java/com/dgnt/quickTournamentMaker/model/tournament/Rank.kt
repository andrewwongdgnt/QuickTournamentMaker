package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rank(val known:List<Set<Participant>>, val unknown:Set<Participant>) : Parcelable