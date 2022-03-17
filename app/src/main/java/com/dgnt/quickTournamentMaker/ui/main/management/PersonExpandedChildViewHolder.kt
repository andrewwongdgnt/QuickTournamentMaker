package com.dgnt.quickTournamentMaker.ui.main.management

import android.widget.Checkable
import android.widget.CheckedTextView
import com.dgnt.quickTournamentMaker.databinding.SingleCheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class PersonExpandedChildViewHolder(
    private val binding: SingleCheckableListItemBinding,
    private val setDrawable: (CheckedTextView, Boolean) -> Unit,
    private val selectedPersons: Set<Person>,
    private val clickListener: (Boolean, Person) -> Unit
) : ChildViewHolder(binding.root) {
    fun bind(person: Person, selectable: Boolean) =
        binding.checkableListItemCtv.run{
            text = person.name
            setDrawable(binding.checkableListItemCtv, selectable)
            isChecked = if (!selectable) false else selectedPersons.contains(person)
            itemView.setOnClickListener {
                this.isChecked = !this.isChecked
                clickListener(this.isChecked, person)
            }
        }



}