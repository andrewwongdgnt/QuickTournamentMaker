package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import org.joda.time.format.DateTimeFormat


class RestoredTournamentInformationItemViewHolder(
    private val context: Context,
    private val binding: TournamentListItemBinding,
    private val moreInfoListener: (RestoredTournamentInformation) -> Unit,
    private val loadListener: (RestoredTournamentInformation) -> Unit
) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(
        value: RestoredTournamentInformation,
        viewMode: ViewMode
    ) =
        binding.run {
            value.extendedTournamentInformation.let { ext ->
                ext.basicTournamentInformation.let { basic ->
                    titleTv.text = basic.title
                    descriptionTv.text = basic.description
                    descriptionTv.visibility = if (viewMode == ViewMode.MINIMAL) View.INVISIBLE else View.VISIBLE
                    tournamentIv.setImageResource(basic.tournamentType.drawableResource)

                    val dateFormat = DateTimeFormat.mediumDateTime()

                    typeInfoTv.text = context.getString(basic.tournamentType.stringResource)
                    seedTypeInfoTv.text = context.getString(basic.seedType.resource)
                    createdDateInfoTv.text = context.getString(R.string.createdDateInfo, dateFormat.print(basic.creationDate))
                    lastModifiedDateInfoTv.text = context.getString(R.string.lastModifiedDateInfo, dateFormat.print(basic.lastModifiedDate))
                }

                roundInfoTv.text = context.getString(R.string.roundInfo, ext.numRounds)
                matchUpInfoTv.text = context.getString(R.string.matchUpInfo, ext.numMatchUps)
                matchUpSubInfoTv.text = context.getString(R.string.matchUpSubInfo, ext.numMatchUpsWithByes)
                participantInfoTv.text = context.getString(R.string.playerInfo, ext.numParticipants)

                listOf(typeInfoTv, seedTypeInfoTv, createdDateInfoTv, lastModifiedDateInfoTv, roundInfoTv, matchUpInfoTv, matchUpSubInfoTv, participantInfoTv, extendedInfoDivider, foundationalInfoDivider).forEach {
                    it.visibility = if (viewMode == ViewMode.DETAILED) View.VISIBLE else View.GONE
                }
            }
            moreInfoBtn.setOnClickListener { moreInfoListener.invoke(value) }
            loadBtn.setOnClickListener { loadListener.invoke(value) }
        }


}