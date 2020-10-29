package com.dgnt.quickTournamentMaker.ui.main.management

import com.dgnt.quickTournamentMaker.databinding.CheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class PersonViewHolder(private val binding: CheckableListItemBinding) : ChildViewHolder(binding.root) {

    fun bind(person: Person) {
        binding.checkableListItemTv.text = person.name
    }
}