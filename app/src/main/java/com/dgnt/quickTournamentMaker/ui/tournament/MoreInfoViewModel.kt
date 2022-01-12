package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentGeneralEditorViewModel
import org.joda.time.LocalDateTime
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
        title:String,
        description:String,
        typeInfo: String,
        seedTypeInfo: String,
        getCreatedDateInfoString: Pair<(String) -> String, LocalDateTime>,
        getLastModifiedDateInfoString: Pair<(String) -> String, LocalDateTime?>,
        roundInfo: String,
        matchUpInfo: String,
        matchUpSubInfo: String,
        participantInfo: String
    ) {
        this.title.value = title
        this.description.value = description
        this.typeInfo.value = typeInfo
        this.seedTypeInfo.value = seedTypeInfo

        DateTimeFormat.mediumDateTime().run {
            createdDateInfo.value = getCreatedDateInfoString.first(print(getCreatedDateInfoString.second))
            lastModifiedDateInfo.value = getLastModifiedDateInfoString.first(getLastModifiedDateInfoString.second?.let {
                print(it)
            } ?: "-")
        }

        this.roundInfo.value = roundInfo
        this.matchUpInfo.value = matchUpInfo
        this.matchUpSubInfo.value = matchUpSubInfo
        this.participantInfo.value = participantInfo

    }
}