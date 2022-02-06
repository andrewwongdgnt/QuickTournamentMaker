package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ParticipantEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class ParticipantEditorDialogFragment(
    private val participant: Participant,
    private val listener: OnEditListener<Participant>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: ParticipantEditorViewModelFactory by instance()

    companion object {

        const val TAG = "ParticipantEditorDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            participant: Participant,
            listener: OnEditListener<Participant>
        ) =
            ParticipantEditorDialogFragment(participant, listener).show(fragmentManager, TAG)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        activity?.let { activity ->
            val binding = ParticipantEditorFragmentBinding.inflate(activity.layoutInflater)

            val viewModel = ViewModelProvider(this, viewModelFactory)[ParticipantEditorViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.setData(participant, resources.getStringArray(R.array.colorOptionsNames).asList().zip(resources.getIntArray(R.array.colorOptions).asList()).map {
                ColorInfo(it.first, it.second)
            })

            AlertDialog.Builder(activity)
                .setTitle(getString(R.string.editing, participant.name))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    listener.onEdit(Participant(participant.person, participant.participantType, viewModel.name.value ?: "", viewModel.note.value ?: "", viewModel.color.value?.hex ?: TournamentUtil.DEFAULT_DISPLAY_COLOR))
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