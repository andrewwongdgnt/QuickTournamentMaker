package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.RebuildTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
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

            val viewModel = ViewModelProvider(this, viewModelFactory).get(RebuildTournamentViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.setData(
                binding.tournamentTypeEditor.eliminationRb.id,
                binding.tournamentTypeEditor.doubleEliminationRb.id,
                binding.tournamentTypeEditor.roundRobinRb.id,
                binding.tournamentTypeEditor.swissRb.id,
                binding.tournamentTypeEditor.survivalRb.id,

                binding.tournamentTypeEditor.randomSeedRb.id,
                binding.tournamentTypeEditor.customSeedRb.id,
                binding.tournamentTypeEditor.compareRankFromPriorityRb.id,
                binding.tournamentTypeEditor.compareRankFromScoreRb.id,
                arguments?.getParcelable(KEY_TOURNAMENT_INFO)!!,
                arguments?.getParcelableArrayList(KEY_ORDERED_PARTICIPANTS)!!
            )

            viewModel.tournamentEvent.observe(activity, {
                it.getContentIfNotHandled()?.let {

                    Log.d("DGNTTAG", "start tournament with random seed: $it")

//                    startActivity(TournamentActivity.createIntent(this, it.first,it.second))

                }
            })

            viewModel.customSeedTournamentEvent.observe(activity, {
                it.getContentIfNotHandled()?.let {

                    Log.d("DGNTTAG", "start tournament with custom seed: $it")

//                    CustomSeedDialogFragment.newInstance(it.first, it.second).show(activity?.supportFragmentManager!!, CustomSeedDialogFragment.TAG)

                }
            })

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

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

}