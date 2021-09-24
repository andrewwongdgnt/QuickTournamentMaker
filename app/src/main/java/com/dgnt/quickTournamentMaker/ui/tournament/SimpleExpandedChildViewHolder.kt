package com.dgnt.quickTournamentMaker.ui.tournament

import android.graphics.Typeface
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Rank
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class SimpleExpandedChildViewHolder(private val binding: ChildListItemBinding) : ChildViewHolder(binding.root) {
    fun bind(value: String) =
        binding.childListItemTv.run {
            text = value
        }
}