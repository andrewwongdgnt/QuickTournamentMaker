package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.EliminationSeedService
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class EliminationSeedServiceTest {
    private val sut: EliminationSeedService = EliminationSeedService()
    private lateinit var participantList: List<Participant>

    @Before
    fun setUp() {
        participantList = listOf(ParticipantData.ANDREW, ParticipantData.KYRA, ParticipantData.DGNT, ParticipantData.KELSEY, ParticipantData.FIRE, ParticipantData.SUPER, ParticipantData.HERO, ParticipantData.DEMON)
    }

    @Test
    fun testSeedCheckSuccess() {
        Assert.assertTrue(sut.seedCheck(participantList))
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
        Assert.assertEquals(4+2+1, sut.build(participantList)[0].rounds.fold(0){sum,round -> sum+round.matchUps.size})
    }
}