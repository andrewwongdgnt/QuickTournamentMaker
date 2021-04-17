package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Checkable
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.databinding.ListItemBinding
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class RoundExpandableRecyclerViewAdapter(matchUpRounds: List<RoundExpandableGroup>, private val createDefaultTitleService: ICreateDefaultTitleService, private val clickListener: (MatchUp) -> Unit) : ExpandableRecyclerViewAdapter<RoundExpandedGroupViewHolder, MatchUpExpandedChildViewHolder>(matchUpRounds) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int) = RoundExpandedGroupViewHolder(GroupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int) = MatchUpExpandedChildViewHolder(ChildListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), createDefaultTitleService, clickListener)

    override fun onBindChildViewHolder(holder: MatchUpExpandedChildViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) = holder.bind((group as RoundExpandableGroup).items[childIndex])

    override fun onBindGroupViewHolder(holder: RoundExpandedGroupViewHolder, flatPosition: Int, group: ExpandableGroup<*>) = holder.bind((group as RoundExpandableGroup).round)
}
