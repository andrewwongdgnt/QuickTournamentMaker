package com.dgnt.quickTournamentMaker.ui.main.management

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.ActionMode
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.ManagementFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person



class ManagementFragmentActionModeCallBack(private val binding: ManagementFragmentBinding, private val selectedPersons: MutableSet<Person>, private val selectedGroups: MutableSet<Group>, private val menuResolver: (Int, Set<Person>, Set<Group>) -> Unit) : ActionMode.Callback {

    enum class SelectType {
        NONE, PERSON, GROUP
    }

    var multiSelectRequest = SelectType.NONE
    var multiSelect = SelectType.NONE

    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        multiSelect = multiSelectRequest
        multiSelectRequest = SelectType.NONE
        binding.addFab.visibility = View.INVISIBLE
        actionMode.title = if (multiSelect == SelectType.PERSON) selectedPersons.size.toString() else selectedGroups.size.toString()
        actionMode.menuInflater.inflate(R.menu.actions_management_contextual, menu)
        menu.findItem(R.id.action_delete)?.isVisible = if (multiSelect == SelectType.PERSON) selectedPersons.isNotEmpty() else selectedGroups.isNotEmpty()
        menu.findItem(R.id.action_move)?.isVisible = if (multiSelect == SelectType.PERSON) selectedPersons.isNotEmpty() else selectedGroups.isNotEmpty()
        binding.personRv.adapter?.notifyDataSetChanged()
        return true;
    }

    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
        menuResolver(menuItem.itemId, selectedPersons.toSet(), selectedGroups.toSet())
        reset()
        actionMode.finish()
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode) {
        reset()
    }

    private fun reset() {
        multiSelect = SelectType.NONE
        binding.addFab.visibility = View.VISIBLE
        selectedPersons.clear()
        selectedGroups.clear()
        binding.personRv.adapter?.notifyDataSetChanged()
    }

}