package com.dgnt.quickTournamentMaker.ui.tournament

import android.graphics.Typeface
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class RoundExpandedChildViewHolder(private val binding: ChildListItemBinding, private val clickListener: (Round) -> Unit) : ChildViewHolder(binding.root) {
    fun bind(round: Round) =
        binding.childListItemTv.run {
            text = round.title
                .run {
                    this + (if (round.note.isEmpty()) "" else "*")
                }
            setTextColor(round.color)
            itemView.setOnClickListener {
                clickListener(round)
            }
        }


}