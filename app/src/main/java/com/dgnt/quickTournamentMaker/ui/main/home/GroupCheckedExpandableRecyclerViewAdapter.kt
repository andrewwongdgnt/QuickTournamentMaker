package com.dgnt.quickTournamentMaker.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class GroupCheckedExpandableRecyclerViewAdapter(personGroups: List<GroupCheckedExpandableGroup>, private val selectedGroups: Set<String>, private val groupCheckMarkTintColor:Int, private val personClickListener: (String) -> Unit, private val groupClickListener: (String, Boolean) -> Unit) : CheckableChildRecyclerViewAdapter<HomeExpandedGroupViewHolder, CheckedExpandedPersonViewHolder>(personGroups) {


    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): HomeExpandedGroupViewHolder = HomeExpandedGroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.group_item, parent, false), selectedGroups, groupClickListener)

    override fun onCreateCheckChildViewHolder(parent: ViewGroup, viewType: Int): CheckedExpandedPersonViewHolder = CheckedExpandedPersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.single_checkable_list_item, parent, false),  personClickListener)

    override fun onBindGroupViewHolder(holder: HomeExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(group.title,groupCheckMarkTintColor)

    override fun onBindCheckChildViewHolder(holder: CheckedExpandedPersonViewHolder, flatPosition: Int, group: CheckedExpandableGroup, childIndex: Int) = holder.bind(group.items[childIndex] as Person)

}
