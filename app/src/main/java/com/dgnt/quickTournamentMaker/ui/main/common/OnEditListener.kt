package com.dgnt.quickTournamentMaker.ui.main.common

interface OnEditListener<T> {

    /**
     * handles the edit
     *
     * @param value the new edited value
     */
    fun onEdit(value:T)
}