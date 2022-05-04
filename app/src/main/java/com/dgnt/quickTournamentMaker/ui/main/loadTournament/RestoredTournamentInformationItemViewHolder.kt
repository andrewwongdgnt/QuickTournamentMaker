package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.content.Context
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentListItemBinding
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.util.highlight
import org.joda.time.format.DateTimeFormat


class RestoredTournamentInformationItemViewHolder(
    private val context: Context,
    private val binding: TournamentListItemBinding,
    private val moreInfoListener: (RestoredTournamentInformation) -> Unit,
    private val loadListener: (RestoredTournamentInformation) -> Unit,
    private val clickListener: (Boolean, RestoredTournamentInformation) -> Unit
) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(
        value: RestoredTournamentInformation,
        viewMode: ViewMode,
        searchText: String,
        selectable: Boolean
    ) =
        binding.run {
            value.extendedTournamentInformation.let { ext ->
                ext.basicTournamentInformation.let { basic ->

                    val color = ResourcesCompat.getColor(context.resources, R.color.searchHighlight, context.theme)

                    titleTv.text = basic.title
                    titleTv.highlight(searchText, color)

                    descriptionTv.text = basic.description
                    descriptionTv.visibility = if (viewMode == ViewMode.MINIMAL) View.INVISIBLE else View.VISIBLE
                    descriptionTv.highlight(searchText, color)

                    tournamentIv.setImageResource(basic.tournamentType.drawableResource)

                    val dateFormat = DateTimeFormat.mediumDateTime()

                    createdDateInfoTv.text = context.getString(R.string.createdDateInfo, dateFormat.print(basic.creationDate))
                    lastModifiedDateInfoTv.text = context.getString(R.string.lastModifiedDateInfo, dateFormat.print(basic.lastModifiedDate))

                    listOf(createdDateInfoTv, lastModifiedDateInfoTv, baseInfoDivider).forEach {
                        it.visibility = if (viewMode == ViewMode.DETAILED) View.VISIBLE else View.GONE
                    }
                    moreInfoIv.setOnClickListener { moreInfoListener.invoke(value) }
                    listOf(loadIv, titleTv, descriptionTv, lastModifiedDateInfoTv, createdDateInfoTv, baseInfoDivider).forEach {
                        it.setOnClickListener { if (!selectable) loadListener.invoke(value) }
                    }
                    loadIv.visibility = if (selectable) View.GONE else View.VISIBLE
                    tournamentCb.apply {
                        visibility = if (selectable) View.VISIBLE else View.INVISIBLE
                        setOnCheckedChangeListener { _, b ->
                            clickListener(b, value)
                        }
                    }
                }
            }
            Unit
        }
}