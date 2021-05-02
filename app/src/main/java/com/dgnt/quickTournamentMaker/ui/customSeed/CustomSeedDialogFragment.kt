package com.dgnt.quickTournamentMaker.ui.customSeed

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.CustomSeedFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class CustomSeedDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: CustomSeedViewModelFactory by instance()

    companion object {
        const val TAG = "CustomSeedDialogFragment"

        private const val KEY_TOURNAMENT_INFO = "KEY_TOURNAMENT_INFO"

        fun newInstance(tournamentInformation: TournamentInformation) =
            CustomSeedDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TOURNAMENT_INFO, tournamentInformation)
                }

            }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val binding = CustomSeedFragmentBinding.inflate(activity.layoutInflater)
            val viewModel = ViewModelProvider(this, viewModelFactory).get(CustomSeedViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this

            val tournamentInformation = arguments?.getParcelable<TournamentInformation>(KEY_TOURNAMENT_INFO)!!


            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.customSeed))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    startActivity(TournamentActivity.createIntent(activity, tournamentInformation))
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