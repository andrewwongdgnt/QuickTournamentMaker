package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
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
    private lateinit var withByeStateKnownRank: List<Set<Participant>>
    private lateinit var withByeStateUnknownRank: Set<Participant>

    @Before
    fun setUp() {

        val initialState = listOf(
            RoundGroup(0,
                listOf(
                    Round(0, 0,
                        listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, "").apply {
                                participant1.record = Record()
                                participant2.record = Record()
                            },
                            MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, "").apply {
                                participant1.record = Record()
                                participant2.record = Record()
                            }
                        ),
                        ""
                    ),
                    Round(
                        0, 1,
                        listOf(
                            MatchUp(0, 1, 0, Data.ANDREW, Data.DGNT, ""),
                            MatchUp(0, 1, 1, Data.KELSEY, Data.KYRA, "")
                        ),
                        ""
                    ),
                    Round(
                        0, 2,
                        listOf(
                            MatchUp(0, 2, 0, Data.ANDREW, Data.KELSEY, ""),
                            MatchUp(0, 2, 1, Data.KYRA, Data.DGNT, "")
                        ),
                        ""
                    )
                ),
                ""
            )
        )
        val initialStateRank = sut.calculate(initialState)
        initialStateKnownRank = initialStateRank.known
        initialStateUnknownRank = initialStateRank.unknown

        val normalState = listOf(
            RoundGroup(0,
                listOf(
                    Round(0, 0,
                        listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, "").apply {
                                participant1.record = Record(0, 1, 2)
                                participant2.record = Record(0, 2, 1)
                            },
                            MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, "").apply {
                                participant1.record = Record(2, 1, 0)
                                participant2.record = Record(2, 0, 1)
                            }
                        ),
                        ""
                    ),
                    Round(
                        0, 1,
                        listOf(
                            MatchUp(0, 1, 0, Data.ANDREW, Data.DGNT, ""),
                            MatchUp(0, 1, 1, Data.KELSEY, Data.KYRA, "")
                        ),
                        ""
                    ),
                    Round(
                        0, 2,
                        listOf(
                            MatchUp(0, 2, 0, Data.ANDREW, Data.KELSEY, ""),
                            MatchUp(0, 2, 1, Data.KYRA, Data.DGNT, "")
                        ),
                        ""
                    )
                ),
                ""
            )
        )
        val normalStateRank = sut.calculate(normalState)
        normalStateKnownRank = normalStateRank.known
        normalStateUnknownRank = normalStateRank.unknown

        val withByeState = listOf(
            RoundGroup(0,
                listOf(
                    Round(0, 0,
                        listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, "").apply {
                                participant1.record = Record(0, 1, 2)
                                participant2.record = Record(0, 2, 1)
                            },
                            MatchUp(0, 0, 1, Data.DGNT, Participant.BYE_PARTICIPANT, "").apply {
                                participant1.record = Record(2, 1, 0)
                                participant2.record = Record(86, 0, 0)
                            }
                        ),
                        ""
                    ),
                    Round(
                        0, 1,
                        listOf(
                            MatchUp(0, 1, 0, Data.ANDREW, Data.DGNT, ""),
                            MatchUp(0, 1, 1, Participant.BYE_PARTICIPANT, Data.KYRA, "")
                        ),
                        ""
                    ),
                    Round(
                        0, 2,
                        listOf(
                            MatchUp(0, 2, 0, Data.ANDREW, Participant.BYE_PARTICIPANT, ""),
                            MatchUp(0, 2, 1, Data.KYRA, Data.DGNT, "")
                        ),
                        ""
                    )
                ),
                ""
            )
        )
        val withByeStateRank = sut.calculate(withByeState)
        withByeStateKnownRank = withByeStateRank.known
        withByeStateUnknownRank = withByeStateRank.unknown
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
        Assert.assertEquals(4, normalStateKnownRank.size)
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
        Assert.assertEquals(0, normalStateUnknownRank.size)
    }

    //----------

    @Test
    fun testWithByeStateKnownRankingTotal() {
        Assert.assertEquals(3, withByeStateKnownRank.size)
    }

    @Test
    fun testWithByeStateKnownRankingTotalPerRank() {
        Assert.assertEquals(1, withByeStateKnownRank[0].size)
        Assert.assertEquals(1, withByeStateKnownRank[1].size)
        Assert.assertEquals(1, withByeStateKnownRank[2].size)
    }

    @Test
    fun testWithByeStateKnownRanking() {
        Assert.assertTrue(withByeStateKnownRank[0].contains(Data.KYRA))
        Assert.assertTrue(withByeStateKnownRank[1].contains(Data.ANDREW))
        Assert.assertTrue(withByeStateKnownRank[2].contains(Data.DGNT))
    }

    @Test
    fun testWithByeStateUnknownRankingTotal() {
        Assert.assertEquals(0, withByeStateUnknownRank.size)
    }

}