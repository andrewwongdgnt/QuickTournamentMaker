package com.dgnt.quickTournamentMaker.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleParcelable(
    val value: String
) : Parcelable