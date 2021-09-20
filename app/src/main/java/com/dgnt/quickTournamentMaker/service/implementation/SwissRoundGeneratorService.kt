package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService
import java.util.*

class SwissRoundGeneratorService(
    private val participantService: IParticipantService
) : IRoundGeneratorService {
    override fun build(
        orderedParticipants: List<Participant>,
        defaultRoundGroupTitleFunc: (Int) -> String,
        defaultRoundTitleFunc: (Int) -> String,
        defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String
    ): List<RoundGroup> {

        val round1 = participantService.createRound(orderedParticipants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)
        val rounds = ArrayList<Round>()
        rounds.add(round1)
        for (i in 2 until orderedParticipants.size) {
            val roundIndex = i - 1
            rounds.add(
                Round(
                    0,
                    roundIndex,
                    (orderedParticipants.indices step 2)
                        .map {
                            MatchUp(
                                0,
                                roundIndex,
                                it / 2,
                                Participant.NULL_PARTICIPANT,
                                Participant.NULL_PARTICIPANT,
                                defaultMatchUpTitleFunc(it / 2, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                            )
                        },
                    defaultRoundTitleFunc(roundIndex)
                )
            )
        }

        return listOf(RoundGroup(0, rounds, defaultRoundGroupTitleFunc(0)))
    }
}