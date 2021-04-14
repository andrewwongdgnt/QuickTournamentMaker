package com.dgnt.quickTournamentMaker.ui.tournament

interface IMatchUpEditorDialogFragmentListener {

    /**
     * handles the change
     *
     * @param key the key that identifies a match up
     * @param useTitle whether to use custom title
     * @param note the new note
     * @param color the new color
     */
    fun onEditMatchUp(key: Triple<Int, Int, Int>, useTitle: Boolean, title: String, note: String, color: Int)
}