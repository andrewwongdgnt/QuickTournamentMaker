package com.dgnt.quickTournamentMaker

import android.util.Log
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.ToggleStatusService
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class, IMatchUpStatusTransformService::class)
class ToggleStatusServiceTest {
    private val mockMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)

    private val sut = ToggleStatusService(mockMatchUpStatusTransformService)

    @Before
    fun setUp() {
    }

    @Test
    fun testByes() {
        val matchUp = MatchUp(0,0,0,Participant.BYE_PARTICIPANT, Participant.BYE_PARTICIPANT)
        Assert.assertFalse(sut.toggle(matchUp, ParticipantPosition.P1))
        Mockito.verify(mockMatchUpStatusTransformService, Mockito.never()).transform(MockitoHelper.anyObject(),MockitoHelper.anyObject())
    }

    @Test
    fun testStatusChange() {
        val matchUp = MatchUp(0,0,0,Data.ANDREW, Data.KYRA)
        matchUp.status = MatchUpStatus.P2_WINNER
        PowerMockito.`when`(mockMatchUpStatusTransformService.transform(matchUp.status,ParticipantPosition.P1)).thenReturn(MatchUpStatus.P1_WINNER)
        Assert.assertTrue(sut.toggle(matchUp, ParticipantPosition.P1))
        Assert.assertEquals(MatchUpStatus.P1_WINNER,matchUp.status)
    }
}