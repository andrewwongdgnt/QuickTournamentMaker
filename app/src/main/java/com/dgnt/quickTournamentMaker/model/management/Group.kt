package com.dgnt.quickTournamentMaker.model.management

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.model.IKeyable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(var name:String, var note:String, var favourite:Boolean, val persons:List<Person> = mutableListOf()): IKeyable, Comparable<Group>, Parcelable {
    override val key: String = name
    override fun compareTo(other: Group): Int {
        if (favourite && !other.favourite) return -1
        if (!favourite && other.favourite) return 1
        return name.compareTo(other.name)
    }
}