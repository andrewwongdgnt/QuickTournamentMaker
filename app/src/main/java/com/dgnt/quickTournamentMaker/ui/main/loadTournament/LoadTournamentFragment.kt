package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
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

    private lateinit var binding: LoadTournamentFragmentBinding
    private lateinit var viewModel: LoadTournamentViewModel

    private lateinit var mainAdapter: RestoredTournamentInformationRecyclerViewAdapter

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

            viewModel = ViewModelProvider(fragment, viewModelFactory)[LoadTournamentViewModel::class.java]
            binding.vm = viewModel

            binding.lifecycleOwner = viewLifecycleOwner

            viewModel.tournamentLiveData.observe(viewLifecycleOwner) {
                Log.d(LoadTournamentFragment::class.simpleName, "restored tournament info: $it")

                val sortedAndFiltered = filterAndSort(it)

                binding.tournamentRv.adapter = RestoredTournamentInformationRecyclerViewAdapter(
                    sortedAndFiltered.toMutableList(),
                    { restoredTournamentInformation ->
                        activity?.supportFragmentManager?.let { fragManager ->
                            MoreInfoDialogFragment.newInstance(fragManager, restoredTournamentInformation.extendedTournamentInformation, tournamentEditListener)
                        }
                    },
                    { restoredTournamentInformation ->
                        startActivity(TournamentActivity.createIntent(this, restoredTournamentInformation))
                    }
                ).also { adapter ->
                    mainAdapter = adapter
                }
            }
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
                mainAdapter.updateList(filterAndSort(it))
            }
        }
    }

    private fun filterAndSort(list: List<RestoredTournamentInformation>) = viewModel.applyFilter(list).let { viewModel.applySort(it) }

}