package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class EliminationRoundGeneratorServiceTest {
    private val sut: EliminationRoundGeneratorService = EliminationRoundGeneratorService()
    private lateinit var participantList: List<Participant>

    @Before
    fun setUp() {
        participantList = listOf(ParticipantData.ANDREW, ParticipantData.KYRA, ParticipantData.DGNT, ParticipantData.KELSEY, ParticipantData.FIRE, ParticipantData.SUPER, ParticipantData.HERO, ParticipantData.DEMON)
    }

    @Test
    fun testSeedCheck() {
        Assert.assertTrue(sut.seedCheck(participantList))
        Assert.assertFalse(sut.seedCheck(participantList.dropLast(1)))
        Assert.assertFalse(sut.seedCheck(participantList.dropLast(2)))
        Assert.assertTrue(sut.seedCheck(participantList.dropLast(4)))
    }


    @Test
    fun testTotalRoundGroup() {
        Assert.assertEquals(1, sut.build(participantList).size)
    }

    @Test
    fun testTotalRounds() {
        Assert.assertEquals(3,sut.build(participantList)[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = sut.build(participantList)[0].rounds
        Assert.assertEquals(4, rounds[0].matchUps.size)
        Assert.assertEquals(2, rounds[1].matchUps.size)
        Assert.assertEquals(1, rounds[2].matchUps.size)

    }
}