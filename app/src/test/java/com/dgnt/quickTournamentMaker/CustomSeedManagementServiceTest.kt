package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.service.implementation.CustomSeedManagementService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito

class CustomSeedManagementServiceTest {
    private val mockParticipantService = PowerMockito.mock(IParticipantService::class.java)

    private val sut = CustomSeedManagementService(mockParticipantService)
    private lateinit var participants: List<Participant>
    private lateinit var participantsWithBye: List<Participant>

    @Before
    fun setUp() {

        participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)
        PowerMockito.`when`(mockParticipantService.createRound(participants)).thenReturn(
            Round(
                0, 0,
                listOf(
                    MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA),
                    MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY),
                    MatchUp(0, 0, 2, Data.FIRE, Data.SUPER),
                    MatchUp(0, 0, 3, Data.HERO, Data.DEMON)
                )
            )
        )

        participantsWithBye = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Participant.BYE_PARTICIPANT, Data.FIRE, Participant.BYE_PARTICIPANT, Data.HERO, Participant.BYE_PARTICIPANT)
        PowerMockito.`when`(mockParticipantService.createRound(participantsWithBye)).thenReturn(
            Round(
                0, 0,
                listOf(
                    MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA),
                    MatchUp(0, 0, 1, Data.DGNT, Participant.BYE_PARTICIPANT),
                    MatchUp(0, 0, 2, Data.FIRE, Participant.BYE_PARTICIPANT),
                    MatchUp(0, 0, 3, Data.HERO, Participant.BYE_PARTICIPANT)
                )
            )
        )
    }

    @Test(expected = IllegalStateException::class)
    fun testCustomizationNoSetUp() {
        sut.select(0, true)
    }

    @Test
    fun testCustomizationRepeat() {
        val matchUps = sut.setUp(participants)

        val update1 = sut.select(0, true)
        Assert.assertEquals(0, update1.first)
        Assert.assertEquals(true, update1.second)

        val update2 = sut.select(0, true)
        Assert.assertEquals(0, update1.first)
        Assert.assertNull(update2.second)
        Assert.assertEquals(Data.ANDREW, matchUps[0].participant1)
    }

    @Test
    fun testCustomizationNormal() {

        val matchUps = sut.setUp(participants)

        val update1 = sut.select(0, true)
        Assert.assertEquals(0, update1.first)
        Assert.assertEquals(true, update1.second)

        val update2 = sut.select(1, true)
        Assert.assertEquals(0, update2.first)
        Assert.assertNull(update2.second)
        Assert.assertEquals(Data.DGNT, matchUps[0].participant1)
        Assert.assertEquals(Data.ANDREW, matchUps[1].participant1)

        val update3 = sut.select(2, false)
        Assert.assertEquals(2, update3.first)
        Assert.assertEquals(false, update3.second)

        val update4 = sut.select(3, false)
        Assert.assertEquals(2, update4.first)
        Assert.assertNull(update4.second)
        Assert.assertEquals(Data.DEMON, matchUps[2].participant2)
        Assert.assertEquals(Data.SUPER, matchUps[3].participant2)

        val update5 = sut.select(0, true)
        Assert.assertEquals(0, update5.first)
        Assert.assertEquals(true, update5.second)

        val update6 = sut.select(1, false)
        Assert.assertEquals(0, update6.first)
        Assert.assertNull(update6.second)
        Assert.assertEquals(Data.KELSEY, matchUps[0].participant1)
        Assert.assertEquals(Data.DGNT, matchUps[1].participant2)

        val update7 = sut.select(2, false)
        Assert.assertEquals(2, update7.first)
        Assert.assertEquals(false, update7.second)

        val update8 = sut.select(3, true)
        Assert.assertEquals(2, update8.first)
        Assert.assertNull(update8.second)
        Assert.assertEquals(Data.HERO, matchUps[2].participant2)
        Assert.assertEquals(Data.DEMON, matchUps[3].participant1)

    }

    @Test
    fun testCustomizationBye() {
        val matchUps = sut.setUp(participantsWithBye)

        val update1 = sut.select(0, true)
        Assert.assertEquals(0, update1.first)
        Assert.assertEquals(true, update1.second)

        val update2 = sut.select(1, false)
        Assert.assertEquals(0, update2.first)
        Assert.assertNull(update2.second)
        Assert.assertEquals(Data.KYRA, matchUps[0].participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[0].participant2)
        Assert.assertEquals(Data.ANDREW, matchUps[1].participant2)

        val update3 = sut.select(0, false)
        Assert.assertEquals(0, update3.first)
        Assert.assertEquals(false, update3.second)

        val update4 = sut.select(1, true)
        Assert.assertEquals(0, update4.first)
        Assert.assertNull(update4.second)
        Assert.assertEquals(Data.DGNT, matchUps[0].participant2)
        Assert.assertEquals(Data.ANDREW, matchUps[1].participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[1].participant2)

        val update5 = sut.select(2, true)
        Assert.assertEquals(2, update5.first)
        Assert.assertEquals(true, update5.second)

        val update6 = sut.select(3, false)
        Assert.assertEquals(2, update6.first)
        Assert.assertEquals(true, update6.second)
        Assert.assertEquals(Data.FIRE, matchUps[2].participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[3].participant2)
        sut.select(2, true)

        val update7 = sut.select(2, false)
        Assert.assertEquals(2, update7.first)
        Assert.assertEquals(false, update7.second)

        val update8 = sut.select(3, true)
        Assert.assertEquals(2, update8.first)
        Assert.assertEquals(false, update8.second)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[2].participant2)
        Assert.assertEquals(Data.HERO, matchUps[3].participant1)
        sut.select(2, false)
    }


}