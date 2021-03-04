package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.tournament.Participant

class ParticipantRecyclerViewAdapter(private val participants:List<Participant>) : RecyclerView.Adapter<ParticipantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ParticipantViewHolder (DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.editable_list_item, parent, false))


    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) = holder.bind(participants[position])

    override fun getItemCount(): Int = participants.size
}