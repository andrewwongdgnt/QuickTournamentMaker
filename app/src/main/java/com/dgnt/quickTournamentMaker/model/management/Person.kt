package com.dgnt.quickTournamentMaker.model.management

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.model.IKeyable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(val id: String = "", var name: String = "", var note: String = "") : IKeyable<String>, Comparable<Person>, Parcelable {
    override val key = id + name
    override fun compareTo(other: Person): Int = name.compareTo(other.name)
    override fun equals(other: Any?): Boolean {
        if (other is Person) {
            return other.key == key
        }
        return super.equals(other)
    }

    override fun hashCode(): Int = id.hashCode()
    fun toEntity(groupName: String): PersonEntity = PersonEntity(id, name, note, groupName)

    companion object {
        fun fromEntity(personEntity: PersonEntity): Person = Person(personEntity.id, personEntity.name, personEntity.note)
    }

}
