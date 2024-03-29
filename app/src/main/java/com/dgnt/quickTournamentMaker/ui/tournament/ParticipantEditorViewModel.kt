package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.Participant

class ParticipantEditorViewModel : Observable, ViewModel() {
    @Bindable
    val name = MutableLiveData<String>()

    @Bindable
    val note = MutableLiveData<String>()

    @Bindable
    val colors = MutableLiveData<List<ColorInfo>>()

    @Bindable
    val color = MutableLiveData<ColorInfo>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(participant: Participant, colors: List<ColorInfo>) {
        this.name.value = participant.name
        this.note.value = participant.note
        this.color.value = colors.find { it.hex == participant.color }
        this.colors.value = colors

    }

    fun setNewColor(selectedValue: Any) {
        color.value = selectedValue as ColorInfo
    }
}