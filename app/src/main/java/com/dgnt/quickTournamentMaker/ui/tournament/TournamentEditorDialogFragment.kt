package com.dgnt.quickTournamentMaker.ui.tournament

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.EditTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import kotlinx.android.synthetic.main.main_activity.*

class TournamentEditorDialogFragment : DialogFragment(), IParticipantEditorDialogFragmentListener {

    companion object {

        const val TAG = "TournamentEditorDialogFragment"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_DESCRIPTION = "KEY_DESCRIPTION"
        private const val KEY_PARTICIPANTS = "KEY_PARTICIPANTS"

        fun newInstance(title: String, description: String, participants: Array<Participant>) =
            TournamentEditorDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TITLE, title)
                    putString(KEY_DESCRIPTION, description)
                    putParcelableArray(KEY_PARTICIPANTS, participants)
                }

            }

    }

    private lateinit var listenerEditor: ITournamentEditorDialogFragmentListener

    private lateinit var binding: EditTournamentFragmentBinding
    private lateinit var editorViewModel: TournamentEditorViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listenerEditor = context as ITournamentEditorDialogFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement ITournamentEditorDialogFragmentListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }

        binding = DataBindingUtil.inflate(activity?.layoutInflater!!, R.layout.edit_tournament_fragment, container, false)

        editorViewModel = ViewModelProvider(this).get(TournamentEditorViewModel::class.java)
        binding.vm = editorViewModel
        binding.lifecycleOwner = this

        editorViewModel.setData(arguments?.getString(KEY_TITLE)!!, arguments?.getString(KEY_DESCRIPTION)!!)

        val participants = arguments?.getParcelableArray(KEY_PARTICIPANTS)?.toList()?.map { it as Participant } ?: listOf()

        binding.participantRv.adapter = ParticipantRecyclerViewAdapter(participants)

        return AlertDialog.Builder(activity)
            .setTitle(R.string.editTournament)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->

                listenerEditor.onEditTournament(editorViewModel.title.value ?: "", editorViewModel.description.value ?: "")
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onEditParticipant(displayName: String, note: String, color: Int) {
        TODO("Not yet implemented")
    }
}