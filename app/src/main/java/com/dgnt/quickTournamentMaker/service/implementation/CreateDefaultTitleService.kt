package com.dgnt.quickTournamentMaker.service.implementation

import android.content.res.Resources
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import java.lang.IllegalArgumentException

class CreateDefaultTitleService : ICreateDefaultTitleService {
    override fun forRoundGroup(resources: Resources, tournamentType: TournamentType, roundGroup: RoundGroup) = resources.run {
        when (tournamentType) {
            TournamentType.DOUBLE_ELIMINATION -> {
                when (roundGroup.roundGroupIndex){
                    0 -> resources.getString(R.string.winnersBracket)
                    1 -> resources.getString(R.string.losersBracket)
                    2 -> resources.getString(R.string.finalsBracket)
                    else -> throw IndexOutOfBoundsException("Extra round group for some reason")
                }
            }
            else -> ""
        }
    }

    override fun forRound(resources: Resources, round: Round) = resources.getString(R.string.roundHeader, round.roundIndex + 1)

    override fun forMatchUp(resources: Resources, matchUp: MatchUp) = resources.run {
        matchUp.run {
            if (participant1.participantType == ParticipantType.BYE) {
                getString(R.string.participantWithABye, participant2.displayName)
            } else if (participant2.participantType == ParticipantType.BYE) {
                getString(R.string.participantWithABye, participant1.displayName)
            } else if (participant1.participantType == ParticipantType.NULL && participant2.participantType == ParticipantType.NULL) {
                getString(R.string.emptyMatchUp, matchUpIndex + 1)
            } else if (participant1.participantType == ParticipantType.NULL) {
                getString(R.string.participantVsNoOne, participant2.displayName)
            } else if (participant2.participantType == ParticipantType.NULL) {
                getString(R.string.participantVsNoOne, participant1.displayName)
            } else {
                getString(R.string.participant1VsParticipant2, matchUp.participant1.displayName, matchUp.participant2.displayName)

            }
        }
    }

    override fun forParticipant(resources: Resources, participantNumber: Int) = resources.getString(R.string.participantDefaultName, participantNumber)
}