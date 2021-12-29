package com.dgnt.quickTournamentMaker.ui.main.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import com.dgnt.quickTournamentMaker.util.Event

interface TournamentBuilderViewModel : TournamentTypeEditorViewModel {

    val tournamentEvent: LiveData<Event<Pair<TournamentInformation, List<Participant>>>>

    val customSeedTournamentEvent: LiveData<Event<Pair<TournamentInformation, List<Participant>>>>

    val failedToStartTournamentMessage: LiveData<Event<Boolean>>
}