package com.dgnt.quickTournamentMaker.ui.main.home

import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.SingleCheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder


class CheckedExpandedPersonViewHolder(private val binding: SingleCheckableListItemBinding, private val selectedPersons: List<Person>, private val clickListener: (String) -> Unit) : CheckableChildViewHolder(binding.root) {
    fun bind(person: Person) =
        binding.let  {
            it.checkableListItemCtv.run {
                text = person.name
                isChecked = selectedPersons.contains(person)
            }
            it.root.setOnClickListener {
                this.onClick(it)
                clickListener(person.name)
            }
        }

    override fun getCheckable(): Checkable = binding.checkableListItemCtv


}