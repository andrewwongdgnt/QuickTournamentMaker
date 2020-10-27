package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantPosition
import com.dgnt.quickTournamentMaker.service.implementation.OneWinnerMatchUpStatusTransformService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class OneWinnerMatchUpStatusTransformServiceTest {
    private val sut = OneWinnerMatchUpStatusTransformService()

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
    fun testP1WinnerToP2Winner() {
        Assert.assertEquals(MatchUpStatus.P2_WINNER,sut.transform(MatchUpStatus.P1_WINNER,ParticipantPosition.P2))
    }

    @Test
    fun testP1WinnerToDefault() {
        Assert.assertEquals(MatchUpStatus.DEFAULT,sut.transform(MatchUpStatus.P1_WINNER,ParticipantPosition.P1))
    }

    @Test
    fun testP2WinnerToP1Winner() {
        Assert.assertEquals(MatchUpStatus.P1_WINNER,sut.transform(MatchUpStatus.P2_WINNER,ParticipantPosition.P1))
    }

    @Test
    fun testP2WinnerToDefault() {
        Assert.assertEquals(MatchUpStatus.DEFAULT,sut.transform(MatchUpStatus.P2_WINNER,ParticipantPosition.P2))
    }

    @Test
    fun testInvalidConditions() {
        Assert.assertEquals(MatchUpStatus.DEFAULT,sut.transform(MatchUpStatus.TIE,ParticipantPosition.P2))
        Assert.assertEquals(MatchUpStatus.DEFAULT,sut.transform(MatchUpStatus.TIE,ParticipantPosition.P1))
    }


}