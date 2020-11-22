package com.dgnt.quickTournamentMaker.ui

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData

interface TournamentEditorViewModel {


    val title:MutableLiveData<String>


    val description:MutableLiveData<String>
}