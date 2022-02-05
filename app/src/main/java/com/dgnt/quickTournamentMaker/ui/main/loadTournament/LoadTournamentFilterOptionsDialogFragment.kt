package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.LoadTournamentFilterOptionsFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class LoadTournamentFilterOptionsDialogFragment(
    private val listener: OnEditListener<Unit>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: LoadTournamentFilterOptionsViewModelFactory by instance()

    companion object {

        const val TAG = "LoadTournamentFilterOptionsDialogFragment"

        fun newInstance(listener: OnEditListener<Unit>): LoadTournamentFilterOptionsDialogFragment =
            LoadTournamentFilterOptionsDialogFragment(listener)

    }

    private lateinit var binding: LoadTournamentFilterOptionsFragmentBinding
    private lateinit var viewModel: LoadTournamentFilterOptionsViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            binding = LoadTournamentFilterOptionsFragmentBinding.inflate(activity.layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory)[LoadTournamentFilterOptionsViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.init()

            viewModel.eliminationFilteredOn.observe(this) {
                viewModel.setFilteredOnTournamentType(TournamentType.ELIMINATION, it)
            }
            viewModel.doubleEliminationFilteredOn.observe(this) {
                viewModel.setFilteredOnTournamentType(TournamentType.DOUBLE_ELIMINATION, it)
            }
            viewModel.roundRobinFilteredOn.observe(this) {
                viewModel.setFilteredOnTournamentType(TournamentType.ROUND_ROBIN, it)
            }
            viewModel.swissFilteredOn.observe(this) {
                viewModel.setFilteredOnTournamentType(TournamentType.SWISS, it)
            }
            viewModel.survivalFilteredOn.observe(this) {
                viewModel.setFilteredOnTournamentType(TournamentType.SURVIVAL, it)
            }


            return AlertDialog.Builder(activity)
                .setTitle(getString(R.string.filter))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ -> listener.onEdit(Unit) }
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

}