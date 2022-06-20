package com.dgnt.quickTournamentMaker.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.HomeFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService
import com.dgnt.quickTournamentMaker.ui.layout.NonScrollingLinearLayoutManager
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import com.dgnt.quickTournamentMaker.util.update
import com.dgnt.quickTournamentMaker.util.viewBinding
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import org.joda.time.LocalDateTime
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance
import java.text.DateFormat


class HomeFragment : Fragment(R.layout.home_fragment), DIAware {
    override val di by di()
    private val viewModelFactory: HomeViewModelFactory by instance()
    private val binding by viewBinding(HomeFragmentBinding::bind)
    private lateinit var viewModel: HomeViewModel

    private val createDefaultTitleService: ICreateDefaultTitleService by instance()

    companion object {
        fun newInstance() = HomeFragment()

        private const val KEY_TOURNAMENT_TYPE_ID = "KEY_TOURNAMENT_TYPE_ID"
        private const val KEY_SEED_TYPE_ID = "KEY_SEED_TYPE_ID"
    }

    private val selectedPersons = mutableListOf<Person>()
    private var allGroups: List<GroupCheckedExpandableGroup>? = null
    private var personToGroupNameMap: Map<String, String>? = null

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            viewModel.tournamentType.value?.let { putInt(KEY_TOURNAMENT_TYPE_ID, it) }
            viewModel.seedType.value?.let { putInt(KEY_SEED_TYPE_ID, it) }
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragment = this
        context?.apply {
            viewModel = ViewModelProvider(fragment, viewModelFactory)[HomeViewModel::class.java]
            binding.vm = viewModel

            binding.lifecycleOwner = viewLifecycleOwner

            setVMData(viewModel)

            TournamentUtil.setUpTournamentTypeUI(
                viewModel,
                binding.tournamentTypeEditor,
                savedInstanceState?.getInt(KEY_TOURNAMENT_TYPE_ID),
                savedInstanceState?.getInt(KEY_SEED_TYPE_ID),
                viewLifecycleOwner,
                this,
                false
            )

            TournamentUtil.setUpTournamentEvents(
                viewModel,
                viewLifecycleOwner,
                this,
                requireActivity().supportFragmentManager
            )

            binding.personToParticipateRv.layoutManager = NonScrollingLinearLayoutManager(this)

            viewModel.selectedPersons.observe(viewLifecycleOwner) {
                viewModel.numberOfPersonsSelected.value = getString(R.string.numberOfPlayersSelected, it.size)
                selectedPersons.update(it)
                binding.personToParticipateRv.adapter?.notifyDataSetChanged()
            }

            viewModel.personAndGroupLiveData.observe(viewLifecycleOwner) { (personEntities, groupEntities) ->
                val groups = groupEntities.map { Group.fromEntity(it) }.sorted()
                personToGroupNameMap = personEntities.associate { it.name to it.groupName }

                val emptyGroupExpandableGroupMap = groups.map { it.name }.subtract(personEntities.map { it.groupName }.toSet()).map { GroupCheckedExpandableGroup(it, listOf()) }
                val groupExpandableGroupMap = personEntities.groupBy { it.groupName }.map { it.key to it.value.map { p -> Person.fromEntity(p) } }.map { GroupCheckedExpandableGroup(it.first, it.second.sorted()) }
                val allGroups = (groupExpandableGroupMap + emptyGroupExpandableGroupMap).sorted().also {
                    this@HomeFragment.allGroups = it
                }

                //remove groups that don't exist anymore
                viewModel.groupsExpanded.removeAll(viewModel.groupsExpanded.minus(groups.map { it.name }.toSet()))
                viewModel.selectedGroups.removeAll(viewModel.selectedGroups.minus(groups.map { it.name }.toSet()))
                //remove persons that don't exist anymore
                selectedPersons.removeAll(selectedPersons.minus(personEntities.map { p -> Person.fromEntity(p) }.toSet()).toSet())

                val adapter = GroupCheckedExpandableRecyclerViewAdapter(
                    allGroups,
                    viewModel.selectedGroups,
                    selectedPersons,
                    { person: String -> personClicked(person) },
                    { group: String, checked: Boolean -> groupClicked(group, checked) }
                )
                adapter.setOnGroupExpandCollapseListener(object : GroupExpandCollapseListener {
                    override fun onGroupExpanded(group: ExpandableGroup<*>) {
                        viewModel.groupsExpanded.add(group.title)
                    }

                    override fun onGroupCollapsed(group: ExpandableGroup<*>) {
                        viewModel.groupsExpanded.remove(group.title)
                    }

                })
                binding.personToParticipateRv.adapter = adapter
                adapter.groups.forEach { g ->
                    if (viewModel.groupsExpanded.contains(g.title))
                        adapter.toggleGroup(g)
                }
            }

            viewModel.expandAll.observe(viewLifecycleOwner) {
                val adapter = binding.personToParticipateRv.adapter as GroupCheckedExpandableRecyclerViewAdapter
                adapter.groups.forEach { g ->
                    if ((it && !adapter.isGroupExpanded(g)) || (!it && adapter.isGroupExpanded(g))) {
                        adapter.toggleGroup(g)
                    }
                }
            }

            viewModel.selectAll.observe(viewLifecycleOwner) {
                val adapter = binding.personToParticipateRv.adapter as GroupCheckedExpandableRecyclerViewAdapter
                adapter.groups.forEach { g ->
                    selectGroup(g as GroupCheckedExpandableGroup, it)
                }
            }
        }
    }

    private fun setVMData(viewModel: HomeViewModel) {
        val date = DateFormat.getDateInstance(DateFormat.SHORT).format(LocalDateTime().toDateTime().millis)
        //TODO dont pass the date here. Do it in TournamentInformationCreatorService
        viewModel.setData(
            mapOf(
                Pair(TournamentType.ELIMINATION, getString(R.string.defaultTitle, getString(TournamentType.ELIMINATION.stringResource), date)),
                Pair(TournamentType.DOUBLE_ELIMINATION, getString(R.string.defaultTitle, getString(TournamentType.DOUBLE_ELIMINATION.stringResource), date)),
                Pair(TournamentType.ROUND_ROBIN, getString(R.string.defaultTitle, getString(TournamentType.ROUND_ROBIN.stringResource), date)),
                Pair(TournamentType.SWISS, getString(R.string.defaultTitle, getString(TournamentType.SWISS.stringResource), date)),
                Pair(TournamentType.SURVIVAL, getString(R.string.defaultTitle, getString(TournamentType.SURVIVAL.stringResource), date))
            ),
            binding.tournamentTypeEditor.eliminationRb.id,
            binding.tournamentTypeEditor.doubleEliminationRb.id,
            binding.tournamentTypeEditor.roundRobinRb.id,
            binding.tournamentTypeEditor.swissRb.id,
            binding.tournamentTypeEditor.survivalRb.id,

            binding.tournamentTypeEditor.randomSeedRb.id,
            binding.tournamentTypeEditor.customSeedRb.id,
            binding.tournamentTypeEditor.compareRankFromPriorityRb.id,
            binding.tournamentTypeEditor.compareRankFromScoreRb.id
        ) { p: Int -> createDefaultTitleService.forParticipant(resources, p) }
    }

    private fun groupClicked(group: String, checked: Boolean) {
        allGroups?.find { it.title == group }?.let {
            selectGroup(it, checked)
        }
    }

    private fun selectGroup(group: GroupCheckedExpandableGroup, checked: Boolean) {
        if (checked) {
            viewModel.selectedGroups.add(group.title)
            group.checkSelections()
        } else {
            viewModel.selectedGroups.remove(group.title)
            group.clearSelections()
        }
        update()
    }

    private fun personClicked(person: String) {
        personToGroupNameMap?.get(person)?.let { groupName ->
            if (allGroups?.find { it.title == groupName }?.selectedChildren?.all { it } == true) {
                viewModel.selectedGroups.add(groupName)
            } else {
                viewModel.selectedGroups.remove(groupName)
            }
            update()
        }
    }

    private fun update() {
        (binding.personToParticipateRv.adapter as? GroupCheckedExpandableRecyclerViewAdapter)?.let { adapter ->
            viewModel.selectedPersons.value = adapter.groups.flatMap { (it as GroupCheckedExpandableGroup).items.zip(it.selectedChildren.asList()) }.filter { it.second }.map { it.first as Person }
        }
    }

}