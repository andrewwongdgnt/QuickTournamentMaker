package com.dgnt.quickTournamentMaker.ui.main.management

import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.CheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder


class PersonViewHolder(private val binding: CheckableListItemBinding) : CheckableChildViewHolder(binding.root) {

    fun bind(person: Person) {
        binding.checkableListItemTv.text = person.name
//        binding.listItemLayout.setOnClickListener {
//            clickListener(person)
//        }
    }

    override fun getCheckable(): Checkable {
        return binding.checkableListItemTv
    }
}