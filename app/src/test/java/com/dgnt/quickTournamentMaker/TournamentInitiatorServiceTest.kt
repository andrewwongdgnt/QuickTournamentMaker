package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.TournamentInitiatorService
import com.dgnt.quickTournamentMaker.service.interfaces.*
import org.joda.time.LocalDateTime
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
class TournamentInitiatorServiceTest {

    private val mockByeStatusResolverService = PowerMockito.mock(IByeStatusResolverService::class.java)
    private val mockRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockRankingConfigService = PowerMockito.mock(IRankingConfigService::class.java)
    private val sut = TournamentInitiatorService(mockByeStatusResolverService)

    private lateinit var roundGroups: List<RoundGroup>
    private lateinit var tournament: Tournament

    @Before
    fun setUp() {
        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                MatchUp(0, 0, 2, Data.FIRE, Participant.BYE_PARTICIPANT, ""),
                MatchUp(0, 0, 3, Data.HERO, Participant.BYE_PARTICIPANT, "")
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
        roundGroups = listOf(RoundGroup(0, listOf(round1, round2), ""), RoundGroup(1, listOf(round1_2, round2_2), ""))

    }

    private fun setUpTournament(tournamentType: TournamentType) {
        tournament = Tournament(
            TournamentInformation("title", "description", tournamentType, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, LocalDateTime.now()),
            roundGroups,
            mockMatchUpStatusTransformService,
            mockRoundUpdateService,
            mockRankingService,
            mockRankingConfigService
        )
    }

    @Test
    fun testElimination() {
        setUpTournament(TournamentType.ELIMINATION)
        sut.initiate(tournament)

        Mockito.verify(mockByeStatusResolverService, Mockito.times(1)).resolve(MockitoHelper.anyObject(), MockitoHelper.anyObject())

        Mockito.verify(mockRoundUpdateService, Mockito.times(4)).update(MockitoHelper.anyObject(), Mockito.eq(0), Mockito.eq(0), Mockito.intThat { it in 0..3 }, MockitoHelper.anyObject())
    }

    @Test
    fun testDoubleElimination() {
        setUpTournament(TournamentType.DOUBLE_ELIMINATION)
        sut.initiate(tournament)

        Mockito.verify(mockByeStatusResolverService, Mockito.times(3)).resolve(MockitoHelper.anyObject(), MockitoHelper.anyObject())

        Mockito.verify(mockRoundUpdateService, Mockito.times(4)).update(MockitoHelper.anyObject(), Mockito.eq(0), Mockito.eq(0), Mockito.intThat { it in 0..3 }, MockitoHelper.anyObject())

        Mockito.verify(mockRoundUpdateService, Mockito.times(4)).update(MockitoHelper.anyObject(), Mockito.eq(1), Mockito.eq(0), Mockito.intThat { it in 0..3 }, MockitoHelper.anyObject())

        Mockito.verify(mockRoundUpdateService, Mockito.times(2)).update(MockitoHelper.anyObject(), Mockito.eq(1), Mockito.eq(1), Mockito.intThat { it in 0..1 }, MockitoHelper.anyObject())
    }

    @Test
    fun testRoundRobin() {
        setUpTournament(TournamentType.ROUND_ROBIN)
        sut.initiate(tournament)

        Mockito.verify(mockByeStatusResolverService, Mockito.times(1)).resolve(MockitoHelper.anyObject(), MockitoHelper.anyObject())

        Mockito.verify(mockRoundUpdateService, Mockito.times(4)).update(MockitoHelper.anyObject(), Mockito.eq(0), Mockito.eq(0), Mockito.intThat { it in 0..3 }, MockitoHelper.anyObject())
    }

    @Test
    fun testSwiss() {
        setUpTournament(TournamentType.SWISS)
        sut.initiate(tournament)

        Mockito.verify(mockByeStatusResolverService, Mockito.times(1)).resolve(MockitoHelper.anyObject(), MockitoHelper.anyObject())

        Mockito.verify(mockRoundUpdateService, Mockito.times(4)).update(MockitoHelper.anyObject(), Mockito.eq(0), Mockito.eq(0), Mockito.intThat { it in 0..3 }, MockitoHelper.anyObject())
    }

    @Test
    fun testSurvival() {
        setUpTournament(TournamentType.SURVIVAL)
        sut.initiate(tournament)

        Mockito.verify(mockByeStatusResolverService, Mockito.never()).resolve(MockitoHelper.anyObject(), MockitoHelper.anyObject())
        Mockito.verify(mockRoundUpdateService, Mockito.never()).update(MockitoHelper.anyObject(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), MockitoHelper.anyObject())
    }
}