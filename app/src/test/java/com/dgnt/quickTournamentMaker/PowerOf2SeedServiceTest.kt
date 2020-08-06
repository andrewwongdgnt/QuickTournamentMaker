package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.management.Person
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.PowerOf2SeedService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PowerOf2SeedServiceTest {

    private val sut = PowerOf2SeedService()
    private lateinit var people: List<Person>

    @Before
    fun setUp() {
        people = listOf(Data.ANDREW_PERSON, Data.KYRA_PERSON, Data.DGNT_PERSON, Data.KELSEY_PERSON, Data.FIRE_PERSON, Data.SUPER_PERSON, Data.HERO_PERSON, Data.DEMON_PERSON)

    }

    @Test
    fun testSeedWithExactPowerOf2_8() {
        val participants= sut.seed(people)
        Assert.assertEquals(Data.ANDREW_PERSON, participants[0].person)
        Assert.assertEquals(Data.KYRA_PERSON, participants[1].person)
        Assert.assertEquals(Data.DGNT_PERSON, participants[2].person)
        Assert.assertEquals(Data.KELSEY_PERSON, participants[3].person)
        Assert.assertEquals(Data.FIRE_PERSON, participants[4].person)
        Assert.assertEquals(Data.SUPER_PERSON, participants[5].person)
        Assert.assertEquals(Data.HERO_PERSON, participants[6].person)
        Assert.assertEquals(Data.DEMON_PERSON, participants[7].person)
    }

    @Test
    fun testSeedWithoutPowerOf2_4() {
        val participants= sut.seed(people.dropLast(4))
        Assert.assertEquals(Data.ANDREW_PERSON, participants[0].person)
        Assert.assertEquals(Data.KYRA_PERSON, participants[1].person)
        Assert.assertEquals(Data.DGNT_PERSON, participants[2].person)
        Assert.assertEquals(Data.KELSEY_PERSON, participants[3].person)
    }

    @Test
    fun testSeedWithoutPowerOf2_3() {
        val participants= sut.seed(people.dropLast(5))
        Assert.assertEquals(Data.ANDREW_PERSON, participants[0].person)
        Assert.assertEquals(Data.KYRA_PERSON, participants[1].person)
        Assert.assertEquals(Data.DGNT_PERSON, participants[2].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[3])
    }

    @Test
    fun testSeedWithoutPowerOf2_5() {
        val participants= sut.seed(people.dropLast(3))
        Assert.assertEquals(Data.ANDREW_PERSON, participants[0].person)
        Assert.assertEquals(Data.KYRA_PERSON, participants[1].person)
        Assert.assertEquals(Data.DGNT_PERSON, participants[2].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[3])
        Assert.assertEquals(Data.KELSEY_PERSON, participants[4].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.FIRE_PERSON, participants[6].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[7])
    }

    @Test
    fun testSeedWithoutPowerOf2_6() {
        val participants= sut.seed(people.dropLast(2))
        Assert.assertEquals(Data.ANDREW_PERSON, participants[0].person)
        Assert.assertEquals(Data.KYRA_PERSON, participants[1].person)
        Assert.assertEquals(Data.DGNT_PERSON, participants[2].person)
        Assert.assertEquals(Data.KELSEY_PERSON, participants[3].person)
        Assert.assertEquals(Data.FIRE_PERSON, participants[4].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.SUPER_PERSON, participants[6].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[7])
    }


}