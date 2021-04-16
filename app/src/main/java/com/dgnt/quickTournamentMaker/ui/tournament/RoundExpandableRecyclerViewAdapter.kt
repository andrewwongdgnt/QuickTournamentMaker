package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.databinding.ListItemBinding
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class RoundExpandableRecyclerViewAdapter(matchUpRounds: List<RoundExpandableGroup>, private val createDefaultTitleService: ICreateDefaultTitleService) : ExpandableRecyclerViewAdapter<RoundExpandedGroupViewHolder, MatchUpExpandedChildViewHolder>(matchUpRounds) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int) = RoundExpandedGroupViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int) = MatchUpExpandedChildViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), createDefaultTitleService)

    override fun onBindChildViewHolder(holder: MatchUpExpandedChildViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as RoundExpandableGroup).items[childIndex])

    override fun onBindGroupViewHolder(holder: RoundExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind((group as RoundExpandableGroup).round)
}
