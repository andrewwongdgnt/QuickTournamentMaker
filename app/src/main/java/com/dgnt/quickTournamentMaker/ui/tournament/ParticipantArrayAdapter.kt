package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant

class ParticipantArrayAdapter(
    context: Context,
    private val participants: List<Participant>
) : ArrayAdapter<Participant>(context, R.layout.list_item, participants) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        convertView ?: ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            listItemTv.apply {
                text = participants[position].displayName
                setTextColor(participants[position].color)
            }
        }

}