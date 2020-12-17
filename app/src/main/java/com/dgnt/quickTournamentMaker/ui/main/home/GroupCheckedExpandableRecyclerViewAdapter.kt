package com.dgnt.quickTournamentMaker.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class GroupCheckedExpandableRecyclerViewAdapter(personGroups: List<GroupCheckedExpandableGroup>) : CheckableChildRecyclerViewAdapter<HomeExpandedGroupViewHolder, CheckedExpandedPersonViewHolder>(personGroups) {


    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): HomeExpandedGroupViewHolder = HomeExpandedGroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.group_item, parent, false))

    override fun onCreateCheckChildViewHolder(parent: ViewGroup, viewType: Int): CheckedExpandedPersonViewHolder = CheckedExpandedPersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.person_item, parent, false))

    override fun onBindGroupViewHolder(holder: HomeExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(group.title)

    override fun onBindCheckChildViewHolder(holder: CheckedExpandedPersonViewHolder, flatPosition: Int, group: CheckedExpandableGroup, childIndex: Int) = holder.bind(group.items[childIndex] as Person)

}
