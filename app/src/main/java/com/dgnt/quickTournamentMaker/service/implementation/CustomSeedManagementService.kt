package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantType
import com.dgnt.quickTournamentMaker.service.interfaces.ICustomSeedManagementService
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService

class CustomSeedManagementService(private val participantService: IParticipantService) : ICustomSeedManagementService {
    private var selection: Pair<Int,Boolean?>? = null
    private lateinit var matchUps: List<MatchUp>

    override fun setUp(orderedParticipants: List<Participant>) =
        participantService.createRound(orderedParticipants).matchUps.also {
            matchUps = it
        }


    override fun select(matchUpIndex: Int, isParticipant1Selected: Boolean): Pair<Int, Boolean?> {
        if (!this::matchUps.isInitialized)
            throw IllegalStateException("match ups not set up")
        return selection?.run {
            val sourceMatchUp = matchUps[first]
            val sourceParticipant = sourceMatchUp.run {
                if (second == true)
                    participant1
                else
                    participant2
            }
            val targetMatchUp = matchUps[matchUpIndex]
            val targetParticipant = targetMatchUp.run { if (isParticipant1Selected) participant1 else participant2 }

            val sourceContainsBye = sourceMatchUp.let { it.participant1.participantType == ParticipantType.BYE || it.participant2.participantType == ParticipantType.BYE }
            val targetContainsBye = targetMatchUp.let { it.participant1.participantType == ParticipantType.BYE || it.participant2.participantType == ParticipantType.BYE }

            if (
                (sourceParticipant.participantType == ParticipantType.NORMAL && targetParticipant.participantType == ParticipantType.NORMAL) ||
                (!sourceContainsBye && sourceParticipant.participantType == ParticipantType.NORMAL && targetParticipant.participantType == ParticipantType.BYE) ||
                (!targetContainsBye && sourceParticipant.participantType == ParticipantType.BYE && targetParticipant.participantType == ParticipantType.NORMAL)
            ) {
                swap(sourceMatchUp, second ?: false, sourceParticipant, targetMatchUp, isParticipant1Selected, targetParticipant)
                if (sourceMatchUp.participant1.participantType == ParticipantType.BYE){
                    swapParticipant(sourceMatchUp)
                }
                if (targetMatchUp.participant1.participantType == ParticipantType.BYE){
                    swapParticipant(targetMatchUp)
                }
                selection=null
                Pair(first,null)
            } else {
               this
            }


        } ?: run {
            Pair(matchUpIndex, isParticipant1Selected).also {
                selection = it
            }
        }
    }

    private fun swapParticipant(targetMatchUp: MatchUp) = targetMatchUp.apply {
        val tempParticipant = participant2
        participant2 = participant1
        participant1= tempParticipant
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