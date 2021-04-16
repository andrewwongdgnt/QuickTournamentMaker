package com.dgnt.quickTournamentMaker.ui.tournament

import android.graphics.Typeface
import com.dgnt.quickTournamentMaker.databinding.ListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder


class MatchUpExpandedChildViewHolder(private val binding: ListItemBinding, private val createDefaultTitleService: ICreateDefaultTitleService) : ChildViewHolder(binding.root) {
    fun bind(matchUp: MatchUp) =
        binding.listItemTv.run {
            text = matchUp.run {
                if (useTitle)
                    title
                else
                    createDefaultTitleService.forMatchUp(context.resources, this)
            }
            setTypeface(typeface, if (matchUp.note.isEmpty()) Typeface.NORMAL else Typeface.ITALIC)
            setTextColor(matchUp.color)
        }


}