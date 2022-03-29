package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ComponentTournamentTypeEditorBinding
import com.dgnt.quickTournamentMaker.databinding.RebuildTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class RebuildTournamentDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: RebuildTournamentViewModelFactory by instance()

    companion object {

        const val TAG = "RebuildTournamentDialogFragment"
        private const val KEY_TOURNAMENT_INFO = "KEY_TOURNAMENT_INFO"
        private const val KEY_ORDERED_PARTICIPANTS = "KEY_ORDERED_PARTICIPANTS"

        fun newInstance(
            tournamentInformation: TournamentInformation,
            orderedParticipants: List<Participant>
        ) =
            RebuildTournamentDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TOURNAMENT_INFO, tournamentInformation)
                    putParcelableArrayList(KEY_ORDERED_PARTICIPANTS, ArrayList(orderedParticipants))
                }
            }
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
                requireActivity().supportFragmentManager,
                object : OnEditListener<Unit> {
                    override fun onEdit(editedValue: Unit) {
                        activity.finish()
                    }
                }
            )

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
            arguments?.getParcelable(KEY_TOURNAMENT_INFO)!!,
            arguments?.getParcelableArrayList(KEY_ORDERED_PARTICIPANTS)!!
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