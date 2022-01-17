package com.dgnt.quickTournamentMaker.ui.tournament

interface IParticipantEditorDialogFragmentListener {

    /**
     * handles the change
     *
     * @param key the key that identifies a participant
     * @param name the new name
     * @param note the new note
     * @param color the new color
     */
    fun onEditParticipant(key: String, name: String, note: String, color: Int)
}