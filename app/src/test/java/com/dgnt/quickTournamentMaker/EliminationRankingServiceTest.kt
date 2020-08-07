package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito

class EliminationRankingServiceTest {

    private val mockEliminationRankingHelperService = PowerMockito.mock(IEliminationRankingHelperService::class.java)

    private val sut = EliminationRankingService(mockEliminationRankingHelperService)

    private lateinit var rounds:List<Round>

    @Before
    fun setUp() {

        val roundGroups = listOf(
            RoundGroup(
                listOf(
                    Round(listOf(MatchUp(Data.ANDREW, Data.KYRA), MatchUp(Data.DGNT, Data.KELSEY), MatchUp(Data.FIRE, Data.SUPER), MatchUp(Data.HERO, Data.DEMON))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
                )
            )
        )
        rounds = roundGroups.first().rounds
        sut.calculate(roundGroups)

    }

    @Test
    fun testRoundGroupSplitting() {
        Mockito.verify(mockEliminationRankingHelperService, Mockito.times(1)).calculate(rounds,rounds.dropLast(1),rounds.takeLast(1))
    }

}