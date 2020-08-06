package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.implementation.ParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito


class ParticipantServiceTest {

    private val sut = ParticipantService()
    private lateinit var round: Round

    @Before
    fun setUp() {
        round = sut.createRound(listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON))
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