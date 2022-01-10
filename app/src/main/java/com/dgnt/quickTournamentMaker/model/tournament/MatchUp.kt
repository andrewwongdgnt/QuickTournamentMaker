package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.tournament.MatchUpEntity
import com.dgnt.quickTournamentMaker.model.IKeyable
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime


enum class MatchUpStatus {
    DEFAULT, P1_WINNER, P2_WINNER, TIE,
}

@Parcelize
data class MatchUp(
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val matchUpIndex: Int,
    var participant1: Participant,
    var participant2: Participant,
    var title: String,
    var useTitle: Boolean = false,
    var status: MatchUpStatus = MatchUpStatus.DEFAULT,
    var note: String = "",
    var color: Int = TournamentUtil.DEFAULT_DISPLAY_COLOR
) : IKeyable<Triple<Int, Int, Int>>, Parcelable {
    @IgnoredOnParcel
    override val key = Triple(roundGroupIndex, roundIndex, matchUpIndex)
    fun containsBye(singular: Boolean = false) =
        (participant1.participantType == ParticipantType.BYE || participant2.participantType == ParticipantType.BYE)
                && (!singular || !(participant1.participantType == ParticipantType.BYE && participant2.participantType == ParticipantType.BYE))

    fun getDisplayTitle() = (if (note.isEmpty()) "" else "*") + title
    fun toEntity(id: LocalDateTime) = MatchUpEntity(id, roundGroupIndex, roundIndex, matchUpIndex, useTitle, title, note, color, status, containsBye(true))
}