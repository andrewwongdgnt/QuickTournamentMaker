package com.dgnt.quickTournamentMaker.ui.main.home

import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.PersonItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder


class CheckedExpandedPersonViewHolder(private val binding: PersonItemBinding) : CheckableChildViewHolder(binding.root) {
    fun bind(person: Person) {
        binding.checkableListItemCtv.text = person.name
    }

    override fun getCheckable(): Checkable = binding.checkableListItemCtv


}