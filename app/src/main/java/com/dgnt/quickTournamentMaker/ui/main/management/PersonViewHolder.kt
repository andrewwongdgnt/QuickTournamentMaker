package com.dgnt.quickTournamentMaker.ui.main.management

import com.dgnt.quickTournamentMaker.databinding.CheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.MultiChoiceHelper


class PersonViewHolder(private val binding: CheckableListItemBinding, private val clickListener: (Person) -> Unit) : MultiChoiceHelper.ViewHolder(binding.root) {

    fun bind(person: Person) {
        binding.checkableListItemTv.text = person.name
        binding.listItemLayout.isClickable=true
        setOnClickListener {
            clickListener(person)
        }
    }

}