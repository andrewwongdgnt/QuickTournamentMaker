package com.dgnt.quickTournamentMaker.data.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.IKeyable
import com.dgnt.quickTournamentMaker.data.management.Person
import kotlinx.android.parcel.Parcelize

enum class ParticipantType {
    NORMAL, NULL, BYE
}

@Parcelize
data class Participant(var person: Person, val participantType: ParticipantType = ParticipantType.NORMAL, var displayName: String = person.name, var note: String = person.note) : IKeyable, Comparable<Participant>,
    Parcelable {
    companion object {
        val NULL_PARTICIPANT =
            Participant(Person("", ""), ParticipantType.NULL)

        val BYE_PARTICIPANT =
            Participant(Person("", ""), ParticipantType.BYE)
    }

    var record: Record = Record()
    var color = 0xff000000

    override val key: String = person.name + participantType.name
    override fun compareTo(other: Participant): Int = displayName.compareTo(other.displayName)
}

