package com.dgnt.quickTournamentMaker.ui.main.management

import com.dgnt.quickTournamentMaker.databinding.SectionHeaderItemBinding
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class GroupViewHolder(private val binding: SectionHeaderItemBinding) : GroupViewHolder(binding.root) {

    fun bind(group: ExpandableGroup<*>) {
        binding.sectionHeaderTv.text = group.title
    }
}