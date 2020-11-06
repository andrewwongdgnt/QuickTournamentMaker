package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.generated.callback.OnClickListener
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class GroupExpandableRecyclerViewAdapter(private val personGroups: List<CheckedExpandableGroup>) : CheckableChildRecyclerViewAdapter<GroupViewHolder, PersonViewHolder>(personGroups) {
    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.section_header_item, parent, false))
    }

    override fun onBindGroupViewHolder(holder: GroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        holder.bind(group)
    }

    override fun onCreateCheckChildViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.checkable_list_item, parent, false))
    }

    override fun onBindCheckChildViewHolder(holder: PersonViewHolder, flatPosition: Int, group: CheckedExpandableGroup, childIndex: Int) {
        holder.bind(group.items[childIndex] as Person)
    }
//    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder = GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.section_header_item, parent, false))
//
//    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder = PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.checkable_list_item, parent, false))
//
//    override fun onBindChildViewHolder(holder: PersonViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind(group.items[childIndex])
//
//    override fun onBindGroupViewHolder(holder: GroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(group)
}
