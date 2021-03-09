package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfigType
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.service.implementation.RankingConfigService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class RankingConfigServiceTest {

    private val sut = RankingConfigService()

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
        val rankScoreConfig = sut.buildRankingFromScore("2.0;0.0;1.0")
        Assert.assertEquals(2f, rankScoreConfig.win)
        Assert.assertEquals(0f, rankScoreConfig.loss)
        Assert.assertEquals(1f, rankScoreConfig.tie)
    }

    @Test
    fun testBuildRankingFromScore10half() {
        val rankScoreConfig = sut.buildRankingFromScore("1.0;0.0;0.5")
        Assert.assertEquals(1f, rankScoreConfig.win)
        Assert.assertEquals(0f, rankScoreConfig.loss)
        Assert.assertEquals(0.5f, rankScoreConfig.tie)
    }

    @Test
    fun testBuildRankingFromScoreMinus221014() {
        val rankScoreConfig = sut.buildRankingFromScore("-22.0;10.0;14.0")
        Assert.assertEquals(-22f, rankScoreConfig.win)
        Assert.assertEquals(10f, rankScoreConfig.loss)
        Assert.assertEquals(14f, rankScoreConfig.tie)
    }

    @Test
    fun testBuildRankingFromScoreInvalidString() {
        val rankScoreConfig = sut.buildRankingFromScore("38472a;3df")
        Assert.assertEquals(1f, rankScoreConfig.win)
        Assert.assertEquals(0f, rankScoreConfig.loss)
        Assert.assertEquals(0.5f, rankScoreConfig.tie)
    }

    @Test
    fun testBuildRankingFromPriority4Numbers() {
        val rankScoreConfig = sut.buildRankingFromScore("3.0;4.0;5.0;3.0;3.0;3.0")
        Assert.assertEquals(1f, rankScoreConfig.win)
        Assert.assertEquals(0f, rankScoreConfig.loss)
        Assert.assertEquals(0.5f, rankScoreConfig.tie)
    }

    @Test
    fun testGetRankingFromScoreAsString() {
        Assert.assertEquals("2.0;0.0;1.0", sut.getRankingFromScoreAsString(RankScoreConfig(2f, 0f, 1f)))
        Assert.assertEquals("2.5;0.5;1.0", sut.getRankingFromScoreAsString(RankScoreConfig(2.5f, 0.5f, 1f)))
        Assert.assertEquals("-22.0;10.0;14.0", sut.getRankingFromScoreAsString(RankScoreConfig(-22f, 10f, 14f)))
    }

}