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
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class TournamentEditorDialogFragment : DialogFragment(), DIAware, IParticipantEditorDialogFragmentListener {
    override val di by di()
    private val viewModelFactory: TournamentEditorViewModelFactory by instance()

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

    private lateinit var binding: TournamentEditorFragmentBinding
    private lateinit var viewModel: TournamentEditorViewModel

    private lateinit var participants: MutableList<Participant>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listenerEditor = (context as? ITournamentEditorDialogFragmentListener) ?: (targetFragment as? ITournamentEditorDialogFragmentListener) ?: (throw IllegalArgumentException("ITournamentEditorDialogFragmentListener not found"))

    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =


        activity?.let { activity ->


            binding = TournamentEditorFragmentBinding.inflate(layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory).get(TournamentEditorViewModel::class.java)
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.setData(arguments?.getString(KEY_TITLE)!!, arguments?.getString(KEY_DESCRIPTION)!!)

            participants = arguments?.getParcelableArray(KEY_PARTICIPANTS)?.map { it as Participant }?.toMutableList() ?: mutableListOf()

            binding.participantRv.adapter = ParticipantRecyclerViewAdapter(participants) { p: Participant -> ParticipantEditorDialogFragment.newInstance(p).also { it.setTargetFragment(this, 1) }.show(activity.supportFragmentManager, ParticipantEditorDialogFragment.TAG) }
            updateParticipantList()
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

    override fun onEditParticipant(key: String, name: String, note: String, color: Int) {
        participants.find { it.key == key }?.also {
            it.displayName = name
            it.note = note
            it.color = color
        }?.apply {
            updateParticipantList()
        }
    }

    private fun updateParticipantList() {
        participants.sort()
        binding.participantRv.adapter?.notifyDataSetChanged()
    }
}