package com.dgnt.quickTournamentMaker.ui.tournament

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MatchUpEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class MatchUpEditorDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: MatchUpEditorViewModelFactory by instance()
    private val binding by viewBinding(MatchUpEditorFragmentBinding::inflate)

    companion object {
        const val TAG = "MatchUpEditorDialogFragment"

        private const val KEY_MATCH_UP = "KEY_MATCH_UP"
        private const val KEY_ROUND_GROUP_INDEX = "KEY_ROUND_GROUP_INDEX"
        private const val KEY_ROUND_INDEX = "KEY_ROUND_INDEX"
        private const val KEY_MATCH_UP_INDEX = "KEY_MATCH_UP_INDEX"
        private const val KEY_LISTENER = "KEY_LISTENER"

        fun newInstance(
            matchUp: MatchUp,
            roundGroupIndex: Int,
            roundIndex: Int,
            matchUpIndex: Int,
            listener: OnEditListener<MatchUp>
        ) =
            MatchUpEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MATCH_UP, matchUp)
                    putInt(KEY_ROUND_GROUP_INDEX, roundGroupIndex)
                    putInt(KEY_ROUND_INDEX, roundIndex)
                    putInt(KEY_MATCH_UP_INDEX, matchUpIndex)
                    putParcelable(KEY_LISTENER, listener)
                }

            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        context?.let { context ->
            val viewModel = ViewModelProvider(this, viewModelFactory)[MatchUpEditorViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            val matchUp = arguments?.getParcelable<MatchUp>(KEY_MATCH_UP)!!
            val roundGroupIndex = arguments?.getInt(KEY_ROUND_GROUP_INDEX)!!
            val roundIndex = arguments?.getInt(KEY_ROUND_INDEX)!!
            val matchUpIndex = arguments?.getInt(KEY_MATCH_UP_INDEX)!!
            val listener = arguments?.getParcelable<OnEditListener<MatchUp>>(KEY_LISTENER)!!

            viewModel.setData(resources, matchUp, resources.getStringArray(R.array.colorOptionsNames).asList().zip(resources.getIntArray(R.array.colorOptions).asList()).map {
                ColorInfo(it.first, it.second)
            })

            MaterialAlertDialogBuilder(context, R.style.MyDialogTheme)
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