package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.ViewGroup
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class RoundExpandableRecyclerViewAdapter(matchUpRounds: List<RoundExpandableGroup>, private val createDefaultTitleService: ICreateDefaultTitleService, private val clickListener: (MatchUp) -> Unit) : ExpandableRecyclerViewAdapter<RoundExpandedGroupViewHolder, MatchUpExpandedChildViewHolder>(matchUpRounds) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int) = RoundExpandedGroupViewHolder(parent.viewBinding(GroupItemBinding::inflate))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int) = MatchUpExpandedChildViewHolder(parent.viewBinding(ChildListItemBinding::inflate), createDefaultTitleService, clickListener)

    override fun onBindChildViewHolder(holder: MatchUpExpandedChildViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as RoundExpandableGroup).items[childIndex])

    override fun onBindGroupViewHolder(holder: RoundExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind((group as RoundExpandableGroup).round)
}
