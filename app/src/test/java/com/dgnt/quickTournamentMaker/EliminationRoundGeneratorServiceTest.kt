package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito


class EliminationRoundGeneratorServiceTest {
    private val mockParticipantService = PowerMockito.mock(IParticipantService::class.java)

    private val sut = EliminationRoundGeneratorService(mockParticipantService)
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {
        val participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)
        PowerMockito.`when`(mockParticipantService.createRound(participants)).thenReturn(
            Round(
                listOf(
                    MatchUp(Data.ANDREW,Data.KYRA),
                    MatchUp(Data.DGNT,Data.KELSEY),
                    MatchUp(Data.FIRE,Data.SUPER),
                    MatchUp(Data.HERO,Data.DEMON)
                )
            )
        );
        roundGroups = sut.build(participants)
    }

    @Test
    fun testTotalRoundGroup() {
        Assert.assertEquals(1, roundGroups.size)
    }

    @Test
    fun testTotalRounds() {
        Assert.assertEquals(3,roundGroups[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = roundGroups[0].rounds
        Assert.assertEquals(4, rounds[0].matchUps.size)
        Assert.assertEquals(2, rounds[1].matchUps.size)
        Assert.assertEquals(1, rounds[2].matchUps.size)

    }


}