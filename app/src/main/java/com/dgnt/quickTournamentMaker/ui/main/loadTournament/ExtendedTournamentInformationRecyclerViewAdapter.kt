package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation

class ExtendedTournamentInformationRecyclerViewAdapter(
    private val items: List<ExtendedTournamentInformation>,
    private val moreInfoListener: (ExtendedTournamentInformation) -> Unit,
    private val loadListener: (ExtendedTournamentInformation) -> Unit,
) : RecyclerView.Adapter<ExtendedTournamentInformationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtendedTournamentInformationItemViewHolder = ExtendedTournamentInformationItemViewHolder( TournamentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), moreInfoListener, loadListener)

    override fun onBindViewHolder(holder: ExtendedTournamentInformationItemViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size


}