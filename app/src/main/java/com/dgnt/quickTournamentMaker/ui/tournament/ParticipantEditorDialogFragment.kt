package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ParticipantEditorFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.ColorInfo
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import kotlinx.android.synthetic.main.main_activity.*
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class ParticipantEditorDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: ParticipantEditorViewModelFactory by instance()

    companion object {

        const val TAG = "ParticipantEditorDialogFragment"

        private const val KEY_PARTICIPANT = "KEY_PARTICIPANT"

        fun newInstance(participant: Participant) =
            ParticipantEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_PARTICIPANT, participant)
                }

            }

    }

    private lateinit var listenerEditor: IParticipantEditorDialogFragmentListener

    private lateinit var key: String

    private lateinit var binding: ParticipantEditorFragmentBinding
    private lateinit var viewModel: ParticipantEditorViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)

        listenerEditor = (context as? IParticipantEditorDialogFragmentListener) ?: (targetFragment as? IParticipantEditorDialogFragmentListener) ?: (throw IllegalArgumentException("IParticipantEditorDialogFragmentListener not found"))

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }
        binding = DataBindingUtil.inflate(activity?.layoutInflater!!, R.layout.participant_editor_fragment, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ParticipantEditorViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val participant = arguments?.getParcelable<Participant>(KEY_PARTICIPANT)!!
        key = participant.key

        viewModel.setData(participant, resources.getStringArray(R.array.colorOptionsNames).asList().zip(resources.getIntArray(R.array.colorOptions).asList()).map {
            ColorInfo(it.first, it.second)
        })


        return AlertDialog.Builder(activity)
            .setTitle(getString(R.string.editing, participant.displayName))
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                listenerEditor.onEditParticipant(key, viewModel.name.value ?: "", viewModel.note.value ?: "", viewModel.color.value?.hex ?: TournamentUtil.DEFAULT_DISPLAY_COLOR)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

}