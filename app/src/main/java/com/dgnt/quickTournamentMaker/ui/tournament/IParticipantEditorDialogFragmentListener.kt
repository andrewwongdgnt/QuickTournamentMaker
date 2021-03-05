package com.dgnt.quickTournamentMaker.ui.tournament

import com.dgnt.quickTournamentMaker.model.tournament.Participant

interface IParticipantEditorDialogFragmentListener {

    /**
     * handles the change
     *
     * @param participant the updated participant
     */
    fun onEditParticipant(participant:Participant)
}