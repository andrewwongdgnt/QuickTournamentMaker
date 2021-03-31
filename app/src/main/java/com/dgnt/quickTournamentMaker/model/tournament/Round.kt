package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.model.IKeyable
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Round(
    val roundGroupIndex: Int,
    val roundIndex: Int,
    var matchUps: List<MatchUp>
) : IKeyable, Parcelable {
    var title: String = ""
    var note: String = ""
    var color: Int = TournamentUtil.DEFAULT_DISPLAY_COLOR
    override val key: String = "$roundGroupIndex;$roundIndex"

}