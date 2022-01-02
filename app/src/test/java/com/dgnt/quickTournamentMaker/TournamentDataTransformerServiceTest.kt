package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.TournamentDataTransformerService
import com.dgnt.quickTournamentMaker.service.interfaces.*
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito

class TournamentDataTransformerServiceTest {

    private val mockRankingConfigService = PowerMockito.mock(IRankingConfigService::class.java)
    private val mockRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val sut = TournamentDataTransformerService(mockRankingConfigService)

    private lateinit var simpleTournament: Tournament

    @Before
    fun setUp() {
        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                MatchUp(0, 0, 1, Data.FIRE, Participant.BYE_PARTICIPANT, ""),
                MatchUp(0, 0, 2, Data.HERO, Participant.BYE_PARTICIPANT, "")
            ),
            ""
        )
        val round2 = Round(
            0, 1,
            listOf(
                MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.BYE_PARTICIPANT, ""),
                MatchUp(0, 1, 0, Participant.BYE_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )

        val round1_2 = Round(
            1, 0, listOf(
                MatchUp(1, 0, 0, Data.ANDREW, Data.KYRA, ""),
                MatchUp(1, 0, 1, Data.DGNT, Data.KELSEY, ""),
                MatchUp(1, 0, 2, Data.FIRE, Participant.BYE_PARTICIPANT, ""),
                MatchUp(1, 0, 3, Data.HERO, Participant.BYE_PARTICIPANT, "")
            ),
            ""
        )
        val round2_2 = Round(
            1, 1, listOf(
                MatchUp(1, 1, 0, Participant.NULL_PARTICIPANT, Participant.BYE_PARTICIPANT, ""),
                MatchUp(1, 1, 1, Participant.BYE_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )
        val roundGroups = listOf(RoundGroup(0, listOf(round1, round2), ""), RoundGroup(1, listOf(round1_2, round2_2), ""))
        simpleTournament = Tournament(
            TournamentInformation("title", "description", TournamentType.ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, LocalDateTime.now()),
            roundGroups,
            mockMatchUpStatusTransformService,
            mockRoundUpdateService,
            mockRankingService
        )
    }



    @Test
    fun testTransform() {
        val tournamentData = sut.transform(simpleTournament)
        Assert.assertEquals("title", tournamentData.title)
    }


}