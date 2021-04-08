package com.dgnt.quickTournamentMaker

import android.content.res.Resources
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.service.implementation.CreateDefaultTitleService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito

class CreateDefaultTitleServiceTest {

    private val round1 = "Round 1"
    private val participant1 = Data.ANDREW.displayName
    private val participant2 = Data.KELSEY.displayName
    private val p1vsp2 = "p1 vs p2"
    private val p1WithABye = "p1 with a bye"
    private val p2WithABye = "p1 with a bye"
    private val emptyMatch = "match 1"
    private val p1Waiting = "p1 waiting"
    private val p2Waiting = "p2 waiting"
    private val player1 = "Player 1"

    private val sut = CreateDefaultTitleService()

    private val resources: Resources = PowerMockito.mock(Resources::class.java)

    @Before
    fun setUp() {
        PowerMockito.`when`(resources.getString(R.string.roundHeader, 1)).thenReturn(round1)
        PowerMockito.`when`(resources.getString(R.string.participant1VsParticipant2, participant1, participant2)).thenReturn(p1vsp2)
        PowerMockito.`when`(resources.getString(R.string.participantWithABye, participant1)).thenReturn(p1WithABye)
        PowerMockito.`when`(resources.getString(R.string.participantWithABye, participant2)).thenReturn(p2WithABye)
        PowerMockito.`when`(resources.getString(R.string.emptyMatchUp, 1)).thenReturn(emptyMatch)
        PowerMockito.`when`(resources.getString(R.string.participantVsNoOne, participant1)).thenReturn(p1Waiting)
        PowerMockito.`when`(resources.getString(R.string.participantVsNoOne, participant2)).thenReturn(p2Waiting)
        PowerMockito.`when`(resources.getString(R.string.participantDefaultName, 1)).thenReturn(player1)

    }

    @Test
    fun testForRound() {
        Assert.assertEquals(round1, sut.forRound(resources, Round(0, 0, listOf())))
    }

    @Test
    fun testForMatchUp() {
        Assert.assertEquals(p1vsp2, sut.forMatchUp(resources, MatchUp(0, 0, 0, Data.ANDREW, Data.KELSEY)))
        Assert.assertEquals(p1WithABye, sut.forMatchUp(resources, MatchUp(0, 0, 0, Data.ANDREW, Participant.BYE_PARTICIPANT)))
        Assert.assertEquals(p2WithABye, sut.forMatchUp(resources, MatchUp(0, 0, 0, Data.KELSEY, Participant.BYE_PARTICIPANT)))
        Assert.assertEquals(emptyMatch, sut.forMatchUp(resources, MatchUp(0, 0, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
        Assert.assertEquals(p1Waiting, sut.forMatchUp(resources, MatchUp(0, 0, 0, Data.ANDREW, Participant.NULL_PARTICIPANT)))
        Assert.assertEquals(p2Waiting, sut.forMatchUp(resources, MatchUp(0, 0, 0, Data.KELSEY, Participant.NULL_PARTICIPANT)))
    }

    @Test
    fun testForParticipant() {
        Assert.assertEquals(player1, sut.forParticipant(resources, 1))
    }

}