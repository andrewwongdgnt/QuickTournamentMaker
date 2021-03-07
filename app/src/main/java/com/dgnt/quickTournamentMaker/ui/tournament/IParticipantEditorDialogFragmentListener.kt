package com.dgnt.quickTournamentMaker.ui.tournament

import com.dgnt.quickTournamentMaker.model.tournament.Participant

interface IParticipantEditorDialogFragmentListener {

    /**
     * handles the change
     *
     * @param key the key that identifies a participant
     * @param name the new name
     * @param note the new note
     * @param color the new color
     */
    fun onEditParticipant(key:String, name:String,note:String,color:Int)
}