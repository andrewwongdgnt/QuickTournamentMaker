package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.EditableListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant

class ParticipantViewHolder(private val binding: EditableListItemBinding, private val onClickListener: (Participant) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    fun bind(participant: Participant) =
        binding.run {
            editableListItemTv.run {
                text = participant.displayName
                setTextColor(participant.color)
            }
            root.setOnClickListener {
                onClickListener(participant)
            }
        }

}