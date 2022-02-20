package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.TournamentSortViaSharedPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito

class TournamentSortViaSharedPreferenceServiceTest {

    private val mockPreferenceService = PowerMockito.mock(IPreferenceService::class.java)


    private val sut = TournamentSortViaSharedPreferenceService(mockPreferenceService)

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
                Progress(2,3)
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
                Progress(21,33)
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
                Progress(10,15)
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
                Progress(10,15)
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
                Progress(4,15)
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
                Progress(4,15)
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
                Progress(19,95)
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
                Progress(10,10)
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
                Progress(10,10)
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
                Progress(2,4)
            ),
            foundationalTournamentEntities
        )

        list = listOf(tournament8, tournament2, tournament10, tournament3, tournament4, tournament7, tournament6, tournament1, tournament5, tournament9)


    }

    @Test
    fun testSortOnCreatedDate() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.CREATION_DATE_NEWEST)

        Assert.assertEquals(listOf(tournament7, tournament6, tournament4, tournament2, tournament5, tournament3, tournament1, tournament10, tournament9, tournament8), sut.applySort(list))
    }

    @Test
    fun testSortOnCreatedDateReversed() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.CREATION_DATE_OLDEST)

        Assert.assertEquals(listOf(tournament8, tournament10, tournament9, tournament1, tournament3, tournament5, tournament2, tournament4, tournament7, tournament6), sut.applySort(list))
    }

    @Test
    fun testSortOnLastModifiedDate() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.LAST_MODIFIED_DATE_NEWEST)

        Assert.assertEquals(listOf(tournament3, tournament5, tournament1, tournament2, tournament4, tournament8, tournament10, tournament7, tournament6, tournament9), sut.applySort(list))
    }

    @Test
    fun testSortOnLastModifiedDateReversed() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.LAST_MODIFIED_DATE_OLDEST)

        Assert.assertEquals(listOf(tournament4, tournament2, tournament1, tournament5, tournament3, tournament8, tournament10, tournament7, tournament6, tournament9), sut.applySort(list))
    }

    @Test
    fun testSortOnTitle() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.NAME)

        Assert.assertEquals(listOf(tournament1, tournament10, tournament2, tournament3, tournament4, tournament5, tournament6, tournament7, tournament8, tournament9), sut.applySort(list))
    }

    @Test
    fun testSortOnTitleReversed() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.NAME_REVERSED)

        Assert.assertEquals(listOf(tournament9, tournament8, tournament7, tournament6, tournament5, tournament4, tournament3, tournament2, tournament10, tournament1), sut.applySort(list))
    }

    @Test
    fun testSortOnTournamentType() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.TOURNAMENT_TYPE)

        Assert.assertEquals(listOf(tournament2, tournament1, tournament9, tournament7, tournament6, tournament8, tournament3, tournament4, tournament5, tournament10), sut.applySort(list))
    }

    @Test
    fun testSortOnTournamentTypeReversed() {
        PowerMockito.`when`(mockPreferenceService.getSort()).thenReturn(Sort.TOURNAMENT_TYPE_REVERSED)

        Assert.assertEquals(listOf(tournament10, tournament4, tournament5, tournament8, tournament3, tournament7, tournament6, tournament2, tournament1, tournament9), sut.applySort(list))
    }


}