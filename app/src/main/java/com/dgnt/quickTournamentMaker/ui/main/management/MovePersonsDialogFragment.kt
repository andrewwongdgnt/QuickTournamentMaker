package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.MovePersonsFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.main_activity.*
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

    private lateinit var binding: MovePersonsFragmentBinding
    private lateinit var viewModel: MovePersonsViewModel
    private lateinit var selectedPersons: List<Person>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null || activity?.layoutInflater == null) {
            return super.onCreateDialog(savedInstanceState)
        }

        binding = DataBindingUtil.inflate(activity?.layoutInflater!!, R.layout.move_persons_fragment, container, false)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MovePersonsViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.messageEvent.observe(activity!!, Observer {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        })

        selectedPersons = arguments?.getParcelableArrayList<Person>(KEY_PERSONS)!!
        val groups = arguments?.getParcelableArrayList<Group>(KEY_GROUPS)!!

        binding.groupRv.adapter = GroupRecyclerViewAdapter(groups) { g: Group -> handle(g) }


        return AlertDialog.Builder(activity)
            .setTitle(getString(R.string.movingPlayers, selectedPersons.size))
            .setView(binding.root)

            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    private fun handle(group: Group) {
        viewModel.move(selectedPersons.map { it.toEntity(group.name) }, getString(R.string.movePlayerSuccessfulMsg, selectedPersons.size), getString(R.string.movePlayerFailedMsg))
        dismiss()
    }
}