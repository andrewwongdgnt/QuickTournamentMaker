package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.service.implementation.DefaultSelectedPlayersService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class DefaultSelectedPlayersServiceTest {

    private val sut = DefaultSelectedPlayersService()
    private lateinit var names: List<String>
    private var numberOfPlayers = 30
    private lateinit var quickNames: List<String>

    @Before
    fun setUp() {
        names = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
        quickNames = (1..numberOfPlayers).map { it.toString() }

    }

    @Test
    fun testRandomResolve() {
        //Small chance this will fail because of the nature of randomness
        Assert.assertNotEquals(names, sut.resolve(names, numberOfPlayers, false, SeedType.RANDOM))
        Assert.assertNotEquals(names, sut.resolve(names, null, false, SeedType.RANDOM))
    }

    @Test
    fun testRandomResolveQuickStart() {
        //Small chance this will fail because of the nature of randomness
        Assert.assertNotEquals(quickNames, sut.resolve(names, numberOfPlayers, true, SeedType.RANDOM))
        Assert.assertNotEquals(quickNames, sut.resolve(null, numberOfPlayers, true, SeedType.RANDOM))
    }

    @Test
    fun testNonRandomResolve() {
        Assert.assertEquals(names, sut.resolve(names, numberOfPlayers, false, SeedType.CUSTOM))
        Assert.assertEquals(names, sut.resolve(names, numberOfPlayers, false, SeedType.SAME))
    }

    @Test
    fun testNonRandomResolveQuickStart() {
        Assert.assertEquals(quickNames, sut.resolve(names, numberOfPlayers, true, SeedType.CUSTOM))
        Assert.assertEquals(quickNames, sut.resolve(names, numberOfPlayers, true, SeedType.SAME))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testBadParameters() {
       sut.resolve(names, null, true, SeedType.CUSTOM)
        sut.resolve(null, numberOfPlayers, false, SeedType.RANDOM)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testLessThan3() {
        sut.resolve(listOf("m","d"), 2, false, SeedType.CUSTOM)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testLessThan3QuickStart() {
        sut.resolve(listOf("m","d"), 2, true, SeedType.CUSTOM)
    }

}