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
    constructor(roundGroupIndex: Int, roundIndex: Int, title: String, note: String, color: Int) :
            this(roundGroupIndex, roundIndex, listOf(), "", title, note, color)

    fun isUpdatedTitle() = title != originalTitle
    fun getDisplayTitle() = (if (note.isEmpty()) "" else "*") + title
    fun toEntity(id: LocalDateTime) = RoundEntity(id, roundGroupIndex, roundIndex, title, note, color)
    fun updateWith(entity: RoundEntity) {
        title = entity.name
        note = entity.note
        color = entity.color
    }

    override val key = Pair(roundGroupIndex, roundIndex)

}