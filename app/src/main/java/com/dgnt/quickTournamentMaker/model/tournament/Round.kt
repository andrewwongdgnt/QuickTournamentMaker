package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.data.tournament.RoundEntity
import com.dgnt.quickTournamentMaker.model.IKeyable
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime

@Parcelize
data class Round(
    val roundGroupIndex: Int,
    val roundIndex: Int,
    val matchUps: List<MatchUp>,
    val originalTitle: String,
    var title: String = originalTitle,
    var note: String = "",
    var color: Int = TournamentUtil.DEFAULT_DISPLAY_COLOR
) : IKeyable<Pair<Int, Int>>, Parcelable {
    fun isUpdatedTitle() = title != originalTitle
    fun getDisplayTitle() = (if (note.isEmpty()) "" else "*") + title
    fun toEntity(id: LocalDateTime) = RoundEntity(id, roundGroupIndex, roundIndex, originalTitle, title, note, color)

    override val key = Pair(roundGroupIndex, roundIndex)

}