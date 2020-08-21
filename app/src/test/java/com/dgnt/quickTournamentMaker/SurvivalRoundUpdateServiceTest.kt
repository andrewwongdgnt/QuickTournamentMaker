package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundUpdateService
import com.dgnt.quickTournamentMaker.service.implementation.SurvivalRoundUpdateService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SurvivalRoundUpdateServiceTest {

    private val sut = SurvivalRoundUpdateService()
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {

        val round1 = Round(listOf(MatchUp(Data.ANDREW,Participant.NULL_PARTICIPANT), MatchUp(Data.KYRA,Participant.NULL_PARTICIPANT), MatchUp(Data.DGNT,Participant.NULL_PARTICIPANT), MatchUp(Data.KELSEY,Participant.NULL_PARTICIPANT)))
        val round2 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round3 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round4 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT),MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        roundGroups = listOf(RoundGroup(listOf(round1,round2,round3,round4)))
    }

    @Test
    fun testP1Win_first() {
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups,0,0,0)
        Assert.assertEquals(Data.ANDREW,roundGroups[0].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[1].matchUps[2].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[1].matchUps[3].participant1)
    }

    @Test
    fun testP1Win_second() {
        roundGroups[0].rounds[0].matchUps[1].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups,0,0,1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.KYRA,roundGroups[0].rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[1].matchUps[2].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroups[0].rounds[1].matchUps[3].participant1)
    }



}