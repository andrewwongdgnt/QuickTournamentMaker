package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.SuperExtendedTournamentInformation

class SuperExtendedTournamentInformationRecyclerViewAdapter(
    private val items: List<SuperExtendedTournamentInformation>,
    private val moreInfoListener: (SuperExtendedTournamentInformation) -> Unit,
    private val loadListener: (SuperExtendedTournamentInformation) -> Unit,
) : RecyclerView.Adapter<SuperExtendedTournamentInformationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperExtendedTournamentInformationItemViewHolder = SuperExtendedTournamentInformationItemViewHolder( TournamentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), moreInfoListener, loadListener)

    override fun onBindViewHolder(holder: SuperExtendedTournamentInformationItemViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size


}