package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.RoundEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class RoundEditorDialogFragment(
    private val round: Round,
    private val roundGroupIndex: Int,
    private val roundIndex: Int,
    private val listener: OnEditListener<Round>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: RoundEditorViewModelFactory by instance()

    companion object {
        const val TAG = "RoundEditorDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            round: Round,
            roundGroupIndex: Int,
            roundIndex: Int,
            listener: OnEditListener<Round>
        ) =
            RoundEditorDialogFragment(
                round,
                roundGroupIndex,
                roundIndex,
                listener
            ).show(fragmentManager, TAG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val binding = RoundEditorFragmentBinding.inflate(activity.layoutInflater)
            val viewModel = ViewModelProvider(this, viewModelFactory)[RoundEditorViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.setData(round, resources.getStringArray(R.array.colorOptionsNames).asList().zip(resources.getIntArray(R.array.colorOptions).asList()).map {
                ColorInfo(it.first, it.second)
            })


            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.editingThisRound))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    listener.onEdit(Round(roundGroupIndex, roundIndex, viewModel.title.value ?: "", viewModel.note.value ?: "", viewModel.color.value?.hex ?: TournamentUtil.DEFAULT_DISPLAY_COLOR))
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