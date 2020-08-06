package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.implementation.RoundRobinRoundGeneratorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class RoundRobinRoundGeneratorServiceTest {
    private val sut = RoundRobinRoundGeneratorService()
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
        Assert.assertEquals(7,sut.build(participants)[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = sut.build(participants)[0].rounds
        rounds.forEach {
            Assert.assertEquals(4, it.matchUps.size)
        }
    }

    @Test
    fun testParticipantPlacements() {
        val rounds = sut.build(participants)[0].rounds
        Assert.assertEquals(Data.ANDREW, rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.DGNT, rounds[1].matchUps[0].participant2)
        Assert.assertEquals(Data.FIRE, rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Data.KYRA, rounds[1].matchUps[1].participant2)
        Assert.assertEquals(Data.HERO, rounds[1].matchUps[2].participant1)
        Assert.assertEquals(Data.KELSEY, rounds[1].matchUps[2].participant2)
        Assert.assertEquals(Data.DEMON, rounds[1].matchUps[3].participant1)
        Assert.assertEquals(Data.SUPER, rounds[1].matchUps[3].participant2)

        Assert.assertEquals(Data.ANDREW, rounds[2].matchUps[0].participant1)
        Assert.assertEquals(Data.FIRE, rounds[2].matchUps[0].participant2)
        Assert.assertEquals(Data.HERO, rounds[2].matchUps[1].participant1)
        Assert.assertEquals(Data.DGNT, rounds[2].matchUps[1].participant2)
        Assert.assertEquals(Data.DEMON, rounds[2].matchUps[2].participant1)
        Assert.assertEquals(Data.KYRA, rounds[2].matchUps[2].participant2)
        Assert.assertEquals(Data.SUPER, rounds[2].matchUps[3].participant1)
        Assert.assertEquals(Data.KELSEY, rounds[2].matchUps[3].participant2)

        //probably can test for the rest of the 5 rounds but too much work

    }


}