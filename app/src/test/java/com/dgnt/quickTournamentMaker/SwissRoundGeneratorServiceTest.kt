package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.SwissRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito


class SwissRoundGeneratorServiceTest {
    private val mockParticipantService = PowerMockito.mock(IParticipantService::class.java)

    private val sut = SwissRoundGeneratorService(mockParticipantService)
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {
        val participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY)
        val defaultRoundTitleFunc: (Int) -> String = { rIndex -> rIndex.toString() }
        val defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String = {  mIndex, p1, p2 -> "$mIndex, $p1, $p2" }
        PowerMockito.`when`(mockParticipantService.createRound(participants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)).thenReturn(
            Round(
                0, 0,
                listOf(
                    MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                    MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, "")
                ),
                ""
            )
        );
        roundGroups = sut.build(participants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)

    }

    @Test
    fun testTotalRoundGroup() {
        Assert.assertEquals(1, roundGroups.size)
    }

    @Test
    fun testTotalRounds() {
        Assert.assertEquals(3, roundGroups[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = roundGroups[0].rounds
        rounds.forEach {
            Assert.assertEquals(2, it.matchUps.size)
        }
    }

    @Test
    fun testParticipantPlacements() {
        val rounds = roundGroups[0].rounds
        for (i in 1..2) {
            for (j in 0..1) {
                val matchUp = rounds[i].matchUps[j]
                Assert.assertEquals(Participant.NULL_PARTICIPANT, matchUp.participant1)
                Assert.assertEquals(Participant.NULL_PARTICIPANT, matchUp.participant2)
            }
        }


    }


}