package com.dgnt.quickTournamentMaker.ui.tournament

import android.graphics.Typeface
import android.view.View
import com.dgnt.quickTournamentMaker.databinding.GroupItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.ui.main.common.ExpandedGroupViewHolder


class RoundExpandedGroupViewHolder(private val binding: GroupItemBinding) : ExpandedGroupViewHolder(binding) {

    fun bind(round: Round) =
        binding.run {
            sectionHeaderTv.run {
                text = round.title
                setTypeface(typeface, if (round.note.isEmpty()) Typeface.NORMAL else Typeface.ITALIC)
                setTextColor(round.color)
            }
            sectionHeaderCtv.run {
                visibility = View.GONE
            }
            sectionHeaderIv.run {
                visibility = View.GONE

            }
        }


}