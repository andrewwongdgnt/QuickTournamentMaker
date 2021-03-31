package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.SurvivalRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito


class SurvivalRoundGeneratorServiceTest {
    private val mockParticipantService = PowerMockito.mock(IParticipantService::class.java)

    private val sut = SurvivalRoundGeneratorService(mockParticipantService)
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {
        val participants = listOf(Data.ANDREW, Participant.NULL_PARTICIPANT, Data.KYRA, Participant.NULL_PARTICIPANT, Data.DGNT, Participant.NULL_PARTICIPANT, Data.KELSEY, Participant.NULL_PARTICIPANT, Data.FIRE, Participant.NULL_PARTICIPANT, Data.SUPER, Participant.NULL_PARTICIPANT, Data.HERO, Participant.NULL_PARTICIPANT, Data.DEMON, Participant.NULL_PARTICIPANT)
        PowerMockito.`when`(mockParticipantService.createRound(participants)).thenReturn(
            Round(0,0,
                listOf(
                    MatchUp(0,0,0,Data.ANDREW, Participant.NULL_PARTICIPANT),
                    MatchUp(0,0,1,Data.KYRA, Participant.NULL_PARTICIPANT),
                    MatchUp(0,0,2,Data.DGNT, Participant.NULL_PARTICIPANT),
                    MatchUp(0,0,3,Data.KELSEY, Participant.NULL_PARTICIPANT),
                    MatchUp(0,0,4,Data.FIRE, Participant.NULL_PARTICIPANT),
                    MatchUp(0,0,5,Data.SUPER, Participant.NULL_PARTICIPANT),
                    MatchUp(0,0,6,Data.HERO, Participant.NULL_PARTICIPANT),
                    MatchUp(0,0,7,Data.DEMON, Participant.NULL_PARTICIPANT)
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
        Assert.assertEquals(7, roundGroups[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = roundGroups[0].rounds
        for (i in 0..6)
            Assert.assertEquals(8, rounds[i].matchUps.size)

    }


}