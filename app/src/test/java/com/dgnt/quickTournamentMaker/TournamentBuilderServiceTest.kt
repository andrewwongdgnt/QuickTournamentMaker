package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.data.TournamentTypeServices
import com.dgnt.quickTournamentMaker.service.implementation.TournamentBuilderService
import com.dgnt.quickTournamentMaker.service.interfaces.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(IRoundGeneratorService::class, IRoundUpdateService::class, IRankingService::class, ISeedService::class, IMatchUpStatusTransformService::class)
class TournamentBuilderServiceTest {

    private val mockEliminationRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockDoubleEliminationRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockRoundRobinRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockSwissRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockSurvivalRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)

    private val mockEliminationRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockDoubleEliminationRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRoundRobinRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockSwissRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockSurvivalRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)

    private val mockEliminationRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockDoubleEliminationRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockRoundRobinRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockSwissRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockSurvivalRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)

    private val mockEliminationSeedService: ISeedService = PowerMockito.mock(ISeedService::class.java)
    private val mockDoubleEliminationSeedService: ISeedService = PowerMockito.mock(ISeedService::class.java)
    private val mockRoundRobinSeedService: ISeedService = PowerMockito.mock(ISeedService::class.java)
    private val mockSwissSeedService: ISeedService = PowerMockito.mock(ISeedService::class.java)
    private val mockSurvivalSeedService: ISeedService = PowerMockito.mock(ISeedService::class.java)

    private val mockEliminationMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockDoubleEliminationMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockRoundRobinMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockSwissMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockSurvivalMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)


    private val sut = TournamentBuilderService(
        mapOf(
            TournamentType.ELIMINATION to TournamentTypeServices(
                roundGeneratorService = mockEliminationRoundGeneratorService,
                roundUpdateService = mockEliminationRoundUpdateService,
                rankingService = mockEliminationRankingService,
                seedService = mockEliminationSeedService,
                matchUpStatusTransformService = mockEliminationMatchUpStatusTransformService
            ),
            TournamentType.DOUBLE_ELIMINATION to TournamentTypeServices(
                roundGeneratorService = mockDoubleEliminationRoundGeneratorService,
                roundUpdateService = mockDoubleEliminationRoundUpdateService,
                rankingService = mockDoubleEliminationRankingService,
                seedService = mockDoubleEliminationSeedService,
                matchUpStatusTransformService = mockDoubleEliminationMatchUpStatusTransformService
            ),
            TournamentType.ROUND_ROBIN to TournamentTypeServices(
                roundGeneratorService = mockRoundRobinRoundGeneratorService,
                roundUpdateService = mockRoundRobinRoundUpdateService,
                rankingService = mockRoundRobinRankingService,
                seedService = mockRoundRobinSeedService,
                matchUpStatusTransformService = mockRoundRobinMatchUpStatusTransformService
            ),
            TournamentType.SWISS to TournamentTypeServices(
                roundGeneratorService = mockSwissRoundGeneratorService,
                roundUpdateService = mockSwissRoundUpdateService,
                rankingService = mockSwissRankingService,
                seedService = mockSwissSeedService,
                matchUpStatusTransformService = mockSwissMatchUpStatusTransformService
            ),
            TournamentType.SURVIVAL to TournamentTypeServices(
                roundGeneratorService = mockSurvivalRoundGeneratorService,
                roundUpdateService = mockSurvivalRoundUpdateService,
                rankingService = mockSurvivalRankingService,
                seedService = mockSurvivalSeedService,
                matchUpStatusTransformService = mockSurvivalMatchUpStatusTransformService
            )
        )
    )

    private val eliminationTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val doubleEliminationTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.DOUBLE_ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val roundRobinTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.ROUND_ROBIN, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val swissTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.SWISS, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val survivalTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.SURVIVAL, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private lateinit var round1: Round
    private lateinit var round2: Round
    private lateinit var round3: Round

    @Before
    fun setUp() {

        round1 = Round(0,0,listOf(
            MatchUp(0,0,0,Data.ANDREW, Data.KYRA),
            MatchUp(0,0,1,Data.DGNT, Data.KELSEY),
            MatchUp(0,0,2,Data.FIRE, Data.SUPER),
            MatchUp(0,0,3,Data.HERO, Data.DEMON)))
        round2 = Round(0,1,listOf(
            MatchUp(0,1,0,Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT),
            MatchUp(0,1,1,Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
        round3 = Round(0,2,listOf(
            MatchUp(0,2,0,Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))
        PowerMockito.`when`(mockEliminationRoundGeneratorService.build(MockitoHelper.anyObject())).thenReturn(listOf(RoundGroup(0,listOf(round1, round2, round3))))
    }

    @Test
    fun testEliminationServiceCall() {
        sut.build(eliminationTournamentInformation).also {
            Mockito.verify(mockEliminationRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
            Mockito.verify(mockEliminationSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
        }
    }

    @Test
    fun testDoubleEliminationServiceCall() {
        sut.build(doubleEliminationTournamentInformation).also {
            Mockito.verify(mockDoubleEliminationRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
            Mockito.verify(mockDoubleEliminationSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
        }
    }

    @Test
    fun testRoundRobinServiceCall() {
        sut.build(roundRobinTournamentInformation).also {
            Mockito.verify(mockRoundRobinRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
            Mockito.verify(mockRoundRobinSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
        }
    }

    @Test
    fun testSwissServiceCall() {
        sut.build(swissTournamentInformation).also {
            Mockito.verify(mockSwissRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
            Mockito.verify(mockSwissSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
        }
    }

    @Test
    fun testSurvivalServiceCall() {
        sut.build(survivalTournamentInformation).also {
            Mockito.verify(mockSurvivalRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
            Mockito.verify(mockSurvivalSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
        }
    }

    @Test
    fun testEquality() {
        sut.build(eliminationTournamentInformation).run {
            Assert.assertEquals(mockEliminationRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockEliminationRankingService, rankingService)
            Assert.assertEquals(mockEliminationMatchUpStatusTransformService, matchUpStatusTransformService)
        }

        sut.build(doubleEliminationTournamentInformation).run {
            Assert.assertEquals(mockDoubleEliminationRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockDoubleEliminationRankingService, rankingService)
            Assert.assertEquals(mockDoubleEliminationMatchUpStatusTransformService, matchUpStatusTransformService)
        }
        sut.build(roundRobinTournamentInformation).run {
            Assert.assertEquals(mockRoundRobinRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockRoundRobinRankingService, rankingService)
            Assert.assertEquals(mockRoundRobinMatchUpStatusTransformService, matchUpStatusTransformService)
        }

        sut.build(swissTournamentInformation).run {
            Assert.assertEquals(mockSwissRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockSwissRankingService, rankingService)
            Assert.assertEquals(mockSwissMatchUpStatusTransformService, matchUpStatusTransformService)
        }

        sut.build(survivalTournamentInformation).run {
            Assert.assertEquals(mockSurvivalRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockSurvivalRankingService, rankingService)
            Assert.assertEquals(mockSurvivalMatchUpStatusTransformService, matchUpStatusTransformService)
        }

    }

    @Test
    fun testMatchUps() {


        sut.build(eliminationTournamentInformation).run {
            Assert.assertEquals(7, matchUps.size)
            Assert.assertEquals(4, matchUps.map { it.first }.filter { it == round1 }.size)
            Assert.assertEquals(2, matchUps.map { it.first }.filter { it == round2 }.size)
            Assert.assertEquals(1, matchUps.map { it.first }.filter { it == round3 }.size)
            Assert.assertEquals(Pair(Data.ANDREW, Data.KYRA), matchUps[0].second.run { Pair(participant1, participant2) })
            Assert.assertEquals(Pair(Data.DGNT, Data.KELSEY), matchUps[1].second.run { Pair(participant1, participant2) })
            Assert.assertEquals(Pair(Data.FIRE, Data.SUPER), matchUps[2].second.run { Pair(participant1, participant2) })
            Assert.assertEquals(Pair(Data.HERO, Data.DEMON), matchUps[3].second.run { Pair(participant1, participant2) })
            Assert.assertEquals(Pair(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), matchUps[4].second.run { Pair(participant1, participant2) })
            Assert.assertEquals(Pair(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), matchUps[5].second.run { Pair(participant1, participant2) })
            Assert.assertEquals(Pair(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT), matchUps[6].second.run { Pair(participant1, participant2) })
        }
    }


    @Test
    fun testRounds() {
        sut.build(eliminationTournamentInformation).run {
            Assert.assertEquals(3, rounds.size)
            Assert.assertEquals(round1, rounds[0])
            Assert.assertEquals(round2, rounds[1])
            Assert.assertEquals(round3, rounds[2])
        }
    }


}