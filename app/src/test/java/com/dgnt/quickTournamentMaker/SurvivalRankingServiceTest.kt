package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.SurvivalRankingService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SurvivalRankingServiceTest {


    private val sut = SurvivalRankingService()
    private lateinit var initialStateKnownRank: List<Set<Participant>>
    private lateinit var initialStateUnknownRank: Set<Participant>
    private lateinit var normalStateKnownRank: List<Set<Participant>>
    private lateinit var normalStateUnknownRank: Set<Participant>

    @Before
    fun setUp() {

        val initialState = listOf(
            RoundGroup(
                listOf(
                    Round(
                        listOf(
                            MatchUp(Data.ANDREW, Participant.NULL_PARTICIPANT),
                            MatchUp(Data.KYRA, Participant.NULL_PARTICIPANT),
                            MatchUp(Data.DGNT, Participant.NULL_PARTICIPANT),
                            MatchUp(Data.KELSEY, Participant.NULL_PARTICIPANT)
                        )
                    )
                )
            )
        )
        val initialStateRank = sut.calculate(initialState)
        initialStateKnownRank = initialStateRank.known
        initialStateUnknownRank = initialStateRank.unknown

        val normalState = listOf(
            RoundGroup(
                listOf(
                    Round(listOf(
                        MatchUp(Data.ANDREW, Participant.NULL_PARTICIPANT),
                        MatchUp(Data.KYRA, Participant.NULL_PARTICIPANT)
                    )
                    ),
                    Round(listOf(
                        MatchUp(Data.DGNT, Participant.NULL_PARTICIPANT)
                    )
                    ),
                    Round(listOf(
                        MatchUp(Data.KELSEY, Participant.NULL_PARTICIPANT)
                    )
                    )
                )
            )
        )
        val normalStateRank = sut.calculate(normalState)
        normalStateKnownRank = normalStateRank.known
        normalStateUnknownRank = normalStateRank.unknown
    }

    @Test
    fun testInitialStateKnownRankingTotal() {
        Assert.assertEquals(1, initialStateKnownRank.size)
    }

    @Test
    fun testInitialStateKnownRankingTotalPerRank() {
        Assert.assertEquals(4, initialStateKnownRank[0].size)
    }

    @Test
    fun testInitialStateUnknownRankingTotal() {
        Assert.assertEquals(0, initialStateUnknownRank.size)
    }

    @Test
    fun testNormalStateKnownRankingTotal() {
        Assert.assertEquals(3, normalStateKnownRank.size)
    }

    @Test
    fun testNormalStateKnownRankingTotalPerRank() {
        Assert.assertEquals(2, normalStateKnownRank[0].size)
        Assert.assertEquals(1, normalStateKnownRank[1].size)
        Assert.assertEquals(1, normalStateKnownRank[2].size)
    }

    @Test
    fun testNormalStateKnownRanking() {
        Assert.assertTrue(normalStateKnownRank[0].contains(Data.ANDREW))
        Assert.assertTrue(normalStateKnownRank[0].contains(Data.KYRA))
        Assert.assertTrue(normalStateKnownRank[1].contains(Data.DGNT))
        Assert.assertTrue(normalStateKnownRank[2].contains(Data.KELSEY))
    }

    @Test
    fun testNormalStateUnknownRankingTotal() {
        Assert.assertEquals(0, normalStateUnknownRank.size)
    }

}