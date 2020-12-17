package com.dgnt.quickTournamentMaker.ui.main.common

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

abstract class ExpandedGroupViewHolder(private val binding: GroupItemBinding) : GroupViewHolder(binding.root) {

    override fun expand() {
        val rotate = RotateAnimation(270F, 360F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        binding.arrowDown.animation = rotate
    }

    override fun collapse() {
        val rotate = RotateAnimation(360F, 270F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.duration = 300
        rotate.fillAfter = true
        binding.arrowDown.animation = rotate
    }
}