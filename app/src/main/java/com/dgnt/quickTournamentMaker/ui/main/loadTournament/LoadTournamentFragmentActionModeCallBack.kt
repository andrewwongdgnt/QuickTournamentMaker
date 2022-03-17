package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation


class LoadTournamentFragmentActionModeCallBack(
    private val selectedTournaments: MutableSet<RestoredTournamentInformation>,
    private val onChange: () -> Unit,
    private val menuResolver: (Int, Set<RestoredTournamentInformation>) -> Unit
) : ActionMode.Callback {

    private var _multiSelect = false
    val multiSelect
        get() = _multiSelect

    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        actionMode.title = selectedTournaments.size.toString()
        actionMode.menuInflater.inflate(R.menu.actions_load_tournament_contextual, menu)
        menu.findItem(R.id.action_delete)?.isVisible = selectedTournaments.isNotEmpty()
        onChange()
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode, menu: Menu): Boolean {
        _multiSelect = (actionMode.tag as? Boolean) ?: false
        return false
    }

    override fun onActionItemClicked(actionMode: ActionMode, menuItem: MenuItem): Boolean {
        menuResolver(menuItem.itemId, selectedTournaments.toSet())
        reset()
        actionMode.finish()
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode) {
        reset()
    }

    private fun reset() {
        _multiSelect = false
        selectedTournaments.clear()
        onChange()
    }

}