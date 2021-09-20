package com.dgnt.quickTournamentMaker

import android.util.Log
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.DoubleEliminationRoundUpdateService
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundUpdateService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class DoubleEliminationRoundUpdateServiceTest {
    private val mockRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)

    private val sut = DoubleEliminationRoundUpdateService(mockRoundUpdateService)
    private lateinit var roundGroupsNoByes: List<RoundGroup>

    @Before
    fun setUp() {

        val round1WB = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                MatchUp(0, 0, 2, Data.FIRE, Data.SUPER, ""),
                MatchUp(0, 0, 3, Data.HERO, Data.DEMON, "")
            ),
            ""
        )
        val round2WB = Round(
            0, 1, listOf(
                MatchUp(0, 1, 0, Data.KYRA, Data.KELSEY, ""),
                MatchUp(0, 1, 1, Data.FIRE, Data.DEMON, "")
            ),
            ""
        )
        val round3WB = Round(
            0, 2, listOf(
                MatchUp(0, 2, 0, Data.KELSEY, Data.FIRE, "")
            ),
            ""
        )

        val round1LB = Round(
            1, 0, listOf(
                MatchUp(1, 0, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                MatchUp(1, 0, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )
        val round2LB = Round(
            1, 1, listOf(
                MatchUp(1, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                MatchUp(1, 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )
        val round3LB = Round(
            1, 2, listOf(
                MatchUp(1, 2, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )
        val round4LB = Round(
            1, 3, listOf(
                MatchUp(1, 3, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )

        val round1FB = Round(
            2, 0, listOf(
                MatchUp(2, 0, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )
        val round2FB = Round(
            2, 1, listOf(
                MatchUp(2, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )

        roundGroupsNoByes = listOf(
            RoundGroup(0, listOf(round1WB, round2WB, round3WB), ""),
            RoundGroup(1, listOf(round1LB, round2LB, round3LB, round4LB), ""),
            RoundGroup(2, listOf(round1FB, round2FB), "")
        )

    }

    @Test
    fun testMain() {
        roundGroupsNoByes[0].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 0, 0, 0)
        roundGroupsNoByes[0].rounds[0].matchUps[1].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes, 0, 0, 1)
        roundGroupsNoByes[0].rounds[0].matchUps[2].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 0, 0, 2)
        roundGroupsNoByes[0].rounds[0].matchUps[3].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes, 0, 0, 3)
        Assert.assertEquals(Data.KYRA, roundGroupsNoByes[1].rounds[0].matchUps[0].participant1)
        Assert.assertEquals(Data.DGNT, roundGroupsNoByes[1].rounds[0].matchUps[0].participant2)
        Assert.assertEquals(Data.SUPER, roundGroupsNoByes[1].rounds[0].matchUps[1].participant1)
        Assert.assertEquals(Data.HERO, roundGroupsNoByes[1].rounds[0].matchUps[1].participant2)

        roundGroupsNoByes[1].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 1, 0, 0)
        roundGroupsNoByes[1].rounds[0].matchUps[1].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes, 1, 0, 1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroupsNoByes[1].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.KYRA, roundGroupsNoByes[1].rounds[1].matchUps[0].participant2)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroupsNoByes[1].rounds[1].matchUps[1].participant1)
        Assert.assertEquals(Data.HERO, roundGroupsNoByes[1].rounds[1].matchUps[1].participant2)

        roundGroupsNoByes[0].rounds[0].matchUps[0].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes, 0, 0, 0)
        Assert.assertEquals(Data.ANDREW, roundGroupsNoByes[1].rounds[0].matchUps[0].participant1)
        Assert.assertEquals(Data.ANDREW, roundGroupsNoByes[1].rounds[0].matchUps[0].participant1)

        roundGroupsNoByes[0].rounds[1].matchUps[0].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes, 0, 1, 0)
        roundGroupsNoByes[0].rounds[1].matchUps[1].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 0, 1, 1)
        Assert.assertEquals(Data.DEMON, roundGroupsNoByes[1].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.KYRA, roundGroupsNoByes[1].rounds[1].matchUps[1].participant1)

        roundGroupsNoByes[0].rounds[2].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 0, 2, 0)
        Assert.assertEquals(Data.KELSEY, roundGroupsNoByes[2].rounds[0].matchUps[0].participant1)

        roundGroupsNoByes[1].rounds[1].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 1, 1, 0)
        roundGroupsNoByes[1].rounds[1].matchUps[1].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 1, 1, 1)
        roundGroupsNoByes[1].rounds[2].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 1, 2, 0)
        roundGroupsNoByes[1].rounds[3].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 1, 3, 0)
        Assert.assertEquals(Data.FIRE, roundGroupsNoByes[1].rounds[3].matchUps[0].participant1)
        Assert.assertEquals(Data.FIRE, roundGroupsNoByes[2].rounds[0].matchUps[0].participant2)

        roundGroupsNoByes[2].rounds[0].matchUps[0].status = MatchUpStatus.P1_WINNER
        sut.update(roundGroupsNoByes, 2, 0, 0)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroupsNoByes[2].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Participant.NULL_PARTICIPANT, roundGroupsNoByes[2].rounds[1].matchUps[0].participant2)

        roundGroupsNoByes[2].rounds[0].matchUps[0].status = MatchUpStatus.P2_WINNER
        sut.update(roundGroupsNoByes, 2, 0, 0)
        Assert.assertEquals(Data.KELSEY, roundGroupsNoByes[2].rounds[1].matchUps[0].participant1)
        Assert.assertEquals(Data.FIRE, roundGroupsNoByes[2].rounds[1].matchUps[0].participant2)
    }


}