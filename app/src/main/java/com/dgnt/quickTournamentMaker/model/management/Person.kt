package com.dgnt.quickTournamentMaker.model.management

import android.os.Parcelable
import androidx.room.Entity
import com.dgnt.quickTournamentMaker.model.IKeyable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "personTable")
@Parcelize
data class Person(var name:String, var note:String): IKeyable, Comparable<Person>, Parcelable {
    override val key: String = name
    override fun compareTo(other: Person): Int = name.compareTo(other.name)
}
