package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.dgnt.quickTournamentMaker.databinding.SectionHeaderItemBinding
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class GroupViewHolder(private val binding: SectionHeaderItemBinding) : GroupViewHolder(binding.root) {

    fun bind(group: ExpandableGroup<*>) {
        binding.sectionHeaderTv.text = group.title
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