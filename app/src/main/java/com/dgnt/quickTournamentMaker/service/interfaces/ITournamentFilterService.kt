package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation

interface ITournamentFilterService {

    /**
     * takes in a base list of RestoredTournamentInformation and apply a filter
     *
     * @param restoredTournamentInformationList the list to be filtered
     * @return the filtered list
     */
    fun applyFilter(restoredTournamentInformationList: List<RestoredTournamentInformation>): List<RestoredTournamentInformation>
}