package com.dgnt.quickTournamentMaker.ui.main.home

import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.ui.main.common.ExpandedGroupViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class HomeExpandedGroupViewHolder(private val binding: GroupItemBinding) : ExpandedGroupViewHolder(binding) {

    fun bind(title: String) {
        binding.sectionHeaderTv.text = title
        binding.sectionHeaderIv.visibility = View.GONE
    }




}