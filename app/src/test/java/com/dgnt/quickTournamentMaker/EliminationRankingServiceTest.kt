package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito

class EliminationRankingServiceTest {

    private val mockEliminationRankingHelperService = PowerMockito.mock(IEliminationRankingHelperService::class.java)

    private val sut = EliminationRankingService(mockEliminationRankingHelperService)

    private lateinit var rounds: List<Round>

    @Before
    fun setUp() {

        val roundGroups = listOf(
            RoundGroup(
                0,
                listOf(
                    Round(
                        0, 0, listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                            MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                            MatchUp(0, 0, 2, Data.FIRE, Data.SUPER, ""),
                            MatchUp(0, 0, 3, Data.HERO, Data.DEMON, "")
                        ),
                        ""
                    ),
                    Round(
                        0, 1, listOf(
                            MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                            MatchUp(0, 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
                        ),
                        ""
                    ),
                    Round(
                        0, 2, listOf(
                            MatchUp(0, 2, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
                        ),
                        ""
                    )
                ),
                ""
            )
        )
        rounds = roundGroups.first().rounds
        sut.calculate(roundGroups)

    }

    @Test
    fun testRoundGroupSplitting() {
        Mockito.verify(mockEliminationRankingHelperService, Mockito.times(1)).calculate(rounds, rounds.dropLast(1), rounds.takeLast(1))
    }


}