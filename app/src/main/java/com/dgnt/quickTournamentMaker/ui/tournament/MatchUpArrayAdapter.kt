package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.R
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
                //FIXME this is wrong
                matchUps[position].apply {
                    text = context.getString(R.string.participant1VsParticipant2, second.participant1.displayName, second.participant2.displayName, first.title)
                }
                setTextColor(matchUps[position].second.color)
            }
        }

}