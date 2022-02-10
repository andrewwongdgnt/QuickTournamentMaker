package com.dgnt.quickTournamentMaker.model.tournament

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.tournament.TournamentEntity
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService

enum class TournamentType(
    @StringRes
    val stringResource: Int,
    @DrawableRes
    val drawableResource: Int
) {
    ELIMINATION(R.string.elimination, R.drawable.ic_elimination),
    DOUBLE_ELIMINATION(R.string.doubleElimination, R.drawable.ic_double_elimination),
    ROUND_ROBIN(R.string.roundRobin, R.drawable.ic_round_robin),
    SWISS(R.string.swiss, R.drawable.ic_swiss),
    SURVIVAL(R.string.survival, R.drawable.ic_survival)
}

enum class SeedType(val resource: Int) {
    RANDOM(R.string.randomSeed),
    CUSTOM(R.string.customSeed),
    SAME(R.string.sameSeed)
}

data class Tournament(
    val tournamentInformation: TournamentInformation,
    val roundGroups: List<RoundGroup>,
    val matchUpStatusTransformService: IMatchUpStatusTransformService,
    val roundUpdateService: IRoundUpdateService,
    val rankingService: IRankingService,
    private val rankingConfigService: IRankingConfigService,
) {

    val rounds = roundGroups.flatMap { it.rounds }

    val matchUps = rounds.flatMap { it.matchUps }

    fun getNumMatchUpsWithSingleByes() = matchUps.count { it.containsBye(true) }

    val orderedParticipants = roundGroups.getOrNull(0)?.rounds?.getOrNull(0)?.matchUps?.flatMap { listOf(it.participant1, it.participant2) } ?: listOf()

    val sortedNormalParticipants = orderedParticipants.filter { it.participantType == ParticipantType.NORMAL }.sorted()

    fun updateRound(roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int) = roundUpdateService.update(roundGroups, roundGroupIndex, roundIndex, matchUpIndex, tournamentInformation.rankConfig)

    fun getRanking() = rankingService.calculate(roundGroups, tournamentInformation.rankConfig)

    fun toEntity() = tournamentInformation.run {
        TournamentEntity(
            creationDate,
            lastModifiedDate,
            title,
            description,
            tournamentType,
            when (tournamentType) {
                TournamentType.ROUND_ROBIN, TournamentType.SWISS -> rankingConfigService.toString(rankConfig)
                else -> ""
            },
            seedType
        )
    }
}
