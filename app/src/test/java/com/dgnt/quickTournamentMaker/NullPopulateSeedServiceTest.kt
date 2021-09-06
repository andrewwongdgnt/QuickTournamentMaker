package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.NullPopulateSeedService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NullPopulateSeedServiceTest {

    private val sut = NullPopulateSeedService()
    private lateinit var participants: List<Participant>

    @Before
    fun setUp() {
        participants = sut.seed(listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON))
    }

    @Test
    fun testSeedEven() {
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[1])
        Assert.assertEquals(Data.KYRA, participants[2])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[3])
        Assert.assertEquals(Data.DGNT, participants[4])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.KELSEY, participants[6])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[7])
        Assert.assertEquals(Data.FIRE, participants[8])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[9])
        Assert.assertEquals(Data.SUPER, participants[10])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[11])
        Assert.assertEquals(Data.HERO, participants[12])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[13])
        Assert.assertEquals(Data.DEMON, participants[14])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[15])
    }

    @Test
    fun testSeedOdd() {
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[1])
        Assert.assertEquals(Data.KYRA, participants[2])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[3])
        Assert.assertEquals(Data.DGNT, participants[4])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.KELSEY, participants[6])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[7])
        Assert.assertEquals(Data.FIRE, participants[8])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[9])
        Assert.assertEquals(Data.SUPER, participants[10])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[11])
        Assert.assertEquals(Data.HERO, participants[12])
        Assert.assertEquals(Participant.NULL_PARTICIPANT, participants[13])
    }

   

}