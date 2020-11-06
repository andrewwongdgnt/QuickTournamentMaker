package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dgnt.quickTournamentMaker.R
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class GroupExpandableRecyclerViewAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<GroupViewHolder, PersonViewHolder>(groups) {
    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder = GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.section_header_item, parent, false))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder = PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.checkable_list_item, parent, false))

    override fun onBindChildViewHolder(holder: PersonViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as GroupExpandableGroup).items[childIndex])

    override fun onBindGroupViewHolder(holder: GroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(group)
}
