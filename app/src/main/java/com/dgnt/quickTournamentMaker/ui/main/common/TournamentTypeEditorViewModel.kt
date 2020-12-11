package com.dgnt.quickTournamentMaker.ui.main.common

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData

interface TournamentTypeEditorViewModel {


    val tournamentType:MutableLiveData<Int>
    val rankConfig:MutableLiveData<Int>
    val seedType:MutableLiveData<Int>
    val showRankConfig:MutableLiveData<Boolean>
    val showSeedType:MutableLiveData<Boolean>
    val showPriorityContent:MutableLiveData<Boolean>
    val showScoringContent:MutableLiveData<Boolean>
    val winValue:MutableLiveData<Int>
    val lossValue:MutableLiveData<Int>
    val tieValue:MutableLiveData<Int>

}