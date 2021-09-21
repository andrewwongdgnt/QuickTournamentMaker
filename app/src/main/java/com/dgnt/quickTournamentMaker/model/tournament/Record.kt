package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Record(var wins:Int=0, var losses:Int=0, var ties:Int = 0): Parcelable