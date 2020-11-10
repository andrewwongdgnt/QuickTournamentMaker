package com.dgnt.quickTournamentMaker.model.management

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.model.IKeyable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(val id:String, var name:String, var note:String): IKeyable, Comparable<Person>, Parcelable {
    override val key: String = name
    override fun compareTo(other: Person): Int = name.compareTo(other.name)

    companion object {
        fun fromEntity(personEntity: PersonEntity): Person = Person(personEntity.id, personEntity.name, personEntity.note)
    }

}
