package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.View
import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.CheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class PersonViewHolder(private val binding: CheckableListItemBinding, private val selectedPersons: MutableSet<String>, private val clickListener: (Checkable, Person) -> Unit) : ChildViewHolder(binding.root) {
    //if (selectable) android.R.attr.listChoiceIndicatorMultiple else null
    fun bind(person: Person, selectable: Boolean) {


        binding.checkableListItemCtv.text = person.name
        binding.checkableListItemCtv.visibility = if (selectable) View.VISIBLE else View.GONE
        if (!selectable)
            binding.checkableListItemCtv.isChecked = false
        else
            binding.checkableListItemCtv.isChecked = selectedPersons.contains(person.name)

        binding.checkableListItemTv.text = person.name
        binding.checkableListItemTv.visibility = if (selectable) View.GONE else View.VISIBLE

        itemView.setOnClickListener {
            clickListener(binding.checkableListItemCtv, person)
        }

    }

}