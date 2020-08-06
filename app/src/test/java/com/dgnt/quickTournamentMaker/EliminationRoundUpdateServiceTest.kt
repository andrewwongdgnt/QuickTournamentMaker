package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundUpdateService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EliminationRoundUpdateServiceTest {

    private val sut = EliminationRoundUpdateService()
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {

        val round1 = Round(listOf(MatchUp(Data.ANDREW,Data.KYRA), MatchUp(Data.DGNT,Data.KELSEY), MatchUp(Data.FIRE,Data.SUPER), MatchUp(Data.HERO,Data.DEMON)))
        val round2 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round3 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        roundGroups = listOf(RoundGroup(listOf(round1,round2,round3)))
    }

    @Test
    fun testP1Win_evenMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups,0,0,0)
        Assert.assertEquals(Data.ANDREW,roundGroups[0].rounds[1].matchUps[0].participant1)
    }

    @Test
    fun testP2Win_evenMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroups,0,0,0)
        Assert.assertEquals(Data.KYRA,roundGroups[0].rounds[1].matchUps[0].participant1)
    }

    @Test
    fun testP1Win_oddMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[1].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups,0,0,1)
        Assert.assertEquals(Data.DGNT,roundGroups[0].rounds[1].matchUps[0].participant2)
    }

    @Test
    fun testP2Win_oddMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[1].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroups,0,0,1)
        Assert.assertEquals(Data.KELSEY,roundGroups[0].rounds[1].matchUps[0].participant2)
    }

    @Test
    fun testCascade() {
        roundGroups[0].rounds[1].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups,0,1,0)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[2].matchUps[0].participant1)
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups,0,0,0)
        Assert.assertEquals(Data.ANDREW,roundGroups[0].rounds[2].matchUps[0].participant1)
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.DEFAULT
        sut.update(roundGroups,0,0,0)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[2].matchUps[0].participant1)
    }

}