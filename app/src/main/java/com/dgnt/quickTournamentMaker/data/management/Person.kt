package com.dgnt.quickTournamentMaker.data.management

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.IKeyable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(var name:String, var note:String): IKeyable, Comparable<Person>, Parcelable {
    override val key: String = name
    override fun compareTo(other: Person): Int = name.compareTo(other.name)
}
