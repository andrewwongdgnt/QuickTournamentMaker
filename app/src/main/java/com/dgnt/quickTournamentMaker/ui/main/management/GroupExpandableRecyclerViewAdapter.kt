package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.CheckedTextView
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.databinding.SingleCheckableListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class GroupExpandableRecyclerViewAdapter(private val setDrawable: (CheckedTextView, Boolean) -> Unit, private val actionModeCallback: ManagementFragmentActionModeCallBack, private val selectedPersons: Set<Person>, private val selectedGroups: Set<Group>, private val groupMap: Map<String, Group>, private val nonEmptyGroups: Set<Group>, personGroups: List<ExpandableGroup<Person>>, private val personClickListener: (Checkable, Person) -> Unit, private val groupClickListener: (Checkable, Group, ManagementFragment.GroupEditType) -> Unit) : ExpandableRecyclerViewAdapter<ManagementExpandedGroupViewHolder, PersonExpandedChildViewHolder>(personGroups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ManagementExpandedGroupViewHolder = ManagementExpandedGroupViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), nonEmptyGroups, selectedGroups, groupClickListener)

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PersonExpandedChildViewHolder = PersonExpandedChildViewHolder(SingleCheckableListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), setDrawable, selectedPersons, personClickListener)

    override fun onBindChildViewHolder(holder: PersonExpandedChildViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as GroupExpandableGroup).items[childIndex], actionModeCallback.multiSelect == ManagementFragmentActionModeCallBack.SelectType.PERSON)

    override fun onBindGroupViewHolder(holder: ManagementExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(groupMap.getValue(group.title), actionModeCallback.multiSelect)
}
