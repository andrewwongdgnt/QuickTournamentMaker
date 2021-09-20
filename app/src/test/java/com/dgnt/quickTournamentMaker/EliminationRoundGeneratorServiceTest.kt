package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.implementation.EliminationRoundGeneratorService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.powermock.api.mockito.PowerMockito


class EliminationRoundGeneratorServiceTest {
    private val mockParticipantService = PowerMockito.mock(IParticipantService::class.java)

    private val sut = EliminationRoundGeneratorService(mockParticipantService)
    private lateinit var roundGroups: List<RoundGroup>

    @Before
    fun setUp() {
        val participants = listOf(Data.ANDREW, Data.KYRA, Data.DGNT, Data.KELSEY, Data.FIRE, Data.SUPER, Data.HERO, Data.DEMON)
        val defaultRoundTitleFunc: (Int) -> String = { rIndex -> rIndex.toString() }
        val defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String = {  mIndex, p1, p2 -> "$mIndex, $p1, $p2" }
        PowerMockito.`when`(mockParticipantService.createRound(participants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)).thenReturn(
            Round(
                0,
                0,
                listOf(
                    MatchUp(0, 0, 0, Data.ANDREW, Data.KYRA, ""),
                    MatchUp(0, 0, 1, Data.DGNT, Data.KELSEY, ""),
                    MatchUp(0, 0, 2, Data.FIRE, Data.SUPER, ""),
                    MatchUp(0, 0, 3, Data.HERO, Data.DEMON, "")
                )
                , ""
            )
        )
        roundGroups = sut.build(participants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)
    }

    @Test
    fun testTotalRoundGroup() {
        Assert.assertEquals(1, roundGroups.size)
    }

    @Test
    fun testTotalRounds() {
        Assert.assertEquals(3, roundGroups[0].rounds.size)
    }

    @Test
    fun testTotalMatchUps() {
        val rounds = roundGroups[0].rounds
        Assert.assertEquals(4, rounds[0].matchUps.size)
        Assert.assertEquals(2, rounds[1].matchUps.size)
        Assert.assertEquals(1, rounds[2].matchUps.size)

    }


}