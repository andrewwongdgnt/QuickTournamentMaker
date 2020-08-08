package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.RecordRankingService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RecordRankingServiceTest {


    private val sut = RecordRankingService()
    private lateinit var initialStateKnownRank: List<Set<Participant>>
    private lateinit var initialStateUnknownRank: Set<Participant>
    private lateinit var normalStateKnownRank: List<Set<Participant>>
    private lateinit var normalStateUnknownRank: Set<Participant>

    @Before
    fun setUp() {

        val initialState = listOf(
            RoundGroup(
                listOf(
                    Round(listOf(
                        MatchUp(Data.ANDREW, Data.KYRA).apply {
                            participant1.record = Record()
                            participant2.record = Record()
                        },
                        MatchUp(Data.DGNT, Data.KELSEY).apply {
                            participant1.record = Record()
                            participant2.record = Record()
                        }
                    )
                    ),
                    Round(
                        listOf(
                            MatchUp(Data.ANDREW, Data.DGNT),
                            MatchUp(Data.KELSEY, Data.KYRA)
                        )
                    ),
                    Round(
                        listOf(
                            MatchUp(Data.ANDREW, Data.KELSEY),
                            MatchUp(Data.KYRA, Data.DGNT)
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
                        MatchUp(Data.ANDREW, Data.KYRA).apply {
                            participant1.record = Record(0, 1, 2)
                            participant2.record = Record(0, 2, 1)
                        },
                        MatchUp(Data.DGNT, Data.KELSEY).apply {
                            participant1.record = Record(2, 1, 0)
                            participant2.record = Record(2, 0, 1)
                        }
                    )
                    ),
                    Round(
                        listOf(
                            MatchUp(Data.ANDREW, Data.DGNT),
                            MatchUp(Data.KELSEY, Data.KYRA)
                        )
                    ),
                    Round(
                        listOf(
                            MatchUp(Data.ANDREW, Data.KELSEY),
                            MatchUp(Data.KYRA, Data.DGNT)
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
        Assert.assertEquals(1,initialStateKnownRank.size)
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
        Assert.assertEquals(4,normalStateKnownRank.size)
    }

    @Test
    fun testNormalStateKnownRankingTotalPerRank() {
        Assert.assertEquals(1, normalStateKnownRank[0].size)
        Assert.assertEquals(1, normalStateKnownRank[1].size)
        Assert.assertEquals(1, normalStateKnownRank[2].size)
        Assert.assertEquals(1, normalStateKnownRank[3].size)
    }

    @Test
    fun testNormalStateKnownRanking() {
        Assert.assertTrue(normalStateKnownRank[0].contains(Data.KYRA))
        Assert.assertTrue(normalStateKnownRank[1].contains(Data.ANDREW))
        Assert.assertTrue(normalStateKnownRank[2].contains(Data.DGNT))
        Assert.assertTrue(normalStateKnownRank[3].contains(Data.KELSEY))
    }

    @Test
    fun testNormalStateUnknownRankingTotal() {
        Assert.assertEquals(0,normalStateUnknownRank.size)
    }

}