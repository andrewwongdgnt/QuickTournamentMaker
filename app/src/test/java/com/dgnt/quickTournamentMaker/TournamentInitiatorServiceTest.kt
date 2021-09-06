package com.dgnt.quickTournamentMaker

import android.util.Log
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.ByeStatusResolverService
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.implementation.TournamentInitiatorService
import com.dgnt.quickTournamentMaker.service.interfaces.IByeStatusResolverService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class TournamentInitiatorServiceTest {

    private val mockByeStatusResolverService = PowerMockito.mock(IByeStatusResolverService::class.java)
    private val sut = TournamentInitiatorService(mockByeStatusResolverService)

    private val mockTournament = PowerMockito.mock(Tournament::class.java)
    private val mockTournamentInformation = PowerMockito.mock(TournamentInformation::class.java)

    @Before
    fun setUp() {
       // PowerMockito.`when`(mockByeStatusResolverService.resolve()).thenReturn()

        PowerMockito.`when`(mockTournament.tournamentInformation).thenReturn(mockTournamentInformation)

    }

    @Test
    fun testSurvival() {
        PowerMockito.`when`(mockTournamentInformation.tournamentType).thenReturn(TournamentType.SURVIVAL)
        sut.initiate(mockTournament)

        Mockito.verify(mockByeStatusResolverService, Mockito.never()).resolve(MockitoHelper.anyObject(),MockitoHelper.anyObject())

    }
}