package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Checkable
import android.widget.CheckedTextView
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
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.synthetic.main.management_fragment.*


class ManagementFragment : Fragment() {
    companion object {
        fun newInstance() = ManagementFragment()
    }

    private val selectedPersons = mutableSetOf<String>()
    private val selectedGroups = mutableSetOf<String>()
    private lateinit var actionModeCallback: ManagementFragmentActionModeCallBack
    private lateinit var binding: ManagementFragmentBinding
    private lateinit var viewModel: ManagementViewModel
    private lateinit var personToGroupNameMap: Map<String, String>
    private lateinit var groupNames: List<String>;
    private var actionMode: ActionMode? = null

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
            R.id.action_editPersonMode,R.id.action_editGroupMode -> {
                actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (context == null) {
            return
        }

        add_fab.setOnClickListener { add() }

        actionModeCallback = ManagementFragmentActionModeCallBack(binding, selectedPersons)

        setHasOptionsMenu(true)
        val db = QTMDatabase.getInstance(context!!)
        val personRepository = PersonRepository.getInstance(db.personDAO)
        val groupRepository = GroupRepository.getInstance(db.groupDAO)
        val factory = ManagementViewModelFactory(personRepository, groupRepository)
        viewModel = ViewModelProvider(this, factory).get(ManagementViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this


        binding.personRv.layoutManager = LinearLayoutManager(context)


        val setDrawable = {checkedTextView: CheckedTextView, selectable:Boolean ->
            if (selectable) {
                val attrs = intArrayOf(android.R.attr.listChoiceIndicatorMultiple)
                val ta = context!!.theme.obtainStyledAttributes(attrs)
                val indicator = ta.getDrawable(0)
                checkedTextView.checkMarkDrawable = indicator;
                ta.recycle()
            } else {
                checkedTextView.checkMarkDrawable = null
            }

        }



        viewModel.personAndGroupLiveData.observe(viewLifecycleOwner, Observer { (persons, groups) ->

            Log.d("DGNTTAG", "person: $persons")
            Log.d("DGNTTAG", "group: $groups")

            groupNames = groups.map { it.name }
            val groupMap = groups.map { it.name to Group.fromEntity(it) }.toMap()

            personToGroupNameMap = persons.map { it.name to it.groupName }.toMap()
            val extraGroupExpandableGroupMap = groupNames.subtract(persons.map { it.groupName }.toSet()).map { GroupExpandableGroup(it, listOf()) }
            val groupExpandableGroupMap = persons.groupBy { it.groupName }.map { it.key to it.value.map { Person.fromEntity(it) } }.map { GroupExpandableGroup(it.first, it.second.sorted()) }

            val adapter = GroupExpandableRecyclerViewAdapter(setDrawable, actionModeCallback, selectedPersons, selectedGroups, groupMap, (groupExpandableGroupMap + extraGroupExpandableGroupMap).sorted(), { checkable: Checkable, person: Person -> personClicked(checkable, person) }, { checkable: Checkable, group: Group -> groupClicked(checkable, group) })
            binding.personRv.adapter = adapter

            add_fab.visibility = View.VISIBLE
        })
    }
private fun something():Int = 5
    private fun add() = AddChoiceDialogFragment.newInstance(groupNames).show(activity?.supportFragmentManager!!, AddChoiceDialogFragment.TAG)

    private fun personClicked(checkable: Checkable, person: Person) {

        if (actionModeCallback.multiSelect) {
            val isChecked = !checkable.isChecked
            if (isChecked)
                selectedPersons.add(person.name)
            else
                selectedPersons.remove(person.name)
            checkable.isChecked = isChecked

            val menu = actionMode?.menu
            menu?.findItem(R.id.action_delete)?.isVisible = selectedPersons.size > 0
            menu?.findItem(R.id.action_move)?.isVisible = selectedPersons.size > 0

            actionMode?.title = selectedPersons.size.toString()
        } else
            PersonEditorDialogFragment.newInstance(true, getString(R.string.editing, person.name), person, personToGroupNameMap[person.name] ?: "", groupNames).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)

    }


    private fun groupClicked(checkable: Checkable, group: Group) {

        if (actionModeCallback.multiSelect) {
            val isChecked = !checkable.isChecked
            if (isChecked)
                selectedGroups.add(group.name)
            else
                selectedGroups.remove(group.name)
            checkable.isChecked = isChecked

            val menu = actionMode?.menu
            menu?.findItem(R.id.action_delete)?.isVisible = selectedGroups.size > 0
            menu?.findItem(R.id.action_move)?.isVisible = selectedGroups.size > 0

            actionMode?.title = selectedGroups.size.toString()
        } else
            GroupEditorDialogFragment.newInstance(true, getString(R.string.editing, group.name), group).show(activity?.supportFragmentManager!!, GroupEditorDialogFragment.TAG)
    }


}

class ManagementFragmentActionModeCallBack(private val binding: ManagementFragmentBinding, private val selectedPersons: MutableSet<String>) : ActionMode.Callback {
    var multiSelect = false

    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        multiSelect = true
        binding.addFab.visibility = View.INVISIBLE
        actionMode.title = selectedPersons.size.toString()
        actionMode.menuInflater.inflate(R.menu.actions_management_contextual, menu)
        menu.findItem(R.id.action_delete)?.isVisible = selectedPersons.size > 0
        menu.findItem(R.id.action_move)?.isVisible = selectedPersons.size > 0
        binding.personRv.adapter?.notifyDataSetChanged()
        return true;
    }

    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
        reset()
        actionMode.finish()
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode) {
        reset()
    }

    private fun reset() {
        multiSelect = false
        binding.addFab.visibility = View.VISIBLE
        selectedPersons.clear()
        binding.personRv.adapter?.notifyDataSetChanged()
    }

}