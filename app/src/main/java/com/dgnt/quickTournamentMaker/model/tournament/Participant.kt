package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.model.IKeyable
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.android.parcel.Parcelize

enum class ParticipantType {
    NORMAL, NULL, BYE
}

enum class ParticipantPosition {
    P1, P2
}

@Parcelize
data class Participant(var person: Person, val participantType: ParticipantType = ParticipantType.NORMAL, var displayName: String = person.name, var note: String = person.note) : IKeyable, Comparable<Participant>, Parcelable {
    companion object {
        val NULL_PARTICIPANT =
            Participant(Person("", "", ""), ParticipantType.NULL)

        val BYE_PARTICIPANT =
            Participant(Person("", "", ""), ParticipantType.BYE)
    }

    var record: Record = Record()
    var color = TournamentUtil.DEFAULT_DISPLAY_COLOR

    override val key: String = person.name + participantType.name
    override fun compareTo(other: Participant): Int = displayName.compareTo(other.displayName)

    override fun toString(): String {
        return displayName
    }
}

