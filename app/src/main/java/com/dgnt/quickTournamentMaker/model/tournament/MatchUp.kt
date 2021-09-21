package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.model.IKeyable
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.parcelize.Parcelize


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
    override val key = Triple(roundGroupIndex, roundIndex, matchUpIndex)
    fun containsBye() = participant1.participantType == ParticipantType.BYE || participant2.participantType == ParticipantType.BYE
}