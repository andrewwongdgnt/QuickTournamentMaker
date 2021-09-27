package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService

class ParticipantService : IParticipantService {
    override fun createRound(
        participants: List<Participant>,
        roundGroupIndex: Int,
        roundIndex: Int,
        defaultRoundTitleFunc: (Int) -> String,
        defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String
    ) =
        Round(
            roundGroupIndex,
            roundIndex,
            participants
                .zipWithNext()
                .filterIndexed { index, _ -> index % 2 == 0 }
                .mapIndexed { i, it ->
                    MatchUp(
                        roundGroupIndex,
                        roundIndex,
                        i,
                        it.first,
                        it.second,
                        defaultMatchUpTitleFunc(i, it.first, it.second)
                    )
                },
            defaultRoundTitleFunc(roundIndex)
        )
}