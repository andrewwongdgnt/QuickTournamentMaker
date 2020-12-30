package com.dgnt.quickTournamentMaker.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.HomeFragmentBinding
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.ui.layout.NonScrollingLinearLayoutManager
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.synthetic.main.component_tournament_type_editor.*
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class HomeFragment : Fragment(), DIAware {


    override val di by di()
    private val viewModelFactory: HomeViewModelFactory by instance()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val groupsExpanded = mutableSetOf<String>()
    private val selectedGroups = mutableSetOf<String>()
    private lateinit var allGroups: List<GroupCheckedExpandableGroup>
    private lateinit var personToGroupNameMap: Map<String, String>
    private lateinit var binding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (context == null) {
            return
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this
        viewModel.tournamentType.value = elimination_rb.id
        viewModel.seedType.value = randomSeed_rb.id
        sameSeed_rb.visibility = View.GONE
        viewModel.tournamentType.observe(viewLifecycleOwner, Observer {
            viewModel.showRankConfig.value = when (it) {
                elimination_rb.id, doubleElimination_rb.id, survival_rb.id -> false
                else -> true
            }
            viewModel.showSeedType.value = when (it) {
                survival_rb.id -> false
                else -> true
            }

            viewModel.handleTournamentTypeChange(it, roundRobin_rb.id, swiss_rb.id, compareRankFromPriority_rb.id, compareRankFromScore_rb.id)


        })
        viewModel.rankConfig.observe(viewLifecycleOwner, Observer {
            viewModel.showPriorityContent.value = it == compareRankFromPriority_rb.id

            viewModel.showScoringContent.value = it == compareRankFromScore_rb.id

            viewModel.handleRankConfigChange(it == compareRankFromPriority_rb.id, roundRobin_rb.id, swiss_rb.id)
        })

        viewModel.scoreConfigLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.handleScoreConfigChange(it.first, it.second, it.third, roundRobin_rb.id, swiss_rb.id)
        })

        binding.playerRv.layoutManager = NonScrollingLinearLayoutManager(context!!)

        viewModel.personAndGroupLiveData.observe(viewLifecycleOwner, Observer { (persons, groupEntities) ->

            val groups = groupEntities.map { Group.fromEntity(it) }.sorted()
            personToGroupNameMap = persons.map { it.name to it.groupName }.toMap()

            val emptyGroupExpandableGroupMap = groups.map { it.name }.subtract(persons.map { it.groupName }.toSet()).map { GroupCheckedExpandableGroup(it, listOf()) }
            val groupExpandableGroupMap = persons.groupBy { it.groupName }.map { it.key to it.value.map { Person.fromEntity(it) } }.map { GroupCheckedExpandableGroup(it.first, it.second.sorted()) }
            allGroups = (groupExpandableGroupMap + emptyGroupExpandableGroupMap).sorted()
            groupsExpanded.removeAll(groupsExpanded.minus(groups.map { it.key }))

            val adapter = GroupCheckedExpandableRecyclerViewAdapter(allGroups, selectedGroups, ResourcesCompat.getColor(resources,R.color.colorAccent,null), { person: String -> personClicked(person) }, { group: String, checked: Boolean -> groupClicked(group, checked) })
            adapter.setOnGroupExpandCollapseListener(object : GroupExpandCollapseListener {
                override fun onGroupExpanded(group: ExpandableGroup<*>) {
                    groupsExpanded.add(group.title)
                }

                override fun onGroupCollapsed(group: ExpandableGroup<*>) {
                    groupsExpanded.remove(group.title)
                }

            })
            binding.playerRv.adapter = adapter
            adapter.groups.forEach { g ->
                if (groupsExpanded.contains(g.title))
                    adapter.toggleGroup(g)
            }


        })

    }

    private fun groupClicked(group: String, checked: Boolean) {

        val theGroup = allGroups.find { it.title == group }
        if (checked) {
            selectedGroups.add(group)
            theGroup?.checkSelections()
        } else {
            selectedGroups.remove(group)
            theGroup?.clearSelections()
        }
        binding.playerRv.adapter?.notifyDataSetChanged()
    }


    private fun personClicked(person: String) {
        val groupName = personToGroupNameMap[person]
        if (groupName != null) {
            val theGroup = allGroups.find { it.title == groupName }

            if (theGroup?.selectedChildren?.all { it } == true) {
                selectedGroups.add(groupName)
            } else  {
                selectedGroups.remove(groupName)
            }
            binding.playerRv.adapter?.notifyDataSetChanged()
        }
    }

}