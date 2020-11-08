package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.util.Log
import android.view.*

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.data.QTMDatabase
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.databinding.ManagementFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Person

class ManagementFragment : Fragment() {
    companion object {
        fun newInstance() = ManagementFragment()
    }

    private val selectedPersons = mutableSetOf<String>()
    private lateinit var actionModeCallback: ManagementFragmentActionModeCallBack
    private lateinit var binding: ManagementFragmentBinding
    private lateinit var viewModel: ManagementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.management_fragment, container, false)
        return binding.root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_management, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_editMode -> {
                (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)
            }
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (context == null) {
            return
        }

        actionModeCallback = ManagementFragmentActionModeCallBack(binding, selectedPersons)

        setHasOptionsMenu(true)
        val db = QTMDatabase.getInstance(context!!)
        val personRepository = PersonRepository.getInstance(db.personDAO)
        val groupRepository = GroupRepository.getInstance(db.groupDAO)
        val factory = ManagementViewModelFactory(personRepository, groupRepository)
        viewModel = ViewModelProvider(this, factory).get(ManagementViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToPersonDetails.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { triple ->// Only proceed if the event has never been handled
                val person = triple.first
                val groupName = triple.second
                val editing = triple.third

                PersonEditorDialogFragment.newInstance(editing, if (editing) getString(R.string.editing, person.name) else getString(R.string.adding), person, groupName).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)

            }
        })


        binding.personRv.layoutManager = LinearLayoutManager(context)
        viewModel.persons.observe(viewLifecycleOwner, Observer {
            Log.d("DGNTTAG", "person: $it")

            val groupExpandableGroupMap = it.groupBy { it.groupName }.map { it.key to it.value.map { Person(it.name, it.note) } }.map { GroupExpandableGroup(it.first, it.second) }
            val adapter = GroupExpandableRecyclerViewAdapter(actionModeCallback, selectedPersons, groupExpandableGroupMap) { person: Person -> itemClicked(person) }
            binding.personRv.adapter = adapter


        })
        viewModel.groups.observe(viewLifecycleOwner, Observer {
            Log.d("DGNTTAG", "group: $it")
        })
    }

    private fun itemClicked(person: Person) {
        Log.d("DGNTTAG", "person: $person")
    }


}

class ManagementFragmentActionModeCallBack(private val binding: ManagementFragmentBinding, private val selectedPersons:MutableSet<String>) : ActionMode.Callback {
    var multiSelect = false

    override fun onCreateActionMode(actionMode: ActionMode, Menu: Menu): Boolean {
        multiSelect = true
        binding.personRv.adapter?.notifyDataSetChanged()
        return true;
    }

    override fun onPrepareActionMode(actionMode: ActionMode, Menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(actionMode: ActionMode, MenuItem: MenuItem): Boolean {
        reset()
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode) {
        reset()
        binding.personRv.adapter?.notifyDataSetChanged()
    }

    private fun reset(){
        multiSelect = false
        selectedPersons.clear()
    }

}