package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.tournament.ExtendedTournamentInformation
import org.joda.time.format.DateTimeFormat


class ExtendedTournamentInformationItemViewHolder(private val context: Context, private val binding: TournamentListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(value: ExtendedTournamentInformation) =
        binding.run {
            titleTv.text = value.basicTournamentInformation.title
            descriptionTv.text = value.basicTournamentInformation.description
            typeInfoTv.text = context.getString(R.string.typeInfo, context.getString(value.basicTournamentInformation.tournamentType.stringResource))
            seedTypeInfoTv.text = context.getString(R.string.seedTypeInfo, context.getString(value.basicTournamentInformation.seedType.resource))
            DateTimeFormat.mediumDateTime().run {
                createdDateInfoTv.text = context.getString(R.string.createdDateInfo, print(value.basicTournamentInformation.creationDate))
                lastModifiedDateInfoTv.text = context.getString(R.string.lastModifiedDateInfo, value.basicTournamentInformation.lastModifiedDate?.let {
                    print(it)
                } ?: "-")
            }
            roundInfoTv.text = context.getString(R.string.roundInfo, value.numRounds)
            matchUpInfoTv.text = context.getString(R.string.matchUpInfo, value.numMatchUps)
            value.numMatchUpsWithByes.let {
                if (it == 0)
                    matchUpSubInfoTv.visibility = View.GONE
                else {
                    matchUpSubInfoTv.visibility = View.VISIBLE
                    matchUpSubInfoTv.text = context.getString(R.string.matchUpSubInfo, it)
                }
            }
            participantInfoTv.text = context.getString(R.string.playerInfo, value.numParticipants)
            tournamentIv.setImageResource(value.basicTournamentInformation.tournamentType.drawableResource)

        }


}