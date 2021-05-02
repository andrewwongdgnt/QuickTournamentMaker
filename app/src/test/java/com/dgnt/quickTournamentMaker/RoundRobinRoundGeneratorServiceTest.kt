package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.RoundRobinRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito


class RoundRobinRoundGeneratorServiceTest {
    private val mockParticipantService = PowerMockito.mock(IParticipantService::class.java)

    private val sut = RoundRobinRoundGeneratorService(mockParticipantService)
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {
        val participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)
        val defaultRoundTitleFunc: (Round) -> String = { r -> r.roundIndex.toString() }
        val defaultMatchUpTitleFunc: (MatchUp) -> String = { m -> m.matchUpIndex.toString() }
        PowerMockito.`when`(mockParticipantService.createRound(participants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)).thenReturn(
            Round(
                0, 0,
                listOf(
                    MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA),
                    MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY),
                    MatchUp(0, 0, 2, Data.FIRE, Data.SUPER),
                    MatchUp(0, 0, 3, Data.HERO, Data.DEMON)
                )
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
        Assert.assertEquals(7, roundGroups[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = roundGroups[0].rounds
        rounds.forEach {
            Assert.assertEquals(4, it.matchUps.size)
        }
    }

    @Test
    fun testParticipantPlacements() {
        val rounds = roundGroups[0].rounds
        Assert.assertEquals(Data.ANDREW, rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.DGNT, rounds[1].matchUps[0].participant2)
        Assert.assertEquals(Data.FIRE, rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Data.KYRA, rounds[1].matchUps[1].participant2)
        Assert.assertEquals(Data.HERO, rounds[1].matchUps[2].participant1)
        Assert.assertEquals(Data.KELSEY, rounds[1].matchUps[2].participant2)
        Assert.assertEquals(Data.DEMON, rounds[1].matchUps[3].participant1)
        Assert.assertEquals(Data.SUPER, rounds[1].matchUps[3].participant2)

        Assert.assertEquals(Data.ANDREW, rounds[2].matchUps[0].participant1)
        Assert.assertEquals(Data.FIRE, rounds[2].matchUps[0].participant2)
        Assert.assertEquals(Data.HERO, rounds[2].matchUps[1].participant1)
        Assert.assertEquals(Data.DGNT, rounds[2].matchUps[1].participant2)
        Assert.assertEquals(Data.DEMON, rounds[2].matchUps[2].participant1)
        Assert.assertEquals(Data.KYRA, rounds[2].matchUps[2].participant2)
        Assert.assertEquals(Data.SUPER, rounds[2].matchUps[3].participant1)
        Assert.assertEquals(Data.KELSEY, rounds[2].matchUps[3].participant2)

        //probably can test for the rest of the 5 rounds but too much work

    }


}