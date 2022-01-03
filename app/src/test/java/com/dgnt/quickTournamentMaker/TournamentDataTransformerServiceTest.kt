package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.Data.Companion.ANDREW_PERSON
import com.dgnt.quickTournamentMaker.Data.Companion.DGNT_PERSON
import com.dgnt.quickTournamentMaker.Data.Companion.FIRE_PERSON
import com.dgnt.quickTournamentMaker.Data.Companion.HERO_PERSON
import com.dgnt.quickTournamentMaker.Data.Companion.KELSEY_PERSON
import com.dgnt.quickTournamentMaker.Data.Companion.KYRA_PERSON
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.TournamentDataTransformerService
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito

class TournamentDataTransformerServiceTest {

    private val PARTICIPANT_TITLE = "participantTitle"
    private val PARTICIPANT_NOTE = "participantNote"
    private val PARTICIPANT_COLOR = 4321

    private val MATCH_UP_TITLE = "matchUpTitle"
    private val MATCH_UP_NOTE = "matchUpNote"
    private val MATCH_UP_COLOR = 1234

    private val ROUND_ORIGINAL_TITLE = "roundOriginalTitle"
    private val ROUND_TITLE = "roundTitle"
    private val ROUND_NOTE = "roundNote"
    private val ROUND_COLOR = -666

    private val TOURNAMENT_TITLE = "tournamentTitle"
    private val TOURNAMENT_DESCRIPTION = "tournamentDescription"

    private val mockRankingConfigService = PowerMockito.mock(IRankingConfigService::class.java)
    private val mockRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val sut = TournamentDataTransformerService(mockRankingConfigService)

    private val CREATION_TIME = LocalDateTime(666555444)

    private lateinit var tournament1: Tournament

    @Before
    fun setUp() {
        val round1 = Round(
            0, 0,
            listOf(
                MatchUp(
                    0,
                    0,
                    0,
                    Participant(
                        ANDREW_PERSON,
                        name = PARTICIPANT_TITLE,
                        note = PARTICIPANT_NOTE,
                        color = PARTICIPANT_COLOR,
                    ),
                    Data.KYRA,
                    MATCH_UP_TITLE,
                    true,
                    MatchUpStatus.P1_WINNER,
                    MATCH_UP_NOTE,
                    MATCH_UP_COLOR
                ),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, "", status = MatchUpStatus.P2_WINNER),
                MatchUp(0, 0, 1, Data.FIRE, Participant.BYE_PARTICIPANT, ""),
                MatchUp(0, 0, 2, Data.HERO, Participant.BYE_PARTICIPANT, "")
            ),
            ROUND_ORIGINAL_TITLE,
            ROUND_TITLE,
            ROUND_NOTE,
            ROUND_COLOR,
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
        tournament1 = Tournament(
            TournamentInformation(TOURNAMENT_TITLE, TOURNAMENT_DESCRIPTION, TournamentType.ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, CREATION_TIME),
            roundGroups,
            mockMatchUpStatusTransformService,
            mockRoundUpdateService,
            mockRankingService
        )
    }


    @Test
    fun testTransform() {
        val tournamentData = sut.transform(tournament1)
        Assert.assertEquals(TOURNAMENT_TITLE, tournamentData.title)
        Assert.assertEquals(TOURNAMENT_DESCRIPTION, tournamentData.description)
        Assert.assertEquals(TournamentType.ELIMINATION, tournamentData.type)
        Assert.assertNull(tournamentData.rankingConfig)
        Assert.assertEquals(SeedType.CUSTOM, tournamentData.seedType)
        Assert.assertEquals(CREATION_TIME, tournamentData.creationDate)
        Assert.assertNull(tournamentData.lastModifiedDate)
        Assert.assertEquals(8, tournamentData.participants.size)
        tournamentData.participants[0].let { participantData ->
            Assert.assertEquals(ANDREW_PERSON.name, participantData.name)
            Assert.assertEquals(PARTICIPANT_TITLE, participantData.displayName)
            Assert.assertEquals(PARTICIPANT_NOTE, participantData.note)
            Assert.assertEquals(PARTICIPANT_COLOR, participantData.color)
        }
        Assert.assertEquals(KYRA_PERSON.name, tournamentData.participants[1].name)
        Assert.assertEquals(DGNT_PERSON.name, tournamentData.participants[2].name)
        Assert.assertEquals(KELSEY_PERSON.name, tournamentData.participants[3].name)
        Assert.assertEquals(FIRE_PERSON.name, tournamentData.participants[4].name)
        Assert.assertEquals(ParticipantType.BYE, tournamentData.participants[5].type)
        Assert.assertEquals(HERO_PERSON.name, tournamentData.participants[6].name)
        Assert.assertEquals(ParticipantType.BYE, tournamentData.participants[7].type)
    }


}