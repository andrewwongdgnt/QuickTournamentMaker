package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation

interface ITournamentFilterService {

    /**
     * takes in a base list of RestoredTournamentInformation and apply a filter and sort
     *
     * @param restoredTournamentInformationList the list to be filtered and sorted
     * @return the filtered and sorted list
     */
    fun update(restoredTournamentInformationList: List<RestoredTournamentInformation>): List<RestoredTournamentInformation>
}