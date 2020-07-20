package com.dgnt.quickTournamentMaker

import android.util.Log
import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.DoubleEliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class, EliminationRoundGeneratorService::class)
class DoubleEliminationRoundGeneratorServiceTest {
    private val mockEliminationRoundGeneratorService: EliminationRoundGeneratorService = PowerMockito.mock(EliminationRoundGeneratorService::class.java)

    private val sut: DoubleEliminationRoundGeneratorService = DoubleEliminationRoundGeneratorService(mockEliminationRoundGeneratorService)
    private lateinit var participantList: List<Participant>

    @Before
    fun setUp() {
        participantList = listOf(ParticipantData.ANDREW, ParticipantData.KYRA, ParticipantData.DGNT, ParticipantData.KELSEY, ParticipantData.FIRE, ParticipantData.SUPER, ParticipantData.HERO, ParticipantData.DEMON)

        val round1 = Round(listOf(MatchUp(ParticipantData.ANDREW,ParticipantData.KYRA), MatchUp(ParticipantData.DGNT,ParticipantData.KELSEY),MatchUp(ParticipantData.FIRE,ParticipantData.SUPER),MatchUp(ParticipantData.HERO,ParticipantData.DEMON)))
        val round2 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        val round3 = Round(listOf(MatchUp(Participant.NULL_PARTICIPANT,Participant.NULL_PARTICIPANT)))
        PowerMockito.`when`(mockEliminationRoundGeneratorService.build(participantList)).thenReturn(listOf(RoundGroup(listOf(round1,round2,round3))))

        PowerMockito.`when`(mockEliminationRoundGeneratorService.seedCheck(participantList)).thenReturn(true)
    }

    @Test
    fun testSeedCheck() {
        sut.build(participantList)
        Mockito.verify(mockEliminationRoundGeneratorService, Mockito.times(1)).seedCheck(participantList)
    }


    @Test
    fun testTotalRoundGroup() {
        Assert.assertEquals(3, sut.build(participantList).size)
    }

    @Test
    fun testTotalRounds() {
        Assert.assertEquals(3,sut.build(participantList)[0].rounds.size)
        Assert.assertEquals(4,sut.build(participantList)[1].rounds.size)
        Assert.assertEquals(2,sut.build(participantList)[2].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val winnerRounds = sut.build(participantList)[0].rounds
        Assert.assertEquals(4, winnerRounds[0].matchUps.size)
        Assert.assertEquals(2, winnerRounds[1].matchUps.size)
        Assert.assertEquals(1, winnerRounds[2].matchUps.size)

        val loserRounds = sut.build(participantList)[1].rounds
        Assert.assertEquals(2, loserRounds[0].matchUps.size)
        Assert.assertEquals(2, loserRounds[1].matchUps.size)
        Assert.assertEquals(1, loserRounds[2].matchUps.size)
        Assert.assertEquals(1, loserRounds[3].matchUps.size)

        val finalRounds = sut.build(participantList)[2].rounds
        Assert.assertEquals(1, finalRounds[0].matchUps.size)
        Assert.assertEquals(1, finalRounds[1].matchUps.size)

    }
}