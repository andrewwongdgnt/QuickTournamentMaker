package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ParticipantEditorViewModel : Observable,ViewModel() {
    @Bindable
     val name = MutableLiveData<String>()

    @Bindable
    val note = MutableLiveData<String>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(name: String, note: String) {
        this.name.value = name
        this.note.value = note

    }
}