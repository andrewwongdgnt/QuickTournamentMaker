package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.management.Person
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.ByePopulateSeedService
import com.dgnt.quickTournamentMaker.service.implementation.ByeStatusResolverService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ByeStatusResolverServiceTest {

    private val sut: ByeStatusResolverService = ByeStatusResolverService()
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {

        val round1 = Round(listOf(MatchUp(Data.ANDREW,Data.KYRA), MatchUp(Data.DGNT,Data.KELSEY), MatchUp(Data.FIRE,Participant.BYE_PARTICIPANT), MatchUp(Data.HERO, Participant.BYE_PARTICIPANT)))
        val round2 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.BYE_PARTICIPANT), MatchUp(Participant.BYE_PARTICIPANT,Participant.NULL_PARTICIPANT)))

        val round1_2 = Round(listOf(MatchUp(Data.ANDREW,Data.KYRA), MatchUp(Data.DGNT,Data.KELSEY), MatchUp(Data.FIRE,Participant.BYE_PARTICIPANT), MatchUp(Data.HERO, Participant.BYE_PARTICIPANT)))
        val round2_2 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.BYE_PARTICIPANT), MatchUp(Participant.BYE_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        roundGroups = listOf(RoundGroup(listOf(round1,round2)),RoundGroup(listOf(round1_2,round2_2)))
    }

    @Test
    fun testFirstRound() {
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[3].status)
        sut.resolve(roundGroups,Pair(0,0))
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[3].status)
    }

    @Test
    fun testSecondRound() {
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[1].status)
        sut.resolve(roundGroups,Pair(0,1))
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.P2_WINNER, roundGroups[0].rounds[1].matchUps[1].status)
    }

    @Test
    fun testBothRounds() {
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[3].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[1].status)
        sut.resolve(roundGroups,Pair(0,0),Pair(0,1))
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[3].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.P2_WINNER, roundGroups[0].rounds[1].matchUps[1].status)
    }

    @Test
    fun testBothRoundGroups() {
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[0].matchUps[3].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[0].rounds[1].matchUps[1].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[1].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[1].rounds[0].matchUps[3].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[1].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.DEFAULT, roundGroups[1].rounds[1].matchUps[1].status)
        sut.resolve(roundGroups,Pair(0,0),Pair(0,1),Pair(1,0),Pair(1,1))
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[0].matchUps[3].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[0].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.P2_WINNER, roundGroups[0].rounds[1].matchUps[1].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[1].rounds[0].matchUps[2].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[1].rounds[0].matchUps[3].status)
        Assert.assertEquals(MatchUpStatus.P1_WINNER, roundGroups[1].rounds[1].matchUps[0].status)
        Assert.assertEquals(MatchUpStatus.P2_WINNER, roundGroups[1].rounds[1].matchUps[1].status)
    }
}