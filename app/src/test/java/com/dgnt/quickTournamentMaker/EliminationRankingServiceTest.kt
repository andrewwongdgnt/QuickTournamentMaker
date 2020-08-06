package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRankingService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EliminationRankingServiceTest {

    private val sut = EliminationRankingService()
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
                listOf(
                    Round(listOf(MatchUp(Data.ANDREW, Data.KYRA), MatchUp(Data.DGNT, Data.KELSEY), MatchUp(Data.FIRE, Data.SUPER), MatchUp(Data.HERO, Data.DEMON))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
                )
            )
        )
        val initialStateRank = sut.calculate(initialState)
        initialStateKnownRank = initialStateRank.known
        initialStateUnknownRank = initialStateRank.unknown

        val finalState = listOf(
            RoundGroup(
                listOf(
                    Round(listOf(MatchUp(Data.ANDREW, Data.KYRA).also { it.status = MatchUpStatus.P1_WINNER }, MatchUp(Data.DGNT, Data.KELSEY).also { it.status = MatchUpStatus.P1_WINNER }, MatchUp(Data.FIRE, Data.SUPER).also { it.status = MatchUpStatus.P1_WINNER }, MatchUp(Data.HERO, Data.DEMON).also { it.status = MatchUpStatus.P1_WINNER })),
                    Round(listOf(MatchUp(Data.ANDREW, Data.DGNT).also { it.status = MatchUpStatus.P1_WINNER }, MatchUp(Data.FIRE, Data.HERO).also { it.status = MatchUpStatus.P1_WINNER })),
                    Round(listOf(MatchUp(Data.ANDREW, Data.FIRE).also { it.status = MatchUpStatus.P1_WINNER }))
                )
            )
        )
        val finalStateRank = sut.calculate(finalState)
        finalStateKnownRank = finalStateRank.known
        finalStateUnknownRank = finalStateRank.unknown

        val betweenState = listOf(
            RoundGroup(
                listOf(
                    Round(listOf(MatchUp(Data.ANDREW, Data.KYRA).also { it.status = MatchUpStatus.P1_WINNER }, MatchUp(Data.DGNT, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER }, MatchUp(Data.FIRE, Data.SUPER), MatchUp(Data.HERO, Data.DEMON))),
                    Round(listOf(MatchUp(Data.ANDREW, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER }, MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),
                    Round(listOf(MatchUp(Data.KELSEY, Participant.NULL_PARTICIPANT)))
                )
            )
        )
        val betweenStateRank = sut.calculate(betweenState)
        betweenStateKnownRank = betweenStateRank.known
        betweenStateUnknownRank = betweenStateRank.unknown

        val edgeState = listOf(
            RoundGroup(
                listOf(
                    Round(listOf(MatchUp(Data.ANDREW, Data.KYRA).also { it.status = MatchUpStatus.P1_WINNER }, MatchUp(Data.DGNT, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER }, MatchUp(Data.FIRE, Data.SUPER).also { it.status = MatchUpStatus.P1_WINNER }, MatchUp(Data.HERO, Data.DEMON))),
                    Round(listOf(MatchUp(Data.ANDREW, Data.KELSEY).also { it.status = MatchUpStatus.P2_WINNER }, MatchUp(Data.FIRE, Participant.NULL_PARTICIPANT).also { it.status = MatchUpStatus.P2_WINNER })),
                    Round(listOf(MatchUp(Data.KELSEY, Participant.NULL_PARTICIPANT).also { it.status = MatchUpStatus.P1_WINNER }))
                )
            )
        )
        val edgeStateRank = sut.calculate(edgeState)
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
        Assert.assertTrue( finalStateKnownRank[1].contains(Data.DGNT))
        Assert.assertTrue( finalStateKnownRank[1].contains(Data.HERO))
        Assert.assertTrue( finalStateKnownRank[2].contains(Data.FIRE))
        Assert.assertTrue( finalStateKnownRank[3].contains(Data.ANDREW))
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
        Assert.assertTrue( betweenStateKnownRank[1].contains(Data.ANDREW))
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
        Assert.assertTrue( edgeStateKnownRank[3].contains(Data.KELSEY))
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