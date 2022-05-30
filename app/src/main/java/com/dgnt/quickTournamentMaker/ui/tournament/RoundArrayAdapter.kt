package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.databinding.ArrayItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.util.viewBinding

class RoundArrayAdapter(
    context: Context,
    private val rounds: List<Round>
) : ArrayAdapter<Round>(context, 0, rounds) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        convertView ?: parent.viewBinding(ArrayItemBinding::inflate).run {
            arrayItemTv.apply {
                val round = rounds[position]
                text = round.getDisplayTitle()
                setTypeface(typeface, if (round.isUpdatedTitle()) Typeface.BOLD else Typeface.NORMAL)

                setTextColor(round.color)
            }
        }


}