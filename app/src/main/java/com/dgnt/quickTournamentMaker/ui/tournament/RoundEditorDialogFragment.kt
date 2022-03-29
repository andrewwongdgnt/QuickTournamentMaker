package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
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

class RoundEditorDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: RoundEditorViewModelFactory by instance()

    companion object {
        const val TAG = "RoundEditorDialogFragment"

        private const val KEY_ROUND = "KEY_MATCH_UP"
        private const val KEY_ROUND_GROUP_INDEX = "KEY_ROUND_GROUP_INDEX"
        private const val KEY_ROUND_INDEX = "KEY_ROUND_INDEX"
        private const val KEY_LISTENER = "KEY_LISTENER"

        fun newInstance(
            round: Round,
            roundGroupIndex: Int,
            roundIndex: Int,
            listener: OnEditListener<Round>
        ) =
            RoundEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_ROUND, round)
                    putInt(KEY_ROUND_GROUP_INDEX, roundGroupIndex)
                    putInt(KEY_ROUND_INDEX, roundIndex)
                    putParcelable(KEY_LISTENER, listener)
                }

            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val binding = RoundEditorFragmentBinding.inflate(activity.layoutInflater)
            val viewModel = ViewModelProvider(this, viewModelFactory)[RoundEditorViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            val round = arguments?.getParcelable<Round>(KEY_ROUND)!!
            val roundGroupIndex = arguments?.getInt(KEY_ROUND_GROUP_INDEX)!!
            val roundIndex = arguments?.getInt(KEY_ROUND_INDEX)!!
            val listener = arguments?.getParcelable<OnEditListener<Round>>(KEY_LISTENER)!!

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