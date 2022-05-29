package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MovePersonsFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class MovePersonsDialogFragment : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: MovePersonsViewModelFactory by instance()

    companion object {

        const val TAG = "MovePersonsDialogFragment"

        private const val KEY_PERSONS = "KEY_PERSONS"
        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(persons: List<Person>, group: List<Group>): MovePersonsDialogFragment =
            MovePersonsDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_PERSONS, ArrayList<Person>(persons))
                    putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(group))
                }
            }
    }

    private val binding by viewBinding(MovePersonsFragmentBinding::inflate)
    private lateinit var viewModel: MovePersonsViewModel
    private var selectedPersons: List<Person>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            viewModel = ViewModelProvider(this, viewModelFactory)[MovePersonsViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.messageEvent.observe(this) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            val selectedPersons = arguments?.getParcelableArrayList<Person>(KEY_PERSONS)!!.also {
                this.selectedPersons = it
            }
            val groups = arguments?.getParcelableArrayList<Group>(KEY_GROUPS)!!

            binding.groupRv.adapter = GroupRecyclerViewAdapter(groups) { g: Group -> handle(g) }


            return MaterialAlertDialogBuilder(activity, R.style.MyDialogTheme)
                .setTitle(getString(R.string.movingPlayers, selectedPersons.size))
                .setView(binding.root)
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

    private fun handle(group: Group) {
        selectedPersons?.let { selectedPersons ->
            viewModel.move(selectedPersons.map { it.toEntity(group.name) }, getString(R.string.movePlayerSuccessfulMsg, selectedPersons.size), getString(R.string.movePlayerFailedMsg))
        }
        dismiss()
    }
}