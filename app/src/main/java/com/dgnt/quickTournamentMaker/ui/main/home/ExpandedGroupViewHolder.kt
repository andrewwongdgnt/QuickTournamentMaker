package com.dgnt.quickTournamentMaker.ui.main.home

import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class ExpandedGroupViewHolder(private val binding: GroupItemBinding) : GroupViewHolder(binding.root) {

    fun bind(title: String) {
        binding.sectionHeaderCtv.text = title
        binding.sectionHeaderIv.visibility = View.GONE
    }

    override fun expand() {
        val rotate = RotateAnimation(360F, 180F, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        binding.arrowDown.animation = rotate
    }

    override fun collapse() {
        val rotate = RotateAnimation(180F, 360F, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        binding.arrowDown.animation = rotate
    }


}