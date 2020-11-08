package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.View
import com.dgnt.quickTournamentMaker.databinding.CheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class PersonViewHolder(private val binding: CheckableListItemBinding, private val selectedPersons: MutableSet<String>, private val clickListener: (Person) -> Unit) : ChildViewHolder(binding.root) {

    fun bind(person: Person, selectable: Boolean) {

        binding.checkableListItemCtv.text = person.name
        binding.checkableListItemCtv.visibility = if (selectable) View.VISIBLE else View.GONE
        if (!selectable)
            binding.checkableListItemCtv.isChecked = false
        else
            binding.checkableListItemCtv.isChecked = selectedPersons.contains(person.name)

        itemView.setOnClickListener {
            if (selectable) {
                val isChecked = !binding.checkableListItemCtv.isChecked
                if (isChecked)
                    selectedPersons.add(person.name)
                else
                    selectedPersons.remove(person.name)
                binding.checkableListItemCtv.isChecked = isChecked
            } else
                clickListener(person)
        }
    }

}