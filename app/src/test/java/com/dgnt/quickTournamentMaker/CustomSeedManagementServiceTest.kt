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
                    MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                    MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                    MatchUp(0, 0, 2, Data.FIRE, Data.SUPER, ""),
                    MatchUp(0, 0, 3, Data.HERO, Data.DEMON, "")
                ),
                ""
            )
        )

        participantsWithBye = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Participant.BYE_PARTICIPANT, Data.FIRE, Participant.BYE_PARTICIPANT, Data.HERO, Participant.BYE_PARTICIPANT)
        PowerMockito.`when`(mockParticipantService.createRound(participantsWithBye)).thenReturn(
            Round(
                0, 0,
                listOf(
                    MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                    MatchUp(0, 0, 1, Data.DGNT, Participant.BYE_PARTICIPANT, ""),
                    MatchUp(0, 0, 2, Data.FIRE, Participant.BYE_PARTICIPANT, ""),
                    MatchUp(0, 0, 3, Data.HERO, Participant.BYE_PARTICIPANT, "")
                ),
                ""
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
        Assert.assertEquals(0, update1.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.first.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.first.matchUp.participant2)
        Assert.assertEquals(true, update1.first.isParticipant1Highlighted)
        Assert.assertEquals(0, update1.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.second.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.second.matchUp.participant2)
        Assert.assertEquals(true, update1.second.isParticipant1Highlighted)

        val update2 = sut.select(0, true)
        Assert.assertEquals(0, update1.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.first.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.first.matchUp.participant2)
        Assert.assertNull(update2.first.isParticipant1Highlighted)
        Assert.assertEquals(0, update1.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.second.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.second.matchUp.participant2)
        Assert.assertNull(update2.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.ANDREW, matchUps[0].participant1)
    }

    @Test
    fun testCustomizationNormal() {

        val matchUps = sut.setUp(participants)

        val update1 = sut.select(0, true)
        Assert.assertEquals(0, update1.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.first.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.first.matchUp.participant2)
        Assert.assertEquals(true, update1.first.isParticipant1Highlighted)
        Assert.assertEquals(0, update1.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.second.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.second.matchUp.participant2)
        Assert.assertEquals(true, update1.second.isParticipant1Highlighted)

        val update2 = sut.select(1, true)
        Assert.assertEquals(0, update2.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.DGNT, update2.first.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update2.first.matchUp.participant2)
        Assert.assertNull(update2.first.isParticipant1Highlighted)
        Assert.assertEquals(1, update2.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update2.second.matchUp.participant1)
        Assert.assertEquals(Data.KELSEY, update2.second.matchUp.participant2)
        Assert.assertNull(update2.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.DGNT, matchUps[0].participant1)
        Assert.assertEquals(Data.ANDREW, matchUps[1].participant1)

        val update3 = sut.select(2, false)
        Assert.assertEquals(2, update3.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update3.first.matchUp.participant1)
        Assert.assertEquals(Data.SUPER, update3.first.matchUp.participant2)
        Assert.assertEquals(false, update3.first.isParticipant1Highlighted)
        Assert.assertEquals(2, update3.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update3.second.matchUp.participant1)
        Assert.assertEquals(Data.SUPER, update3.second.matchUp.participant2)
        Assert.assertEquals(false, update3.second.isParticipant1Highlighted)

        val update4 = sut.select(3, false)
        Assert.assertEquals(2, update4.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update4.first.matchUp.participant1)
        Assert.assertEquals(Data.DEMON, update4.first.matchUp.participant2)
        Assert.assertNull(update4.first.isParticipant1Highlighted)
        Assert.assertEquals(3, update4.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.HERO, update4.second.matchUp.participant1)
        Assert.assertEquals(Data.SUPER, update4.second.matchUp.participant2)
        Assert.assertNull(update4.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.DEMON, matchUps[2].participant2)
        Assert.assertEquals(Data.SUPER, matchUps[3].participant2)

        val update5 = sut.select(0, true)
        Assert.assertEquals(0, update5.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.DGNT, update5.first.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update5.first.matchUp.participant2)
        Assert.assertEquals(true, update5.first.isParticipant1Highlighted)
        Assert.assertEquals(0, update5.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.DGNT, update5.second.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update5.second.matchUp.participant2)
        Assert.assertEquals(true, update5.second.isParticipant1Highlighted)

        val update6 = sut.select(1, false)
        Assert.assertEquals(0, update6.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.KELSEY, update6.first.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update6.first.matchUp.participant2)
        Assert.assertNull(update6.first.isParticipant1Highlighted)
        Assert.assertEquals(1, update6.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update6.second.matchUp.participant1)
        Assert.assertEquals(Data.DGNT, update6.second.matchUp.participant2)
        Assert.assertNull(update6.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.KELSEY, matchUps[0].participant1)
        Assert.assertEquals(Data.DGNT, matchUps[1].participant2)

        val update7 = sut.select(2, false)
        Assert.assertEquals(2, update7.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update7.first.matchUp.participant1)
        Assert.assertEquals(Data.DEMON, update7.first.matchUp.participant2)
        Assert.assertEquals(false, update7.first.isParticipant1Highlighted)
        Assert.assertEquals(2, update7.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update7.second.matchUp.participant1)
        Assert.assertEquals(Data.DEMON, update7.second.matchUp.participant2)
        Assert.assertEquals(false, update7.second.isParticipant1Highlighted)

        val update8 = sut.select(3, true)
        Assert.assertEquals(2, update8.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update8.first.matchUp.participant1)
        Assert.assertEquals(Data.HERO, update8.first.matchUp.participant2)
        Assert.assertNull(update8.first.isParticipant1Highlighted)
        Assert.assertEquals(3, update8.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.DEMON, update8.second.matchUp.participant1)
        Assert.assertEquals(Data.SUPER, update8.second.matchUp.participant2)
        Assert.assertNull(update8.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.HERO, matchUps[2].participant2)
        Assert.assertEquals(Data.DEMON, matchUps[3].participant1)

    }

    @Test
    fun testCustomizationBye() {
        val matchUps = sut.setUp(participantsWithBye)

        val update1 = sut.select(0, true)
        Assert.assertEquals(0, update1.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.first.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.first.matchUp.participant2)
        Assert.assertEquals(true, update1.first.isParticipant1Highlighted)
        Assert.assertEquals(0, update1.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update1.second.matchUp.participant1)
        Assert.assertEquals(Data.KYRA, update1.second.matchUp.participant2)
        Assert.assertEquals(true, update1.second.isParticipant1Highlighted)

        val update2 = sut.select(1, false)
        Assert.assertEquals(0, update2.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.KYRA, update2.first.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update2.first.matchUp.participant2)
        Assert.assertNull(update2.first.isParticipant1Highlighted)
        Assert.assertEquals(1, update2.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.DGNT, update2.second.matchUp.participant1)
        Assert.assertEquals(Data.ANDREW, update2.second.matchUp.participant2)
        Assert.assertNull(update2.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.KYRA, matchUps[0].participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[0].participant2)
        Assert.assertEquals(Data.ANDREW, matchUps[1].participant2)

        val update3 = sut.select(0, false)
        Assert.assertEquals(0, update3.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.KYRA, update3.first.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update3.first.matchUp.participant2)
        Assert.assertEquals(false, update3.first.isParticipant1Highlighted)
        Assert.assertEquals(0, update3.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.KYRA, update3.second.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update3.second.matchUp.participant2)
        Assert.assertEquals(false, update3.second.isParticipant1Highlighted)

        val update4 = sut.select(1, true)
        Assert.assertEquals(0, update4.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.KYRA, update4.first.matchUp.participant1)
        Assert.assertEquals(Data.DGNT, update4.first.matchUp.participant2)
        Assert.assertNull(update4.first.isParticipant1Highlighted)
        Assert.assertEquals(1, update4.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.ANDREW, update4.second.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update4.second.matchUp.participant2)
        Assert.assertNull(update4.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.DGNT, matchUps[0].participant2)
        Assert.assertEquals(Data.ANDREW, matchUps[1].participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[1].participant2)

        val update5 = sut.select(2, true)
        Assert.assertEquals(2, update5.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update5.first.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update5.first.matchUp.participant2)
        Assert.assertEquals(true, update5.first.isParticipant1Highlighted)
        Assert.assertEquals(2, update5.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update5.second.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update5.second.matchUp.participant2)
        Assert.assertEquals(true, update5.second.isParticipant1Highlighted)

        val update6 = sut.select(3, false)
        Assert.assertEquals(2, update6.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update6.first.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update6.first.matchUp.participant2)
        Assert.assertEquals(true, update6.first.isParticipant1Highlighted)
        Assert.assertEquals(3, update6.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.HERO, update6.second.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update6.second.matchUp.participant2)
        Assert.assertNull(update6.second.isParticipant1Highlighted)
        Assert.assertEquals(Data.FIRE, matchUps[2].participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[3].participant2)
        sut.select(2, true)

        val update7 = sut.select(2, false)
        Assert.assertEquals(2, update7.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update7.first.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update7.first.matchUp.participant2)
        Assert.assertEquals(false, update7.first.isParticipant1Highlighted)
        Assert.assertEquals(2, update7.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update7.second.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update7.second.matchUp.participant2)
        Assert.assertEquals(false, update7.second.isParticipant1Highlighted)

        val update8 = sut.select(3, true)
        Assert.assertEquals(2, update8.first.matchUp.matchUpIndex)
        Assert.assertEquals(Data.FIRE, update8.first.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update8.first.matchUp.participant2)
        Assert.assertEquals(false, update8.first.isParticipant1Highlighted)
        Assert.assertEquals(3, update8.second.matchUp.matchUpIndex)
        Assert.assertEquals(Data.HERO, update8.second.matchUp.participant1)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, update8.second.matchUp.participant2)
        Assert.assertNull(update8.second.isParticipant1Highlighted)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, matchUps[2].participant2)
        Assert.assertEquals(Data.HERO, matchUps[3].participant1)
        sut.select(2, false)
    }

    //TODO test self swaps with and without byes

}