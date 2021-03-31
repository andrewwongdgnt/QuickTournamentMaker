package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundUpdateService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EliminationRoundUpdateServiceTest {

    private val sut = EliminationRoundUpdateService()
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {

        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY),
                MatchUp(0, 0, 2, Data.FIRE, Data.SUPER),
                MatchUp(0, 0, 3, Data.HERO, Data.DEMON)
            )
        )
        val round2 = Round(
            0, 1, listOf(
                MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        val round3 = Round(
            0, 2, listOf(
                MatchUp(0, 2, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        roundGroups = listOf(RoundGroup(0, listOf(round1, round2, round3)))
    }

    @Test
    fun testP1Win_evenMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups, 0, 0, 0)
        Assert.assertEquals(Data.ANDREW, roundGroups[0].rounds[1].matchUps[0].participant1)
    }

    @Test
    fun testP2Win_evenMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroups, 0, 0, 0)
        Assert.assertEquals(Data.KYRA, roundGroups[0].rounds[1].matchUps[0].participant1)
    }

    @Test
    fun testP1Win_oddMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[1].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups, 0, 0, 1)
        Assert.assertEquals(Data.DGNT, roundGroups[0].rounds[1].matchUps[0].participant2)
    }

    @Test
    fun testP2Win_oddMatchUpIndex() {
        roundGroups[0].rounds[0].matchUps[1].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroups, 0, 0, 1)
        Assert.assertEquals(Data.KELSEY, roundGroups[0].rounds[1].matchUps[0].participant2)
    }

    @Test
    fun testCascade() {
        roundGroups[0].rounds[1].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups, 0, 1, 0)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[2].matchUps[0].participant1)
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups, 0, 0, 0)
        Assert.assertEquals(Data.ANDREW, roundGroups[0].rounds[2].matchUps[0].participant1)
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.DEFAULT
        sut.update(roundGroups, 0, 0, 0)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[2].matchUps[0].participant1)
    }

}