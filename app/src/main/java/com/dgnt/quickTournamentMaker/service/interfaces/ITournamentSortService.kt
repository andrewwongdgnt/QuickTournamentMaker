package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation

interface ITournamentSortService {

    /**
     * takes in a base list of RestoredTournamentInformation and apply a sort
     *
     * @param restoredTournamentInformationList the list to be sorted
     * @return the sorted list
     */
    fun applySort(restoredTournamentInformationList: List<RestoredTournamentInformation>): List<RestoredTournamentInformation>
}