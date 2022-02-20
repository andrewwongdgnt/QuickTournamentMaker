package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.ProgressCalculatorViaMatchUpsService
import com.dgnt.quickTournamentMaker.service.interfaces.*
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class ProgressCalculatorViaMatchUpsServiceTest {

    private val mockRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockProgressCalculatorService = PowerMockito.mock(IProgressCalculatorService::class.java)
    private val mockRankingConfigService = PowerMockito.mock(IRankingConfigService::class.java)
    private val sut = ProgressCalculatorViaMatchUpsService()

    private lateinit var tournament: Tournament


    @Before
    fun setUp() {

        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, "").apply { status = MatchUpStatus.P1_WINNER },
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                MatchUp(0, 0, 2, Data.FIRE, Participant.BYE_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER },
                MatchUp(0, 0, 3, Data.HERO, Participant.BYE_PARTICIPANT, "").apply { status = MatchUpStatus.TIE }
            ),
            "round 1 original title"
        )
        val round2 = Round(
            0, 1,
            listOf(
                MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.BYE_PARTICIPANT, ""),
                MatchUp(0, 1, 1, Participant.BYE_PARTICIPANT, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER }
            ),
            "round 2 original title"
        )

        val round1_2 = Round(
            1, 0, listOf(
                MatchUp(1, 0, 0, Data.ANDREW, Data.KYRA, ""),
                MatchUp(1, 0, 1, Data.DGNT, Data.KELSEY, "").apply { status = MatchUpStatus.P2_WINNER },
                MatchUp(1, 0, 2, Data.FIRE, Participant.BYE_PARTICIPANT, ""),
                MatchUp(1, 0, 3, Data.HERO, Participant.BYE_PARTICIPANT, "")
            ),
            ""
        )
        val round2_2 = Round(
            1, 1, listOf(
                MatchUp(1, 1, 0, Data.SUPER, Participant.BYE_PARTICIPANT, ""),
                MatchUp(1, 1, 1, Data.DEMON, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.TIE },
                MatchUp(1, 1, 2, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                MatchUp(1, 1, 3, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                MatchUp(1, 1, 4, Data.DEMON, Participant.NULL_PARTICIPANT, ""),
            ),
            ""
        )
        val roundGroups = listOf(RoundGroup(0, listOf(round1, round2), ""), RoundGroup(1, listOf(round1_2, round2_2), ""))
        tournament = Tournament(
            TournamentInformation(
                "new Title",
                "new Description",
                TournamentType.ROUND_ROBIN,
                SeedType.RANDOM,
                RankScoreConfig(9f, 3f, 2.5f),
                LocalDateTime.now()
            ),
            roundGroups,
            mockMatchUpStatusTransformService,
            mockRoundUpdateService,
            mockRankingService,
            mockProgressCalculatorService,
            mockRankingConfigService
        )
    }


    @Test
    fun testProgressCalculation() {

        Assert.assertEquals(Progress(2, 8), sut.calculate(tournament))
    }

}