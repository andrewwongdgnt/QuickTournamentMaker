package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.service.implementation.ParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class ParticipantServiceTest {

    private val sut = ParticipantService()
    private lateinit var round: Round
    private val participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)

    @Before
    fun setUp() {
        round = sut.createRound(participants)
    }

    @Test
    fun testCreateRound() {
        Assert.assertEquals(Data.ANDREW, round.matchUps[0].participant1)
        Assert.assertEquals(Data.KYRA, round.matchUps[0].participant2)
        Assert.assertEquals(Data.DGNT, round.matchUps[1].participant1)
        Assert.assertEquals(Data.KELSEY, round.matchUps[1].participant2)
        Assert.assertEquals(Data.FIRE, round.matchUps[2].participant1)
        Assert.assertEquals(Data.SUPER, round.matchUps[2].participant2)
        Assert.assertEquals(Data.HERO, round.matchUps[3].participant1)
        Assert.assertEquals(Data.DEMON, round.matchUps[3].participant2)

    }
}