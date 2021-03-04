package com.dgnt.quickTournamentMaker.ui.main.home

import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.SingleCheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder


class CheckedExpandedPersonViewHolder(private val binding: SingleCheckableListItemBinding, private val clickListener: (String) -> Unit) : CheckableChildViewHolder(binding.root) {
    fun bind(person: Person) {
        binding.checkableListItemCtv.text = person.name

        binding.root.setOnClickListener {
            this.onClick(it)
            clickListener(person.name)
        }
    }

    override fun getCheckable(): Checkable = binding.checkableListItemCtv


}