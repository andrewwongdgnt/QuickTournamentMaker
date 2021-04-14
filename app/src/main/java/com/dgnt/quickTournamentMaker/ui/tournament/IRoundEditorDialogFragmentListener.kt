package com.dgnt.quickTournamentMaker.ui.tournament

interface IRoundEditorDialogFragmentListener {

    /**
     * handles the change
     *
     * @param key the key that identifies a round
     * @param note the new note
     * @param color the new color
     */
    fun onEditRound(key: Pair<Int, Int>, title: String, note: String, color: Int)
}