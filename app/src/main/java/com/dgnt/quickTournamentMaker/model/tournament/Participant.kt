package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.model.IKeyable
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.parcelize.Parcelize

enum class ParticipantType {
    NORMAL, NULL, BYE
}

enum class ParticipantPosition {
    P1, P2
}

@Parcelize
data class Participant(
    val person: Person,
    val participantType: ParticipantType = ParticipantType.NORMAL,
    var displayName: String = person.name,
    var note: String = person.note,
    var record: Record = Record(),
    var color: Int = TournamentUtil.DEFAULT_DISPLAY_COLOR
) : IKeyable<String>, Comparable<Participant>, Parcelable {
    companion object {
        val NULL_PARTICIPANT =
            Participant(Person("", "", ""), ParticipantType.NULL)

        val BYE_PARTICIPANT =
            Participant(Person("", "", ""), ParticipantType.BYE)
    }

    fun updatedTitle() = displayName != person.name

    override val key = person.name + participantType.name
    override fun compareTo(other: Participant): Int = displayName.compareTo(other.displayName)

    override fun toString(): String {
        return displayName
    }
}

