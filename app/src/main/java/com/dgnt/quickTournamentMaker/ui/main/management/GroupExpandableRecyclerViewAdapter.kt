package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Checkable
import androidx.databinding.DataBindingUtil
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class GroupExpandableRecyclerViewAdapter(private val actionModeCallback: ManagementFragmentActionModeCallBack, private val selectedPersons: MutableSet<String>, personGroups: List<ExpandableGroup<*>>, private val clickListener: (Checkable, Person) -> Unit) : ExpandableRecyclerViewAdapter<GroupViewHolder, PersonViewHolder>(personGroups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder = GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.section_header_item, parent, false))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder = PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.checkable_list_item, parent, false), selectedPersons, clickListener)

    override fun onBindChildViewHolder(holder: PersonViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as GroupExpandableGroup).items[childIndex], actionModeCallback.multiSelect)

    override fun onBindGroupViewHolder(holder: GroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(group)
}
