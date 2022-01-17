package com.dgnt.quickTournamentMaker.ui.main.common

interface OnEditListener<T> {

    /**
     * handles the edit
     *
     * @param editedValue the new edited value
     */
    fun onEdit(editedValue: T)
}