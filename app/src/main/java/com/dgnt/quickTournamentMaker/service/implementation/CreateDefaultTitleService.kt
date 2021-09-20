package com.dgnt.quickTournamentMaker.service.implementation

import android.content.res.Resources
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService

class CreateDefaultTitleService : ICreateDefaultTitleService {
    override fun forRoundGroup(resources: Resources, tournamentType: TournamentType, roundGroupIndex: Int) = resources.run {
        when (tournamentType) {
            TournamentType.DOUBLE_ELIMINATION -> {
                when (roundGroupIndex) {
                    0 -> resources.getString(R.string.winnersBracket)
                    1 -> resources.getString(R.string.losersBracket)
                    2 -> resources.getString(R.string.finalsBracket)
                    else -> throw IndexOutOfBoundsException("Extra round group for some reason")
                }
            }
            else -> "" //round group doesn't need to be defined
        }
    }

    override fun forRound(resources: Resources, roundIndex: Int) = resources.getString(R.string.roundHeader, roundIndex + 1)

    override fun forMatchUp(resources: Resources, matchUpIndex: Int, participant1: Participant, participant2: Participant) = resources.run {
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
            getString(R.string.participant1VsParticipant2, participant1.displayName, participant2.displayName)
        }
    }

    override fun forParticipant(resources: Resources, participantNumber: Int) = resources.getString(R.string.participantDefaultName, participantNumber)
}