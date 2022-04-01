package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.LoadTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.adapter.SuggestionCursorAdapter
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.ui.tournament.MoreInfoDialogFragment
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import com.dgnt.quickTournamentMaker.util.SimpleLogger
import com.dgnt.quickTournamentMaker.util.update
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class LoadTournamentFragment : Fragment(), DIAware {
    override val di by di()
    private val viewModelFactory: LoadTournamentViewModelFactory by instance()

    companion object {
        fun newInstance() = LoadTournamentFragment()
    }

    private val selectedTournaments = mutableSetOf<RestoredTournamentInformation>()
    private var actionMode: ActionMode? = null

    private lateinit var actionModeCallback: LoadTournamentFragmentActionModeCallBack
    private lateinit var binding: LoadTournamentFragmentBinding
    private lateinit var viewModel: LoadTournamentViewModel

    val suggestions = mutableListOf<Pair<String, Int>>()

    private var mainAdapter: RestoredTournamentInformationRecyclerViewAdapter? = null
    private var suggestionCursorAdapter: SuggestionCursorAdapter? = null
    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoadTournamentFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_load_tournament, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val searchMenu = menu.findItem(R.id.action_search)
        suggestionCursorAdapter?.let { suggestionCursorAdapter ->

            (searchMenu?.actionView as? SearchView)?.let { searchView ->
                this.searchView = searchView
                searchView.suggestionsAdapter = suggestionCursorAdapter
                searchView.setIconifiedByDefault(false)

                searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                    override fun onSuggestionClick(position: Int): Boolean {
                        val cursor = suggestionCursorAdapter.getItem(position) as Cursor
                        cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1).takeIf { it >= 0 }?.apply {
                            val txt: String = cursor.getString(this)
                            searchView.setQuery(txt, true)
                            return true
                        }
                        return false
                    }

                    override fun onSuggestionSelect(position: Int): Boolean {
                        // Your code here
                        return true
                    }
                })
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextChange(qString: String): Boolean {
                        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_ICON_1))

                        suggestions.map { it.first }.forEachIndexed { index, suggestion ->
                            if (suggestion.contains(qString, true))
                                cursor.addRow(arrayOf(index, suggestion, suggestion))
                        }

                        suggestionCursorAdapter.changeCursor(cursor)
                        return true
                    }

                    override fun onQueryTextSubmit(qString: String): Boolean {
                        binding.clearSearchTv.apply {
                            visibility = View.VISIBLE
                            text = getString(R.string.clearSearch, qString)
                        }
                        mainAdapter?.updateList(qString)?.takeUnless { it }?.run {
                            binding.noResultsTv.visibility = View.VISIBLE
                        } ?: run { binding.noResultsTv.visibility = View.GONE }

                        val count = suggestions.find { it.first == qString }?.second ?: 0
                        viewModel.addSearchTerm(Pair(qString, count))

                        searchMenu.collapseActionView()
                        return true
                    }
                })

            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden)
            actionMode?.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_edit -> {
                actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)?.also {
                    it.tag = true
                    it.invalidate()
                }

            }
            R.id.action_startTournamentFromFile -> {}
            R.id.action_sort -> {
                context?.let { context ->
                    val choices = Sort.values()

                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.sortOptionsTitle))
                        .setSingleChoiceItems(choices.map { getString(it.stringRes) }.toTypedArray(), choices.indexOf(viewModel.getSort())) { dialog, position ->
                            viewModel.setSort(choices[position])
                            onFilterSortListener.onEdit(Unit)
                            dialog.dismiss()
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show()
                }

            }
            R.id.action_filter -> {
                activity?.supportFragmentManager?.let {
                    LoadTournamentFilterOptionsDialogFragment.newInstance(onFilterSortListener).show(it, LoadTournamentFilterOptionsDialogFragment.TAG)
                }

            }
            R.id.action_viewAs -> {
                context?.let { context ->
                    val choices = ViewMode.values()

                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.sortOptionsTitle))
                        .setSingleChoiceItems(choices.map { getString(it.stringRes) }.toTypedArray(), choices.indexOf(viewModel.getViewMode())) { dialog, position ->
                            viewModel.setViewMode(choices[position])
                            onUpdateViewMode()
                            dialog.dismiss()
                        }
                        .setNegativeButton(android.R.string.cancel, null)
                        .create()
                        .show()
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        context?.let { context ->

            setHasOptionsMenu(true)

            actionModeCallback = LoadTournamentFragmentActionModeCallBack(
                selectedTournaments,
                { mainAdapter?.notifyDataSetChanged() },
                { menuId: Int, tournaments: Set<RestoredTournamentInformation> -> menuResolver(menuId, tournaments) }
            )

            viewModel = ViewModelProvider(this, viewModelFactory)[LoadTournamentViewModel::class.java]
            binding.vm = viewModel
            binding.lifecycleOwner = viewLifecycleOwner

            binding.swipeRefresh.setOnRefreshListener {
                mainAdapter?.reset()?.takeUnless { it }?.run {
                    binding.noResultsTv.visibility = View.VISIBLE
                } ?: run { binding.noResultsTv.visibility = View.GONE }
                binding.clearSearchTv.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }

            viewModel.messageEvent.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }

            viewModel.tournamentLiveData.observe(viewLifecycleOwner) {
                SimpleLogger.d(this, "restored tournament info: $it")
                val filteredAndSorted = filterAndSort(it)

                binding.tournamentRv.adapter = RestoredTournamentInformationRecyclerViewAdapter(
                    context,
                    filteredAndSorted.toMutableList(),
                    { restoredTournamentInformation ->
                        activity?.supportFragmentManager?.let { fragManager ->
                            MoreInfoDialogFragment.newInstance(restoredTournamentInformation.extendedTournamentInformation, tournamentEditListener).show(fragManager, MoreInfoDialogFragment.TAG)
                        }
                    },
                    { restoredTournamentInformation ->
                        startActivity(TournamentActivity.createIntent(context, restoredTournamentInformation))
                    },
                    viewModel.getViewMode(),
                    actionModeCallback,
                    { checked, tournament -> tournamentClicked(checked, tournament) }
                ).also { adapter ->
                    mainAdapter = adapter
                }
                filteredAndSorted.takeUnless { it.isEmpty() }?.run {
                    binding.noResultsTv.visibility = View.GONE
                    binding.clearSearchTv.visibility = View.GONE

                } ?: run {
                    binding.noResultsTv.visibility = View.VISIBLE
                }
            }

            viewModel.searchTerms.observe(viewLifecycleOwner) { searchTermEntities ->
                suggestions.update(searchTermEntities.map { Pair(it.term, it.count) })
            }

            suggestionCursorAdapter = SuggestionCursorAdapter(
                context,
                R.layout.search_suggest_list_item,
                arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_ICON_1),
                intArrayOf(R.id.search_item_tv, R.id.clear_search_iv),
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER,
            ) {
                viewModel.clearSearchTerm(it)
                searchView?.onActionViewCollapsed()
            }

        }

    }

    private fun tournamentClicked(checked: Boolean, tournament: RestoredTournamentInformation) {

        if (checked)
            selectedTournaments.add(tournament)
        else
            selectedTournaments.remove(tournament)

        actionMode?.run {
            menu.run {
                (selectedTournaments.size > 0).let {
                    findItem(R.id.action_delete).isVisible = it
                }
            }
            title = selectedTournaments.size.toString()
        }
    }

    private val tournamentEditListener = object : OnEditListener<TournamentInformation> {
        override fun onEdit(editedValue: TournamentInformation) {
            viewModel.updateTournament(editedValue)
        }
    }

    private val onFilterSortListener = object : OnEditListener<Unit> {
        override fun onEdit(editedValue: Unit) {
            viewModel.tournamentLiveData.value?.let {
                mainAdapter?.updateList(filterAndSort(it))
            }
        }
    }

    private fun filterAndSort(list: List<RestoredTournamentInformation>) = viewModel.applyFilter(list).let { viewModel.applySort(it) }

    private fun onUpdateViewMode() {
        mainAdapter?.updateList(viewModel.getViewMode())
    }

    private fun menuResolver(menuId: Int, selectedTournaments: Set<RestoredTournamentInformation>) {
        when (menuId) {
            R.id.action_delete -> {
                if (actionModeCallback.multiSelect) {
                    activity?.let { activity ->
                        AlertDialog.Builder(activity)
                            .setMessage(getString(R.string.deleteTournamentMsg, selectedTournaments.size))
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                SimpleLogger.d(this, "Delete ${selectedTournaments.size} tournaments")
                                viewModel.delete(selectedTournaments, getString(R.string.deleteTournamentSuccessfulMsg, selectedTournaments.size))
                            }
                            .setNegativeButton(android.R.string.cancel, null).create().show()
                    }
                }
            }
        }
    }
}