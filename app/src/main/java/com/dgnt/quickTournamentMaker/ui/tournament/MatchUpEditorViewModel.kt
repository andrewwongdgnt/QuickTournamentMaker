package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.res.Resources
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService

class MatchUpEditorViewModel(private val createDefaultTitleService: ICreateDefaultTitleService) : Observable, ViewModel() {
    @Bindable
    val useCustomTitle = MutableLiveData<Boolean>()

    @Bindable
    val defaultTitle = MutableLiveData<String>()

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

    fun setData(resources: Resources, matchUp: MatchUp, colors: List<ColorInfo>) {
        useCustomTitle.value = matchUp.useTitle
        this.defaultTitle.value = createDefaultTitleService.forMatchUp(resources, matchUp.matchUpIndex, matchUp.participant1, matchUp.participant2)
        this.title.value = matchUp.title
        this.note.value = matchUp.note
        this.color.value = colors.find { it.hex == matchUp.color }
        this.colors.value = colors

    }

    fun setNewColor(selectedValue: Any) {
        color.value = selectedValue as ColorInfo
    }
}