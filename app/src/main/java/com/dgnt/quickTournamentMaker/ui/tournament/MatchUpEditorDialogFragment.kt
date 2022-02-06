package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MatchUpEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class MatchUpEditorDialogFragment(
    private val matchUp: MatchUp,
    private val roundGroupIndex: Int,
    private val roundIndex: Int,
    private val matchUpIndex: Int,
    private val listener: OnEditListener<MatchUp>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: MatchUpEditorViewModelFactory by instance()

    companion object {
        const val TAG = "MatchUpEditorDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            matchUp: MatchUp,
            roundGroupIndex: Int,
            roundIndex: Int,
            matchUpIndex: Int,
            listener: OnEditListener<MatchUp>
        ) =
            MatchUpEditorDialogFragment(
                matchUp,
                roundGroupIndex,
                roundIndex,
                matchUpIndex,
                listener,
            ).show(fragmentManager, TAG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val binding = MatchUpEditorFragmentBinding.inflate(activity.layoutInflater)
            val viewModel = ViewModelProvider(this, viewModelFactory)[MatchUpEditorViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.setData(resources, matchUp, resources.getStringArray(R.array.colorOptionsNames).asList().zip(resources.getIntArray(R.array.colorOptions).asList()).map {
                ColorInfo(it.first, it.second)
            })


            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.editingThisMatchUp))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    listener.onEdit(MatchUp(roundGroupIndex, roundIndex, matchUpIndex, viewModel.title.value ?: "", viewModel.useCustomTitle.value ?: false, viewModel.note.value ?: "", viewModel.color.value?.hex ?: TournamentUtil.DEFAULT_DISPLAY_COLOR))
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