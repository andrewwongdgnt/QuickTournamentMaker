package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MovePersonsFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class MovePersonsDialogFragment(
    private val selectedPersons: List<Person>,
    private val groups: List<Group>
) : DialogFragment(), DIAware {
    override val di by di()
    private val viewModelFactory: MovePersonsViewModelFactory by instance()

    companion object {

        const val TAG = "MovePersonsDialogFragment"

        fun newInstance(
            fragmentManager: FragmentManager,
            selectedPersons: List<Person>,
            groups: List<Group>
        ) =
            MovePersonsDialogFragment(selectedPersons, groups).show(fragmentManager, TAG)
    }

    private lateinit var binding: MovePersonsFragmentBinding
    private lateinit var viewModel: MovePersonsViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?) =

        activity?.let { activity ->

            binding = MovePersonsFragmentBinding.inflate(activity.layoutInflater)

            viewModel = ViewModelProvider(this, viewModelFactory)[MovePersonsViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = this

            viewModel.messageEvent.observe(this) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            binding.groupRv.adapter = GroupRecyclerViewAdapter(groups) { g: Group -> handle(g) }


            return AlertDialog.Builder(activity)
                .setTitle(getString(R.string.movingPlayers, selectedPersons.size))
                .setView(binding.root)
                .setNegativeButton(android.R.string.cancel, null)
                .create()

        } ?: run {
            super.onCreateDialog(savedInstanceState)
        }

    private fun handle(group: Group) {
        viewModel.move(selectedPersons.map { it.toEntity(group.name) }, getString(R.string.movePlayerSuccessfulMsg, selectedPersons.size), getString(R.string.movePlayerFailedMsg))
        dismiss()
    }
}