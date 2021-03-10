package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.EvenNumberSeedService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EvenNumberSeedServiceTest {

    private val sut = EvenNumberSeedService()
    private lateinit var participants: List<Participant>

    @Before
    fun setUp() {
        participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)
    }

    @Test
    fun testSeedEven() {
        val participants= sut.seed(participants)
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Data.KYRA, participants[1])
        Assert.assertEquals(Data.DGNT, participants[2])
        Assert.assertEquals(Data.KELSEY, participants[3])
        Assert.assertEquals(Data.FIRE, participants[4])
        Assert.assertEquals(Data.SUPER, participants[5])
        Assert.assertEquals(Data.HERO, participants[6])
        Assert.assertEquals(Data.DEMON, participants[7])
    }

    @Test
    fun testSeedOdd() {
        val participants= sut.seed(participants.dropLast(1))
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Data.KYRA, participants[1])
        Assert.assertEquals(Data.DGNT, participants[2])
        Assert.assertEquals(Data.KELSEY, participants[3])
        Assert.assertEquals(Data.FIRE, participants[4])
        Assert.assertEquals(Data.SUPER, participants[5])
        Assert.assertEquals(Data.HERO, participants[6])
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[7])
    }



}