package com.dgnt.quickTournamentMaker.ui.main.management

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.MovePersonsFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.main_activity.*


class MovePersonsDialogFragment : DialogFragment() {


    companion object {

        const val TAG = "MovePersonsDialogFragment"

        private const val KEY_PERSONS = "KEY_PERSONS"
        private const val KEY_GROUPS = "KEY_GROUPS"

        fun newInstance(persons: List<Person>, group: List<Group>): MovePersonsDialogFragment {
            val args = Bundle()
            args.putParcelableArrayList(KEY_PERSONS, ArrayList<Person>(persons))
            args.putParcelableArrayList(KEY_GROUPS, ArrayList<Group>(group))
            val fragment = MovePersonsDialogFragment()
            fragment.arguments = args
            return fragment
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
        val db = QTMDatabase.getInstance(activity!!)
        val personRepository = PersonRepository.getInstance(db.personDAO)
        val factory = MovePersonsViewModelFactory(personRepository)
        viewModel = ViewModelProvider(this, factory).get(MovePersonsViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

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
        viewModel.move(selectedPersons.map{it.toEntity(group.name)})
        dismiss()
    }
}