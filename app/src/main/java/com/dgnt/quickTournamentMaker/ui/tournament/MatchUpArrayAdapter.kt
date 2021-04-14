package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.databinding.ArrayItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService

class MatchUpArrayAdapter(
    context: Context,
    private val createDefaultTitleService: ICreateDefaultTitleService,
    private val matchUps: List<Pair<Round, MatchUp>>
) : ArrayAdapter<Pair<Round, MatchUp>>(context, 0, matchUps) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        convertView ?: ArrayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            arrayItemTv.apply {

                text = matchUps[position].second.run {
                    if (useTitle)
                        title
                    else
                        createDefaultTitleService.forMatchUp(context.resources, this)
                }
                setTypeface(typeface, if ( matchUps[position].second.note.isEmpty()) Typeface.NORMAL else Typeface.ITALIC)
                setTextColor(matchUps[position].second.color)
            }
        }

}