package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.ProgressCalculatorViaRoundsService
import com.dgnt.quickTournamentMaker.service.interfaces.*
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class ProgressCalculatorViaRoundsServiceTest {

    private val mockRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockProgressCalculatorService = PowerMockito.mock(IProgressCalculatorService::class.java)
    private val mockRankingConfigService = PowerMockito.mock(IRankingConfigService::class.java)
    private val sut = ProgressCalculatorViaRoundsService()

    private lateinit var tournament: Tournament


    @Before
    fun setUp() {

        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P1_WINNER },
                MatchUp(0, 0, 1, Data.DGNT, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P1_WINNER },
                MatchUp(0, 0, 2, Data.FIRE, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER },
                MatchUp(0, 0, 3, Data.HERO, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER }
            ),
            "round 1 original title"
        )
        val round2 = Round(
            0, 1,
            listOf(
                MatchUp(0, 1, 0, Data.ANDREW, Participant.NULL_PARTICIPANT, ""),
                MatchUp(0, 1, 1, Data.DGNT, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P1_WINNER },
                MatchUp(0, 1, 2, Data.FIRE, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER },
                MatchUp(0, 1, 3, Data.HERO, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER }
            ),
            "round 2 original title"
        )
        val round3 = Round(
            0, 1,
            listOf(
                MatchUp(0, 2, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P1_WINNER },
                MatchUp(0, 2, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P1_WINNER },
                MatchUp(0, 2, 2, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER },
                MatchUp(0, 2, 3, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "").apply { status = MatchUpStatus.P2_WINNER }
            ),
            "round 3 original title"
        )


        val roundGroups = listOf(RoundGroup(0, listOf(round1, round2, round3), ""))
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

        Assert.assertEquals(Progress(1, 3), sut.calculate(tournament))
    }

}