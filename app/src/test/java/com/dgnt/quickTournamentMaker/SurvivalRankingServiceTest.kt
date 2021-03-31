package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
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
                0,
                listOf(
                    Round(
                        0, 0,
                        listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Participant.NULL_PARTICIPANT),
                            MatchUp(0, 0, 1, Data.KYRA, Participant.NULL_PARTICIPANT),
                            MatchUp(0, 0, 2, Data.DGNT, Participant.NULL_PARTICIPANT),
                            MatchUp(0, 0, 3, Data.KELSEY, Participant.NULL_PARTICIPANT)
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
                0,
                listOf(
                    Round(
                        0, 0, listOf(
                            MatchUp(0, 0, 0, Data.ANDREW, Participant.NULL_PARTICIPANT),
                            MatchUp(0, 0, 1, Data.KYRA, Participant.NULL_PARTICIPANT)
                        )
                    ),
                    Round(
                        0, 1, listOf(
                            MatchUp(0, 1, 0, Data.DGNT, Participant.NULL_PARTICIPANT)
                        )
                    ),
                    Round(
                        0, 2, listOf(
                            MatchUp(0, 2, 0, Data.KELSEY, Participant.NULL_PARTICIPANT)
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