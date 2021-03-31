package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRankingHelperService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EliminationRankingHelperServiceTest {

    private val sut = EliminationRankingHelperService()
    private lateinit var initialStateKnownRank: List<Set<Participant>>
    private lateinit var initialStateUnknownRank: Set<Participant>
    private lateinit var finalStateKnownRank: List<Set<Participant>>
    private lateinit var finalStateUnknownRank: Set<Participant>
    private lateinit var betweenStateKnownRank: List<Set<Participant>>
    private lateinit var betweenStateUnknownRank: Set<Participant>
    private lateinit var edgeStateKnownRank: List<Set<Participant>>
    private lateinit var edgeStateUnknownRank: Set<Participant>

    @Before
    fun setUp() {

        val initialState = listOf(
            RoundGroup(
                0,
                listOf(
                    Round(
                        0, 0, listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA),
                            MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY),
                            MatchUp(0, 0, 2, Data.FIRE, Data.SUPER),
                            MatchUp(0, 0, 3, Data.HERO, Data.DEMON)
                        )
                    ),
                    Round(
                        0, 1, listOf(
                            MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                            MatchUp(0, 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                        )
                    ),
                    Round(
                        0, 2, listOf(
                            MatchUp(0, 2, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                        )
                    )
                )
            )
        ).first().rounds
        val initialStateRank = sut.calculate(initialState, initialState.dropLast(1), initialState.takeLast(1))
        initialStateKnownRank = initialStateRank.known
        initialStateUnknownRank = initialStateRank.unknown

        val finalState = listOf(
            RoundGroup(
                0,
                listOf(
                    Round(
                        0, 0, listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA).also { it.status = MatchUpStatus.P1_WINNER },
                            MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY).also { it.status = MatchUpStatus.P1_WINNER },
                            MatchUp(0, 0, 2, Data.FIRE, Data.SUPER).also { it.status = MatchUpStatus.P1_WINNER },
                            MatchUp(0, 0, 3, Data.HERO, Data.DEMON).also { it.status = MatchUpStatus.P1_WINNER })
                    ),
                    Round(
                        0, 1, listOf(
                            MatchUp(0, 1, 0, Data.ANDREW, Data.DGNT).also { it.status = MatchUpStatus.P1_WINNER },
                            MatchUp(0, 1, 1, Data.FIRE, Data.HERO).also { it.status = MatchUpStatus.P1_WINNER })
                    ),
                    Round(
                        0, 2, listOf(
                            MatchUp(0, 2, 0, Data.ANDREW, Data.FIRE).also { it.status = MatchUpStatus.P1_WINNER })
                    )
                )
            )
        ).first().rounds
        val finalStateRank = sut.calculate(finalState, finalState.dropLast(1), finalState.takeLast(1))
        finalStateKnownRank = finalStateRank.known
        finalStateUnknownRank = finalStateRank.unknown

        val betweenState = listOf(
            RoundGroup(
                0,
                listOf(
                    Round(
                        0, 0, listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA).also { it.status = MatchUpStatus.P1_WINNER },
                            MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER },
                            MatchUp(0, 0, 2, Data.FIRE, Data.SUPER),
                            MatchUp(0, 0, 3, Data.HERO, Data.DEMON)
                        )
                    ),
                    Round(
                        0, 1, listOf(
                            MatchUp(0, 1, 0, Data.ANDREW, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER },
                            MatchUp(0, 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                        )
                    ),
                    Round(
                        0, 2, listOf(
                            MatchUp(0, 2, 0, Data.KELSEY, Participant.NULL_PARTICIPANT)
                        )
                    )
                )
            )
        ).first().rounds
        val betweenStateRank = sut.calculate(betweenState, betweenState.dropLast(1), betweenState.takeLast(1))
        betweenStateKnownRank = betweenStateRank.known
        betweenStateUnknownRank = betweenStateRank.unknown

        val edgeState = listOf(
            RoundGroup(
                0,
                listOf(
                    Round(
                        0, 0, listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA).also { it.status = MatchUpStatus.P1_WINNER },
                            MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER },
                            MatchUp(0, 0, 2, Data.FIRE, Data.SUPER).also { it.status = MatchUpStatus.P1_WINNER },
                            MatchUp(0, 0, 3, Data.HERO, Data.DEMON)
                        )
                    ),
                    Round(
                        0, 1, listOf(
                            MatchUp(0, 1, 0, Data.ANDREW, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER },
                            MatchUp(0, 1, 1, Data.FIRE, Participant.NULL_PARTICIPANT).also { it.status = MatchUpStatus.P2_WINNER })
                    ),
                    Round(
                        0, 2, listOf(
                            MatchUp(0, 2, 0, Data.KELSEY, Participant.NULL_PARTICIPANT).also { it.status = MatchUpStatus.P1_WINNER })
                    )
                )
            )
        ).first().rounds
        val edgeStateRank = sut.calculate(edgeState, edgeState.dropLast(1), edgeState.takeLast(1))
        edgeStateKnownRank = edgeStateRank.known
        edgeStateUnknownRank = edgeStateRank.unknown

    }

    @Test
    fun testInitialStateKnownRankingTotal() {
        Assert.assertEquals(4, initialStateKnownRank.size)
    }

    @Test
    fun testInitialStateKnownRankingTotalPerRank() {
        Assert.assertEquals(0, initialStateKnownRank[0].size)
        Assert.assertEquals(0, initialStateKnownRank[1].size)
        Assert.assertEquals(0, initialStateKnownRank[2].size)
        Assert.assertEquals(0, initialStateKnownRank[3].size)
    }

    @Test
    fun testInitialStateUnknownRankingTotal() {
        Assert.assertEquals(8, initialStateUnknownRank.size)
    }

    @Test
    fun testInitialStateUnknownRanking() {
        Assert.assertTrue(initialStateUnknownRank.contains(Data.ANDREW))
        Assert.assertTrue(initialStateUnknownRank.contains(Data.KYRA))
        Assert.assertTrue(initialStateUnknownRank.contains(Data.DGNT))
        Assert.assertTrue(initialStateUnknownRank.contains(Data.KELSEY))
        Assert.assertTrue(initialStateUnknownRank.contains(Data.FIRE))
        Assert.assertTrue(initialStateUnknownRank.contains(Data.SUPER))
        Assert.assertTrue(initialStateUnknownRank.contains(Data.HERO))
        Assert.assertTrue(initialStateUnknownRank.contains(Data.DEMON))
    }

    @Test
    fun testFinalStateKnownRankingTotal() {
        Assert.assertEquals(4, finalStateKnownRank.size)
    }

    @Test
    fun testFinalStateKnownRankingTotalPerRank() {
        Assert.assertEquals(4, finalStateKnownRank[0].size)
        Assert.assertEquals(2, finalStateKnownRank[1].size)
        Assert.assertEquals(1, finalStateKnownRank[2].size)
        Assert.assertEquals(1, finalStateKnownRank[3].size)
    }

    @Test
    fun testFinalStateKnownRanking() {
        Assert.assertTrue(finalStateKnownRank[0].contains(Data.KYRA))
        Assert.assertTrue(finalStateKnownRank[0].contains(Data.KELSEY))
        Assert.assertTrue(finalStateKnownRank[0].contains(Data.SUPER))
        Assert.assertTrue(finalStateKnownRank[0].contains(Data.DEMON))
        Assert.assertTrue(finalStateKnownRank[1].contains(Data.DGNT))
        Assert.assertTrue(finalStateKnownRank[1].contains(Data.HERO))
        Assert.assertTrue(finalStateKnownRank[2].contains(Data.FIRE))
        Assert.assertTrue(finalStateKnownRank[3].contains(Data.ANDREW))
    }

    @Test
    fun testFinalStateUnknownRankingTotal() {
        Assert.assertEquals(0, finalStateUnknownRank.size)
    }

    @Test
    fun testBetweenStateKnownRankingTotalPerRank() {
        Assert.assertEquals(2, betweenStateKnownRank[0].size)
        Assert.assertEquals(1, betweenStateKnownRank[1].size)
        Assert.assertEquals(0, betweenStateKnownRank[2].size)
        Assert.assertEquals(0, betweenStateKnownRank[3].size)
    }

    @Test
    fun testBetweenStateKnownRanking() {
        Assert.assertTrue(betweenStateKnownRank[0].contains(Data.KYRA))
        Assert.assertTrue(betweenStateKnownRank[0].contains(Data.DGNT))
        Assert.assertTrue(betweenStateKnownRank[1].contains(Data.ANDREW))
    }

    @Test
    fun testBetweenStateUnknownRankingTotal() {
        Assert.assertEquals(5, betweenStateUnknownRank.size)
    }

    @Test
    fun testBetweenStateUnknownRanking() {
        Assert.assertTrue(betweenStateUnknownRank.contains(Data.KELSEY))
        Assert.assertTrue(betweenStateUnknownRank.contains(Data.FIRE))
        Assert.assertTrue(betweenStateUnknownRank.contains(Data.SUPER))
        Assert.assertTrue(betweenStateUnknownRank.contains(Data.HERO))
        Assert.assertTrue(betweenStateUnknownRank.contains(Data.DEMON))
    }

    @Test
    fun testEdgeStateKnownRankingTotal() {
        Assert.assertEquals(4, edgeStateKnownRank.size)
    }

    @Test
    fun testEdgeStateKnownRankingTotalPerRank() {
        Assert.assertEquals(3, edgeStateKnownRank[0].size)
        Assert.assertEquals(2, edgeStateKnownRank[1].size)
        Assert.assertEquals(0, edgeStateKnownRank[2].size)
        Assert.assertEquals(1, edgeStateKnownRank[3].size)
    }

    @Test
    fun testEdgeStateKnownRanking() {
        Assert.assertTrue(edgeStateKnownRank[0].contains(Data.KYRA))
        Assert.assertTrue(edgeStateKnownRank[0].contains(Data.DGNT))
        Assert.assertTrue(edgeStateKnownRank[0].contains(Data.SUPER))
        Assert.assertTrue(edgeStateKnownRank[1].contains(Data.ANDREW))
        Assert.assertTrue(edgeStateKnownRank[1].contains(Data.FIRE))
        Assert.assertTrue(edgeStateKnownRank[3].contains(Data.KELSEY))
    }

    @Test
    fun testEdgeStateUnknownRankingTotal() {
        Assert.assertEquals(2, edgeStateUnknownRank.size)
    }

    @Test
    fun testEdgeStateUnknownRanking() {
        Assert.assertTrue(edgeStateUnknownRank.contains(Data.HERO))
        Assert.assertTrue(edgeStateUnknownRank.contains(Data.DEMON))
    }
}