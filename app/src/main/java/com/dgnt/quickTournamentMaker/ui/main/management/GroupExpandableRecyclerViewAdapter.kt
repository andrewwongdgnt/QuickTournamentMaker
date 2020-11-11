package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.CheckedTextView
import androidx.databinding.DataBindingUtil
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class GroupExpandableRecyclerViewAdapter(private val setDrawable: (CheckedTextView, Boolean) -> Unit, private val actionModeCallback: ManagementFragmentActionModeCallBack, private val selectedPersons: Set<Person>, private val selectedGroups: Set<Group>, private val groupMap: Map<String, Group>, personGroups: List<ExpandableGroup<*>>, private val personClickListener: (Checkable, Person) -> Unit, private val groupClickListener: (Checkable, Group, ManagementFragment.GroupEditType) -> Unit) : ExpandableRecyclerViewAdapter<GroupViewHolder, PersonViewHolder>(personGroups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder = GroupViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.section_header_item, parent, false), selectedGroups, setDrawable, groupClickListener)

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder = PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.checkable_list_item, parent, false), selectedPersons, setDrawable, personClickListener)

    override fun onBindChildViewHolder(holder: PersonViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as GroupExpandableGroup).items[childIndex], actionModeCallback.multiSelect == ManagementFragmentActionModeCallBack.SelectType.PERSON)

    override fun onBindGroupViewHolder(holder: GroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind(groupMap.getValue(group.title), actionModeCallback.multiSelect == ManagementFragmentActionModeCallBack.SelectType.GROUP)
}
