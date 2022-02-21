package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.TournamentFilterViaSharedPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito
//TODO test progress
class TournamentFilterViaSharedPreferenceServiceTest {

    private val mockPreferenceService = PowerMockito.mock(IPreferenceService::class.java)


    private val sut = TournamentFilterViaSharedPreferenceService(mockPreferenceService)

    private lateinit var list: List<RestoredTournamentInformation>

    private lateinit var tournament1: RestoredTournamentInformation
    private lateinit var tournament2: RestoredTournamentInformation
    private lateinit var tournament3: RestoredTournamentInformation
    private lateinit var tournament4: RestoredTournamentInformation
    private lateinit var tournament5: RestoredTournamentInformation
    private lateinit var tournament6: RestoredTournamentInformation
    private lateinit var tournament7: RestoredTournamentInformation
    private lateinit var tournament8: RestoredTournamentInformation
    private lateinit var tournament9: RestoredTournamentInformation
    private lateinit var tournament10: RestoredTournamentInformation

    @Before
    fun setUp() {

        val foundationalTournamentEntities = FoundationalTournamentEntities(listOf(), listOf(), listOf())

        tournament1 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title1",
                    "description1",
                    TournamentType.ELIMINATION,
                    SeedType.RANDOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2022, 2, 12, 8, 24, 4),
                    LocalDateTime(2010, 12, 2, 13, 5, 7),

                    ),
                8,
                4,
                0,
                8,
                Progress(2,3) //66.66
            ),
            foundationalTournamentEntities
        )
        tournament2 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title2",
                    "description2",
                    TournamentType.ELIMINATION,
                    SeedType.CUSTOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2021, 10, 2, 1, 3, 44),
                    LocalDateTime(2011, 3, 12, 14, 0, 4),
                ),
                8,
                4,
                0,
                8,
                Progress(1,3) //33.33
            ),
            foundationalTournamentEntities
        )
        tournament3 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title3",
                    "description3",
                    TournamentType.ROUND_ROBIN,
                    SeedType.CUSTOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2022, 1, 21, 3, 17, 34),
                    LocalDateTime(2009, 1, 13, 12, 57, 28),
                ),
                7,
                4,
                3,
                7,
                Progress(2,9) //22.22
            ),
            foundationalTournamentEntities
        )
        tournament4 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title4",
                    "description4",
                    TournamentType.SWISS,
                    SeedType.RANDOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2021, 6, 11, 13, 4, 34),
                    LocalDateTime(2011, 6, 1, 23, 26, 19),
                ),
                3,
                2,
                0,
                2,
                Progress(25,50) //50.00
            ),
            foundationalTournamentEntities
        )
        tournament5 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title5",
                    "description5",
                    TournamentType.SWISS,
                    SeedType.CUSTOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2021, 12, 10, 0, 0, 0),
                    LocalDateTime(2010, 4, 30, 0, 0, 0),
                ),
                4,
                5,
                1,
                4,
                Progress(0,9) //0.00
            ),
            foundationalTournamentEntities
        )
        tournament6 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title6",
                    "description6",
                    TournamentType.DOUBLE_ELIMINATION,
                    SeedType.CUSTOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2020, 1, 1, 0, 0, 0)
                ),
                4,
                2,
                4,
                4,
                Progress(22,53) //41.50
            ),
            foundationalTournamentEntities
        )
        tournament7 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title7",
                    "description7",
                    TournamentType.DOUBLE_ELIMINATION,
                    SeedType.RANDOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2020, 1, 1, 0, 0, 0)
                ),
                4,
                2,
                4,
                4,
                Progress(2,3) //66.66
            ),
            foundationalTournamentEntities
        )
        tournament8 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title8",
                    "description8",
                    TournamentType.ROUND_ROBIN,
                    SeedType.RANDOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2022, 12, 31, 23, 59, 10)
                ),
                1,
                2,
                4,
                4,
                Progress(2,3) //66.66
            ),
            foundationalTournamentEntities
        )
        tournament9 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title9",
                    "description9",
                    TournamentType.ELIMINATION,
                    SeedType.CUSTOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2022, 6, 6, 6, 59, 10)
                ),
                6,
                6,
                4,
                6,
                Progress(25,50) //50.00
            ),
            foundationalTournamentEntities
        )
        tournament10 = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "title10",
                    "description10",
                    TournamentType.SURVIVAL,
                    SeedType.CUSTOM,
                    RankPriorityConfig.DEFAULT,
                    LocalDateTime(2022, 6, 6, 6, 59, 10)
                ),
                6,
                6,
                4,
                6,
                Progress(60,60) //100.00
            ),
            foundationalTournamentEntities
        )

        list = listOf(tournament1, tournament2, tournament3, tournament4, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10)

        // Everything disabled to start
        listOf(TournamentType.ELIMINATION, TournamentType.DOUBLE_ELIMINATION, TournamentType.ROUND_ROBIN, TournamentType.SWISS, TournamentType.SURVIVAL).forEach {
            PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(it)).thenReturn(false)
        }

        PowerMockito.`when`(mockPreferenceService.isFilteredOnMinimumParticipants()).thenReturn(false)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMaximumParticipants()).thenReturn(false)

        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestCreatedDate()).thenReturn(false)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestCreatedDate()).thenReturn(false)

        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestModifiedDate()).thenReturn(false)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestModifiedDate()).thenReturn(false)

        PowerMockito.`when`(mockPreferenceService.isFilteredOnLeastProgress()).thenReturn(false)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMostProgress()).thenReturn(false)
    }

    @Test
    fun testNoFilter() {

        Assert.assertEquals(listOf(tournament1, tournament2, tournament3, tournament4, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testNoFilterButWithValuesAnyway() {

        //test that these are ignored
        PowerMockito.`when`(mockPreferenceService.getMinimumParticipantsToFilterOn()).thenReturn(Int.MAX_VALUE)
        PowerMockito.`when`(mockPreferenceService.getMaximumParticipantsToFilterOn()).thenReturn(Int.MIN_VALUE)
        PowerMockito.`when`(mockPreferenceService.getEarliestCreatedDateToFilterOn()).thenReturn(LocalDateTime.now().plusYears(9999))
        PowerMockito.`when`(mockPreferenceService.getLatestCreatedDateToFilterOn()).thenReturn(LocalDateTime(0))
        PowerMockito.`when`(mockPreferenceService.getEarliestModifiedDateToFilterOn()).thenReturn(LocalDateTime.now().plusYears(9999))
        PowerMockito.`when`(mockPreferenceService.getLatestModifiedDateToFilterOn()).thenReturn(LocalDateTime(0))
        PowerMockito.`when`(mockPreferenceService.getLeastProgressToFilterOn()).thenReturn(100)
        PowerMockito.`when`(mockPreferenceService.getMostProgressToFilterOn()).thenReturn(0)

        Assert.assertEquals(listOf(tournament1, tournament2, tournament3, tournament4, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testEliminationOnly() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.ELIMINATION)).thenReturn(true)
        Assert.assertEquals(listOf(tournament1, tournament2, tournament9), sut.applyFilter(list))
    }

    @Test
    fun testDoubleEliminationOnly() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.DOUBLE_ELIMINATION)).thenReturn(true)
        Assert.assertEquals(listOf(tournament6, tournament7), sut.applyFilter(list))
    }

    @Test
    fun testRoundRobinOnly() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.ROUND_ROBIN)).thenReturn(true)
        Assert.assertEquals(listOf(tournament3, tournament8), sut.applyFilter(list))
    }

    @Test
    fun testSwissOnly() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.SWISS)).thenReturn(true)
        Assert.assertEquals(listOf(tournament4, tournament5), sut.applyFilter(list))
    }

    @Test
    fun testSurvivalOnly() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.SURVIVAL)).thenReturn(true)
        Assert.assertEquals(listOf(tournament10), sut.applyFilter(list))
    }

    @Test
    fun testAllTournamentTypeFilter() {
        listOf(TournamentType.ELIMINATION, TournamentType.DOUBLE_ELIMINATION, TournamentType.ROUND_ROBIN, TournamentType.SWISS, TournamentType.SURVIVAL).forEach {
            PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(it)).thenReturn(true)
        }

        // same outcome as no filter
        Assert.assertEquals(listOf(tournament1, tournament2, tournament3, tournament4, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testMin6Participants() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMinimumParticipants()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMinimumParticipantsToFilterOn()).thenReturn(6)

        Assert.assertEquals(listOf(tournament1, tournament2, tournament3, tournament9, tournament10), sut.applyFilter(list))
    }

    @Test
    fun testMin1000Participants() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMinimumParticipants()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMinimumParticipantsToFilterOn()).thenReturn(1000)

        Assert.assertTrue(sut.applyFilter(list).isEmpty())
    }

    @Test
    fun testMax6Participants() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMaximumParticipants()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMaximumParticipantsToFilterOn()).thenReturn(6)

        Assert.assertEquals(listOf(tournament4, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))
    }

    @Test
    fun testMax0Participants() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMaximumParticipants()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMaximumParticipantsToFilterOn()).thenReturn(0)

        Assert.assertTrue(sut.applyFilter(list).isEmpty())
    }

    @Test
    fun testEarliestCreatedDate() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestCreatedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getEarliestCreatedDateToFilterOn()).thenReturn(LocalDateTime(2021, 12, 10, 0, 0, 0))

        Assert.assertEquals(listOf(tournament1, tournament3, tournament5, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testEarliestCreatedDateTimeAgnostic() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestCreatedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getEarliestCreatedDateToFilterOn()).thenReturn(LocalDateTime(2021, 12, 10, 7, 7, 7))

        Assert.assertEquals(listOf(tournament1, tournament3, tournament5, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testEarliestCreatedDateInDistantFuture() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestCreatedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getEarliestCreatedDateToFilterOn()).thenReturn(LocalDateTime(3021, 12, 10, 0, 0, 0))

        Assert.assertTrue(sut.applyFilter(list).isEmpty())

    }

    @Test
    fun testLatestCreatedDate() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestCreatedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLatestCreatedDateToFilterOn()).thenReturn( LocalDateTime(2021, 6, 11, 13, 4, 34))

        Assert.assertEquals(listOf(tournament4, tournament6, tournament7), sut.applyFilter(list))
    }

    @Test
    fun testLatestCreatedDateTimeAgnostic() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestCreatedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLatestCreatedDateToFilterOn()).thenReturn( LocalDateTime(2021, 6, 11, 0, 0, 0))

        Assert.assertEquals(listOf(tournament4, tournament6, tournament7), sut.applyFilter(list))
    }

    @Test
    fun testLatestCreatedDateLongLongTimeAgo() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestCreatedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLatestCreatedDateToFilterOn()).thenReturn(LocalDateTime(1021, 12, 10, 0, 0, 0))

        Assert.assertTrue(sut.applyFilter(list).isEmpty())

    }

    @Test
    fun testEarliestModifiedDate() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestModifiedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getEarliestModifiedDateToFilterOn()).thenReturn(LocalDateTime(2010, 4, 30, 0, 0, 0))

        Assert.assertEquals(listOf(tournament1, tournament2, tournament4, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))
    }

    @Test
    fun testEarliestModifiedDateTimeAgnostic() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestModifiedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getEarliestModifiedDateToFilterOn()).thenReturn(LocalDateTime(2010, 4, 30, 2, 3, 4))

        Assert.assertEquals(listOf(tournament1, tournament2, tournament4, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))
    }

    @Test
    fun testEarliestModifiedDateInDistantFuture() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnEarliestModifiedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getEarliestModifiedDateToFilterOn()).thenReturn(LocalDateTime(3021, 12, 10, 0, 0, 0))

        Assert.assertEquals(listOf(tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testLatestModifiedDate() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestModifiedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLatestModifiedDateToFilterOn()).thenReturn(LocalDateTime(2010, 4, 30, 0, 0, 0))

        Assert.assertEquals(listOf(tournament3, tournament5, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testLatestModifiedDateTimeAgnostic() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestModifiedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLatestModifiedDateToFilterOn()).thenReturn(LocalDateTime(2009, 1, 13, 0, 0, 0))

        Assert.assertEquals(listOf(tournament3, tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testLatestModifiedDateLongLongTimeAgo() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestModifiedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLatestModifiedDateToFilterOn()).thenReturn(LocalDateTime(1021, 12, 10, 0, 0, 0))

        Assert.assertEquals(listOf(tournament6, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))

    }

    @Test
    fun testEliminationTypeWithAtLeast8Participants() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.ELIMINATION)).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMinimumParticipants()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMinimumParticipantsToFilterOn()).thenReturn(8)

        Assert.assertEquals(listOf(tournament1, tournament2), sut.applyFilter(list))

    }

    @Test
    fun testLeastProgress() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLeastProgress()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLeastProgressToFilterOn()).thenReturn(50)

        Assert.assertEquals(listOf(tournament1, tournament4, tournament7, tournament8, tournament9, tournament10), sut.applyFilter(list))
    }

    @Test
    fun testMostProgress() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMostProgress()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMostProgressToFilterOn()).thenReturn(50)

        Assert.assertEquals(listOf(tournament2, tournament3, tournament4, tournament5, tournament6, tournament9), sut.applyFilter(list))
    }

    //---------------------------------------------------
    // Mixed Filters
    //---------------------------------------------------

    @Test
    fun testEliminationAndDoubleEliminationType() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.ELIMINATION)).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.DOUBLE_ELIMINATION)).thenReturn(true)

        Assert.assertEquals(listOf(tournament1, tournament2, tournament6, tournament7, tournament9), sut.applyFilter(list))
    }

    @Test
    fun testRoundRobinTypeWithAtLeast5Participants() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.ROUND_ROBIN)).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMinimumParticipants()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMinimumParticipantsToFilterOn()).thenReturn(5)

        Assert.assertEquals(listOf(tournament3), sut.applyFilter(list))
    }

    @Test
    fun testRoundRobinTypeWithAtLeast5ParticipantsAndEarlyLatestCreatedDate() {
        PowerMockito.`when`(mockPreferenceService.isFilteredOnTournamentType(TournamentType.ROUND_ROBIN)).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnMinimumParticipants()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getMinimumParticipantsToFilterOn()).thenReturn(5)
        PowerMockito.`when`(mockPreferenceService.isFilteredOnLatestCreatedDate()).thenReturn(true)
        PowerMockito.`when`(mockPreferenceService.getLatestCreatedDateToFilterOn()).thenReturn(LocalDateTime(2022, 1, 20, 3, 17, 34))

        Assert.assertTrue(sut.applyFilter(list).isEmpty())
    }

}