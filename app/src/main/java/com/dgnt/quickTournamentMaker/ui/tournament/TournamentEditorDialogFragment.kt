package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.TournamentEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class TournamentEditorDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: TournamentEditorViewModelFactory by instance()

    companion object {

        const val TAG = "TournamentEditorDialogFragment"

        private const val KEY_TOURNAMENT = "KEY_TOURNAMENT"

        fun newInstance(tournamentInformation: TournamentInformation) =
            TournamentEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TOURNAMENT, tournamentInformation)
                }
            }
    }

    private lateinit var listenerEditor: ITournamentEditorDialogFragmentListener

    private lateinit var binding: TournamentEditorFragmentBinding
    private lateinit var viewModel: TournamentEditorViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)

        listenerEditor = (context as? ITournamentEditorDialogFragmentListener) ?: (targetFragment as? ITournamentEditorDialogFragmentListener) ?: (throw IllegalArgumentException("ITournamentEditorDialogFragmentListener not found"))

    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =


        activity?.let { activity ->


            binding = TournamentEditorFragmentBinding.inflate(activity.layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory).get(TournamentEditorViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this


            viewModel.setData(arguments?.getParcelable(KEY_TOURNAMENT)!!)




            AlertDialog.Builder(activity)
                .setTitle(R.string.editTournament)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    listenerEditor.onEditTournament(viewModel.title.value ?: "", viewModel.description.value ?: "")
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