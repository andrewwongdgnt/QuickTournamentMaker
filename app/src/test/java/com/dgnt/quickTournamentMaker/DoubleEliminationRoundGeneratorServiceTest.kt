package com.dgnt.quickTournamentMaker

import android.util.Log
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.DoubleEliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class, EliminationRoundGeneratorService::class)
class DoubleEliminationRoundGeneratorServiceTest {
    private val mockRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)

    private val sut = DoubleEliminationRoundGeneratorService(mockRoundGeneratorService)
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {
        val participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)


        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY),
                MatchUp(0, 0, 2, Data.FIRE, Data.SUPER),
                MatchUp(0, 0, 3, Data.HERO, Data.DEMON)
            )
        )
        val round2 = Round(
            0, 1, listOf(
                MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        val round3 = Round(
            0, 2, listOf(
                MatchUp(0, 2, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        PowerMockito.`when`(mockRoundGeneratorService.build(participants)).thenReturn(listOf(RoundGroup(0, listOf(round1, round2, round3))))

        roundGroups = sut.build(participants)

    }

    @Test
    fun testTotalRoundGroup() {
        Assert.assertEquals(3, roundGroups.size)
    }

    @Test
    fun testTotalRounds() {
        Assert.assertEquals(3, roundGroups[0].rounds.size)
        Assert.assertEquals(4, roundGroups[1].rounds.size)
        Assert.assertEquals(2, roundGroups[2].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val winnerRounds = roundGroups[0].rounds
        Assert.assertEquals(4, winnerRounds[0].matchUps.size)
        Assert.assertEquals(2, winnerRounds[1].matchUps.size)
        Assert.assertEquals(1, winnerRounds[2].matchUps.size)

        val loserRounds = roundGroups[1].rounds
        Assert.assertEquals(2, loserRounds[0].matchUps.size)
        Assert.assertEquals(2, loserRounds[1].matchUps.size)
        Assert.assertEquals(1, loserRounds[2].matchUps.size)
        Assert.assertEquals(1, loserRounds[3].matchUps.size)

        val finalRounds = roundGroups[2].rounds
        Assert.assertEquals(1, finalRounds[0].matchUps.size)
        Assert.assertEquals(1, finalRounds[1].matchUps.size)

    }
}