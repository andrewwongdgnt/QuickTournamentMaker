package com.dgnt.quickTournamentMaker.ui.main.home

import android.view.View
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.ui.main.common.ExpandedGroupViewHolder


class HomeExpandedGroupViewHolder(private val binding: GroupItemBinding, private val selectedGroups: Set<String>, private val clickListener: (String, Boolean) -> Unit) : ExpandedGroupViewHolder(binding) {

    fun bind(title: String) =
        binding.run {
            sectionHeaderTv.text = title
            sectionHeaderIv.visibility = View.GONE
            binding.sectionHeaderCtv.run {
                isChecked = selectedGroups.contains(title)
                setOnClickListener {
                    isChecked = !isChecked
                    clickListener(title, isChecked)
                }
            }
        }


}