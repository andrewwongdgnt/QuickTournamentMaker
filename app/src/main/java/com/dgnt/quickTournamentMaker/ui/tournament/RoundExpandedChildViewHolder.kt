package com.dgnt.quickTournamentMaker.ui.tournament

import android.graphics.Typeface
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class RoundExpandedChildViewHolder(private val binding: ChildListItemBinding, private val clickListener: (Round) -> Unit) : ChildViewHolder(binding.root) {
    fun bind(round: Round) =
        binding.childListItemTv.run {
            text = round.getDisplayTitle()
            setTextColor(round.color)
            setTypeface(typeface, if (round.isUpdatedTitle()) Typeface.BOLD else Typeface.NORMAL)
            itemView.setOnClickListener {
                clickListener(round)
            }
        }


}