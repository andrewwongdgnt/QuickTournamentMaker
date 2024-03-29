package com.dgnt.quickTournamentMaker.ui.tournament

import android.graphics.Typeface
import android.view.View
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.ui.main.common.ExpandedGroupViewHolder


class SimpleExpandedGroupViewHolder(private val binding: GroupItemBinding) : ExpandedGroupViewHolder(binding) {

    fun bind(title: String) =
        binding.run {
            sectionHeaderTv.run {
                text = title
            }
            sectionHeaderCtv.run {
                visibility = View.GONE
            }
            sectionHeaderIv.run {
                visibility = View.GONE

            }
        }


}