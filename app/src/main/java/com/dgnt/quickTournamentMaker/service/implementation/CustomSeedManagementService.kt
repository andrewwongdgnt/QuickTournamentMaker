package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import com.dgnt.quickTournamentMaker.service.interfaces.ICustomSeedManagementService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.MatchUpInformation

class CustomSeedManagementService(private val participantService: IParticipantService) : ICustomSeedManagementService {
    private var selection: MatchUpInformation? = null
    private lateinit var matchUps: List<MatchUp>

    override fun setUp(orderedParticipants: List<Participant>) =
        participantService.createRound(orderedParticipants).matchUps.also {
            matchUps = it
            selection=null
        }


    override fun select(matchUpIndex: Int, isParticipant1Selected: Boolean): Pair<MatchUpInformation, MatchUpInformation> {
        if (!this::matchUps.isInitialized)
            throw IllegalStateException("match ups not set up")
        return selection?.run {
            val sourceMatchUp = matchUps[matchUp.matchUpIndex]
            val sourceParticipant = sourceMatchUp.run {
                if (isParticipant1Highlighted == true)
                    participant1
                else
                    participant2
            }
            val targetMatchUp = matchUps[matchUpIndex]
            val targetParticipant = targetMatchUp.run { if (isParticipant1Selected) participant1 else participant2 }


            val sourceContainsBye = sourceMatchUp.let { it.participant1.participantType == ParticipantType.BYE || it.participant2.participantType == ParticipantType.BYE }
            val targetContainsBye = targetMatchUp.let { it.participant1.participantType == ParticipantType.BYE || it.participant2.participantType == ParticipantType.BYE }

            if (
                (sourceMatchUp == targetMatchUp && isParticipant1Highlighted == isParticipant1Selected) ||
                (sourceParticipant.participantType == ParticipantType.NORMAL && targetParticipant.participantType == ParticipantType.NORMAL) ||
                (!sourceContainsBye && sourceParticipant.participantType == ParticipantType.NORMAL && targetParticipant.participantType == ParticipantType.BYE) ||
                (!targetContainsBye && sourceParticipant.participantType == ParticipantType.BYE && targetParticipant.participantType == ParticipantType.NORMAL)
            ) {
                swap(sourceMatchUp, isParticipant1Highlighted ?: false, sourceParticipant, targetMatchUp, isParticipant1Selected, targetParticipant)
                if (sourceMatchUp.participant1.participantType == ParticipantType.BYE) {
                    swapParticipant(sourceMatchUp)
                }
                if (targetMatchUp.participant1.participantType == ParticipantType.BYE) {
                    swapParticipant(targetMatchUp)
                }
                selection = null
                Pair(MatchUpInformation(sourceMatchUp, null), MatchUpInformation(targetMatchUp, null))
            } else if(sourceMatchUp == targetMatchUp ){ //trying to swap 2 participants in the same match up and one of them is a bye
                Pair(this,this)
            }
            else {
                Pair(this, MatchUpInformation(targetMatchUp, null))
            }


        } ?: run {
            Pair(MatchUpInformation(matchUps[matchUpIndex], isParticipant1Selected), MatchUpInformation(matchUps[matchUpIndex], isParticipant1Selected)).also {
                selection = it.first
            }
        }
    }

    private fun swapParticipant(targetMatchUp: MatchUp) = targetMatchUp.apply {
        val tempParticipant = participant2
        participant2 = participant1
        participant1 = tempParticipant
    }

    private fun swap(sourceMatchUp: MatchUp, isSourceParticipant1: Boolean, sourceParticipant: Participant, targetMatchUp: MatchUp, isTargetParticipant1: Boolean, targetParticipant: Participant) {
        sourceMatchUp.apply {
            if (isSourceParticipant1)
                participant1 = targetParticipant
            else
                participant2 = targetParticipant
        }
        targetMatchUp.apply {
            if (isTargetParticipant1)
                participant1 = sourceParticipant
            else
                participant2 = sourceParticipant
        }
    }
}