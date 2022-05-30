package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.ViewGroup
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class SimpleExpandableRecyclerViewAdapter(
    simpleList: List<SimpleExpandableGroup>
) : ExpandableRecyclerViewAdapter<SimpleExpandedGroupViewHolder, SimpleExpandedChildViewHolder>(simpleList) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int) = SimpleExpandedGroupViewHolder(parent.viewBinding(GroupItemBinding::inflate))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int) = SimpleExpandedChildViewHolder(parent.viewBinding(ChildListItemBinding::inflate))

    override fun onBindChildViewHolder(holder: SimpleExpandedChildViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as SimpleExpandableGroup).items[childIndex].value)

    override fun onBindGroupViewHolder(holder: SimpleExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind((group as SimpleExpandableGroup).groupTitle)
}
