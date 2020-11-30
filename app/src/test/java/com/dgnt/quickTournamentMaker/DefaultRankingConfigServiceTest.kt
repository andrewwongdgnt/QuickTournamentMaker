package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfigType
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.service.implementation.DefaultRankingConfigService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class DefaultRankingConfigServiceTest {

    private val sut = DefaultRankingConfigService()

    @Before
    fun setUp() {
    }

    @Test
    fun testBuildRankingFromPriorityWLT() {
        val rankPriorityConfig = sut.buildRankingFromPriority("w;l;t")
        Assert.assertEquals(RankPriorityConfigType.WIN, rankPriorityConfig.first)
        Assert.assertEquals(RankPriorityConfigType.LOSS, rankPriorityConfig.second)
        Assert.assertEquals(RankPriorityConfigType.TIE, rankPriorityConfig.third)
    }

    @Test
    fun testBuildRankingFromPriorityWTL() {
        val rankPriorityConfig = sut.buildRankingFromPriority("w;t;l")
        Assert.assertEquals(RankPriorityConfigType.WIN, rankPriorityConfig.first)
        Assert.assertEquals(RankPriorityConfigType.TIE, rankPriorityConfig.second)
        Assert.assertEquals(RankPriorityConfigType.LOSS, rankPriorityConfig.third)
    }


    @Test
    fun testBuildRankingFromPriorityInvalidString() {
        val rankPriorityConfig = sut.buildRankingFromPriority("wsdfsf334")
        Assert.assertEquals(RankPriorityConfigType.WIN, rankPriorityConfig.first)
        Assert.assertEquals(RankPriorityConfigType.LOSS, rankPriorityConfig.second)
        Assert.assertEquals(RankPriorityConfigType.TIE, rankPriorityConfig.third)
    }

    @Test
    fun testBuildRankingFromPriorityRepeatedPriorityTypes() {
        val rankPriorityConfig = sut.buildRankingFromPriority("w;w;w")
        Assert.assertEquals(RankPriorityConfigType.WIN, rankPriorityConfig.first)
        Assert.assertEquals(RankPriorityConfigType.LOSS, rankPriorityConfig.second)
        Assert.assertEquals(RankPriorityConfigType.TIE, rankPriorityConfig.third)
    }

    @Test
    fun testBuildRankingFromPriority4Types() {
        val rankPriorityConfig = sut.buildRankingFromPriority("w;w;w;t;l;t")
        Assert.assertEquals(RankPriorityConfigType.WIN, rankPriorityConfig.first)
        Assert.assertEquals(RankPriorityConfigType.LOSS, rankPriorityConfig.second)
        Assert.assertEquals(RankPriorityConfigType.TIE, rankPriorityConfig.third)
    }

    @Test
    fun testGetRankingFromPriorityAsString() {
        Assert.assertEquals("w;l;t", sut.getRankingFromPriorityAsString(RankPriorityConfig(RankPriorityConfigType.WIN, RankPriorityConfigType.LOSS, RankPriorityConfigType.TIE)))
        Assert.assertEquals("l;t;w", sut.getRankingFromPriorityAsString(RankPriorityConfig(RankPriorityConfigType.LOSS, RankPriorityConfigType.TIE, RankPriorityConfigType.WIN)))
    }

    @Test
    fun testBuildRankingFromScore201() {
        val rankScoreConfig = sut.buildRankingFromScore("2;0;1")
        Assert.assertEquals(2, rankScoreConfig.win)
        Assert.assertEquals(0, rankScoreConfig.loss)
        Assert.assertEquals(1, rankScoreConfig.tie)
    }

    @Test
    fun testBuildRankingFromScoreMinus221014() {
        val rankScoreConfig = sut.buildRankingFromScore("-22;10;14")
        Assert.assertEquals(-22, rankScoreConfig.win)
        Assert.assertEquals(10, rankScoreConfig.loss)
        Assert.assertEquals(14, rankScoreConfig.tie)
    }

    @Test
    fun testBuildRankingFromScoreInvalidString() {
        val rankScoreConfig = sut.buildRankingFromScore("38472a;3df")
        Assert.assertEquals(2, rankScoreConfig.win)
        Assert.assertEquals(0, rankScoreConfig.loss)
        Assert.assertEquals(1, rankScoreConfig.tie)
    }

    @Test
    fun testBuildRankingFromPriority4Numbers() {
        val rankScoreConfig = sut.buildRankingFromScore("3;4;5;3;3;3")
        Assert.assertEquals(2, rankScoreConfig.win)
        Assert.assertEquals(0, rankScoreConfig.loss)
        Assert.assertEquals(1, rankScoreConfig.tie)
    }

    @Test
    fun testGetRankingFromScoreAsString() {
        Assert.assertEquals("2;0;1", sut.getRankingFromScoreAsString(RankScoreConfig(2, 0, 1)))
        Assert.assertEquals("-22;10;14", sut.getRankingFromScoreAsString(RankScoreConfig(-22, 10, 14)))
    }

}