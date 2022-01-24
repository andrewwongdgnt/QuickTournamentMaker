package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation

class RestoredTournamentInformationRecyclerViewAdapter(
    private val items: List<RestoredTournamentInformation>,
    private val moreInfoListener: (RestoredTournamentInformation) -> Unit,
    private val loadListener: (RestoredTournamentInformation) -> Unit,
) : RecyclerView.Adapter<RestoredTournamentInformationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoredTournamentInformationItemViewHolder = RestoredTournamentInformationItemViewHolder( TournamentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), moreInfoListener, loadListener)

    override fun onBindViewHolder(holder: RestoredTournamentInformationItemViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size


}