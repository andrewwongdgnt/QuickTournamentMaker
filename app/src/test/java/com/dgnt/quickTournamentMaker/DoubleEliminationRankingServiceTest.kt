package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.DoubleEliminationRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito

class DoubleEliminationRankingServiceTest {

    private val mockEliminationRankingHelperService = PowerMockito.mock(IEliminationRankingHelperService::class.java)

    private val sut = DoubleEliminationRankingService(mockEliminationRankingHelperService)

    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {

        roundGroups = listOf(
            RoundGroup(
                listOf(
                    Round(listOf(MatchUp(Data.ANDREW, Data.KYRA), MatchUp(Data.DGNT, Data.KELSEY), MatchUp(Data.FIRE, Data.SUPER), MatchUp(Data.HERO, Data.DEMON))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
                )
            ), RoundGroup(
                listOf(
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
                )
            ), RoundGroup(
                listOf(
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
                )
            )
        )
        sut.calculate(roundGroups)

    }

    @Test
    fun testRoundGroupSplitting() {
        Mockito.verify(mockEliminationRankingHelperService, Mockito.times(1)).calculate(roundGroups.first().rounds, roundGroups[1].rounds, roundGroups.last().rounds)
    }

}