package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.EditParticipantFragmentBinding
import com.dgnt.quickTournamentMaker.databinding.EditTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import kotlinx.android.synthetic.main.main_activity.*

class EditParticipantDialogFragment : DialogFragment() {

    companion object {

        const val TAG = "EditParticipantDialogFragment"

        private const val KEY_DISPLAY_NAME = "KEY_DISPLAY_NAME"
        private const val KEY_NOTE = "KEY_NOTE"
        private const val KEY_COLOR = "KEY_COLOR"

        fun newInstance(title: String, description: String, color:Int) =
            EditParticipantDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_DISPLAY_NAME, title)
                    putString(KEY_NOTE, description)
                    putInt(KEY_COLOR, color)
                }

            }

    }
    private lateinit var listener: IEditParticipantDialogFragmentListener

    private lateinit var binding: EditParticipantFragmentBinding
    private lateinit var viewModel: EditParticipantViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IEditParticipantDialogFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement IEditParticipantDialogFragmentListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }
        binding = DataBindingUtil.inflate(activity?.layoutInflater!!, R.layout.edit_participant_fragment, container, false)

        viewModel = ViewModelProvider(this).get(EditParticipantViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        val displayName =  arguments?.getString(KEY_DISPLAY_NAME)!!

        return AlertDialog.Builder(activity)
            .setTitle(getString(R.string.editing,displayName))
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->

                listener.onEditParticipant(viewModel.name.value ?: "", viewModel.note.value ?: "", 9)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

}