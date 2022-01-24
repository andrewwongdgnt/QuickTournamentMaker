package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation


class RestoredTournamentInformationItemViewHolder(
    private val binding: TournamentListItemBinding,
    private val moreInfoListener: (RestoredTournamentInformation) -> Unit,
    private val loadListener: (RestoredTournamentInformation) -> Unit
) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(value: RestoredTournamentInformation) =
        binding.run {
            value.extendedTournamentInformation.basicTournamentInformation.let {
                titleTv.text = it.title
                descriptionTv.text = it.description
                tournamentIv.setImageResource(it.tournamentType.drawableResource)
            }
            moreInfoBtn.setOnClickListener { moreInfoListener.invoke(value) }
            loadBtn.setOnClickListener { loadListener.invoke(value) }
        }


}