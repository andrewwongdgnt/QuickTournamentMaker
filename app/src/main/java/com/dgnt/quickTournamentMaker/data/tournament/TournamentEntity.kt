package com.dgnt.quickTournamentMaker.data.tournament

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import org.joda.time.LocalDateTime

const val TOURNAMENT_TABLE = "tournamentTable"

@Entity(tableName = TOURNAMENT_TABLE)
data class TournamentEntity(
    @PrimaryKey val epoch: LocalDateTime,
    val lastModifiedTime: LocalDateTime?,
    val name: String,
    val note: String,
    val type: TournamentType,
    val rankingConfig: String,
    val seedType: SeedType,
) {
    fun clone(
        epoch: LocalDateTime = this.epoch,
        lastModifiedTime: LocalDateTime? = this.lastModifiedTime,
        name: String = this.name,
        note: String = this.note,
        type: TournamentType = this.type,
        rankingConfig: String = this.rankingConfig,
        seedType: SeedType = this.seedType,
    ) =
        TournamentEntity(
            epoch,
            lastModifiedTime,
            name,
            note,
            type,
            rankingConfig,
            seedType
        )
}

