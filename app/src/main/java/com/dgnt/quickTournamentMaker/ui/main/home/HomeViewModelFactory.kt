package com.dgnt.quickTournamentMaker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.management.IGroupRepository
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService
import com.dgnt.quickTournamentMaker.service.interfaces.ISelectedPersonsService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService

class HomeViewModelFactory(private val personRepository: IPersonRepository, private val groupRepository: IGroupRepository, private val preferenceService: IPreferenceService, private val tournamentInformationCreatorService: ITournamentInformationCreatorService, private val selectedPersonsService: ISelectedPersonsService, private val seedServices: Map<TournamentType, ISeedService>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(personRepository, groupRepository, preferenceService, tournamentInformationCreatorService, selectedPersonsService, seedServices) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}