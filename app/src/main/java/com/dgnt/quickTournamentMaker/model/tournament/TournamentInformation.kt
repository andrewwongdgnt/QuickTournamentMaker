package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.joda.time.LocalDateTime


@Parcelize
data class TournamentInformation(
    var title: String,
    var description: String,
    val tournamentType: TournamentType,
    val seedType: SeedType,
    val rankConfig: IRankConfig,
    var creationDate: LocalDateTime,
    var lastModifiedDate: LocalDateTime? = null
) : Parcelable {

    //When just the title and description is needed
    constructor(title: String, description: String) : this(title, description, TournamentType.ELIMINATION, SeedType.RANDOM, RankPriorityConfig.DEFAULT, LocalDateTime.now())
}