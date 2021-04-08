package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.databinding.ArrayItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round

class MatchUpArrayAdapter(
    context: Context,
    private val matchUps: List<Pair<Round, MatchUp>>
) : ArrayAdapter<Pair<Round, MatchUp>>(context, 0, matchUps) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        convertView ?: ArrayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            arrayItemTv.apply {
                //FIXME still wrong. needs the actual player names still
                text = matchUps[position].second.title
                setTextColor(matchUps[position].second.color)
            }
        }

}