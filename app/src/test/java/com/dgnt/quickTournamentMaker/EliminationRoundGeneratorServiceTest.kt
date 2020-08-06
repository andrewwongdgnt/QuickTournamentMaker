package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class EliminationRoundGeneratorServiceTest {
    private val sut = EliminationRoundGeneratorService()
    private lateinit var participants: List<Participant>

    @Before
    fun setUp() {
        participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)
    }

    @Test
    fun testTotalRoundGroup() {
        Assert.assertEquals(1, sut.build(participants).size)
    }

    @Test
    fun testTotalRounds() {
        Assert.assertEquals(3,sut.build(participants)[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = sut.build(participants)[0].rounds
        Assert.assertEquals(4, rounds[0].matchUps.size)
        Assert.assertEquals(2, rounds[1].matchUps.size)
        Assert.assertEquals(1, rounds[2].matchUps.size)

    }


}