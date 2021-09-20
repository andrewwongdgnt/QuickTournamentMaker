package com.dgnt.quickTournamentMaker.ui.tournament

import android.graphics.Typeface
import com.dgnt.quickTournamentMaker.databinding.ChildListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class MatchUpExpandedChildViewHolder(private val binding: ChildListItemBinding, private val createDefaultTitleService: ICreateDefaultTitleService, private val clickListener: (MatchUp) -> Unit) : ChildViewHolder(binding.root) {
    fun bind(matchUp: MatchUp) =
        binding.childListItemTv.run {
            text = matchUp.run {
                if (useTitle)
                    title
                else
                    createDefaultTitleService.forMatchUp(context.resources, matchUpIndex, participant1, participant2)
            }.run {
                this + (if (matchUp.note.isEmpty()) "" else "*")
            }
            setTypeface(typeface, if (matchUp.useTitle) Typeface.BOLD else Typeface.NORMAL)
            setTextColor(matchUp.color)
            itemView.setOnClickListener {
                clickListener(matchUp)
            }
        }


}