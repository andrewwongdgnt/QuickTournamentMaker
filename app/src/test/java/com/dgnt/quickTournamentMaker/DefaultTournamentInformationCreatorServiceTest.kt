package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.implementation.DefaultTournamentInformationCreatorService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DefaultTournamentInformationCreatorServiceTest {

    private val title = "title"
    private val description = "description"
    private val participants = listOf(Data.ANDREW, Data.KELSEY)
    private val rankConfig = RankScoreConfig(1f, 1f, 1f)

    private val eliminationTitle = "my elimination"
    private val alternativeTitles = mapOf(Pair(TournamentType.ELIMINATION, eliminationTitle), Pair(TournamentType.DOUBLE_ELIMINATION, "my double elimination"), Pair(TournamentType.ROUND_ROBIN, "my Round Robin"), Pair(TournamentType.SWISS, "my swiss"), Pair(TournamentType.SURVIVAL, "my survival"))

    private val sut = DefaultTournamentInformationCreatorService()

    @Before
    fun setUp() {



    }

    @Test
    fun testNormalCreation() {
        val tournamentInfo = sut.create(title, alternativeTitles, description, participants, TournamentType.ELIMINATION, SeedType.RANDOM, rankConfig)
        Assert.assertEquals(title, tournamentInfo.title)
        Assert.assertEquals(description, tournamentInfo.description)
        Assert.assertEquals(participants, tournamentInfo.participants)
        Assert.assertEquals(TournamentType.ELIMINATION, tournamentInfo.tournamentType)
        Assert.assertEquals(SeedType.RANDOM, tournamentInfo.seedType)
        Assert.assertEquals(rankConfig, tournamentInfo.rankConfig)
    }

    @Test
    fun testEmptyTitle() {
        val tournamentInfo = sut.create("", alternativeTitles, description, participants, TournamentType.ELIMINATION, SeedType.RANDOM, rankConfig)
        Assert.assertEquals(eliminationTitle, tournamentInfo.title)
        Assert.assertEquals(description, tournamentInfo.description)
        Assert.assertEquals(participants, tournamentInfo.participants)
        Assert.assertEquals(TournamentType.ELIMINATION, tournamentInfo.tournamentType)
        Assert.assertEquals(SeedType.RANDOM, tournamentInfo.seedType)
        Assert.assertEquals(rankConfig, tournamentInfo.rankConfig)
    }

    @Test
    fun testBlankTitle() {
        val tournamentInfo = sut.create(" ", alternativeTitles, description, participants, TournamentType.ELIMINATION, SeedType.RANDOM, rankConfig)
        Assert.assertEquals(eliminationTitle, tournamentInfo.title)
        Assert.assertEquals(description, tournamentInfo.description)
        Assert.assertEquals(participants, tournamentInfo.participants)
        Assert.assertEquals(TournamentType.ELIMINATION, tournamentInfo.tournamentType)
        Assert.assertEquals(SeedType.RANDOM, tournamentInfo.seedType)
        Assert.assertEquals(rankConfig, tournamentInfo.rankConfig)
    }

}