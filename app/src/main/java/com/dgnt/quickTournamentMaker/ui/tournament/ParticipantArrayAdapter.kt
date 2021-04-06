package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.databinding.ArrayItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant

class ParticipantArrayAdapter(
    context: Context,
    private val participants: List<Participant>) : ArrayAdapter<Participant>(context, 0, participants) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        convertView ?: ArrayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            arrayItemTv.apply {
                text = participants[position].displayName
                setTextColor(participants[position].color)
            }
        }

}