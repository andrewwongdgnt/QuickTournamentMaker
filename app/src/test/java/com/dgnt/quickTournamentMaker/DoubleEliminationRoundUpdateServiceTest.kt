package com.dgnt.quickTournamentMaker

import android.util.Log
import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.DoubleEliminationRoundUpdateService
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundUpdateService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class, EliminationRoundUpdateService::class)
class DoubleEliminationRoundUpdateServiceTest {
    private val mockEliminationRoundUpdateService: EliminationRoundUpdateService = PowerMockito.mock(EliminationRoundUpdateService::class.java)

    private val sut: DoubleEliminationRoundUpdateService = DoubleEliminationRoundUpdateService(mockEliminationRoundUpdateService)
    private lateinit var roundGroupsNoByes: List<RoundGroup>

    @Before
    fun setUp() {

        val round1WB = Round(listOf(MatchUp(Data.ANDREW,Data.KYRA), MatchUp(Data.DGNT,Data.KELSEY), MatchUp(Data.FIRE,Data.SUPER), MatchUp(Data.HERO,Data.DEMON)))
        val round2WB = Round(listOf(MatchUp(Data.KYRA,Data.KELSEY), MatchUp(Data.FIRE,Data.DEMON)))
        val round3WB = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))

        val round1LB = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round2LB = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round3LB = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round4LB = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))

        val round1FB = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round2FB = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))

        roundGroupsNoByes = listOf(RoundGroup(listOf(round1WB,round2WB,round3WB)),RoundGroup(listOf(round1LB,round2LB,round3LB,round4LB)),RoundGroup(listOf(round1FB,round2FB)))

    }

    @Test
    fun testWinnerToLoser() {
        roundGroupsNoByes[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes,0,0,0)
        roundGroupsNoByes[0].rounds[0].matchUps[1].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes,0,0,1)
        roundGroupsNoByes[0].rounds[0].matchUps[2].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes,0,0,2)
        roundGroupsNoByes[0].rounds[0].matchUps[3].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes,0,0,3)
        Assert.assertEquals(Data.KYRA,roundGroupsNoByes[1].rounds[0].matchUps[0].participant1)
        Assert.assertEquals(Data.DGNT,roundGroupsNoByes[1].rounds[0].matchUps[0].participant2)
        Assert.assertEquals(Data.SUPER,roundGroupsNoByes[1].rounds[0].matchUps[1].participant1)
        Assert.assertEquals(Data.HERO,roundGroupsNoByes[1].rounds[0].matchUps[1].participant2)

        roundGroupsNoByes[1].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes,1,0,0)
        roundGroupsNoByes[1].rounds[0].matchUps[1].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes,1,0,1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroupsNoByes[1].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.KYRA,roundGroupsNoByes[1].rounds[1].matchUps[0].participant2)
        Assert.assertEquals(Participant.NULL_PARTICIPANT,roundGroupsNoByes[1].rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Data.HERO,roundGroupsNoByes[1].rounds[1].matchUps[1].participant2)

        roundGroupsNoByes[0].rounds[0].matchUps[0].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes,0,0,0)
        Assert.assertEquals(Data.ANDREW,roundGroupsNoByes[1].rounds[0].matchUps[0].participant1)
        Assert.assertEquals(Data.ANDREW,roundGroupsNoByes[1].rounds[0].matchUps[0].participant1)

        roundGroupsNoByes[0].rounds[1].matchUps[0].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes,0,1,0)
        roundGroupsNoByes[0].rounds[1].matchUps[1].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes,0,1,1)
        Assert.assertEquals(Data.DEMON,roundGroupsNoByes[1].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.KYRA,roundGroupsNoByes[1].rounds[1].matchUps[1].participant1)
    }



}