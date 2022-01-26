package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.tournament.MatchUpEntity
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantEntity
import com.dgnt.quickTournamentMaker.data.tournament.RoundEntity
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.TournamentRestoreService
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService
import org.joda.time.LocalDateTime
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class TournamentRestoreServiceTest {

    private val mockRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockRankingConfigService = PowerMockito.mock(IRankingConfigService::class.java)
    private val sut = TournamentRestoreService()

    private lateinit var tournament: Tournament

    @Before
    fun setUp() {

        val andrew = Participant(Data.ANDREW_PERSON)
        val kyra = Participant(Data.KYRA_PERSON)
        val dgnt = Participant(Data.DGNT_PERSON)
        val kelsey = Participant(Data.KELSEY_PERSON)
        val fire = Participant(Data.FIRE_PERSON)
        val hero = Participant(Data.HERO_PERSON)

        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, andrew, kyra, ""),
                MatchUp(0, 0, 1, dgnt, kelsey, ""),
                MatchUp(0, 0, 2, fire, Participant.BYE_PARTICIPANT, ""),
                MatchUp(0, 0, 3, hero, Participant.BYE_PARTICIPANT, "")
            ),
            ""
        )
        val round2 = Round(
            0, 1,
            listOf(
                MatchUp(0, 1, 0, Participant.NULL_PARTICIPANT, Participant.BYE_PARTICIPANT, ""),
                MatchUp(0, 1, 1, Participant.BYE_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
            ),
            ""
        )

        val round1_2 = Round(
            1, 0, listOf(
                MatchUp(1, 0, 0, andrew, kyra, ""),
                MatchUp(1, 0, 1, dgnt, kelsey, ""),
                MatchUp(1, 0, 2, fire, Participant.BYE_PARTICIPANT, ""),
                MatchUp(1, 0, 3, hero, Participant.BYE_PARTICIPANT, "")
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
        tournament = Tournament(
            TournamentInformation("title", "description", TournamentType.ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, LocalDateTime.now()),
            roundGroups,
            mockMatchUpStatusTransformService,
            mockRoundUpdateService,
            mockRankingService,
            mockRankingConfigService
        )
    }


    @Test
    fun testRestore() {

        val creationTime = LocalDateTime(2020, 3, 12, 4, 23, 23)
        val lastModifiedTime = LocalDateTime(2020, 6, 14, 6, 43, 3)
        val restoredTournamentInformation = RestoredTournamentInformation(
            ExtendedTournamentInformation(
                TournamentInformation(
                    "new Title",
                    "new Description",
                    TournamentType.ROUND_ROBIN,
                    SeedType.RANDOM,
                    RankScoreConfig(9f, 3f, 2.5f),
                    creationTime,
                    lastModifiedTime,
                ),
                0,
                0,
                0,
                0,
            ),
            listOf(
                RoundEntity(creationTime, 0, 0, "first Round", "first Round edited", "first Round note", 45),
                RoundEntity(creationTime, 0, 1, "second Round", "second Round edited", "second Round note", 77),
                RoundEntity(creationTime, 1, 0, "", "", "", 0),
                RoundEntity(creationTime, 1, 1, "", "", "", 0),
            ),
            listOf(
                MatchUpEntity(creationTime, 0, 0, 0, false, "match up 1 name", "match up 1 note", 232, MatchUpStatus.P1_WINNER, false),
                MatchUpEntity(creationTime, 0, 0, 1, true, "match up 2 name", "match up 2 note", 55, MatchUpStatus.P2_WINNER, false),
                MatchUpEntity(creationTime, 0, 0, 2, false, "", "", 0, MatchUpStatus.TIE, true),
                MatchUpEntity(creationTime, 0, 0, 3, false, "", "", 0, MatchUpStatus.TIE, true),

                MatchUpEntity(creationTime, 0, 1, 0, false, "", "", 0, MatchUpStatus.P2_WINNER, true),
                MatchUpEntity(creationTime, 0, 1, 1, false, "", "", 0, MatchUpStatus.P1_WINNER, true),

                MatchUpEntity(creationTime, 1, 0, 0, false, "Xmatch up 1 name", "Xmatch up 1 note", 232, MatchUpStatus.P1_WINNER, false),
                MatchUpEntity(creationTime, 1, 0, 1, true, "Xmatch up 2 name", "Xmatch up 2 note", 55, MatchUpStatus.P2_WINNER, false),
                MatchUpEntity(creationTime, 1, 0, 2, false, "X", "", 0, MatchUpStatus.TIE, true),
                MatchUpEntity(creationTime, 1, 0, 3, false, "X", "", 0, MatchUpStatus.TIE, true),

                MatchUpEntity(creationTime, 1, 1, 0, false, "", "", 0, MatchUpStatus.P2_WINNER, true),
                MatchUpEntity(creationTime, 1, 1, 1, false, "", "", 0, MatchUpStatus.P1_WINNER, true),
            ),
            listOf(
                ParticipantEntity(creationTime, Data.ANDREW_PERSON.name, 0, "Andrew X", "Andrew Note", ParticipantType.NORMAL, 123),
                ParticipantEntity(creationTime, Data.KYRA_PERSON.name, 1, "Kyra X", "Kyra Note", ParticipantType.NORMAL, 666666),
                ParticipantEntity(creationTime, Data.DGNT_PERSON.name, 2, "", "", ParticipantType.NORMAL, 0),
                ParticipantEntity(creationTime, Data.KELSEY_PERSON.name, 3, "", "", ParticipantType.NORMAL, 0),
                ParticipantEntity(creationTime, Data.FIRE_PERSON.name, 4, "-", "-", ParticipantType.NORMAL, 0),
                ParticipantEntity(creationTime, "bye", 5, "", "", ParticipantType.BYE, 0),
                ParticipantEntity(creationTime, Data.HERO_PERSON.name, 6, "", "", ParticipantType.NORMAL, 0),
                ParticipantEntity(creationTime, "bye", 7, "", "", ParticipantType.BYE, 0),
            ),
        )

        sut.restore(tournament, restoredTournamentInformation)


        Assert.assertEquals("new Title", tournament.tournamentInformation.title)
        Assert.assertEquals("new Description", tournament.tournamentInformation.description)
        Assert.assertEquals(TournamentType.ROUND_ROBIN, tournament.tournamentInformation.tournamentType)
        Assert.assertEquals(SeedType.RANDOM, tournament.tournamentInformation.seedType)
        val rankConfig = tournament.tournamentInformation.rankConfig as RankScoreConfig
        Assert.assertEquals(9f, rankConfig.win)
        Assert.assertEquals(3f, rankConfig.loss)
        Assert.assertEquals(2.5f, rankConfig.tie)
        Assert.assertEquals(creationTime, tournament.tournamentInformation.creationDate)
        Assert.assertEquals(lastModifiedTime, tournament.tournamentInformation.lastModifiedDate)

        Assert.assertEquals(2, tournament.roundGroups.size)

        // Asserting round values
        Assert.assertEquals(2, tournament.roundGroups[0].rounds.size)
        tournament.roundGroups[0].rounds[0].let {
            Assert.assertEquals("first Round", it.originalTitle)
            Assert.assertEquals("first Round edited", it.title)
            Assert.assertEquals("first Round note", it.note)
            Assert.assertEquals(45, it.color)
        }
        tournament.roundGroups[0].rounds[1].let {
            Assert.assertEquals("second Round", it.originalTitle)
            Assert.assertEquals("second Round edited", it.title)
            Assert.assertEquals("second Round note", it.note)
            Assert.assertEquals(77, it.color)
        }

        Assert.assertEquals(2, tournament.roundGroups[1].rounds.size)
        tournament.roundGroups[1].rounds[0].let {
            Assert.assertEquals("", it.originalTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
        }
        tournament.roundGroups[1].rounds[1].let {
            Assert.assertEquals("", it.originalTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
        }

        // Asserting matchUp values
        Assert.assertEquals(4, tournament.roundGroups[0].rounds[0].matchUps.size)
        tournament.roundGroups[0].rounds[0].matchUps[0].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("match up 1 name", it.title)
            Assert.assertEquals("match up 1 note", it.note)
            Assert.assertEquals(232, it.color)
            Assert.assertEquals(MatchUpStatus.P1_WINNER, it.status)
        }
        tournament.roundGroups[0].rounds[0].matchUps[1].let {
            Assert.assertTrue(it.useTitle)
            Assert.assertEquals("match up 2 name", it.title)
            Assert.assertEquals("match up 2 note", it.note)
            Assert.assertEquals(55, it.color)
            Assert.assertEquals(MatchUpStatus.P2_WINNER, it.status)
        }
        tournament.roundGroups[0].rounds[0].matchUps[2].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.TIE, it.status)
        }
        tournament.roundGroups[0].rounds[0].matchUps[3].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.TIE, it.status)
        }

        Assert.assertEquals(2, tournament.roundGroups[0].rounds[1].matchUps.size)
        tournament.roundGroups[0].rounds[1].matchUps[0].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.P2_WINNER, it.status)
        }
        tournament.roundGroups[0].rounds[1].matchUps[1].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.P1_WINNER, it.status)
        }

        Assert.assertEquals(4, tournament.roundGroups[1].rounds[0].matchUps.size)
        tournament.roundGroups[1].rounds[0].matchUps[0].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("Xmatch up 1 name", it.title)
            Assert.assertEquals("Xmatch up 1 note", it.note)
            Assert.assertEquals(232, it.color)
            Assert.assertEquals(MatchUpStatus.P1_WINNER, it.status)
        }
        tournament.roundGroups[1].rounds[0].matchUps[1].let {
            Assert.assertTrue(it.useTitle)
            Assert.assertEquals("Xmatch up 2 name", it.title)
            Assert.assertEquals("Xmatch up 2 note", it.note)
            Assert.assertEquals(55, it.color)
            Assert.assertEquals(MatchUpStatus.P2_WINNER, it.status)
        }
        tournament.roundGroups[1].rounds[0].matchUps[2].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("X", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.TIE, it.status)
        }
        tournament.roundGroups[1].rounds[0].matchUps[3].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("X", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.TIE, it.status)
        }

        Assert.assertEquals(2, tournament.roundGroups[1].rounds[1].matchUps.size)
        tournament.roundGroups[1].rounds[1].matchUps[0].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.P2_WINNER, it.status)
        }
        tournament.roundGroups[1].rounds[1].matchUps[1].let {
            Assert.assertFalse(it.useTitle)
            Assert.assertEquals("", it.title)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(0, it.color)
            Assert.assertEquals(MatchUpStatus.P1_WINNER, it.status)
        }

        // Asserting participant values
        Assert.assertEquals(8, tournament.orderedParticipants.size)
        tournament.orderedParticipants[0].let {
            Assert.assertEquals(Data.ANDREW_PERSON.name, it.person.name)
            Assert.assertEquals("Andrew X", it.name)
            Assert.assertEquals("Andrew Note", it.note)
            Assert.assertEquals(ParticipantType.NORMAL, it.participantType)
            Assert.assertEquals(123, it.color)
        }
        tournament.orderedParticipants[1].let {
            Assert.assertEquals(Data.KYRA_PERSON.name, it.person.name)
            Assert.assertEquals("Kyra X", it.name)
            Assert.assertEquals("Kyra Note", it.note)
            Assert.assertEquals(ParticipantType.NORMAL, it.participantType)
            Assert.assertEquals(666666, it.color)
        }
        tournament.orderedParticipants[2].let {
            Assert.assertEquals(Data.DGNT_PERSON.name, it.person.name)
            Assert.assertEquals("", it.name)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(ParticipantType.NORMAL, it.participantType)
            Assert.assertEquals(0, it.color)
        }
        tournament.orderedParticipants[3].let {
            Assert.assertEquals(Data.KELSEY_PERSON.name, it.person.name)
            Assert.assertEquals("", it.name)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(ParticipantType.NORMAL, it.participantType)
            Assert.assertEquals(0, it.color)
        }
        tournament.orderedParticipants[4].let {
            Assert.assertEquals(Data.FIRE_PERSON.name, it.person.name)
            Assert.assertEquals("", it.name)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(ParticipantType.NORMAL, it.participantType)
            Assert.assertEquals(0, it.color)
        }
        tournament.orderedParticipants[5].let {
            Assert.assertEquals(ParticipantType.BYE, it.participantType)
        }
        tournament.orderedParticipants[6].let {
            Assert.assertEquals(Data.HERO_PERSON.name, it.person.name)
            Assert.assertEquals("", it.name)
            Assert.assertEquals("", it.note)
            Assert.assertEquals(ParticipantType.NORMAL, it.participantType)
            Assert.assertEquals(0, it.color)
        }
        tournament.orderedParticipants[7].let {
            Assert.assertEquals(ParticipantType.BYE, it.participantType)
        }
    }

}