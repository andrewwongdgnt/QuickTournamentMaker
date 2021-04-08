package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.service.implementation.SelectedPersonsService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SelectedPersonsServiceTest {

    private val sut = SelectedPersonsService()
    private lateinit var persons: List<Person>
    private var numberOfParticipants = 30
    private lateinit var fullParticipants: List<Participant>
    private lateinit var quickParticipants: List<Participant>

    @Before
    fun setUp() {
        persons = listOf(
            Person("", "a", ""),
            Person("", "b", ""),
            Person("", "c", ""),
            Person("", "d", ""),
            Person("", "e", ""),
            Person("", "f", ""),
            Person("", "g", ""),
            Person("", "h", ""),
            Person("", "i", ""),
            Person("", "j", ""),
            Person("", "k", ""),
            Person("", "l", ""),
            Person("", "m", ""),
            Person("", "o", ""),
            Person("", "p", ""),
            Person("", "q", ""),
            Person("", "r", ""),
            Person("", "CCrea", ""),
            Person("", "t", ""),
            Person("", "u", ""),
            Person("", "v", ""),
            Person("", "w", ""),
            Person("", "x", ""),
            Person("", "y", ""),
            Person("", "z", "")
        )
        fullParticipants = persons.map{ Participant(it) }
        quickParticipants = (1..numberOfParticipants).map { Participant(Person("",it.toString(),"")) }

    }

    @Test
    fun testRandomResolve() {
        //Small chance this will fail because of the nature of randomness
        Assert.assertNotEquals(fullParticipants, sut.resolve(persons, numberOfParticipants, false, SeedType.RANDOM))
        Assert.assertNotEquals(fullParticipants, sut.resolve(persons, null, false, SeedType.RANDOM))
    }

    @Test
    fun testRandomResolveQuickStart() {
        //Small chance this will fail because of the nature of randomness
        Assert.assertNotEquals(quickParticipants, sut.resolve(persons, numberOfParticipants, true, SeedType.RANDOM))
        Assert.assertNotEquals(quickParticipants, sut.resolve(null, numberOfParticipants, true, SeedType.RANDOM))
    }

    @Test
    fun testNonRandomResolve() {
        Assert.assertEquals(fullParticipants, sut.resolve(persons, numberOfParticipants, false, SeedType.CUSTOM))
        Assert.assertEquals(fullParticipants, sut.resolve(persons, numberOfParticipants, false, SeedType.SAME))
    }

    @Test
    fun testNonRandomResolveQuickStart() {
        Assert.assertEquals(quickParticipants, sut.resolve(persons, numberOfParticipants, true, SeedType.CUSTOM))
        Assert.assertEquals(quickParticipants, sut.resolve(persons, numberOfParticipants, true, SeedType.SAME))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testBadParameters() {
        sut.resolve(persons, null, true, SeedType.CUSTOM)
        sut.resolve(null, numberOfParticipants, false, SeedType.RANDOM)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testLessThan3() {
        sut.resolve(
            listOf(
                Person("", "a", ""),
                Person("", "b", "")
            ), 2, false, SeedType.CUSTOM
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testLessThan3QuickStart() {
        sut.resolve(
            listOf(
                Person("", "a", ""),
                Person("", "b", "")
            ), 2, true, SeedType.CUSTOM
        )
    }

}