package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable


interface IRankConfig : Parcelable {

    /**
     * Get the string triple representation of this rank config
     *
     * @return the string triple
     */
    val stringTripleRepresentation: Triple<String, String, String>
}