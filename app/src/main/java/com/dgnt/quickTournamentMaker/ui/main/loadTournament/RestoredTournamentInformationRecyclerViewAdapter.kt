package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.util.update

class RestoredTournamentInformationRecyclerViewAdapter(
    private val context: Context,
    private val items: MutableList<RestoredTournamentInformation>,
    private val moreInfoListener: (RestoredTournamentInformation) -> Unit,
    private val loadListener: (RestoredTournamentInformation) -> Unit,
    private var viewMode: ViewMode
) : RecyclerView.Adapter<RestoredTournamentInformationItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoredTournamentInformationItemViewHolder = RestoredTournamentInformationItemViewHolder(context, TournamentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), moreInfoListener, loadListener)

    override fun onBindViewHolder(holder: RestoredTournamentInformationItemViewHolder, position: Int) = holder.bind(items[position], viewMode)

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<RestoredTournamentInformation>) {
        items.update(newItems)
        notifyDataSetChanged()
    }

    fun updateList(viewMode: ViewMode) {
        this.viewMode = viewMode
        notifyDataSetChanged()
    }

}