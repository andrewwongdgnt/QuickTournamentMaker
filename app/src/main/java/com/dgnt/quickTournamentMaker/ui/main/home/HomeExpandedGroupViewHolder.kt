package com.dgnt.quickTournamentMaker.ui.main.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.ui.main.common.ExpandedGroupViewHolder


class HomeExpandedGroupViewHolder(private val binding: GroupItemBinding, private val selectedGroups: Set<String>, private val clickListener: (String, Boolean) -> Unit) : ExpandedGroupViewHolder(binding) {

    fun bind(title: String, color:Int) {
        binding.sectionHeaderTv.text = title
        binding.sectionHeaderIv.visibility = View.GONE
        binding.sectionHeaderCtv.isChecked = selectedGroups.contains(title)
        binding.sectionHeaderCtv.checkMarkTintList = ColorStateList.valueOf(color)
        binding.sectionHeaderCtv.setOnClickListener { _ ->
            binding.sectionHeaderCtv.isChecked = !binding.sectionHeaderCtv.isChecked
            clickListener(title, binding.sectionHeaderCtv.isChecked)
        }
    }


}