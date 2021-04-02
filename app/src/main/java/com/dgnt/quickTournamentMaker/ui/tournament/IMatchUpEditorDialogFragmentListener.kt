package com.dgnt.quickTournamentMaker.ui.tournament

import com.dgnt.quickTournamentMaker.model.tournament.Participant

interface IMatchUpEditorDialogFragmentListener {

    /**
     * handles the change
     *
     * @param key the key that identifies a participant
     * @param note the new note
     * @param color the new color
     */
    fun onEditMatchUp(key:Triple<Int,Int,Int>, note:String,color:Int)
}