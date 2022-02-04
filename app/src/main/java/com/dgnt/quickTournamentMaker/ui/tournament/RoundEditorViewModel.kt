package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.Round

class RoundEditorViewModel : Observable, ViewModel() {

    @Bindable
    val title = MutableLiveData<String>()

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

    fun setData(round: Round, colors: List<ColorInfo>) {
        this.title.value = round.title
        this.note.value = round.note
        this.color.value = colors.find { it.hex == round.color }
        this.colors.value = colors

    }

    fun setNewColor(selectedValue: Any) {
        color.value = selectedValue as ColorInfo
    }
}