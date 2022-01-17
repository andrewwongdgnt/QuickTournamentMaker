package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation


class ExtendedTournamentInformationItemViewHolder(
    private val binding: TournamentListItemBinding,
    private val moreInfoListener: (ExtendedTournamentInformation) -> Unit,
    private val loadListener: (ExtendedTournamentInformation) -> Unit
) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(value: ExtendedTournamentInformation) =
        binding.run {
            titleTv.text = value.basicTournamentInformation.title
            descriptionTv.text = value.basicTournamentInformation.description
            tournamentIv.setImageResource(value.basicTournamentInformation.tournamentType.drawableResource)
            moreInfoBtn.setOnClickListener { moreInfoListener.invoke(value) }
            loadBtn.setOnClickListener { loadListener.invoke(value) }
        }


}