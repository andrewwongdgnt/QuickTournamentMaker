package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation
import com.dgnt.quickTournamentMaker.ui.adapter.IItemTouchHelperAdapter

class ExtendedTournamentInformationRecyclerViewAdapter(private val context: Context, private val items: List<ExtendedTournamentInformation>) : RecyclerView.Adapter<ExtendedTournamentInformationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtendedTournamentInformationItemViewHolder = ExtendedTournamentInformationItemViewHolder(context, TournamentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ExtendedTournamentInformationItemViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size


}