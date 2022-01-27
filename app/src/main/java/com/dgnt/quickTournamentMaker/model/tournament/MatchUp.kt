package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.tournament.MatchUpEntity
import com.dgnt.quickTournamentMaker.data.tournament.RoundEntity
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
    constructor(roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int, title: String, useTitle: Boolean, note:String, color:Int):
            this(roundGroupIndex, roundIndex, matchUpIndex, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, title, useTitle, MatchUpStatus.DEFAULT, note, color)

    @IgnoredOnParcel
    override val key = Triple(roundGroupIndex, roundIndex, matchUpIndex)
    fun containsBye(singular: Boolean = false) =
        (participant1.participantType == ParticipantType.BYE || participant2.participantType == ParticipantType.BYE)
                && (!singular || !(participant1.participantType == ParticipantType.BYE && participant2.participantType == ParticipantType.BYE))

    fun getDisplayTitle() = (if (note.isEmpty()) "" else "*") + title
    fun toEntity(id: LocalDateTime) = MatchUpEntity(id, roundGroupIndex, roundIndex, matchUpIndex, useTitle, title, note, color, status, containsBye(true))
    fun updateWith(entity: MatchUpEntity) {
        title = entity.name
        useTitle = entity.useTitle
        status = entity.status
        note = entity.note
        color = entity.color
    }
}