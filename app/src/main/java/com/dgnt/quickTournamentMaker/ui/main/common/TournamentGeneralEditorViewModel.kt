package com.dgnt.quickTournamentMaker.ui.main.common

import androidx.lifecycle.MutableLiveData

interface TournamentGeneralEditorViewModel {

    val title:MutableLiveData<String>

    val description:MutableLiveData<String>
}