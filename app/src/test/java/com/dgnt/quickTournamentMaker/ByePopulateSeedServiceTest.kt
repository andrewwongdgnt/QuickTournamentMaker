package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.management.Person
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.service.implementation.ByePopulateSeedService
import com.dgnt.quickTournamentMaker.service.implementation.EvenNumberSeedService
import com.dgnt.quickTournamentMaker.service.implementation.PowerOf2SeedService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ByePopulateSeedServiceTest {

    private val sut = ByePopulateSeedService()
    private lateinit var participants: List<Participant>

    @Before
    fun setUp() {
        participants = sut.seed(listOf(Data.ANDREW_PERSON, Data.KYRA_PERSON, Data.DGNT_PERSON, Data.KELSEY_PERSON, Data.FIRE_PERSON, Data.SUPER_PERSON, Data.HERO_PERSON, Data.DEMON_PERSON))
    }

    @Test
    fun testSeedEven() {
        Assert.assertEquals(Data.ANDREW_PERSON, participants[0].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[1])
        Assert.assertEquals(Data.KYRA_PERSON, participants[2].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[3])
        Assert.assertEquals(Data.DGNT_PERSON, participants[4].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.KELSEY_PERSON, participants[6].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[7])
        Assert.assertEquals(Data.FIRE_PERSON, participants[8].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[9])
        Assert.assertEquals(Data.SUPER_PERSON, participants[10].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[11])
        Assert.assertEquals(Data.HERO_PERSON, participants[12].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[13])
        Assert.assertEquals(Data.DEMON_PERSON, participants[14].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[15])
    }

    @Test
    fun testSeedOdd() {
        Assert.assertEquals(Data.ANDREW_PERSON, participants[0].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[1])
        Assert.assertEquals(Data.KYRA_PERSON, participants[2].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[3])
        Assert.assertEquals(Data.DGNT_PERSON, participants[4].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[5])
        Assert.assertEquals(Data.KELSEY_PERSON, participants[6].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[7])
        Assert.assertEquals(Data.FIRE_PERSON, participants[8].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[9])
        Assert.assertEquals(Data.SUPER_PERSON, participants[10].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[11])
        Assert.assertEquals(Data.HERO_PERSON, participants[12].person)
        Assert.assertEquals(Participant.BYE_PARTICIPANT, participants[13])
    }

   

}