package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.management.Person
import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantPosition
import com.dgnt.quickTournamentMaker.service.implementation.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TieMatchUpStatusTransformServiceTest {
    private val sut = TieMatchUpStatusTransformService()

    @Before
    fun setUp() {
    }

    @Test
    fun testDefaultToP1Winner() {
        Assert.assertEquals(MatchUpStatus.P1_WINNER,sut.transform(MatchUpStatus.DEFAULT,ParticipantPosition.P1))
    }

    @Test
    fun testDefaultToP2Winner() {
        Assert.assertEquals(MatchUpStatus.P2_WINNER,sut.transform(MatchUpStatus.DEFAULT,ParticipantPosition.P2))
    }

    @Test
    fun testP1WinnerToTie() {
        Assert.assertEquals(MatchUpStatus.TIE,sut.transform(MatchUpStatus.P1_WINNER,ParticipantPosition.P2))
    }

    @Test
    fun testP1WinnerToDefault() {
        Assert.assertEquals(MatchUpStatus.DEFAULT,sut.transform(MatchUpStatus.P1_WINNER,ParticipantPosition.P1))
    }

    @Test
    fun testP2WinnerToTie() {
        Assert.assertEquals(MatchUpStatus.TIE,sut.transform(MatchUpStatus.P2_WINNER,ParticipantPosition.P1))
    }

    @Test
    fun testP2WinnerToDefault() {
        Assert.assertEquals(MatchUpStatus.DEFAULT,sut.transform(MatchUpStatus.P2_WINNER,ParticipantPosition.P2))
    }

    @Test
    fun testTieToP1Winner() {
        Assert.assertEquals(MatchUpStatus.P1_WINNER,sut.transform(MatchUpStatus.TIE,ParticipantPosition.P2))
    }

    @Test
    fun testTieToP2Winner() {
        Assert.assertEquals(MatchUpStatus.P2_WINNER,sut.transform(MatchUpStatus.TIE,ParticipantPosition.P1))
    }


}