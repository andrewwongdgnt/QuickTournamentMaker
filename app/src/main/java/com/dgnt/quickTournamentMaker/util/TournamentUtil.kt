package com.dgnt.quickTournamentMaker.util

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ComponentTournamentTypeEditorBinding
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfigType
import com.dgnt.quickTournamentMaker.ui.customSeed.CustomSeedDialogFragment
import com.dgnt.quickTournamentMaker.ui.main.common.DraggableItemTouchHelperCallback
import com.dgnt.quickTournamentMaker.ui.main.common.RankPriorityRecyclerViewAdapter
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentEventHolder
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentTypeEditorViewModel
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json


class TournamentUtil {
    companion object {

        @ExperimentalSerializationApi
        val jsonMapper = Json { explicitNulls = false }

        const val DEFAULT_DISPLAY_COLOR = -0x1000000

        fun setUpTournamentTypeUI(
            viewModel: TournamentTypeEditorViewModel,
            tournamentTypeEditor: ComponentTournamentTypeEditorBinding,
            lifeCycleOwner: LifecycleOwner,
            context: Context,
            sameSeedAllowed: Boolean
        ) {


            viewModel.tournamentType.value = tournamentTypeEditor.eliminationRb.id
            viewModel.seedType.value = tournamentTypeEditor.randomSeedRb.id
            tournamentTypeEditor.sameSeedRb.visibility = if (sameSeedAllowed) View.VISIBLE else View.GONE
            viewModel.tournamentType.observe(lifeCycleOwner, {
                viewModel.showRankConfig.value = when (it) {
                    tournamentTypeEditor.eliminationRb.id, tournamentTypeEditor.doubleEliminationRb.id, tournamentTypeEditor.survivalRb.id -> false
                    else -> true
                }
                viewModel.showSeedType.value = when (it) {
                    tournamentTypeEditor.survivalRb.id -> false
                    else -> true
                }

                viewModel.handleTournamentTypeChange(it)
                viewModel.handleRankConfigHelpMsgChange(it, context.getString(R.string.rankConfigurationHelpMsg, context.getString(R.string.rankConfigurationForRoundRobinHelpMsg)), context.getString(R.string.rankConfigurationHelpMsg, context.getString(R.string.rankConfigurationForSwissHelpMsg)))

            })
            viewModel.rankConfig.observe(lifeCycleOwner, {
                viewModel.showPriorityContent.value = it == tournamentTypeEditor.compareRankFromPriorityRb.id

                viewModel.showScoringContent.value = it == tournamentTypeEditor.compareRankFromScoreRb.id

                viewModel.handleRankConfigChange(it == tournamentTypeEditor.compareRankFromPriorityRb.id)
            })

            val priorityList = mutableListOf<RankPriorityConfigType>()
            val rankPriorityRecyclerViewAdapter = RankPriorityRecyclerViewAdapter(context, priorityList)
            ItemTouchHelper(DraggableItemTouchHelperCallback(rankPriorityRecyclerViewAdapter)).attachToRecyclerView(tournamentTypeEditor.priorityRv)

            rankPriorityRecyclerViewAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    viewModel.handlePriorityConfigChange(priorityList)
                }
            })

            viewModel.priorityConfig.observe(lifeCycleOwner, {
                priorityList.update(it.toList())
                rankPriorityRecyclerViewAdapter.notifyDataSetChanged()
            })

            tournamentTypeEditor.priorityRv.adapter = rankPriorityRecyclerViewAdapter

            viewModel.scoreConfigLiveData.observe(lifeCycleOwner, {
                viewModel.handleScoreConfigChange(it.first, it.second, it.third)
            })

        }

        fun setUpTournamentEvents(
            tournamentEventHolder: TournamentEventHolder,
            lifeCycleOwner: LifecycleOwner,
            context: Context,
            fragmentManager: FragmentManager,
            onNewTournamentCallback: () -> Unit = {}
        ) {


            tournamentEventHolder.tournamentEvent.observe(lifeCycleOwner, {
                it.getContentIfNotHandled()?.let {

                    Log.d("DGNTTAG", "start tournament with random seed: $it")

                    context.startActivity(TournamentActivity.createIntent(context, it.first, it.second))
                    onNewTournamentCallback.invoke()
                }
            })

            tournamentEventHolder.customSeedTournamentEvent.observe(lifeCycleOwner, {
                it.getContentIfNotHandled()?.let {

                    Log.d("DGNTTAG", "start tournament with custom seed: $it")

                    CustomSeedDialogFragment.newInstance(it.first, it.second).also{ frag ->
                        frag.setOnNewTournamentCallback(onNewTournamentCallback)
                    }.show(fragmentManager, CustomSeedDialogFragment.TAG)

                }
            })

            tournamentEventHolder.failedToStartTournamentMessage.observe(lifeCycleOwner, {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(context, R.string.lessThan3Msg, Toast.LENGTH_LONG).show()

                }
            })
        }
    }
}