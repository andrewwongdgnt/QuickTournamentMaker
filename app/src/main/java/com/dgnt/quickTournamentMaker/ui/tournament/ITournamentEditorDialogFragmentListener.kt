package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.fragment.app.DialogFragment

interface ITournamentEditorDialogFragmentListener {

    /**
     * handles the change
     *
     * @param title the new title
     * @param description the new description
     */
    fun onEditTournament(title:String, description: String)

}