package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
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

    private val eliminationTournamentInformation = TournamentInformation("title","description", listOf(Data.ANDREW,Data.DEMON,Data.DGNT), TournamentType.ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val doubleEliminationTournamentInformation = TournamentInformation("title","description", listOf(Data.ANDREW,Data.DEMON,Data.DGNT), TournamentType.DOUBLE_ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val roundRobinTournamentInformation = TournamentInformation("title","description", listOf(Data.ANDREW,Data.DEMON,Data.DGNT), TournamentType.ROUND_ROBIN, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val swissTournamentInformation = TournamentInformation("title","description", listOf(Data.ANDREW,Data.DEMON,Data.DGNT), TournamentType.SWISS, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val survivalTournamentInformation = TournamentInformation("title","description", listOf(Data.ANDREW,Data.DEMON,Data.DGNT), TournamentType.SURVIVAL, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    @Before
    fun setUp() {
    }

    @Test
    fun testEliminationServiceCall() {
        sut.build(eliminationTournamentInformation)
        Mockito.verify(mockEliminationRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
        Mockito.verify(mockEliminationSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
    }

    @Test
    fun testDoubleEliminationServiceCall() {
        sut.build(doubleEliminationTournamentInformation)
        Mockito.verify(mockDoubleEliminationRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
        Mockito.verify(mockDoubleEliminationSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
    }

    @Test
    fun testRoundRobinServiceCall() {
        sut.build(roundRobinTournamentInformation)
        Mockito.verify(mockRoundRobinRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
        Mockito.verify(mockRoundRobinSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
    }

    @Test
    fun testSwissServiceCall() {
        sut.build(swissTournamentInformation)
        Mockito.verify(mockSwissRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
        Mockito.verify(mockSwissSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
    }

    @Test
    fun testSurvivalServiceCall() {
        sut.build(survivalTournamentInformation)
        Mockito.verify(mockSurvivalRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject())
        Mockito.verify(mockSurvivalSeedService, Mockito.times(1)).seed(MockitoHelper.anyObject())
    }

    @Test
    fun testEquality() {
        val eliminationTournament = sut.build(eliminationTournamentInformation)
        Assert.assertEquals(mockEliminationRoundUpdateService, eliminationTournament.roundUpdateService)
        Assert.assertEquals(mockEliminationRankingService, eliminationTournament.rankingService)
        Assert.assertEquals(mockEliminationMatchUpStatusTransformService, eliminationTournament.matchUpStatusTransformService)

        val doubleEliminationTournament = sut.build(doubleEliminationTournamentInformation)
        Assert.assertEquals(mockDoubleEliminationRoundUpdateService, doubleEliminationTournament.roundUpdateService)
        Assert.assertEquals(mockDoubleEliminationRankingService, doubleEliminationTournament.rankingService)
        Assert.assertEquals(mockDoubleEliminationMatchUpStatusTransformService, doubleEliminationTournament.matchUpStatusTransformService)

        val roundRobinTournament = sut.build(roundRobinTournamentInformation)
        Assert.assertEquals(mockRoundRobinRoundUpdateService, roundRobinTournament.roundUpdateService)
        Assert.assertEquals(mockRoundRobinRankingService, roundRobinTournament.rankingService)
        Assert.assertEquals(mockRoundRobinMatchUpStatusTransformService, roundRobinTournament.matchUpStatusTransformService)

        val swissTournament = sut.build(swissTournamentInformation)
        Assert.assertEquals(mockSwissRoundUpdateService, swissTournament.roundUpdateService)
        Assert.assertEquals(mockSwissRankingService, swissTournament.rankingService)
        Assert.assertEquals(mockSwissMatchUpStatusTransformService, swissTournament.matchUpStatusTransformService)

        val survivalTournament = sut.build(survivalTournamentInformation)
        Assert.assertEquals(mockSurvivalRoundUpdateService, survivalTournament.roundUpdateService)
        Assert.assertEquals(mockSurvivalRankingService, survivalTournament.rankingService)
        Assert.assertEquals(mockSurvivalMatchUpStatusTransformService, survivalTournament.matchUpStatusTransformService)

    }



}