package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ComponentTournamentTypeEditorBinding
import com.dgnt.quickTournamentMaker.databinding.RebuildTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class RebuildTournamentDialogFragment(
    private val tournamentInformation: TournamentInformation,
    private val orderedParticipants: List<Participant>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: RebuildTournamentViewModelFactory by instance()

    companion object {

        const val TAG = "RebuildTournamentDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            tournamentInformation: TournamentInformation,
            orderedParticipants: List<Participant>
        ) =
            RebuildTournamentDialogFragment(
                tournamentInformation,
                orderedParticipants
            ).show(fragmentManager, TAG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            val binding = RebuildTournamentFragmentBinding.inflate(activity.layoutInflater)

            val viewModel = ViewModelProvider(this, viewModelFactory)[RebuildTournamentViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            setVMData(viewModel, binding.tournamentTypeEditor)

            TournamentUtil.setUpTournamentTypeUI(
                viewModel,
                binding.tournamentTypeEditor,
                this,
                activity,
                true
            )

            TournamentUtil.setUpTournamentEvents(
                viewModel,
                activity,
                activity,
                requireActivity().supportFragmentManager
            ) {
                activity.finish()
            }

            AlertDialog.Builder(activity)
                .setTitle(R.string.rebuildTournament)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    viewModel.startTournament()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

    private fun setVMData(viewModel: RebuildTournamentViewModel, tournamentTypeEditor: ComponentTournamentTypeEditorBinding) {
        viewModel.setData(
            tournamentTypeEditor.eliminationRb.id,
            tournamentTypeEditor.doubleEliminationRb.id,
            tournamentTypeEditor.roundRobinRb.id,
            tournamentTypeEditor.swissRb.id,
            tournamentTypeEditor.survivalRb.id,

            tournamentTypeEditor.randomSeedRb.id,
            tournamentTypeEditor.customSeedRb.id,
            tournamentTypeEditor.compareRankFromPriorityRb.id,
            tournamentTypeEditor.compareRankFromScoreRb.id,

            tournamentInformation,
            orderedParticipants
        )
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

}