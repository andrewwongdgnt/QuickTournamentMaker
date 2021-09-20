package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.implementation.SwissRoundUpdateService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito

class SwissRoundUpdateServiceTest {

    private val mockRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val sut = SwissRoundUpdateService(mockRankingService)
    private lateinit var roundGroups: List<RoundGroup>
    private lateinit var roundGroupsWithBye: List<RoundGroup>

    @Before
    fun setUp() {

        val round1 = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                MatchUp(0, 0, 2, Data.FIRE, Data.SUPER, ""),
                MatchUp(0, 0, 3, Data.HERO, Data.DEMON, "")
            ),
            ""
        )
        val rounds = (1..7).map {
            if (it == 1)
                round1
            else
                Round(
                    0, it - 1, listOf(
                        MatchUp(0, it - 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                        MatchUp(0, it - 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                        MatchUp(0, it - 1, 2, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                        MatchUp(0, it - 1, 3, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
                    ),
                    ""
                )
        }
        roundGroups = listOf(RoundGroup(0, rounds, ""))


        val round1WithBye = Round(
            0, 0, listOf(
                MatchUp(0, 0, 0, Data.ANDREW, Data.FIRE, ""),
                MatchUp(0, 0, 1, Data.KYRA, Participant.BYE_PARTICIPANT, "")
            ),
            ""
        )
        val roundsWithBye = (1..3).map {
            if (it == 1)
                round1WithBye
            else
                Round(
                    0, it - 1, listOf(
                        MatchUp(0, it - 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, ""),
                        MatchUp(0, it - 1, 1, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT, "")
                    ),
                    ""
                )
        }
        roundGroupsWithBye = listOf(RoundGroup(0, roundsWithBye, ""))

    }

    private fun helperMockRankingService(list: List<Set<Participant>>) {
        PowerMockito.`when`(mockRankingService.calculate(MockitoHelper.anyObject(), MockitoHelper.anyObject())).thenReturn(
            Rank(list, setOf())
        );
    }

    @Test
    fun testDistributionWithNoTies() {

        val round1 = roundGroups[0].rounds[0]
        val round2 = roundGroups[0].rounds[1]

        val assertRound2NullParticipants = {
            for (i in 0..3) {
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round2.matchUps[i].participant1)
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round2.matchUps[i].participant2)
            }
        }

        //Andrew Vs Kyra
        round1.matchUps[0].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA), setOf(Data.SUPER, Data.KELSEY, Data.HERO, Data.FIRE, Data.DGNT, Data.DEMON), setOf(Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 0)
        assertRound2NullParticipants()

        //Dgnt Vs Kelsey
        round1.matchUps[1].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.KELSEY), setOf(Data.SUPER, Data.HERO, Data.FIRE, Data.DEMON), setOf(Data.DGNT, Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 1)
        assertRound2NullParticipants()

        //Fire vs Super
        round1.matchUps[2].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KYRA, Data.KELSEY), setOf(Data.HERO, Data.DEMON), setOf(Data.FIRE, Data.DGNT, Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 2)
        assertRound2NullParticipants()

        //Hero vs Demon
        round1.matchUps[3].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KYRA, Data.KELSEY, Data.DEMON), setOf(Data.HERO, Data.FIRE, Data.DGNT, Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 3)
        Assert.assertEquals(Data.ANDREW, round2.matchUps[0].participant1)//1-0
        Assert.assertEquals(Data.DGNT, round2.matchUps[0].participant2)//1-0
        Assert.assertEquals(Data.FIRE, round2.matchUps[1].participant1)//1-0
        Assert.assertEquals(Data.HERO, round2.matchUps[1].participant2)//1-0
        Assert.assertEquals(Data.DEMON, round2.matchUps[2].participant1)//0-1
        Assert.assertEquals(Data.KELSEY, round2.matchUps[2].participant2)//0-1
        Assert.assertEquals(Data.KYRA, round2.matchUps[3].participant1)//0-1
        Assert.assertEquals(Data.SUPER, round2.matchUps[3].participant2)//0-1

        val round3 = roundGroups[0].rounds[2]

        val assertRound3NullParticipants = {
            for (i in 0..3) {
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round3.matchUps[i].participant1)
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round3.matchUps[i].participant2)
            }
        }

        //Andrew Vs Dgnt
        round2.matchUps[0].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KYRA, Data.KELSEY, Data.DEMON), setOf(Data.DGNT), setOf(Data.HERO, Data.FIRE), setOf(Data.ANDREW)))
        sut.update(roundGroups, 0, 1, 0)
        assertRound3NullParticipants()

        //Fire Vs Hero
        round2.matchUps[1].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KYRA, Data.KELSEY, Data.DEMON), setOf(Data.HERO, Data.DGNT), setOf(Data.FIRE, Data.ANDREW)))
        sut.update(roundGroups, 0, 1, 1)
        assertRound3NullParticipants()

        //Demon vs Kelsey
        round2.matchUps[2].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KELSEY), setOf(Data.SUPER, Data.KYRA), setOf(Data.HERO, Data.DGNT, Data.DEMON), setOf(Data.FIRE, Data.ANDREW)))
        sut.update(roundGroups, 0, 1, 2)
        assertRound3NullParticipants()

        //Kyra vs Super
        round2.matchUps[3].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KELSEY), setOf(Data.KYRA, Data.HERO, Data.DGNT, Data.DEMON), setOf(Data.FIRE, Data.ANDREW)))
        sut.update(roundGroups, 0, 1, 3)
        Assert.assertEquals(Data.ANDREW, round3.matchUps[0].participant1)//2-0
        Assert.assertEquals(Data.FIRE, round3.matchUps[0].participant2)//2-0
        Assert.assertEquals(Data.DEMON, round3.matchUps[1].participant1)//1-1
        Assert.assertEquals(Data.DGNT, round3.matchUps[1].participant2)//1-1
        Assert.assertEquals(Data.HERO, round3.matchUps[2].participant1)//1-1
        Assert.assertEquals(Data.KYRA, round3.matchUps[2].participant2)//1-1
        Assert.assertEquals(Data.KELSEY, round3.matchUps[3].participant1)//0-2
        Assert.assertEquals(Data.SUPER, round3.matchUps[3].participant2)//0-2

        val round4 = roundGroups[0].rounds[3]

        val assertRound4NullParticipants = {
            for (i in 0..3) {
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round4.matchUps[i].participant1)
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round4.matchUps[i].participant2)
            }
        }

        //Andrew vs Fire
        round3.matchUps[0].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KELSEY), setOf(Data.KYRA, Data.HERO, Data.DGNT, Data.DEMON), setOf(Data.FIRE), setOf(Data.ANDREW)))
        sut.update(roundGroups, 0, 2, 0)
        assertRound4NullParticipants()

        //Demon vs Dgnt
        round3.matchUps[1].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KELSEY), setOf(Data.DGNT), setOf(Data.KYRA, Data.HERO, Data.DEMON), setOf(Data.FIRE, Data.DEMON), setOf(Data.ANDREW)))
        sut.update(roundGroups, 0, 2, 1)
        assertRound4NullParticipants()

        //Hero vs Kyra
        round3.matchUps[2].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KELSEY), setOf(Data.KYRA, Data.DGNT), setOf(Data.HERO, Data.FIRE, Data.DEMON), setOf(Data.ANDREW)))
        sut.update(roundGroups, 0, 2, 2)
        assertRound4NullParticipants()

        //Kelsey vs Super
        round3.matchUps[3].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.SUPER), setOf(Data.KYRA, Data.KELSEY, Data.DGNT), setOf(Data.HERO, Data.FIRE, Data.DEMON), setOf(Data.ANDREW)))
        sut.update(roundGroups, 0, 2, 3)
        Assert.assertEquals(Data.ANDREW, round4.matchUps[0].participant1)//3-0
        Assert.assertEquals(Data.DEMON, round4.matchUps[0].participant2)//2-1
        Assert.assertEquals(Data.FIRE, round4.matchUps[1].participant1)//2-1
        Assert.assertEquals(Data.DGNT, round4.matchUps[1].participant2)//1-2
        Assert.assertEquals(Data.HERO, round4.matchUps[2].participant1)//2-1
        Assert.assertEquals(Data.SUPER, round4.matchUps[2].participant2)//0-3
        Assert.assertEquals(Data.KELSEY, round4.matchUps[3].participant1)//1-2
        Assert.assertEquals(Data.KYRA, round4.matchUps[3].participant2)//0-3

        round1.matchUps[0].status = MatchUpStatus.DEFAULT
        helperMockRankingService(listOf(setOf(Data.SUPER, Data.KELSEY, Data.DEMON), setOf(Data.KYRA, Data.ANDREW), setOf(Data.HERO, Data.FIRE, Data.DGNT)))
        sut.update(roundGroups, 0, 0, 0)
        assertRound2NullParticipants()
        assertRound3NullParticipants()
        assertRound4NullParticipants()
    }

    @Test
    fun testDistributionWithTies() {

        val round1 = roundGroups[0].rounds[0]
        val round2 = roundGroups[0].rounds[1]

        val assertRound2NullParticipants = {
            for (i in 0..3) {
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round2.matchUps[i].participant1)
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round2.matchUps[i].participant2)
            }
        }

        //Andrew Vs Kyra
        round1.matchUps[0].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA), setOf(Data.SUPER, Data.KELSEY, Data.HERO, Data.FIRE, Data.DGNT, Data.DEMON), setOf(Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 0)
        assertRound2NullParticipants()

        //Dgnt Vs Kelsey
        round1.matchUps[1].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.KELSEY), setOf(Data.SUPER, Data.HERO, Data.FIRE, Data.DEMON), setOf(Data.DGNT, Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 1)
        assertRound2NullParticipants()

        //Fire vs Super
        round1.matchUps[2].status = MatchUpStatus.TIE
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.KELSEY), setOf(Data.HERO, Data.DEMON), setOf(Data.SUPER, Data.FIRE), setOf(Data.DGNT, Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 2)
        assertRound2NullParticipants()

        //Hero vs Demon
        round1.matchUps[3].status = MatchUpStatus.P2_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.KELSEY, Data.HERO), setOf(Data.SUPER, Data.FIRE), setOf(Data.DGNT, Data.DEMON, Data.ANDREW)))
        sut.update(roundGroups, 0, 0, 3)
        Assert.assertEquals(Data.ANDREW, round2.matchUps[0].participant1)//1-0-0
        Assert.assertEquals(Data.DEMON, round2.matchUps[0].participant2)//1-0-0
        Assert.assertEquals(Data.DGNT, round2.matchUps[1].participant1)//1-0-0
        Assert.assertEquals(Data.FIRE, round2.matchUps[1].participant2)//0-0-1
        Assert.assertEquals(Data.SUPER, round2.matchUps[2].participant1)//0-0-1
        Assert.assertEquals(Data.HERO, round2.matchUps[2].participant2)//0-1-0
        Assert.assertEquals(Data.KELSEY, round2.matchUps[3].participant1)//0-1-0
        Assert.assertEquals(Data.KYRA, round2.matchUps[3].participant2)//0-1-0

        val round3 = roundGroups[0].rounds[2]

        val assertRound3NullParticipants = {
            for (i in 0..3) {
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round3.matchUps[i].participant1)
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round3.matchUps[i].participant2)
            }
        }

        //Andrew vs Demon
        round2.matchUps[0].status = MatchUpStatus.P2_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.KELSEY, Data.HERO), setOf(Data.SUPER, Data.FIRE), setOf(Data.ANDREW), setOf(Data.DGNT), setOf(Data.DEMON)))
        sut.update(roundGroups, 0, 1, 0)
        assertRound3NullParticipants()

        //Dgnt vs Fire
        round2.matchUps[1].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.KELSEY, Data.HERO), setOf(Data.FIRE), setOf(Data.SUPER), setOf(Data.ANDREW), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 1, 1)
        assertRound3NullParticipants()

        //Super vs Hero
        round2.matchUps[2].status = MatchUpStatus.TIE
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.KELSEY), setOf(Data.HERO, Data.FIRE), setOf(Data.SUPER), setOf(Data.ANDREW), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 1, 2)
        assertRound3NullParticipants()

        //Kelsey vs Kyra
        round2.matchUps[3].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA), setOf(Data.HERO, Data.FIRE), setOf(Data.SUPER), setOf(Data.KELSEY, Data.ANDREW), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 1, 3)
        Assert.assertEquals(Data.DEMON, round3.matchUps[0].participant1)//2-0-0
        Assert.assertEquals(Data.DGNT, round3.matchUps[0].participant2)//2-0-0
        Assert.assertEquals(Data.ANDREW, round3.matchUps[1].participant1)//1-1-0
        Assert.assertEquals(Data.KELSEY, round3.matchUps[1].participant2)//1-1-0
        Assert.assertEquals(Data.SUPER, round3.matchUps[2].participant1)//0-0-2
        Assert.assertEquals(Data.KYRA, round3.matchUps[2].participant2)//0-2-0
        Assert.assertEquals(Data.FIRE, round3.matchUps[3].participant1)//0-1-1
        Assert.assertEquals(Data.HERO, round3.matchUps[3].participant2)//0-1-1

        val round4 = roundGroups[0].rounds[3]

        val assertRound4NullParticipants = {
            for (i in 0..3) {
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round4.matchUps[i].participant1)
                Assert.assertEquals(Participant.NULL_PARTICIPANT, round4.matchUps[i].participant2)
            }
        }

        //Demon vs Dgnt
        round3.matchUps[0].status = MatchUpStatus.TIE
        helperMockRankingService(listOf(setOf(Data.KYRA), setOf(Data.HERO, Data.FIRE), setOf(Data.SUPER), setOf(Data.KELSEY, Data.ANDREW), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 2, 0)
        assertRound4NullParticipants()

        //Andrew vs Kelsey
        round3.matchUps[1].status = MatchUpStatus.P2_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA), setOf(Data.HERO, Data.FIRE), setOf(Data.SUPER), setOf(Data.ANDREW), setOf(Data.KELSEY), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 2, 1)
        assertRound4NullParticipants()

        //Super vs Kyra
        round3.matchUps[2].status = MatchUpStatus.P1_WINNER
        helperMockRankingService(listOf(setOf(Data.KYRA), setOf(Data.HERO, Data.FIRE), setOf(Data.ANDREW), setOf(Data.SUPER), setOf(Data.KELSEY), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 2, 2)
        assertRound4NullParticipants()

        //Fire vs Hero
        round3.matchUps[3].status = MatchUpStatus.TIE
        helperMockRankingService(listOf(setOf(Data.KYRA), setOf(Data.HERO, Data.FIRE), setOf(Data.ANDREW), setOf(Data.SUPER), setOf(Data.KELSEY), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 2, 3)
        Assert.assertEquals(Data.DEMON, round4.matchUps[0].participant1)//2-0-1
        Assert.assertEquals(Data.KELSEY, round4.matchUps[0].participant2)//2-1-0
        Assert.assertEquals(Data.DGNT, round4.matchUps[1].participant1)//2-0-1
        Assert.assertEquals(Data.SUPER, round4.matchUps[1].participant2)//1-0-2
        Assert.assertEquals(Data.ANDREW, round4.matchUps[2].participant1)//1-2-0
        Assert.assertEquals(Data.FIRE, round4.matchUps[2].participant2)//0-1-2
        Assert.assertEquals(Data.HERO, round4.matchUps[3].participant1)//0-1-2
        Assert.assertEquals(Data.KYRA, round4.matchUps[3].participant2)//0-3-0

        round1.matchUps[0].status = MatchUpStatus.DEFAULT
        helperMockRankingService(listOf(setOf(Data.KELSEY, Data.HERO), setOf(Data.KYRA, Data.ANDREW), setOf(Data.SUPER, Data.FIRE), setOf(Data.DGNT, Data.DEMON)))
        sut.update(roundGroups, 0, 0, 0)
        assertRound2NullParticipants()
        assertRound3NullParticipants()
        assertRound4NullParticipants()
    }

    @Test
    fun testDistributionWithBye() {

        val round1 = roundGroupsWithBye[0].rounds[0]
        val round2 = roundGroupsWithBye[0].rounds[1]

        //Andrew Vs Fire
        round1.matchUps[0].status = MatchUpStatus.P1_WINNER
        round1.matchUps[1].status = MatchUpStatus.P1_WINNER//because bye
        helperMockRankingService(listOf(setOf(Data.FIRE), setOf(Data.ANDREW, Data.KYRA)))
        sut.update(roundGroupsWithBye, 0, 0, 0)
        Assert.assertEquals(Data.ANDREW, round2.matchUps[0].participant1)//1-0
        Assert.assertEquals(Data.KYRA, round2.matchUps[0].participant2)//1-0
        Assert.assertEquals(Data.FIRE, round2.matchUps[1].participant1)//0-1
        Assert.assertEquals(Participant.BYE_PARTICIPANT, round2.matchUps[1].participant2)

        val round3 = roundGroupsWithBye[0].rounds[2]

        //Andrew Vs Kyra
        round2.matchUps[0].status = MatchUpStatus.P1_WINNER
        round2.matchUps[1].status = MatchUpStatus.P1_WINNER//because bye
        helperMockRankingService(listOf(setOf(Data.KYRA, Data.FIRE), setOf(Data.ANDREW)))
        sut.update(roundGroupsWithBye, 0, 1, 0)
        Assert.assertEquals(Data.ANDREW, round3.matchUps[0].participant1)//2-0
        Assert.assertEquals(Participant.BYE_PARTICIPANT, round3.matchUps[0].participant2)
        Assert.assertEquals(Data.FIRE, round3.matchUps[1].participant1)//1-1
        Assert.assertEquals(Data.KYRA, round3.matchUps[1].participant2)//1-1

    }
}