package com.dgnt.quickTournamentMaker.ui.tournament

interface IEditParticipantDialogFragmentListener {

    /**
     * handles the change
     *
     * @param displayName the new name
     * @param note the new note
     * @param color the new color
     */
    fun onEditParticipant(displayName:String, note: String, color:Int)
}