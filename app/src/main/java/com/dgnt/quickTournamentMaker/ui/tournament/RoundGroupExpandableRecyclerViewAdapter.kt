package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class RoundGroupExpandableRecyclerViewAdapter(roundRoundGroups: List<RoundGroupExpandableGroup>, private val clickListener: (Round) -> Unit) : ExpandableRecyclerViewAdapter<SimpleExpandedGroupViewHolder, RoundExpandedChildViewHolder>(roundRoundGroups) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int) = SimpleExpandedGroupViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int) = RoundExpandedChildViewHolder(ChildListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener)

    override fun onBindChildViewHolder(holder: RoundExpandedChildViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as RoundGroupExpandableGroup).items[childIndex])

    override fun onBindGroupViewHolder(holder: SimpleExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind((group as RoundGroupExpandableGroup).roundGroup.title)
}
