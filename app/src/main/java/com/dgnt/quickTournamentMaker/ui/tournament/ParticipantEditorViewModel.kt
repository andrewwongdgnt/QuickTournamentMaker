package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.Participant

class ParticipantEditorViewModel : Observable,ViewModel() {
    @Bindable
     val name = MutableLiveData<String>()

    @Bindable
    val note = MutableLiveData<String>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(participant: Participant) {
        this.name.value = participant.displayName
        this.note.value = participant.note

    }
}