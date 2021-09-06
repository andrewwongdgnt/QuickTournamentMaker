package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.ByeStatusResolverService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ByeStatusResolverServiceTest {

    private val sut = ByeStatusResolverService()
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {

        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY),
                MatchUp(0, 0, 1, Data.FIRE, Participant.BYE_PARTICIPANT),
                MatchUp(0, 0, 2, Data.HERO, Participant.BYE_PARTICIPANT)
            )
        )
        val round2 = Round(
            0, 1,
            listOf(
                MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.BYE_PARTICIPANT),
                MatchUp(0, 1, 0, Participant.BYE_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )

        val round1_2 = Round(
            1, 0, listOf(
                MatchUp(1, 0, 0, Data.ANDREW, Data.KYRA),
                MatchUp(1, 0, 1, Data.DGNT, Data.KELSEY),
                MatchUp(1, 0, 2, Data.FIRE, Participant.BYE_PARTICIPANT),
                MatchUp(1, 0, 3, Data.HERO, Participant.BYE_PARTICIPANT)
            )
        )
        val round2_2 = Round(
            1, 1, listOf(
                MatchUp(1, 1, 0, Participant.NULL_PARTICIPANT, Participant.BYE_PARTICIPANT),
                MatchUp(1, 1, 1, Participant.BYE_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        roundGroups = listOf(RoundGroup(0, listOf(round1, round2)), RoundGroup(1, listOf(round1_2, round2_2)))
    }

    @Test
    fun testFirstRound() {
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[1].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[3].status)
        sut.resolve(roundGroups, Pair(0, 0))
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[1].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[3].status)
    }

    @Test
    fun testSecondRound() {
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[1].status)
        sut.resolve(roundGroups, Pair(0, 1))
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.P2_WINNER, roundGroups[0].rounds[1].matchUps[1].status)
    }



    @Test
    fun testSecondRoundGroup() {
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[1].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[1].rounds[0].matchUps[3].status)
        sut.resolve(roundGroups, Pair(1, 0))
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[1].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[1].rounds[0].matchUps[3].status)
    }
}