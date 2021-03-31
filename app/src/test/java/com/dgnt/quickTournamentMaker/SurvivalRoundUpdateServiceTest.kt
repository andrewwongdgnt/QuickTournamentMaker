package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.SurvivalRoundUpdateService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SurvivalRoundUpdateServiceTest {

    private val sut = SurvivalRoundUpdateService()
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {

        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Participant.NULL_PARTICIPANT),
                MatchUp(0, 0, 1, Data.KYRA, Participant.NULL_PARTICIPANT),
                MatchUp(0, 0, 2, Data.DGNT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 0, 3, Data.KELSEY, Participant.NULL_PARTICIPANT)
            )
        )
        val round2 = Round(
            0, 1, listOf(
                MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 1, 2, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 1, 3, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        val round3 = Round(
            0, 2, listOf(
                MatchUp(0, 2, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 2, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 2, 2, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 2, 3, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        val round4 = Round(
            0, 3, listOf(
                MatchUp(0, 3, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 3, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 3, 2, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
                MatchUp(0, 3, 3, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            )
        )
        roundGroups = listOf(RoundGroup(0, listOf(round1, round2, round3, round4)))
    }

    @Test
    fun testP1Win_first() {
        roundGroups[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups, 0, 0, 0)
        Assert.assertEquals(Data.ANDREW, roundGroups[0].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[1].matchUps[2].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[1].matchUps[3].participant1)
    }

    @Test
    fun testP1Win_second() {
        roundGroups[0].rounds[0].matchUps[1].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroups, 0, 0, 1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.KYRA, roundGroups[0].rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[1].matchUps[2].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroups[0].rounds[1].matchUps[3].participant1)
    }


}