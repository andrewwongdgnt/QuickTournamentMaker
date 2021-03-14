package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.EditableListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant

class ParticipantRecyclerViewAdapter(private val participants: List<Participant>, private val onClickListener: (Participant) -> Unit) : RecyclerView.Adapter<ParticipantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ParticipantViewHolder(EditableListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), onClickListener)

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) = holder.bind(participants[position])

    override fun getItemCount(): Int = participants.size
}