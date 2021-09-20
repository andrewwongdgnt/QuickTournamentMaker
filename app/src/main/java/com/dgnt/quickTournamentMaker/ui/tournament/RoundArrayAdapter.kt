package com.dgnt.quickTournamentMaker.ui.tournament

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dgnt.quickTournamentMaker.databinding.ArrayItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Round

class RoundArrayAdapter(
    context: Context,
    private val rounds: List<Round>
) : ArrayAdapter<Round>(context, 0, rounds) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
        convertView ?: ArrayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            arrayItemTv.apply {
                val round = rounds[position]
                text = round.title.run {
                    this + (if (round.note.isEmpty()) "" else "*")
                }
                setTypeface(typeface, if (round.updatedTitle()) Typeface.BOLD else Typeface.NORMAL)

                setTextColor(round.color)
            }
        }


}