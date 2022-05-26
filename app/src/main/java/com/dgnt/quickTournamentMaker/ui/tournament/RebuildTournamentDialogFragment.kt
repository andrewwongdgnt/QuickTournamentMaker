package com.dgnt.quickTournamentMaker.ui.tournament

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ComponentTournamentTypeEditorBinding
import com.dgnt.quickTournamentMaker.databinding.RebuildTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.layout.TitledLinearLayout
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import com.dgnt.quickTournamentMaker.util.getAllViews
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        private const val KEY_TOURNAMENT_TYPE_ID = "KEY_TOURNAMENT_TYPE_ID"
        private const val KEY_SEED_TYPE_ID = "KEY_SEED_TYPE_ID"

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

    private lateinit var viewModel: RebuildTournamentViewModel

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            viewModel.tournamentType.value?.let { putInt(KEY_TOURNAMENT_TYPE_ID, it) }
            viewModel.seedType.value?.let { putInt(KEY_SEED_TYPE_ID, it) }
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            val binding = RebuildTournamentFragmentBinding.inflate(activity.layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory)[RebuildTournamentViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            binding.root.getAllViews().mapNotNull { it as? TitledLinearLayout }.forEach {
                it.setTextBackgroundColor(R.color.dialogBackground)
            }

            setVMData(viewModel, binding.tournamentTypeEditor)

            TournamentUtil.setUpTournamentTypeUI(
                viewModel,
                binding.tournamentTypeEditor,
                savedInstanceState?.getInt(KEY_TOURNAMENT_TYPE_ID),
                savedInstanceState?.getInt(KEY_SEED_TYPE_ID),
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

            MaterialAlertDialogBuilder(activity, R.style.MyDialogTheme)
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