package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.databinding.ArrayItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.util.viewBinding

class ParticipantArrayAdapter(
    context: Context,
    private val participants: List<Participant>
) : ArrayAdapter<Participant>(context, 0, participants) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        convertView ?: parent.viewBinding(ArrayItemBinding::inflate).run {
            arrayItemTv.apply {
                val participant = participants[position]
                text = participant.getDisplayName()
                setTypeface(typeface, if (participant.isUpdatedTitle()) Typeface.BOLD else Typeface.NORMAL)

                setTextColor(participant.color)
            }
        }

}