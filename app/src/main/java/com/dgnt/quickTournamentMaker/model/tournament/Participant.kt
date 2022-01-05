package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.tournament.MatchUpEntity
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantEntity
import com.dgnt.quickTournamentMaker.model.IKeyable
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime

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
    var name: String = person.name,
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

    fun isUpdatedTitle() = name != person.name
    fun getDisplayName() = (if (note.isEmpty()) "" else "*") + name

    override val key = person.name + participantType.name
    override fun compareTo(other: Participant): Int = name.compareTo(other.name)

    override fun toString(): String {
        return name
    }

    fun toEntity(id: LocalDateTime): ParticipantEntity {
        TODO("Not yet implemented")
    }
}

