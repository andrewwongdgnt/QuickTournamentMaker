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

    @Test
    fun testClone() {
        val reference = listOf(
            Participant(Person("", "Andrew", "")),
            Participant(Person("", "Kyra", "")),
            Participant(Person("", "Dgnt", "")),
            Participant(Person("", "Kelsey", "")),
            Participant(Person("", "Fire", "")),
            Participant(Person("", "Super", "")),
            Participant(Person("", "Hero", "")),
            Participant(Person("", "Demon", "")),
            Participant(Person("", "", ""), ParticipantType.NULL),
            Participant(Person("", "", ""), ParticipantType.NULL),
            Participant(Person("", "", ""), ParticipantType.BYE),
            Participant(Person("", "", ""), ParticipantType.BYE)
        )
        val cloneList = sut.cloneList(participants, reference)

        Assert.assertTrue(Data.ANDREW === cloneList[0])
        Assert.assertTrue(Data.KYRA === cloneList[1])
        Assert.assertTrue(Data.DGNT === cloneList[2])
        Assert.assertTrue(Data.KELSEY === cloneList[3])
        Assert.assertTrue(Data.FIRE === cloneList[4])
        Assert.assertTrue(Data.SUPER === cloneList[5])
        Assert.assertTrue(Data.HERO === cloneList[6])
        Assert.assertTrue(Data.DEMON === cloneList[7])
        Assert.assertTrue(Participant.NULL_PARTICIPANT === cloneList[8])
        Assert.assertTrue(Participant.NULL_PARTICIPANT === cloneList[9])
        Assert.assertTrue(Participant.BYE_PARTICIPANT === cloneList[10])
        Assert.assertTrue(Participant.BYE_PARTICIPANT === cloneList[11])


    }
}