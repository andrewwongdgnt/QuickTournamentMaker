package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentGeneralEditorViewModel
import org.joda.time.format.DateTimeFormat


class MoreInfoViewModel : Observable, ViewModel(), TournamentGeneralEditorViewModel {
    @Bindable
    override val title = MutableLiveData<String>()

    @Bindable
    override val description = MutableLiveData<String>()

    @Bindable
    val typeInfo = MutableLiveData<String>()

    @Bindable
    val seedTypeInfo = MutableLiveData<String>()

    @Bindable
    val createdDateInfo = MutableLiveData<String>()

    @Bindable
    val lastModifiedDateInfo = MutableLiveData<String>()

    @Bindable
    val roundInfo = MutableLiveData<String>()

    @Bindable
    val matchUpInfo = MutableLiveData<String>()

    @Bindable
    val matchUpSubInfo = MutableLiveData<String>()

    @Bindable
    val participantInfo = MutableLiveData<String>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(
        tournamentInformation: TournamentInformation,
        getTypeInfoString: (TournamentType) -> String,
        getSeedTypeInfoString: (SeedType) -> String,
        getCreatedDateInfoString: (String) -> String,
        getLastModifiedDateInfoString: (String) -> String,
        roundInfo: String,
        matchUpInfo: String,
        matchUpSubInfo: String,
        participantInfo: String
    ) {
        title.value = tournamentInformation.title
        description.value = tournamentInformation.description
        typeInfo.value = getTypeInfoString(tournamentInformation.tournamentType)
        seedTypeInfo.value = getSeedTypeInfoString(tournamentInformation.seedType)

        DateTimeFormat.mediumDateTime().apply {
            createdDateInfo.value = getCreatedDateInfoString(print(tournamentInformation.creationDate))
            lastModifiedDateInfo.value = getLastModifiedDateInfoString(tournamentInformation.lastModifiedDate?.let {
                print(it)
            } ?: "-")
        }

        this.roundInfo.value = roundInfo
        this.matchUpInfo.value = matchUpInfo
        this.matchUpSubInfo.value = matchUpSubInfo
        this.participantInfo.value = participantInfo

    }
}