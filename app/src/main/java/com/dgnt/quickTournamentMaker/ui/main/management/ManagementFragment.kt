package com.dgnt.quickTournamentMaker.ui.main.management

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CheckedTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ManagementFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.SimpleLogger
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance


class ManagementFragment : Fragment(R.layout.management_fragment), DIAware {

    override val di by di()
    private val viewModelFactory: ManagementViewModelFactory by instance()
    private val binding by viewBinding(ManagementFragmentBinding::bind)
    private lateinit var viewModel: ManagementViewModel

    companion object {
        fun newInstance() = ManagementFragment()
    }

    enum class GroupEditType {
        EDIT, CHECK
    }

    private val selectedPersons = mutableSetOf<Person>()
    private val selectedGroups = mutableSetOf<Group>()
    private lateinit var actionModeCallback: ManagementFragmentActionModeCallBack
    private var personToGroupNameMap: Map<Person, Group>? = null
    private lateinit var groups: List<Group>
    private var actionMode: ActionMode? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_management, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_editPersonMode -> {
                actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)?.also {
                    it.tag = ManagementFragmentActionModeCallBack.SelectType.PERSON
                    it.invalidate()
                }
            }
            R.id.action_editGroupMode -> {
                actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)?.also {
                    it.tag = ManagementFragmentActionModeCallBack.SelectType.GROUP
                    it.invalidate()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            actionMode?.finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { context ->
            binding.addPersonFab.setOnClickListener {
                PersonEditorDialogFragment.newInstance(
                    false,
                    getString(R.string.addingPlayer),
                    Person(),
                    Group(),
                    groups
                ).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)
            }
            binding.addGroupFab.setOnClickListener {
                GroupEditorDialogFragment.newInstance(
                    false,
                    getString(R.string.addingGroup),
                    Group("", "", "", false)
                ).show(activity?.supportFragmentManager!!, GroupEditorDialogFragment.TAG)
            }

            actionModeCallback = ManagementFragmentActionModeCallBack(
                selectedPersons,
                selectedGroups,
                { viewVisibility ->
                    binding.addFab.visibility = viewVisibility
                    binding.personRv.adapter?.notifyDataSetChanged()
                },
                { menuId: Int, persons: Set<Person>, groups: Set<Group> -> menuResolver(menuId, persons, groups) }
            )

            setHasOptionsMenu(true)

            viewModel = ViewModelProvider(this, viewModelFactory)[ManagementViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = viewLifecycleOwner

            viewModel.messageEvent.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            val setDrawable = { checkedTextView: CheckedTextView, selectable: Boolean ->
                if (selectable) {
                    val attrs = intArrayOf(android.R.attr.listChoiceIndicatorMultiple)
                    val ta = context.theme.obtainStyledAttributes(attrs)
                    val indicator = ta.getDrawable(0)
                    checkedTextView.checkMarkDrawable = indicator
                    ta.recycle()
                } else {
                    checkedTextView.checkMarkDrawable = null
                }
            }

            viewModel.personAndGroupLiveData.observe(viewLifecycleOwner) { (persons, groups) ->

                SimpleLogger.d(this, "person: $persons")
                SimpleLogger.d(this, "group: $groups")

                try {
                    this.groups = groups.map { Group.fromEntity(it) }.sorted()

                    val groupMap = groups.associate { it.name to Group.fromEntity(it) }

                    personToGroupNameMap = persons.associate { Person.fromEntity(it) to groupMap.getValue(it.groupName) }
                    val nonEmptyGroups = groups.filter { group -> persons.any { it.groupName == group.name } }.map { Group.fromEntity(it) }.toSet()

                    val emptyGroupExpandableGroupMap = this.groups.map { it.name }.subtract(persons.map { it.groupName }.toSet()).map { GroupExpandableGroup(it, listOf()) }
                    val groupExpandableGroupMap = persons.groupBy { it.groupName }.map { it.key to it.value.map { Person.fromEntity(it) } }.map { GroupExpandableGroup(it.first, it.second.sorted()) }

                    val personGroups = (groupExpandableGroupMap + emptyGroupExpandableGroupMap).sorted()

                    //remove groups that don't exist anymore
                    viewModel.groupsExpanded.removeAll(viewModel.groupsExpanded.minus(groupMap.map { it.key }.toSet()))

                    val adapter = GroupExpandableRecyclerViewAdapter(
                        setDrawable,
                        actionModeCallback,
                        selectedPersons,
                        selectedGroups,
                        groupMap,
                        nonEmptyGroups,
                        personGroups,
                        { checked, person -> personClicked(checked, person) },
                        { checked, group, editType: GroupEditType -> groupClicked(checked, group, editType) }
                    )
                    adapter.setOnGroupExpandCollapseListener(object : GroupExpandCollapseListener {
                        override fun onGroupExpanded(group: ExpandableGroup<*>) {
                            viewModel.groupsExpanded.add(group.title)
                        }

                        override fun onGroupCollapsed(group: ExpandableGroup<*>) {
                            viewModel.groupsExpanded.remove(group.title)
                        }

                    })
                    binding.personRv.adapter = adapter
                    adapter.groups.forEach { g ->
                        if (viewModel.groupsExpanded.contains(g.title))
                            adapter.toggleGroup(g)
                    }

                    binding.addFab.visibility = View.VISIBLE

                    binding.noResultsTv.visibility = if (personGroups.isEmpty()) View.VISIBLE else View.GONE
                    binding.mainContainer.visibility = if (personGroups.isEmpty()) View.GONE else View.VISIBLE
                } catch (e: Exception) {
                    SimpleLogger.e(this, "Something happened (Probably groups didn't resolve yet) so just do nothing and hope the next observed event fixes it", e)
                }
            }

            viewModel.expandAll.observe(viewLifecycleOwner) { expand ->
                viewLifecycleOwner.lifecycleScope.launch {
                    tryToToggleAllGroups(expand)
                }
            }
        }

    }

    private suspend fun tryToToggleAllGroups(expand: Boolean, retries: Int = 20) {
        (binding.personRv.adapter as? GroupExpandableRecyclerViewAdapter)?.let { adapter ->
            adapter.groups.forEach { g ->
                if ((expand && !adapter.isGroupExpanded(g)) || (!expand && adapter.isGroupExpanded(g))) {
                    adapter.toggleGroup(g)
                }
            }
        } ?: retries.takeIf { it > 0 }?.let { retry ->
            delay(3000)
            tryToToggleAllGroups(expand, retry - 1)
        }
    }

    private fun personClicked(checked: Boolean, person: Person) {

        if (actionModeCallback.multiSelect == ManagementFragmentActionModeCallBack.SelectType.PERSON) {
            if (checked)
                selectedPersons.add(person)
            else
                selectedPersons.remove(person)


            actionMode?.run {
                menu.run {
                    (selectedPersons.size > 0).let {
                        findItem(R.id.action_delete).isVisible = it
                        findItem(R.id.action_move).isVisible = it
                    }
                }
                title = selectedPersons.size.toString()
            }
        } else if (actionModeCallback.multiSelect == ManagementFragmentActionModeCallBack.SelectType.NONE)
            PersonEditorDialogFragment.newInstance(true, getString(R.string.editing, person.name), person, personToGroupNameMap?.get(person) ?: Group(), groups).show(activity?.supportFragmentManager!!, PersonEditorDialogFragment.TAG)

    }

    private fun groupClicked(checked: Boolean, group: Group, editType: GroupEditType) {

        if (editType == GroupEditType.CHECK && actionModeCallback.multiSelect == ManagementFragmentActionModeCallBack.SelectType.GROUP) {

            if (checked)
                selectedGroups.add(group)
            else
                selectedGroups.remove(group)


            actionMode?.run {
                menu.run {
                    findItem(R.id.action_delete).isVisible = selectedGroups.isNotEmpty()
                    findItem(R.id.action_move).isVisible = false
                }
                title = selectedGroups.size.toString()
            }
        } else if (editType == GroupEditType.EDIT) {
            GroupEditorDialogFragment.newInstance(true, getString(R.string.editing, group.name), group).show(activity?.supportFragmentManager!!, GroupEditorDialogFragment.TAG)
            actionMode?.finish()
        }


    }

    private fun menuResolver(menuId: Int, selectedPersons: Set<Person>, selectedGroups: Set<Group>) {
        when (menuId) {
            R.id.action_delete -> {
                if (actionModeCallback.multiSelect == ManagementFragmentActionModeCallBack.SelectType.PERSON) {
                    activity?.let { activity ->
                        MaterialAlertDialogBuilder(activity, R.style.MyDialogTheme)
                            .setTitle(getString(R.string.deletingPlayers, selectedPersons.size))
                            .setMessage(getString(R.string.deletePlayerMsg, selectedPersons.size))
                            .setPositiveButton(android.R.string.ok) { _, _ -> viewModel.delete(selectedPersons.map { it.toEntity(personToGroupNameMap?.get(it)?.name ?: "") }, getString(R.string.deletePlayerSuccessfulMsg, selectedPersons.size)) }
                            .setNegativeButton(android.R.string.cancel, null).create().show()
                    }
                } else {
                    GroupDeleteDialogFragment.newInstance(selectedGroups.toList(), groups).show(activity?.supportFragmentManager!!, GroupDeleteDialogFragment.TAG)

                }
            }
            R.id.action_move -> {
                MovePersonsDialogFragment.newInstance(selectedPersons.toList(), this.groups).show(activity?.supportFragmentManager!!, MovePersonsDialogFragment.TAG)
            }
        }
    }


}
