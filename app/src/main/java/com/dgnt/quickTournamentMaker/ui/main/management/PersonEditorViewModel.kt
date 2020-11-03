package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.Event

class PersonEditorViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository): ViewModel() , Observable {

    private val _completeEvent = MutableLiveData<Event<Boolean>>()
    val completeEvent : LiveData<Event<Boolean>>
        get() = _completeEvent
    @Bindable
    val title =  MutableLiveData<String>()
    @Bindable
    val neutralOptionVisible =  MutableLiveData<Int>()
    @Bindable
     val name =  MutableLiveData<String>()
    @Bindable
     val note =  MutableLiveData<String>()
    @Bindable
     val groupName =  MutableLiveData<String>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(editing:Boolean, title:String?, person: Person?, groupName: String?) {
        this.title.value = title.orEmpty()
        neutralOptionVisible.value = if (editing) INVISIBLE else VISIBLE
        if (person!=null){
            name.value=person.name
            note.value=person.note
        }
        if (groupName!=null){
            this.groupName.value=groupName
        }
    }

    fun add(){
        _completeEvent.value = Event(true)

    }

    fun addAndContinue(){
        _completeEvent.value = Event(true)

    }

    fun cancel(){
        _completeEvent.value = Event(true)

    }


}