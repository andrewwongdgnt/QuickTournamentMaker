package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.app.AlertDialog
import android.app.SearchManager
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.LoadTournamentFragmentBinding
import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.ui.main.common.OnEditListener
import com.dgnt.quickTournamentMaker.ui.tournament.MoreInfoDialogFragment
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
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

    private var mainAdapter: RestoredTournamentInformationRecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoadTournamentFragmentBinding.inflate(inflater)
        return binding.root
    }


    //FIXME suggestion part
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions_load_tournament, menu)
        val suggestions = listOf("")
        val searchMenu = menu.findItem(R.id.action_search)

        val simpleCursor = SimpleCursorAdapter(
            activity,
            R.layout.list_item,
            null,
            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
            intArrayOf(R.id.list_item_tv),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        (searchMenu?.actionView as? SearchView)?.apply {
            suggestionsAdapter = simpleCursor
        }?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(qString: String): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))

                suggestions.forEachIndexed { index, suggestion ->
                    if (suggestion.contains(qString, true))
                        cursor.addRow(arrayOf(index, suggestion))
                }

                simpleCursor.changeCursor(cursor)
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

                searchMenu.collapseActionView()
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
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
        val fragment = this
        context?.apply {

            setHasOptionsMenu(true)

            actionModeCallback = LoadTournamentFragmentActionModeCallBack(
                selectedTournaments,
                { mainAdapter?.notifyDataSetChanged() },
                { menuId: Int, tournaments: Set<RestoredTournamentInformation> -> menuResolver(menuId, tournaments) }
            )

            viewModel = ViewModelProvider(fragment, viewModelFactory)[LoadTournamentViewModel::class.java]
            binding.vm = viewModel

            binding.lifecycleOwner = viewLifecycleOwner

            binding.swipeRefresh.setOnRefreshListener {
                mainAdapter?.reset()?.takeUnless { it }?.run {
                    binding.noResultsTv.visibility = View.VISIBLE
                } ?: run { binding.noResultsTv.visibility = View.GONE }
                binding.clearSearchTv.visibility = View.GONE
                binding.swipeRefresh.isRefreshing = false
            }

            viewModel.tournamentLiveData.observe(viewLifecycleOwner) {
                Log.d(LoadTournamentFragment::class.simpleName, "restored tournament info: $it")

                val filteredAndSorted = filterAndSort(it)

                binding.tournamentRv.adapter = RestoredTournamentInformationRecyclerViewAdapter(
                    this,
                    filteredAndSorted.toMutableList(),
                    { restoredTournamentInformation ->
                        activity?.supportFragmentManager?.let { fragManager ->
                            MoreInfoDialogFragment.newInstance(restoredTournamentInformation.extendedTournamentInformation, tournamentEditListener).show(fragManager, MoreInfoDialogFragment.TAG)
                        }
                    },
                    { restoredTournamentInformation ->
                        startActivity(TournamentActivity.createIntent(this, restoredTournamentInformation))
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
                    AlertDialog.Builder(activity)
                        .setMessage(getString(R.string.deleteTournamentMsg, selectedTournaments.size))
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            Log.d("DGNT", "Delete ${selectedTournaments.size} tournaments")
                        }
                        .setNegativeButton(android.R.string.cancel, null).create().show()
                }
            }
        }
    }
}