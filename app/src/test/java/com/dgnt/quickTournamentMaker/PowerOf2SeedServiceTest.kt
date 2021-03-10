package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.PowerOf2SeedService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PowerOf2SeedServiceTest {

    private val sut = PowerOf2SeedService()
    private lateinit var participants: List<Participant>

    @Before
    fun setUp() {
        participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)

    }

    @Test
    fun testSeedWithExactPowerOf2_8() {
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
    fun testSeedWithoutPowerOf2_4() {
        val participants= sut.seed(participants.dropLast(4))
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Data.KYRA, participants[1])
        Assert.assertEquals(Data.DGNT, participants[2])
        Assert.assertEquals(Data.KELSEY, participants[3])
    }

    @Test
    fun testSeedWithoutPowerOf2_3() {
        val participants= sut.seed(participants.dropLast(5))
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Data.KYRA, participants[1])
        Assert.assertEquals(Data.DGNT, participants[2])
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[3])
    }

    @Test
    fun testSeedWithoutPowerOf2_5() {
        val participants= sut.seed(participants.dropLast(3))
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Data.KYRA, participants[1])
        Assert.assertEquals(Data.DGNT, participants[2])
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[3])
        Assert.assertEquals(Data.KELSEY, participants[4])
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.FIRE, participants[6])
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[7])
    }

    @Test
    fun testSeedWithoutPowerOf2_6() {
        val participants= sut.seed(participants.dropLast(2))
        Assert.assertEquals(Data.ANDREW, participants[0])
        Assert.assertEquals(Data.KYRA, participants[1])
        Assert.assertEquals(Data.DGNT, participants[2])
        Assert.assertEquals(Data.KELSEY, participants[3])
        Assert.assertEquals(Data.FIRE, participants[4])
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.SUPER, participants[6])
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[7])
    }


}