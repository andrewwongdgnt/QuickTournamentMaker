package com.dgnt.quickTournamentMaker.ui.main.home

import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.PersonItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder


class CheckedExpandedPersonViewHolder(private val binding: PersonItemBinding, private val selectedPersons: Set<String>, private val clickListener: (String, Boolean) -> Unit) : CheckableChildViewHolder(binding.root) {
    fun bind(person: Person) {
        binding.checkableListItemCtv.text = person.name
        binding.checkableListItemCtv.isChecked = selectedPersons.contains(person.name)

        binding.root.setOnClickListener {
            this.onClick(it)
            clickListener(person.name, binding.checkableListItemCtv.isChecked)
        }
    }

    override fun getCheckable(): Checkable = binding.checkableListItemCtv


}