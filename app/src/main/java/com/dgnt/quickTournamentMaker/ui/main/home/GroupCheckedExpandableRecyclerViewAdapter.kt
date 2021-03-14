package com.dgnt.quickTournamentMaker.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.databinding.SingleCheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class GroupCheckedExpandableRecyclerViewAdapter(personGroups: List<GroupCheckedExpandableGroup>, private val selectedGroups: Set<String>, private val groupCheckMarkTintColor: Int, private val personClickListener: (String) -> Unit, private val groupClickListener: (String, Boolean) -> Unit) : CheckableChildRecyclerViewAdapter<HomeExpandedGroupViewHolder, CheckedExpandedPersonViewHolder>(personGroups) {


    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): HomeExpandedGroupViewHolder = HomeExpandedGroupViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), selectedGroups, groupClickListener)

    override fun onCreateCheckChildViewHolder(parent: ViewGroup, viewType: Int): CheckedExpandedPersonViewHolder = CheckedExpandedPersonViewHolder(SingleCheckableListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), personClickListener)

    override fun onBindGroupViewHolder(holder: HomeExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(group.title, groupCheckMarkTintColor)

    override fun onBindCheckChildViewHolder(holder: CheckedExpandedPersonViewHolder, flatPosition: Int, group: CheckedExpandableGroup, childIndex: Int) = holder.bind(group.items[childIndex] as Person)

}
